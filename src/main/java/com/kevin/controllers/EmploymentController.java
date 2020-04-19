package com.kevin.controllers;

import com.kevin.models.Companies;
import com.kevin.models.Employment;
import com.kevin.models.Stamp;
import com.kevin.models.Users;
import com.kevin.repository.EmploymentRepository;
import com.kevin.repository.StampRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employments")
public class EmploymentController {


    private static final Logger logger = LoggerFactory.getLogger(EmploymentController.class);

    @Autowired
    EmploymentRepository employmentRepository;

    @Autowired
    StampRepository stampRepository;

    @GetMapping
    public List<Employment> list(){
        return employmentRepository.findAll();
    }

    @GetMapping
    @RequestMapping("{id}")
    public Employment get(@PathVariable long id){
        return employmentRepository.getOne(id);
    }

    @GetMapping
    @RequestMapping("{id}/employee")
    public Users getEmployee(@PathVariable long id){
        return employmentRepository.getOne(id).getEmployee();
    }

    @GetMapping
    @RequestMapping("{id}/employer")
    public Companies getEmployer(@PathVariable long id){
        return employmentRepository.getOne(id).getCompany();
    }

    @PostMapping
    @RequestMapping("{id}/stamp/{stamp_code}")
    public Object pushStamp(@PathVariable long id, @PathVariable int stamp_code){

        Employment selectedEmployment = employmentRepository.getOne(id);
        if(selectedEmployment.getStatus().equals(Employment.employmentStatus[2])){

            String last_stamp_value = "NONE";

            if(selectedEmployment.getStamps().size()>0){
                last_stamp_value = selectedEmployment.
                        getStamps().
                        get(selectedEmployment.getStamps().size() - 1).
                        getStamptype();
            }

            String current_stamp_value = Stamp.stamp[stamp_code];

            Stamp current_stamp = new Stamp();
            current_stamp.setEmployment(selectedEmployment);
            current_stamp.setStamptime(Timestamp.valueOf(LocalDateTime.now()));
            current_stamp.setStamptype(current_stamp_value);

            switch (current_stamp_value){
                case "IN":
                    if(!last_stamp_value.equals("OUT") & !last_stamp_value.equals("NONE")){
                        return new ResponseEntity<>(
                                "You cannot clock-in if you have not clocked-out first",
                                HttpStatus.BAD_REQUEST
                        );
                    }
                    return stampRepository.saveAndFlush(current_stamp);
                case "BREAK_START":
                    if(!last_stamp_value.equals("IN")){
                        return new ResponseEntity<>(
                                "You must clock-in before taking a break",
                                HttpStatus.BAD_REQUEST
                        );
                    }
                    return stampRepository.saveAndFlush(current_stamp);
                case "BREAK_END":
                    if(!last_stamp_value.equals("BREAK_START")){
                        return new ResponseEntity<>(
                                "Your break has not been started, start the break first before ending it",
                                HttpStatus.BAD_REQUEST
                        );
                    }
                    return stampRepository.saveAndFlush(current_stamp);
                case "OUT":
                    if(!last_stamp_value.equals("BREAK_END") & !last_stamp_value.equals("IN")){
                        return new ResponseEntity<>(
                                "You cannot clock-out. You need to either end your break or clock-in first",
                                HttpStatus.BAD_REQUEST
                        );
                    }
                    return stampRepository.saveAndFlush(current_stamp);
            }

        }

        return new ResponseEntity<>(
                "The employment type is not valid to enter a timestamp",
                HttpStatus.UNAUTHORIZED
        );
    }

    @RequestMapping(value="{id}", method=RequestMethod.PUT)
    public Employment update(@PathVariable long id, @RequestBody Employment employment){
        Employment existingEmployment = employmentRepository.getOne(id);
        BeanUtils.copyProperties(employment,existingEmployment, "employmentid");
        return employmentRepository.saveAndFlush(existingEmployment);
    }

}
