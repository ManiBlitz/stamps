package com.kevin.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "Companies")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Companies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="companyid",updatable=false,nullable=false)
    private long companyid;

    @Column(name="companyname")
    private String companyname;

    @Column(name="companystreetaddress")
    private String companystreetaddress;

    @Column(name="companycity")
    private String companycity;

    @Column(name="companystate")
    private String companystate;

    @Column(name="companycountry")
    private String companycountry;

    @Column(name="companyzipcode")
    private String companyzipcode;

    @Column(name="companylongitude")
    private double companylongitude;

    @Column(name="companylatitude")
    private double companylatitude;

    @Column(name="companycreationtime")
    private Date companycreationtime;

    // In case of modification of the company details, we also save the date and time the modifications were operated
    // At the beginning, the last update will be the same as the creation time
    @Column(name="companylastupdate")
    private Date companylastupdate;

    // This parameter is set with the expectation that a company has a specific geofence code associated to it
    // This parameter will not be considered until later in the implementation.
    @Column(name="companygeofencecode")
    private String companygeofencecode;

    // Each company has a specific owner, the person that created the company entity.
    @ManyToOne
    @JoinColumn(name="companyowner", nullable=false)
    private Users companyowner;

    // We have a list of employees defined
    @OneToMany(mappedBy = "company")
    @JsonIgnore
    List<Employment> employments;


}
