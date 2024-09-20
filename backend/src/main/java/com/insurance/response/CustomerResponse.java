package com.insurance.response;


import com.insurance.enums.CreationStatus;

import lombok.Data;


@Data
public class CustomerResponse {

  private String customerId;
  private String name;
  private String username;
  private String email;
  private boolean isActive;
  private String status;
  private String phoneNumber;
  private String verifiedby;
  private String agent;
  
    
}