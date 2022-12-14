package com.loofi.backoffice.dtoclass;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Customer {
    private String name;
    private String identifier;
    private String customerId;
    private String state;
}
