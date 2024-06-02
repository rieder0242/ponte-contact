/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.ponte.contact.api;

import hu.riean.ponte.contact.bl.ContactLogic;
import hu.riean.ponte.contact.dao.ContactDao;
import hu.riean.ponte.contact.model.Contact;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author riean
 */
@RestController()
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    ContactDao contactDao;

    @Autowired
    ContactLogic contactLogic;

    @GetMapping("/list/{from}/")
    public Page<Contact> list(@PathVariable int from) {
        return list(from, "");
    }

    @GetMapping("/list/{from}/{fragmant}")
    public Page<Contact> list(@PathVariable int from, @PathVariable String fragmant) {
        return contactDao.findByNameContainingIgnoreCaseOrderByNameAsc(fragmant, PageRequest.of(from, 50));
    }

    @GetMapping("/{id}")
    public Contact one(@PathVariable long id) {
        return contactDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found"));
    }

    @PostMapping("")
    public Contact createContact(@RequestBody Contact freshContact) {
        return contactLogic.insert(freshContact);
    }

    @PutMapping("/{id}")
    public Contact updateContact(@RequestBody Contact freshContact, @PathVariable int id) {
        freshContact.setId(id);
        return contactLogic.update(freshContact);

    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable long id) {
        Optional<Contact> contactOptional = contactDao.findById(id);
        if (contactOptional.isPresent()) {
            contactDao.delete(contactOptional.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found");
        }

    }
}
