package hu.riean.ponte.contact.dao;

import hu.riean.ponte.contact.model.Account;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author riean
 */
public interface AccountDao extends CrudRepository<Account, Long> {

    Account getByName(String name);

}
