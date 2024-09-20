package com.insurance.response;

import com.insurance.enums.ClaimStatus;

import lombok.Data;
@Data
public class ClaimResponse {

    private Long claimId;
    private String policyId;
    
    private String explanation;
    private ClaimStatus status;
 private String customerId;
    public ClaimResponse() {
    }

    

    public Long getClaimId() {
        return claimId;
    }

    public void setClaimId(Long claimId) {
        this.claimId = claimId;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }
}
