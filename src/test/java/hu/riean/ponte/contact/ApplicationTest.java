package hu.riean.ponte.contact;

import hu.riean.ponte.contact.bl.ContactLogic;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author riean
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ApplicationTest {
    
    @Autowired
    private ContactLogic contactLogic;

    @Test
    public void shouldInjectContext() {
        assertNotNull(contactLogic);
    }
    
}
