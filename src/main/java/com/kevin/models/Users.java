package com.kevin.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "Users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="userid",updatable=false,nullable=false)
    private long userid;

    @Column(name="email")
    private String email;

    @Column(name="username")
    private String username;

    @Column(name="firstname")
    private String firstname;

    @Column(name="lastname")
    private String lastname;

    @Column(name="pwd")
    @JsonIgnore
    private String pwd;

    @Column(name="registrationday")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationday;

    @OneToMany(mappedBy="employee")
    @JsonIgnore
    private List<Employment> employements;

    @OneToMany(mappedBy="companyowner")
    @JsonIgnore
    private List<Companies> businesses;

    // TODO: Add duplication blockers in the definition of the table

}
