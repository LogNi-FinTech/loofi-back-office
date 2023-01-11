package com.loofi.backoffice.controller;


import com.loofi.backoffice.entity.Registration;
import com.loofi.backoffice.repository.RegistrationRepository;
import com.loofi.backoffice.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private RegistrationRepository registrationRepository;

    @PostMapping
    Registration saveRegistration(@RequestBody Registration registration){
        return registrationService.save(registration);
    }

    @PostMapping("/bulk")
    boolean saveMultipleRegistration(@RequestBody List<Registration> registrationList){
        return registrationService.saveMultipleRegistration(registrationList);
    }

    @PutMapping("/status")
    Registration updateRegistrationStatus(@RequestBody Registration registration){
        return registrationService.updateRegistrationStatus(registration);
    }
    @GetMapping
    List<Registration> getRegistrationList(){
        return registrationService.getRegistrationList();
    }
    @GetMapping("/identifier/{identifier}")
    boolean findByIdentifier(@PathVariable(value = "identifier") String identifier){
        return registrationService.existsByIdentifier(identifier);
    }
}
