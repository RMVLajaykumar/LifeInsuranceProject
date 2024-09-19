package com.insurance.response;

import java.time.LocalDate;

import com.insurance.enums.PaymentInterval;
import com.insurance.enums.PolicyStatus;

import lombok.Data;

@Data
public class PolicyResponse {
	
    private String policyId;
    
    private String policyName;
    
    //private String plan_id;

    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private int policyTerm;
    
    private int totalInvestmentAmount;
   
    private PaymentInterval paymentInterval;
    
    //private int installmentAmount;
    
    private  Double totalAmountPaid;
    
    private PolicyStatus status;
  //  private LocalDate nextPaymentDate;
	
}
