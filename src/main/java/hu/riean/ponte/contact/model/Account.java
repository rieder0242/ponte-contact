package hu.riean.ponte.contact.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * entiy Account
 *
 * account bejelentkezéshez
 *
 * @author riean
 */
@Entity
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = true)
    private String name;
    @Column(nullable = true)
    private String pass;
    @Column(nullable = true)
    private boolean active;

}
