package com.kevin.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Table(name = "Stamp")
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Stamp {

    // These are the initial stamps that will be allowed for the application
    // The list will be updated as needs grow
    public static final String[] stamp = {
            "IN",
            "OUT",
            "BREAK_START",
            "BREAK_END",
    };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="stampid",updatable=false,nullable=false)
    private long stampid;

    @ManyToOne
    @JoinColumn(name="employmentid", nullable=false)
    private Employment employment;

    @Column(name="stamptype")
    private String stamptype;

    @Column(name="stamptime")
    private Timestamp stamptime;

//    ===> WILL BE IMPLEMENTED IN A LATER PERIOD
//    @Column(name="workperiod")
//    private String workperiod;




}
