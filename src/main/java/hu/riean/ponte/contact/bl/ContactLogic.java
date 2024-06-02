package hu.riean.ponte.contact.bl;

import hu.riean.ponte.contact.dao.ContactDao;
import hu.riean.ponte.contact.model.Address;
import hu.riean.ponte.contact.model.Contact;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author riean
 */
@Service
public class ContactLogic {
    
    @Autowired
    ContactDao contactDao;
    
    @Autowired
    EntityManager em;
    
    Pattern emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    Pattern phonePattern = Pattern.compile("^[0-9-+() ]+$");
    
    @Transactional
    public Contact update(Contact freshContact) {
        Optional<Contact> contactOptional = contactDao.findById(freshContact.getId());
        if (contactOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found");
        } else {
            Contact contact = contactOptional.get();
            return persist(contact, freshContact);
        }
    }
    
    @Transactional
    public Contact insert(Contact freshContact) {
        Contact contact = new Contact();
        contact.setAddresses(new ArrayList<>());
        return persist(contact, freshContact);
    }
    
    Contact persist(Contact contact, Contact freshContact) {
        copyValues(contact, freshContact);
        contact = contactDao.save(contact);
        persistChild(contact, freshContact);
        return contact;
    }
    
    void copyValues(Contact contact, Contact freshContact) {
        contact.setName(validateNullBlankString(freshContact.getName()));
        contact.setMotherName(validateNullString(freshContact.getMotherName()));
        contact.setBirthDate(freshContact.getBirthDate());
        contact.setSocialSecurityNumber(freshContact.getSocialSecurityNumber());
        contact.setTaxNumber(freshContact.getTaxNumber());
        contact.setEmails(validateEmails(freshContact.getEmails()));
        contact.setTels(validateTels(freshContact.getTels()));
    }
    
    void persistChild(Contact contact, Contact freshContact) {
        contact.setAddresses(saveAddressess(freshContact.getAddresses(), contact));
    }
    
    <T> T validateNull(T object) throws ResponseStatusException {
        if (object == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return object;
    }
    
    String validateNullString(String string) throws ResponseStatusException {
        string = validateNull(string).strip();
        if (string.isBlank()) {
            return "";
        } else {
            return string;
        }
    }
    
    String validateNullBlankString(String string) throws ResponseStatusException {
        string = validateNull(string).strip();
        if (string.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return string;
    }
    
    List<String> validateEmails(List<String> emails) {
        validateNull(emails);
        List<String> ret = new ArrayList<>();
        for (var email : emails) {
            email = validateNullBlankString(email);
            if (!emailPattern.matcher(email).find()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            ret.add(email);
        }
        return ret;
    }
    
    List<String> validateTels(List<String> tels) {
        validateNull(tels);
        List<String> ret = new ArrayList<>();
        for (var tel : tels) {
            tel = validateNullBlankString(tel);
            if (!phonePattern.matcher(tel).find()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            ret.add(tel);
        }
        return ret;
    }
    
    private List<Address> saveAddressess(List<Address> addresses, Contact contact) {
        validateNull(addresses);
        List<Address> ret = new ArrayList<>();
        List<Address> oldAddresses = contact.getAddresses();
        for (var freshAddress : addresses) {
            freshAddress = validateNull(freshAddress);
            
            Address address = null;
            for (ListIterator<Address> iterator = oldAddresses.listIterator(); iterator.hasNext();) {
                Address a = iterator.next();
                if (a.getId() == freshAddress.getId()) {
                    iterator.remove();
                    address = a;
                    break;
                }
            }
            boolean isNew = address == null;
            if (isNew) {
                address = new Address();
            }
            
            address.setZipcode(validateNullString(freshAddress.getZipcode()));
            address.setSettlement(validateNullString(freshAddress.getSettlement()));
            address.setStreet(validateNullString(freshAddress.getStreet()));
            address.setHouseNumber(validateNullString(freshAddress.getHouseNumber()));
            address.setContact(contact);
            if (isNew) {
                em.persist(address);
            } else {
                em.merge(address);
            }
            ret.add(address);
            
        }
        for (Address oldAddresse : oldAddresses) {
            em.remove(oldAddresse);
        }
        return ret;
    }
}
