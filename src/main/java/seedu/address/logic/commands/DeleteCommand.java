package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Email;
import seedu.address.model.person.Id;
import seedu.address.model.person.MedicalHistory;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "delete";

    public static final String COMMAND_WORD_ALIAS = "d";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " or " + COMMAND_WORD_ALIAS
            + ": Delete the Patient identified by the full Name or ID of the patient.\n"
            + "Parameters: n/NAME or id/ID\n"
            + "Fields that can be deleted: ap/APPOINTMENT m/MEDICAL_HISTORY \n"
            + "Example 1: " + COMMAND_WORD + " n/John Doe or " + COMMAND_WORD + " id/S1234567A " + "ap/ m/\n"
            + "Example 2: " + COMMAND_WORD_ALIAS + " n/Alex Yeoh or " + COMMAND_WORD_ALIAS + " id/T0123456F";

    public static final String MESSAGE_DELETE_PATIENT_SUCCESS = "Deleted Patient: %1$s";

    public static final String MESSAGE_DELETE_PATIENT_FIELD_SUCCESS = "Deleted Patient's field: %1$s";

    public static final String MESSAGE_PATIENT_NOT_FOUND =
            "The given combination of Name and/or ID does not match any patient in the Patient List.";

    public static final String MESSAGE_NO_APPOINTMENT_TO_DELETE = "Patient does not have an appointment to delete.";

    public static final String MESSAGE_NO_MEDICAL_HISTORY_TO_DELETE =
            "Patient does not have any medical histories to delete.";

    public static final String MESSAGE_INVALID_MEDICAL_HISTORY =
            "Patient does not have the medical histories specified.";

    public static final String MESSAGE_UNDO_DELETE_PATIENT_SUCCESS = "Undoing the deletion of Patient:  %1$s";

    public static final String MESSAGE_UNDO_DELETE_FIELD_SUCCESS = "Undoing the deletion of a Patient's field:  %1$s";

    /**
     * The original state of the patient.
     */
    private Person originalPerson;

    /**
     * The state of the patient after a field has been deleted.
     */
    private Person editedPerson;

    /**
     * The index of the original patient to be fully deleted. To be used with undo.
     */
    private int index;
    private final Name name;
    private final Id id;
    private final DeletePersonDescriptor deletePersonDescriptor;

    /**
     * @param id                     of the patient in the filtered patient list to
     *                               edit
     * @param name                   of the patient in the filtered patient list to
     *                               edit
     * @param deletePersonDescriptor details to delete the patient with
     */
    public DeleteCommand(Id id, Name name, DeletePersonDescriptor deletePersonDescriptor) {
        this.id = id;
        this.name = name;
        this.deletePersonDescriptor = deletePersonDescriptor;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getUnfilteredPersonList();

        Optional<Person> personOptional = CommandUtil.findPersonByIdentifier(name, id, lastShownList);

        if (personOptional.isEmpty()) {
            throw new CommandException(MESSAGE_PATIENT_NOT_FOUND);
        }

        Person personToDelete = personOptional.get();
        originalPerson = personToDelete;

        index = model.getFilteredPersonList().indexOf(personToDelete);

        if (deletePersonDescriptor.isAllFalse()) {
            model.deletePerson(personToDelete);
            model.addToHistory(this);
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            return new CommandResult(String.format(MESSAGE_DELETE_PATIENT_SUCCESS, Messages.format(personToDelete)));
        } else {
            editedPerson = createDeletePerson(personToDelete, deletePersonDescriptor);
            model.setPerson(personToDelete, editedPerson);
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            model.addToHistory(this);
            return new CommandResult(String.format(MESSAGE_DELETE_PATIENT_FIELD_SUCCESS,
                    Messages.format(editedPerson)));
        }
    }

    @Override
    public CommandResult undo(Model model) {
        requireNonNull(model);
        if (deletePersonDescriptor.isAllFalse()) {
            model.addPersonAtIndex(originalPerson, index);
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            return new CommandResult(String.format(MESSAGE_UNDO_DELETE_PATIENT_SUCCESS,
                    Messages.format(originalPerson)));
        } else {
            Person personToDelete = editedPerson;
            model.setPerson(personToDelete, originalPerson);
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            return new CommandResult(String.format(MESSAGE_UNDO_DELETE_FIELD_SUCCESS,
                    Messages.format(editedPerson)));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;

        return Objects.equals(id, otherDeleteCommand.id)
                && Objects.equals(name, otherDeleteCommand.name)
                && Objects.equals(deletePersonDescriptor, otherDeleteCommand.deletePersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("id", id)
                .add("name", name)
                .add("deletePersonDescriptor", deletePersonDescriptor)
                .toString();
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code deletePersonDescriptor}.
     *
     * @throws CommandException
     */
    public static Person createDeletePerson(Person personToEdit, DeletePersonDescriptor deletePersonDescriptor)
            throws CommandException {
        assert personToEdit != null;

        Name updatedName = personToEdit.getName();
        Id updatedId = personToEdit.getId();
        Phone updatedPhone = personToEdit.getPhone();
        Email updatedEmail = personToEdit.getEmail();
        Address updatedAddress = personToEdit.getAddress();
        Set<MedicalHistory> updatedMedicalHistories = personToEdit.getMedicalHistories();
        Appointment updatedAppointment = personToEdit.getAppointment().isPresent() ? personToEdit.getAppointment().get()
                : null;

        if (deletePersonDescriptor.shouldDeleteAppointment()) {
            if (updatedAppointment == null) {
                throw new CommandException(MESSAGE_NO_APPOINTMENT_TO_DELETE);
            } else {
                updatedAppointment = null;
            }
        }

        if (deletePersonDescriptor.shouldDeleteMedicalHistory()) {
            if (updatedMedicalHistories.isEmpty()) {
                throw new CommandException(MESSAGE_NO_MEDICAL_HISTORY_TO_DELETE);
            }

            Set<MedicalHistory> medicalHistoriesToDelete = deletePersonDescriptor.getMedicalHistories();
            Set<MedicalHistory> medicalHistoriesToKeep = new HashSet<>(updatedMedicalHistories);

            if (medicalHistoriesToDelete.isEmpty()) {
                updatedMedicalHistories = medicalHistoriesToDelete;
            } else {
                for (MedicalHistory medicalHistory : medicalHistoriesToDelete) {
                    if (updatedMedicalHistories.contains(medicalHistory)) {
                        medicalHistoriesToKeep.remove(medicalHistory);
                    } else {
                        throw new CommandException(MESSAGE_INVALID_MEDICAL_HISTORY);
                    }
                }
                updatedMedicalHistories = medicalHistoriesToKeep;
            }
        }

        return new Person(updatedName, updatedId, updatedPhone, updatedEmail, updatedAddress, updatedAppointment,
                updatedMedicalHistories);
    }

    /**
     * Stores the values of the fields that is to be deleted from a patient
     * in HealthSync.
     */
    public static class DeletePersonDescriptor {
        private boolean shouldDeleteAppointment;
        private boolean shouldDeleteMedicalHistory;
        private Set<MedicalHistory> medicalHistories;

        public DeletePersonDescriptor() {
        }

        public void setDeleteMedicalHistory() {
            this.shouldDeleteMedicalHistory = true;
        }

        public boolean shouldDeleteMedicalHistory() {
            return this.shouldDeleteMedicalHistory;
        }

        public void setMedicalHistory(Set<MedicalHistory> medicalHistories) {
            this.medicalHistories = medicalHistories;
        }

        public Set<MedicalHistory> getMedicalHistories() {
            return this.medicalHistories;
        }

        public void setDeleteAppointment() {
            this.shouldDeleteAppointment = true;
        }

        public boolean shouldDeleteAppointment() {
            return this.shouldDeleteAppointment;
        }

        /**
         * Returns true if all fields are false.
         */
        public boolean isAllFalse() {
            return !shouldDeleteAppointment && !shouldDeleteMedicalHistory;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof DeletePersonDescriptor)) {
                return false;
            }

            DeletePersonDescriptor otherDeletePersonDescriptor = (DeletePersonDescriptor) other;
            return Objects.equals(medicalHistories, otherDeletePersonDescriptor.medicalHistories)
                    && Objects.equals(shouldDeleteMedicalHistory,
                            otherDeletePersonDescriptor.shouldDeleteMedicalHistory)
                    && Objects.equals(shouldDeleteAppointment, otherDeletePersonDescriptor.shouldDeleteAppointment);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("medicalHistories", medicalHistories)
                    .add("shouldDeleteMedicalHistory", shouldDeleteMedicalHistory)
                    .add("shouldDeleteAppointment", shouldDeleteAppointment)
                    .toString();
        }
    }
}
