package com.loofi.backoffice.entity;

import com.loofi.backoffice.enums.IdType;
import com.loofi.backoffice.enums.RegistrationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Registration extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique=true)
    private String idNumber;
    @Column(unique=true)// NID, passport, driving licence
    private String identifier;
    @Enumerated(EnumType.STRING)
    private IdType idType;
    private String alternativeAccountNumber;
    private String fileName;
    private String batchId;

    private Long customerId;
    private String mobileNo;
    private String email;

    private String area;
    private String region;
    private String territory;

    private String presentAddress;
    private String presentCity;
    private String presentUnion;
    private String presentThana;
    private String presentDistrict;
    private String presentCountry;

    private String permanentAddress;
    private String permanentCity;
    private String permanentUnion;
    private String permanentThana;
    private String permanentDistrict;
    private String permanentCountry;

    private String nomineeName;
    private String nomineeMobile;
    private String nomineeIdNumber;
    @Enumerated(EnumType.STRING)
    private IdType nomineeIdType;
    private String nomineeAddress;
    private String nomineeRelation;

    private String createdBy;
    private String lastModifiedBy;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;
}
