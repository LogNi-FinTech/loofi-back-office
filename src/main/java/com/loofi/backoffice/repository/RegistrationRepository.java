package com.loofi.backoffice.repository;

import com.loofi.backoffice.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RegistrationRepository extends JpaRepository<Registration, Integer> {
    @Query("SELECT res FROM Registration res WHERE id=:registrationId")
    Registration getRegistrationUserById(@Param("registrationId") Long registrationId);
    Boolean existsById(Long id);

    Boolean existsByCustomerId(Long id);
    Boolean existsByIdentifier(String identifier);
}
