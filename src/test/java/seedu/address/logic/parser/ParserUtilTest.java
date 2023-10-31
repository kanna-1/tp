package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.logic.parser.ParserUtil.isAnyPrefixPresent;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Email;
import seedu.address.model.person.MedicalHistory;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Phone;
import seedu.address.model.person.enums.InputSource;
import seedu.address.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";

    // TODO: Modify NRIC Constraints to be more tightly bound
    private static final String INVALID_NRIC = " ";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_APPOINTMENT_FORMAT = "Tomorrow 8PM";
    private static final String INVALID_APPOINTMENT_VALUES = "31-Feb-2023 10:00 12:00";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_MEDICAL_HISTORY = "";
    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_NRIC = "S9876543A";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_APPOINTMENT = "23-Jan-2023 10:00 12:00";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";
    private static final String VALID_MEDICAL_HISTORY_1 = "cancer";
    private static final String VALID_MEDICAL_HISTORY_2 = "fever";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
                -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseNric_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseNric((String) null));
    }

    @Test
    public void parseNric_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseNric(INVALID_NRIC));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseNricvalidValueWithoutWhitespace_returnsName() throws Exception {
        Nric expectedNric = new Nric(VALID_NRIC);
        assertEquals(expectedNric, ParserUtil.parseNric(VALID_NRIC));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parseAppointment_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAppointment((String) null));
    }

    @Test
    public void parseAppointment_validValue_returnsAppointment() throws Exception {
        Appointment expectedAppointment = Appointment.of(VALID_APPOINTMENT, InputSource.USER_INPUT);
        System.out.println(ParserUtil.parseAppointment(VALID_APPOINTMENT));
        assertEquals(expectedAppointment, ParserUtil.parseAppointment(VALID_APPOINTMENT));
    }

    @Test
    public void parseAppointment_invalidFormatValue_returnsAppointment() throws Exception {
        assertThrows(ParseException.class, () -> ParserUtil.parseAppointment(INVALID_APPOINTMENT_FORMAT));
    }

    @Test
    public void parseAppointment_invalidDelimitedValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAppointment(INVALID_APPOINTMENT_VALUES));
    }

    @Test
    public void parseAppointment_validValueWithoutWhitespace_returnsAppointment() throws Exception {
        Appointment expectedAppt = Appointment.of(VALID_APPOINTMENT, InputSource.USER_INPUT);
        assertEquals(expectedAppt, ParserUtil.parseAppointment(VALID_APPOINTMENT));
    }

    @Test
    public void parseAppointment_validValueWithWhitespace_returnsTrimmedAppointment() throws Exception {
        String apptWithWhitespace = WHITESPACE + VALID_APPOINTMENT + WHITESPACE;
        Appointment expectedAppt = Appointment.of(VALID_APPOINTMENT, InputSource.USER_INPUT);
        assertEquals(expectedAppt, ParserUtil.parseAppointment(apptWithWhitespace));
    }

    @Test
    public void parseNric_validValueWithoutWhitespace_returnsNric() throws Exception {
        Nric expectedNric = new Nric(VALID_NRIC);
        assertEquals(expectedNric, ParserUtil.parseNric(VALID_NRIC));
    }

    @Test
    public void parseNric_validValueWithWhitespace_returnsTrimmedNric() throws Exception {
        String nricWithWhitespace = WHITESPACE + VALID_NRIC + WHITESPACE;
        Nric expectedNric = new Nric(VALID_NRIC);
        assertEquals(expectedNric, ParserUtil.parseNric(nricWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseMedicalHistories_emptyHistory_exceptionThrown() {
        // Input data with an empty medical history
        String input = "History 1, , History 3";

        assertThrows(ParseException.class, () -> ParserUtil.parseMedicals(Arrays.asList(input.split(","))));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    public void parseMedical_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseMedical(null));
    }

    @Test
    public void parseMedical_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseMedical(INVALID_MEDICAL_HISTORY));
    }

    @Test
    public void parseMedical_validValueWithoutWhitespace_returnsMedical() throws Exception {
        MedicalHistory expectedMedHistory = new MedicalHistory(VALID_MEDICAL_HISTORY_1);
        assertEquals(expectedMedHistory, ParserUtil.parseMedical(VALID_MEDICAL_HISTORY_1));
    }

    @Test
    public void parseMedical_validValueWithWhitespace_returnsTrimmedMedical() throws Exception {
        String medHistoryWithWhitespace = WHITESPACE + VALID_MEDICAL_HISTORY_1 + WHITESPACE;
        MedicalHistory expectedMedHistory = new MedicalHistory(VALID_MEDICAL_HISTORY_1);
        assertEquals(expectedMedHistory, ParserUtil.parseMedical(medHistoryWithWhitespace));
    }

    @Test
    public void parseMedicals_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseMedicals(null));
    }

    @Test
    public void parseMedicals_collectionWithInvalidMedicals_throwsParseException() {
        assertThrows(ParseException.class, () ->
                ParserUtil.parseMedicals(Arrays.asList(VALID_MEDICAL_HISTORY_1, INVALID_MEDICAL_HISTORY)));
    }

    @Test
    public void parseMedicals_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseMedicals(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseMedicals_collectionWithValidMedicals_returnsMedicalSet() throws Exception {
        Set<MedicalHistory> actualMedSet = ParserUtil.parseMedicals(Arrays.asList(VALID_MEDICAL_HISTORY_1,
                VALID_MEDICAL_HISTORY_2));
        Set<MedicalHistory> expectedMedSet = new HashSet<MedicalHistory>(
                Arrays.asList(new MedicalHistory(VALID_MEDICAL_HISTORY_1),
                        new MedicalHistory(VALID_MEDICAL_HISTORY_2)));

        assertEquals(expectedMedSet, actualMedSet);
    }

    @Test
    public void testIsAnyPrefixPresent() {
        // Create an ArgumentMultimap with some values
        ArgumentMultimap argumentMultimap = new ArgumentMultimap();
        argumentMultimap.put(new Prefix("phone"), "123456789");
        argumentMultimap.put(new Prefix("email"), "john@example.com");

        // Test with prefixes that exist in the ArgumentMultimap
        boolean result1 = isAnyPrefixPresent(argumentMultimap, new Prefix("phone"), new Prefix("email"));
        assertTrue(result1);

        // Test with prefixes that do not exist in the ArgumentMultimap
        boolean result2 = isAnyPrefixPresent(argumentMultimap, new Prefix("address"), new Prefix("tags"));
        assertFalse(result2);

        // Test with a mix of existing and non-existing prefixes
        boolean result3 = isAnyPrefixPresent(argumentMultimap, new Prefix("phone"), new Prefix("address"));
        assertTrue(result3);

        // Test with no prefixes
        boolean result4 = isAnyPrefixPresent(argumentMultimap);
        assertFalse(result4);
    }
}
