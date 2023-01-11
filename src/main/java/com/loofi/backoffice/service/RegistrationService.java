package com.loofi.backoffice.service;

import com.loofi.backoffice.constants.MfsProductErrors;
import com.loofi.backoffice.dtoclass.Customer;
import com.loofi.backoffice.entity.Registration;
import com.loofi.backoffice.exceptions.CommonException;
import com.loofi.backoffice.repository.RegistrationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class RegistrationService {
    @Autowired
    private RegistrationRepository registrationRepository;
    private static RestTemplate restTemplate = new RestTemplate();

    @Value("${core.base.url:http://localhost:8090/}")
    private String customerServiceBaseUrl;

    @Value("${core.base.url:http://103.125.252.81:8080/account/}")
    private String accountServiceBaseUrl;

    public Registration updateRegistrationStatus(Registration registration) {
        if(registrationRepository.existsById(registration.getId())){
            Registration userRegistration = registrationRepository.getRegistrationUserById(registration.getId());
            Long customerId = saveRegistrationToCustomerKycService(userRegistration);
            userRegistration.setStatus(registration.getStatus());
            userRegistration.setLastModifiedBy(registration.getLastModifiedBy());
            userRegistration.setCustomerId(customerId);
            updateDataOnCustomerService(userRegistration);
            return registrationRepository.save(userRegistration);
        }
        throw new CommonException(
                MfsProductErrors.getErrorCode(MfsProductErrors.MFSPRODUCT_MANAGEMENT, MfsProductErrors.REGISTRATION_USER_NOT_FOUND),
                MfsProductErrors.ERROR_MAP.get(MfsProductErrors.REGISTRATION_USER_NOT_FOUND));
    }

    Long saveRegistrationToCustomerKycService(Registration reg){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(reg, headers);
        ResponseEntity<Long> responseCustomerId = restTemplate.exchange(customerServiceBaseUrl + "api/customerkyc",
                HttpMethod.POST,
                requestEntity,
                Long.class);
        return responseCustomerId.getBody();
    }

    void updateDataOnCustomerService(Registration reg){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Customer customer = new Customer();
        customer.setName(reg.getFirstName());
        customer.setCustomerId(reg.getCustomerId().toString());
        customer.setIdentifier(reg.getIdentifier());
        customer.setState("ACTIVE");

        HttpEntity<Object> requestEntity = new HttpEntity<>(customer, headers);
        ResponseEntity<Void> responseCustomerId = restTemplate.exchange(accountServiceBaseUrl + "api/v1/account/customer",
                HttpMethod.POST,
                requestEntity,
                Void.class);
        return;
    }

    public boolean saveMultipleRegistration(List<Registration> registrationList) {
        for (Registration registration: registrationList){
            registrationRepository.save(registration);
        }
        return true;
    }

    public Registration save(Registration registration) {
        return registrationRepository.save(registration);
    }

    public List<Registration> getRegistrationList() {
        return registrationRepository.findAll();
    }

    public boolean existsByIdentifier(String identifier) {
        return registrationRepository.existsByIdentifier(identifier);
    }
}
