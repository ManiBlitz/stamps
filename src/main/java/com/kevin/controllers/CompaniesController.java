package com.kevin.controllers;

import com.kevin.models.Companies;
import com.kevin.models.Employment;
import com.kevin.models.Users;
import com.kevin.repository.CompaniesRepository;
import com.kevin.repository.EmploymentRepository;
import com.kevin.repository.UsersRepository;
import com.kevin.service.EmailUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/companies")
public class CompaniesController {

    private static final Logger logger = LoggerFactory.getLogger(CompaniesController.class);

    @Autowired
    CompaniesRepository companiesRepository;

    @Autowired
    EmploymentRepository employmentRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    EmailUtility emailUtility;

    @GetMapping
    public List<Companies> list(){
        return companiesRepository.findAll();
    }

    @GetMapping
    @RequestMapping("{id}")
    public Companies get(@PathVariable long id){
        return companiesRepository.getOne(id);
    }

    // This function sends back the different employees that still have an active employment status
    @GetMapping
    @RequestMapping("{id}/employees")
    public List<Users> getCurrentEmployees(@PathVariable long id){

        List<Employment> currentEmployements =  companiesRepository.getOne(id).
                getEmployments().
                stream().
                filter(employment -> employment.getStatus().equals(Employment.employmentStatus[2])).collect(Collectors.toList());

        List<Users> currentEmployees = new ArrayList<>();

        for(Employment employment : currentEmployements){
            currentEmployees.add(employment.getEmployee());
        }

        return currentEmployees;
    }

    @GetMapping
    @RequestMapping("{id}/employees/{employee_id}")
    public Users getCurrentEmployee(@PathVariable long id, @PathVariable long employee_id){

        List<Employment> currentEmployements =  companiesRepository.getOne(id).
                getEmployments().
                stream().
                filter(employment -> employment.getStatus().equals(Employment.employmentStatus[2])).collect(Collectors.toList());

        List<Users> currentEmployees = new ArrayList<>();

        for(Employment employment : currentEmployements){
            currentEmployees.add(employment.getEmployee());
        }

        return currentEmployees.get((int)employee_id);
    }

    // This function simply creates an employment request that will be sent to the user
    @PostMapping
    @RequestMapping("{id}/employ/{user_id}/{role}")
    public Employment sendEmploymentRequest(@PathVariable long id, @PathVariable long user_id, @PathVariable String role){

        Companies employer = companiesRepository.getOne(id);
        Users pending_employee = usersRepository.getOne(user_id);
        Employment new_employment = new Employment();
        new_employment.setCompany(employer);
        new_employment.setEmployee(pending_employee);
        new_employment.setStatus(Employment.employmentStatus[0]);
        new_employment.setOffersentday(new Date());
        new_employment.setEmployeerole(role);
        emailUtility.sendSimpleMessage(
                pending_employee.getEmail(),
                "026c25721b-35f2e0@inbox.mailtrap.io",
                EmailUtility.JOB_OFFER);
        return employmentRepository.saveAndFlush(new_employment);

    }

    @PutMapping
    @RequestMapping("{id}/employees/{employee_id}/dismiss")
    public Employment dismissEmployee(@PathVariable long id, @PathVariable long employement_id){
        List<Employment> currentEmployements =  companiesRepository.getOne(id).
                getEmployments().
                stream().
                filter(employment -> employment.getStatus().equals(Employment.employmentStatus[2])).collect(Collectors.toList());

        Employment updated_employment = currentEmployements.get((int)employement_id);
        updated_employment.setStatus(Employment.employmentStatus[3]);
        emailUtility.sendSimpleMessage(updated_employment.getEmployee().getEmail(),"026c25721b-35f2e0@inbox.mailtrap.io", EmailUtility.JOB_DISMISSAL);
        return employmentRepository.saveAndFlush(updated_employment);
    }

    // This function simply returns the owners description
    @GetMapping
    @RequestMapping("{id}/owner")
    public Users getOwner(@PathVariable long id){

        return companiesRepository.getOne(id).getCompanyowner();
    }

    @PostMapping
    public Companies create(@RequestBody final Companies new_company){

        Companies created_company = new Companies();
        created_company.setCompanycreationtime(new Date());
        created_company.setCompanylastupdate(new Date());

        created_company.setCompanycity(new_company.getCompanycity());
        created_company.setCompanystate(new_company.getCompanystate());
        created_company.setCompanycountry(new_company.getCompanycountry());
        created_company.setCompanylatitude(new_company.getCompanylatitude());
        created_company.setCompanylongitude(new_company.getCompanylongitude());
        created_company.setCompanyname(new_company.getCompanyname());
        created_company.setCompanystreetaddress(new_company.getCompanystreetaddress());
        created_company.setCompanyzipcode(new_company.getCompanyzipcode());
        return companiesRepository.saveAndFlush(created_company);
    }

    @RequestMapping(value="{id}", method=RequestMethod.PUT)
    public Companies update(@PathVariable long id, @RequestBody Companies company){
        Companies existingComapny = companiesRepository.getOne(id);
        BeanUtils.copyProperties(company,existingComapny, "companyid");
        return companiesRepository.saveAndFlush(existingComapny);
    }
}
