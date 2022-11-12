package com.loofi.backoffice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TransactionResponse {
    private String status;

    private String description;

    private String errorCode;

    private String message;

    private String fromAc;

    private String toAc;

    private String fromName;

    private String toName;

    private String fromBalance;

    private String toBalance;

    private String txnId;
}
