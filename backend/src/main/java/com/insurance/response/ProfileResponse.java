package com.insurance.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileResponse {
	
	
	private String name;
	
	
	private String password;
    
    private String email;
    
    private String address;
    
    private String phoneNumber;
    
    private String username;
    
    private String role;

}
