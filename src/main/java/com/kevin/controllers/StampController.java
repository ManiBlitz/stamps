package com.kevin.controllers;

import com.kevin.models.Employment;
import com.kevin.models.Stamp;
import com.kevin.repository.StampRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stamps")
public class StampController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    StampRepository stampRepository;

    @GetMapping
    public List<Stamp> list(){
        return stampRepository.findAll();
    }

    @GetMapping
    @RequestMapping("{id}")
    public Stamp get(@PathVariable long id){
        return stampRepository.getOne(id);
    }

    @GetMapping
    @RequestMapping("{id}/employment")
    public Employment getEmployment(@PathVariable long id){
        return stampRepository.getOne(id).getEmployment();
    }

    @RequestMapping(value="{id}", method=RequestMethod.PUT)
    public Stamp update(@PathVariable long id, @RequestBody Stamp stamp){
        Stamp existingStamp = stampRepository.getOne(id);
        BeanUtils.copyProperties(stamp,existingStamp, "stampid");
        return stampRepository.saveAndFlush(existingStamp);
    }
}
