package com.loofi.backoffice.entity;

import com.loofi.backoffice.enums.TransactionType;

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
public class LedgerTransaction extends Auditable<String>{
    @Id
    @GeneratedValue
    private int id;

    private int maker_id;

    private int checker_id;

    private int amount;

    private String status;

    private String txnId;

    private String branch;

    private String fromAC;

    private String toAc;

    private String fromType;

    private String toType;

    private String txnTime;

    private String note;

    private String reference;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
}
