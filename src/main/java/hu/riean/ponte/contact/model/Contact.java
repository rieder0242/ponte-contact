/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.ponte.contact.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.sql.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * entity contact
 *
 * @author riean
 */
@Entity
@Getter
@Setter
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = true)
    private String name;
    @Column(nullable = true)
    private List<String> emails;
    @Column(nullable = true)
    private List<String> tels;
    @Column(name = "birth_date", nullable = true)
    private Date birthDate;
    @Column(name = "mother_name", nullable = true)
    private String motherName;
    @Column(name = "social_security_number", nullable = true)
    private String socialSecurityNumber;
    @Column(name = "tax_number", nullable = true)
    private String taxNumber;

    @OneToMany(mappedBy = "contact", cascade = {CascadeType.PERSIST})
    private List<Address> addresses;

}
