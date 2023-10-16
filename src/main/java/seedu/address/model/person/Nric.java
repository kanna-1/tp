package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
/**
 * Represents a Person's NRIC in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidNric(String)}
 */
public class Nric {

    public static final String MESSAGE_CONSTRAINTS = "NRIC must be in the format LNNNNNNNL, where L represents a letter and N represents a number.";

    /*
     * Validation regex for NRIC in the format LNNNNNNNL, where L represents a letter and N represents a number.
     */
    public static final String VALIDATION_REGEX = "^[A-Za-z]\\d{6}[A-Za-z]$";


    public final String value;

    /**
     * Constructs an {@code Nric}.
     *
     * @param nric A valid NRIC.
     */
    public Nric(String nric) {
        requireNonNull(nric);
        checkArgument(isValidNric(nric), MESSAGE_CONSTRAINTS);
        value = nric;
    }

    /**
     * Returns true if a given string is a valid NRIC.
     */
    public static boolean isValidNric(String test) {

        // to add more checks for a valid nric combination
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Nric)) {
            return false;
        }

        Nric otherNric = (Nric) other;
        return value.equalsIgnoreCase((otherNric.value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
