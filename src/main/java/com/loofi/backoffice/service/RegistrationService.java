package com.loofi.backoffice.service;

import com.loofi.backoffice.constants.MfsProductErrors;
import com.loofi.backoffice.entity.Registration;
import com.loofi.backoffice.exceptions.CommonException;
import com.loofi.backoffice.repository.RegistrationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RegistrationService {
    @Autowired
    private RegistrationRepository registrationRepository;

    public Registration updateRegistrationStatus(Registration registration) {
        if(registrationRepository.existsById(registration.getId())){
            Registration reg = registrationRepository.getRegistrationUserById(registration.getId());
            reg.setStatus(registration.getStatus());
            reg.setLastModifiedBy(registration.getLastModifiedBy());
            registrationRepository.save(reg);
            return reg;
        }
        throw new CommonException(
                MfsProductErrors.getErrorCode(MfsProductErrors.MFSPRODUCT_MANAGEMENT, MfsProductErrors.REGISTRATION_USER_NOT_FOUND),
                MfsProductErrors.ERROR_MAP.get(MfsProductErrors.REGISTRATION_USER_NOT_FOUND));
    }

    public boolean saveMultipleRegistration(List<Registration> registrationList) {
        for (Registration reg: registrationList){
            registrationRepository.save(reg);
        }
        return true;
    }
}
