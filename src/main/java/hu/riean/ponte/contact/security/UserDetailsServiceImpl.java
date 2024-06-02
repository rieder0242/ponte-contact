package hu.riean.ponte.contact.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import hu.riean.ponte.contact.dao.AccountDao;

/**
 *
 * @author riean
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    AccountDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userDao.getByName(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new Details(user);
    }

}
