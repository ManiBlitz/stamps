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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    EmailUtility emailUtility;

    @Autowired
    CompaniesRepository companiesRepository;

    @Autowired
    EmploymentRepository employmentRepository;

    @GetMapping
    public List<Users> list(){
        return usersRepository.findAll();
    }

    @GetMapping
    @RequestMapping("{id}")
    public Users get(@PathVariable long id){
        return usersRepository.getOne(id);
    }

    @GetMapping
    @RequestMapping("{id}/businesses")
    public List<Companies> getBusinesses(@PathVariable long id){
        return usersRepository.getOne(id).getBusinesses();
    }

    @GetMapping
    @RequestMapping("{id}/businesses/{business_id}")
    public Companies getBusiness(@PathVariable long id, @PathVariable long business_id){
        return usersRepository.getOne(id).getBusinesses().get((int)business_id);
    }

    @GetMapping
    @RequestMapping("{id}/employments")
    public List<Employment> getEmployments(@PathVariable long id){
        return usersRepository.getOne(id).getEmployements();
    }

    @GetMapping
    @RequestMapping("{id}/employments/{employment_id}")
    public Employment getEmployments(@PathVariable long id, @PathVariable long employment_id){
        return usersRepository.getOne(id).getEmployements().
                stream().
                filter(employment -> employment.getEmploymentid() == employment_id).collect(Collectors.toList()).get(0);
    }

    @PutMapping
    @RequestMapping("{id}/employments/{employment_id}/accept")
    public Object acceptOffer(@PathVariable long id, @PathVariable long employment_id){
        Employment existingEmployment = employmentRepository.getOne(employment_id);
        if(existingEmployment.getStatus().equals(Employment.employmentStatus[0])){
            existingEmployment.setStatus(Employment.employmentStatus[2]);
            existingEmployment.setRecruitementday(new Date());
        }else{
            return new ResponseEntity<>("The offer is no longer available!",HttpStatus.NOT_ACCEPTABLE);
        }
        emailUtility.sendSimpleMessage(
                existingEmployment.getCompany().getCompanyowner().getEmail(),
                "026c25721b-35f2e0@inbox.mailtrap.io",
                EmailUtility.JOB_ACCEPT);
        return employmentRepository.saveAndFlush(existingEmployment);
    }

    @PutMapping
    @RequestMapping("{id}/employments/{employment_id}/reject")
    public Object rejectOffer(@PathVariable long id, @PathVariable long employment_id){
        Employment existingEmployment = employmentRepository.getOne(employment_id);
        if(existingEmployment.getStatus().equals(Employment.employmentStatus[0])){
            existingEmployment.setStatus(Employment.employmentStatus[1]);
            existingEmployment.setClosingtime(new Date());
        }else{
            return new ResponseEntity<>("The offer is no longer available!", HttpStatus.NOT_ACCEPTABLE);
        }
        emailUtility.sendSimpleMessage(
                existingEmployment.getCompany().getCompanyowner().getEmail(),
                "026c25721b-35f2e0@inbox.mailtrap.io",
                EmailUtility.JOB_REFUSE);
        return employmentRepository.saveAndFlush(existingEmployment);
    }

    // For this function, we create an encryption protocol for the password
    @PostMapping
    public Object create(@RequestBody final Users user){
        Users updated_user = new Users();
        updated_user.setRegistrationday(new Date());

        if(EmailUtility.isValid(user.getEmail())){
            updated_user.setEmail(user.getEmail());
        }else{
            return new ResponseEntity<>("The email provided is invalid", HttpStatus.NOT_ACCEPTABLE);
        }
        updated_user.setFirstname(user.getFirstname());
        updated_user.setLastname(user.getLastname());
        updated_user.setPwd(user.getPwd());
        updated_user.setUsername(user.getUsername());
        emailUtility.sendSimpleMessage(user.getEmail(),"026c25721b-35f2e0@inbox.mailtrap.io", EmailUtility.USER_CREATION);
        return usersRepository.saveAndFlush(updated_user);
    }

    @PostMapping
    @RequestMapping("{id}/createcompany")
    public Companies createCompanies(@PathVariable long id, @RequestBody final Companies new_company){

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
        created_company.setCompanyowner(usersRepository.getOne(id));
        return companiesRepository.saveAndFlush(created_company);
    }

    @RequestMapping(value="{id}", method=RequestMethod.PUT)
    public Users update(@PathVariable long id, @RequestBody Users user){
        Users existingUser = usersRepository.getOne(id);
        BeanUtils.copyProperties(user,existingUser, "userid");
        return usersRepository.saveAndFlush(existingUser);
    }

    // TODO: Implement login functionality and resolve email delivery to have it operational

}
