package com.insurance.entities;


import java.util.List;

import com.insurance.enums.ClaimStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Claim {


		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long claimId;

	    @ManyToOne
	    @JoinColumn(name = "policy_id", nullable = false)
	    private Policy policy;

	    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, orphanRemoval = true) 
	    private List<Document> documents;

	    @Column(nullable = false)
	    private String explanation;

	    @Enumerated(EnumType.STRING)
	    private ClaimStatus status = ClaimStatus.PENDING;

	    
	    public Long getClaimId() {
	        return claimId;
	    }

	    public void setClaimId(Long claimId) {
	        this.claimId = claimId;
	    }

	    public Policy getPolicy() {
	        return policy;
	    }

	    public void setPolicy(Policy policy) {
	        this.policy = policy;
	    }

	    public List<Document> getDocuments() {
	        return documents;
	    }

	    public void setDocuments(List<Document> documents) {
	        this.documents = documents;
	        for (Document doc : documents) {
	            doc.setClaim(this);  
	        }
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

