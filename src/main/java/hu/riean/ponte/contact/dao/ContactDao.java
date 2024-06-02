package hu.riean.ponte.contact.dao;

import hu.riean.ponte.contact.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author riean
 */
public interface ContactDao extends CrudRepository<Contact, Long> {

    Page<Contact> findByNameContainingIgnoreCaseOrderByNameAsc(String fragmant, Pageable pageable);

    public Object findById(int id);


    
}
