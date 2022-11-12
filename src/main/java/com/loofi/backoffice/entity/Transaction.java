package com.loofi.backoffice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Transaction {

    private int amount;

    private String fromAc;

    private String toAc;

    private String note;

    private String referenceId;

    private String tag;

    private String description;

    private String channel;

    private String requestId;

    private String maker;

    private String checker;

    private TransactionData data;

    private TransactionType transactionType;
}
