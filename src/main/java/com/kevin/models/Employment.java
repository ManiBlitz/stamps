package com.kevin.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "Employment")
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Employment {

    // These are the different statuses that can be assigned to the user
    // Pending is the
    public static final String[] employmentStatus = {
            "PENDING",
            "REJECTED",
            "ACTIVE",
            "DISMISSED"
    };

    // The work period is defined by the employment basis
    // We define a list of work periods that can be used by the employer
    // They enable to have a precise view of the work time of the employee
    // ===> WILL BE IMPLEMENTED IN A LATER PERIOD
//    public static final String[] employmentWorkPeriods = {
//            "WEEK",
//            "BI-WEEK",
//            "MONTH"
//    };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="employmentid",updatable=false,nullable=false)
    private long employmentid;

    @ManyToOne
    @JoinColumn(name = "companyid")
    private Companies company;

    @ManyToOne
    @JoinColumn(name = "userid")
    private Users employee;

    @Column(name="employeerole")
    private String employeerole;

    // This column keeps the day the offer was accepted
    @Column(name="offersentday")
    private Date offersentday;

    // This value is updated once the user set accepts the offer and the employment status is set to ACTIVE
    // This value can remain null
    @Column(name="recruitementday")
    private Date recruitementday;

    // This column keeps the day when the offer was either rejected or when the user was dismissed (fired or resigned)
    @Column(name="closingtime")
    private Date closingtime;

    @Column(name="employmentstatus")
    private String status;

    @OneToMany(mappedBy="employment")
    @JsonIgnore
    private List<Stamp> stamps;


}
