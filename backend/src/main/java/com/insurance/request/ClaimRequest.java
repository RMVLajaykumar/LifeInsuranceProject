package com.insurance.request;

import java.util.List;

import lombok.Data;
@Data
public class ClaimRequest {
    
   
	
//	    private List<String> documentIds;  
	    private String explanation;
	    private String documentId;

	    
//	    public List<String> getDocumentIds() {
//	        return documentIds;
//	    }
//
//	    public void setDocumentIds(List<String> documentIds) {
//	        this.documentIds = documentIds;
//	    }

	    public String getExplanation() {
	        return explanation;
	    }

	    public void setExplanation(String explanation) {
	        this.explanation = explanation;
	    }
	}

