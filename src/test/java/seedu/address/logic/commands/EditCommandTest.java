package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;



/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());


    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person person = model.getFilteredPersonList().get(0);

        PersonBuilder personInList = new PersonBuilder(person);

        Person editedPerson = personInList.withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(new Name(person.getName().toString()), null, descriptor);

        String expectedMessage =
                String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(person, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Person lastPerson = model.getFilteredPersonList().get(0);

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(new Name(lastPerson.getName().toString()), null, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void findPersonToEdit_personFoundByName_returnPersonOptional() throws CommandException {
        List<Person> persons = new ArrayList<>();
        Person person = new PersonBuilder().withName("John Doe").build();
        persons.add(person);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().build();
        EditCommand editCommand = new EditCommand(new Name("John Doe"), null, descriptor);

        Optional<Person> personOptional = editCommand.findPersonToEdit(persons);

        assertEquals(person, personOptional.get());
    }
    @Test
    public void findPersonToEdit_personFoundByIC_returnPersonOptional() throws CommandException {
        List<Person> persons = new ArrayList<>();
        Person person = new PersonBuilder().withNric("S2012032B").build();
        persons.add(person);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().build();
        EditCommand editCommand = new EditCommand(null, new Nric("S2012032B"), descriptor);

        Optional<Person> personOptional = editCommand.findPersonToEdit(persons);

        assertEquals(person, personOptional.get());
    }

    @Test
    public void findPersonToEdit_NameAndICGiven_returnPersonOptional() throws CommandException {
        List<Person> persons = new ArrayList<>();
        Person person = new PersonBuilder().withNric("S2012032B").withName("John Doe").build();
        persons.add(person);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().build();
        EditCommand editCommand = new EditCommand(new Name("John Doe"), new Nric("S2012032B"), descriptor);

        Optional<Person> personOptional = editCommand.findPersonToEdit(persons);

        assertEquals(person, personOptional.get());
    }

    @Test
    public void findPersonToEdit_NameAndICNotGiven_returnPersonOptional() throws CommandException {
        List<Person> persons = new ArrayList<>();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().build();
        EditCommand editCommand = new EditCommand(null, null, descriptor);

        Optional<Person> personOptional = editCommand.findPersonToEdit(persons);

        assertEquals(Optional.empty(), personOptional.get());
    }

    @Test
    public void toStringMethod() {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditCommand editCommand = new EditCommand(new Name("Name"), null, editPersonDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{name=Name, nric=null" + ", editPersonDescriptor="
                + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

    @Test
    public void equals() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        descriptor.setName(new Name("John Doe"));
        descriptor.setNric(new Nric("S1234567A"));

        // Create an EditCommand with the descriptor
        EditCommand editCommand = new EditCommand(new Name("John Doe"), new Nric("S1234567A"), descriptor);

        // Create another EditCommand with the same descriptor
        EditCommand editCommandCopy = new EditCommand(new Name("John Doe"), new Nric("S1234567A"), descriptor);

        // Create an EditCommand with a different descriptor
        EditPersonDescriptor differentDescriptor = new EditPersonDescriptor();
        differentDescriptor.setName(new Name("Jane Doe"));

        // Test equals method
        assertTrue(editCommand.equals(editCommand)); // Same object
        assertTrue(editCommand.equals(editCommandCopy)); // Same descriptor
        assertFalse(editCommand.equals(null)); // null
        assertFalse(editCommand.equals(new Object())); // Different class
    }

}
