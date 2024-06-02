package hu.riean.ponte.contact.bl;

import hu.riean.ponte.contact.dao.ContactDao;
import hu.riean.ponte.contact.model.Address;
import hu.riean.ponte.contact.model.Contact;
import jakarta.persistence.EntityManager;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author riean
 */
@SpringBootTest
public class ContactLogicTest {

    final List<Address> address = Arrays.asList(new Address());
    final Date birthDate = new Date(92024531);
    final List<String> emails = Arrays.asList("spam@spam.com");
    final String motherName = "Pőcze Borbála";
    final String name = "Vinkoczi Lőrénc";
    final String ssn = "123234523";
    final String taxNumber = "98431464";
    final List<String> tels = Arrays.asList("+36 (30) 123-21-19");

    public ContactLogicTest() {
        Address get = address.get(0);
        get.setHouseNumber("");
        get.setSettlement("");
        get.setStreet("");
        get.setZipcode("");
    }

    

    @Test
    public void testUpdate() {
        System.out.println("update");
        Contact freshContact = new Contact();
        Contact persistent = new Contact();
        persistent.setAddresses(new ArrayList<>());
        ContactLogic instance = new ContactLogic();

        setupContact(freshContact);

        instance.em = mock(EntityManager.class);
        ContactDao contactDao = mock(ContactDao.class);
        instance.contactDao = contactDao;

        when(contactDao.findById(-1L)).thenReturn(Optional.empty());
        freshContact.setId(-1L);
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            instance.update(freshContact);
        });
        assertEqualsNotFound(exception);

        when(contactDao.findById(1L)).thenReturn(Optional.of(persistent));
        when(contactDao.save(persistent)).thenReturn(persistent);
        freshContact.setId(1L);
        persistent.setId(1L);
        Contact result = instance.update(freshContact);
        assertSame(persistent, result);
        assertContactEqual(result);
    }

    @Test
    public void testCopyValues() {
        System.out.println("copyValues");
        Contact contact = new Contact();
        contact.setAddresses(new ArrayList<>());
        Contact freshContact = new Contact();
        ContactLogic instance = new ContactLogic();
        instance.em = mock(EntityManager.class);
        final int id = 1;

        contact.setId(id);
        freshContact.setId(2);

        setupContact(freshContact);

        instance.copyValues(contact, freshContact);

        assertEquals(id, contact.getId());

        assertContactEqual(contact);
    }

    @Test
    public void testValidateNullString() {
        ContactLogic instance = new ContactLogic();
        String result;

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            instance.validateNullString(null);
        });
        assertEqualsBadRequest(exception);

        String norm = "     ";
        result = instance.validateNullString(norm);
        assertEquals("     ", norm);

        norm = "abc";
        result = instance.validateNullString(norm);
        assertEquals(norm, result);

        String normSapce = "\t  abc  ";
        result = instance.validateNullString(normSapce);
        assertEquals(norm, result);

    }

    @Test
    public void testValidateNullBlankString() {
        ContactLogic instance = new ContactLogic();
        String result;

        Exception exception;

        exception = assertThrows(ResponseStatusException.class, () -> {
            instance.validateNullBlankString(null);
        });
        assertEqualsBadRequest(exception);

        exception = assertThrows(ResponseStatusException.class, () -> {
            instance.validateNullBlankString("");
        });
        assertEqualsBadRequest(exception);

        String norm = "abc";
        result = instance.validateNullBlankString(norm);
        assertEquals(norm, result);

        String normSapce = "\t  abc  ";
        result = instance.validateNullBlankString(normSapce);
        assertEquals(norm, result);
    }

    @Test
    public void testValidateEmails() {
        final List<String> expResult = Arrays.asList(
                "spam@spam.com",
                "nagylaszlo@gamil.com",
                "macilaci@gamil.com"
        );
        List<String> emails = new ArrayList<>(expResult);
        ContactLogic instance = new ContactLogic();
        Exception exception;

        List<String> result = instance.validateEmails(emails);
        assertEquals(expResult, result);

        exception = assertThrows(ResponseStatusException.class, () -> {
            instance.validateEmails(null);
        });
        assertEqualsBadRequest(exception);

        exception = assertThrows(ResponseStatusException.class, () -> {
            emails.set(1, null);
            instance.validateEmails(emails);
        });
        assertEqualsBadRequest(exception);

        exception = assertThrows(ResponseStatusException.class, () -> {
            emails.set(1, "alma@barack");
            instance.validateEmails(emails);
        });
        assertEqualsBadRequest(exception);

        exception = assertThrows(ResponseStatusException.class, () -> {
            emails.set(1, "@barack.com");
            instance.validateEmails(emails);
        });
        assertEqualsBadRequest(exception);

        exception = assertThrows(ResponseStatusException.class, () -> {
            emails.set(1, "123");
            instance.validateEmails(emails);
        });
        assertEqualsBadRequest(exception);
    }

    @Test
    public void testValidateTels() {
        final List<String> expResult = Arrays.asList(
                "+36 (20) 123-45-67",
                "+36 (30) 265-45-67",
                "+36 (60) 251-18-74"
        );
        List<String> tels = new ArrayList<>(expResult);
        ContactLogic instance = new ContactLogic();
        Exception exception;

        List<String> result = instance.validateTels(tels);
        assertEquals(expResult, result);

        exception = assertThrows(ResponseStatusException.class, () -> {
            instance.validateEmails(null);
        });
        assertEqualsBadRequest(exception);

        exception = assertThrows(ResponseStatusException.class, () -> {
            expResult.set(1, "asdf");
            instance.validateEmails(expResult);
        });
        assertEqualsBadRequest(exception);

        exception = assertThrows(ResponseStatusException.class, () -> {
            expResult.set(1, null);
            instance.validateEmails(expResult);
        });
        assertEqualsBadRequest(exception);
    }

    private void setupContact(Contact freshContact) {
        freshContact.setAddresses(address);
        freshContact.setBirthDate(birthDate);
        freshContact.setEmails(emails);
        freshContact.setMotherName(motherName);
        freshContact.setName(name);
        freshContact.setSocialSecurityNumber(ssn);
        freshContact.setTaxNumber(taxNumber);
        freshContact.setTels(tels);
    }

    private void assertContactEqual(Contact result) {
       // assertEquals(address, result.getAddresses());
        assertEquals(birthDate, result.getBirthDate());
        assertEquals(emails, result.getEmails());
        assertEquals(motherName, result.getMotherName());
        assertEquals(name, result.getName());
        assertEquals(ssn, result.getSocialSecurityNumber());
        assertEquals(taxNumber, result.getTaxNumber());
        assertEquals(tels, result.getTels());
    }

    private void assertEqualsBadRequest(Exception exception) {
        assertEquals(ResponseStatusException.class, exception.getClass());
        ResponseStatusException responseStatusException = (ResponseStatusException) exception;
        assertEquals(HttpStatus.BAD_REQUEST, responseStatusException.getStatusCode());
    }

    private void assertEqualsNotFound(Exception exception) {
        assertEquals(ResponseStatusException.class, exception.getClass());
        ResponseStatusException responseStatusException = (ResponseStatusException) exception;
        assertEquals(HttpStatus.NOT_FOUND, responseStatusException.getStatusCode());
    }

}
