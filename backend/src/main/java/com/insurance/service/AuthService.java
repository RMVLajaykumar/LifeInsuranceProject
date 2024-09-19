package com.insurance.service;

import com.insurance.entities.Admin;
import com.insurance.entities.Agent;
import com.insurance.entities.Customer;
import com.insurance.entities.Employee;
import com.insurance.entities.Role;
import com.insurance.entities.User;
import com.insurance.enums.CreationStatusType;
import com.insurance.exceptions.ApiException;
import com.insurance.exceptions.ResourceNotFoundException;
import com.insurance.interfaces.IAuthService;
import com.insurance.repository.AdminRepository;
import com.insurance.repository.AgentRepository;
import com.insurance.repository.CustomerRepository;
import com.insurance.repository.EmployeeRepository;
import com.insurance.repository.RoleRepository;
import com.insurance.repository.UserRepository;
import com.insurance.request.LoginDto;
import com.insurance.request.ProfileRequest;
import com.insurance.response.ProfileResponse;
import com.insurance.request.AdminRegisterRequest;
import com.insurance.request.ChangePasswordRequest;
import com.insurance.request.EmployeeRegisterRequest;
import com.insurance.security.JwtTokenProvider;
import com.insurance.util.UniqueIdGenerator;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements IAuthService {
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	UniqueIdGenerator uniqueIdGenerator;
	
	@Autowired
	private AgentRepository agentRepository;
	
	@Autowired
	EmailService emailService;
	
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private CustomerRepository customerRepository;
    private AdminRepository adminRepository;
    private EmployeeRepository employeeRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    public AuthService(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           CustomerRepository customerRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           AdminRepository adminRepository,
                           EmployeeRepository employeeRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.adminRepository = adminRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public String login(LoginDto loginDto) {
        logger.info("Attempting login for username/email: {}", loginDto.getUsernameOrEmail());
        String usernameOrEmail = loginDto.getUsernameOrEmail();
        String password = loginDto.getPassword();
      
        UsernamePasswordAuthenticationToken temp = new UsernamePasswordAuthenticationToken(usernameOrEmail, password);
        Authentication authentication = authenticationManager.authenticate(temp);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        logger.info("Generated JWT token for user: {}", usernameOrEmail);
      
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).get();
        if (!user.isActive()) {
            logger.warn("Login attempt for inactive user: {}", usernameOrEmail);
            throw new ResourceNotFoundException("User does not exist");
        }
      
        logger.info("Login successful for user: {}", usernameOrEmail);
        return token;
    }

//    @Override
//    public String registerAdmin(AdminRegisterRequest registerDto) {
//        logger.info("Registering new admin with username: {}", registerDto.getUsername());
//        if (userRepository.existsByUsername(registerDto.getUsername())) {
//            logger.warn("Username already exists: {}", registerDto.getUsername());
//            throw new ApiException("Username already exists!");
//        }
//
//        if (userRepository.existsByEmail(registerDto.getEmail())) {
//            logger.warn("Email already exists: {}", registerDto.getEmail());
//            throw new ApiException("Email already exists!");
//        }
//      
//        User user = new User();
//        user.setUserId(uniqueIdGenerator.generateUniqueId(User.class));
//        user.setUsername(registerDto.getUsername());
//        user.setEmail(registerDto.getEmail());
//        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
//        Optional<Role> oRole = roleRepository.findByName("Role_Admin");
//        if (oRole.isEmpty()) {
//            logger.error("Role not found for Role_Admin");
//            throw new ResourceNotFoundException("Role not found");
//        }
//      
//        user.setRole(oRole.get());
//        user.setActive(true);
//        user.setCreatedAt(LocalDateTime.now());
//        user.setUpdatedAt(LocalDateTime.now());
//        userRepository.save(user);
//
//        Admin admin = new Admin();
//        logger.info("Saving new admin details for username: {}", registerDto.getUsername());
//        admin.setAdminId(uniqueIdGenerator.generateUniqueId(Admin.class));
//        admin.setName(registerDto.getName());
//        admin.setUser(user);
//        admin.setPhoneNumber(registerDto.getPhoneNumber());
//        adminRepository.save(admin);
//
//        String subject = "Welcome to SecureLife Insurance - Your Admin Account Has Been Created!";
//        String emailBody = "Dear " + registerDto.getName() + ",\n\n" +
//                           "Congratulations! Your admin account has been successfully created at SecureLife Insurance. " +
//                           "You are now a part of our trusted administrative team overseeing important aspects of the company.\n\n" +
//                           "Best Regards,\n" +
//                           "SecureLife Insurance Team";
//
//        
//        //emailService.sendEmail(user.getEmail(), subject, emailBody);
//
//        logger.info("Admin registered successfully with username: {}", registerDto.getUsername());
//        return "Admin registered successfully!";
//    }

    @Override
    public String getRole(String token) {
        logger.info("Fetching role for token");
        String username = jwtTokenProvider.getUsername(token);
        Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);
        if (oUser.isEmpty()) {
            logger.warn("User not found for token");
            throw new ResourceNotFoundException("User not found");
        }
      
        User user = oUser.get();
        String roleName = user.getRole().getName();
        logger.info("Role fetched for user {}: {}", username, roleName);
        return roleName;
    }
    

    @Override
    @Transactional
    public String profileUpdate(String token, ProfileRequest profileRequest) {
        logger.info("Updating profile for token");
        String username = jwtTokenProvider.getUsername(token);
        Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);
        if (oUser.isEmpty()) {
            logger.warn("User not found for token");
            throw new ResourceNotFoundException("User is not available");
        }
      
        User user = oUser.get();
        String role = user.getRole().getName();
        logger.info("Profile update for role: {}", role);
      
        if (role.equalsIgnoreCase("role_admin")) {
            Admin admin = adminRepository.findByUser(user);
            admin.setName(profileRequest.getName());
            admin.setPhoneNumber(profileRequest.getPhoneNumber());
            adminRepository.save(admin);
            logger.info("Admin profile updated successfully for username: {}", username);
        } else if (role.equalsIgnoreCase("role_employee")) {
            Employee employee = employeeRepository.findByUser(user).get();
            employee.setName(profileRequest.getName());
            employee.setAddress(profileRequest.getAddress());
            employee.setPhoneNumber(profileRequest.getPhoneNumber());
            employeeRepository.save(employee);
            logger.info("Employee profile updated successfully for username: {}", username);
        } else if (role.equalsIgnoreCase("role_agent")) {
            Agent agent = agentRepository.findByUser(user);
            agent.setName(profileRequest.getName());
            agent.setAddress(profileRequest.getAddress());
            agent.setPhoneNumber(profileRequest.getPhoneNumber());
            agentRepository.save(agent);
            logger.info("Agent profile updated successfully for username: {}", username);
        } else if (role.equalsIgnoreCase("role_customer")) {
            Customer customer = customerRepository.findByUser(user);
            customer.setName(profileRequest.getName());
            customer.setAddress(profileRequest.getAddress());
            customer.setPhoneNumber(profileRequest.getPhoneNumber());
            customerRepository.save(customer);
            logger.info("Customer profile updated successfully for username: {}", username);
        }
      
        user.setUsername(profileRequest.getUsername());
        user.setEmail(profileRequest.getEmail());
//        user.setPassword(passwordEncoder.encode(profileRequest.getPassword()));
        userRepository.save(user);
        String subject = "SecureLife Insurance - Your Profile Has Been Updated!";
        String emailBody = "Dear " + profileRequest.getName() + ",\n\n" +
                           "Your profile has been successfully updated. If you did not make these changes, please contact our support team immediately.\n\n" +
                           "Best Regards,\n" +
                           "SecureLife Insurance Team";

        emailService.sendEmail(user.getEmail(), subject, emailBody);
        
      
        logger.info("Profile updated successfully for user: {}", username);
        return "Profile updated successfully";
    }

    @Override
    public String changePassword(String token, ChangePasswordRequest profileRequest) {
        String username = jwtTokenProvider.getUsername(token);
        Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);
        if (oUser.isEmpty()) {
            logger.warn("User not available for token: {}", token);
            throw new ResourceNotFoundException("User is not available");
        }
        User user = oUser.get();
  
        if (!passwordEncoder.matches(profileRequest.getCurrentPassword(), user.getPassword())) {
            throw new ApiException("Current password is wrong");
        }
        if (!profileRequest.getConfirmPassword().equals(profileRequest.getNewPassword())) {
            throw new ApiException("Confirm password is different from new password");
        } else {
            user.setPassword(passwordEncoder.encode(profileRequest.getNewPassword()));
            userRepository.save(user);
            String subject = "SecureLife Insurance - Password Changed Successfully";
            String emailBody = "Dear " + user.getUsername() + ",\n\n" +
                               "Your password has been successfully updated. If you did not make this change, please contact our support team immediately.\n\n" +
                               "Best Regards,\n" +
                               "SecureLife Insurance Team";

            
            emailService.sendEmail(user.getEmail(), subject, emailBody);
        }

        return "Password updated successfully";
    }

	@Override
	public String getName(String token) {
		  logger.info("Fetching role for token");
	        String username = jwtTokenProvider.getUsername(token);
	        Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);
	        if (oUser.isEmpty()) {
	            logger.warn("User not found for token");
	            throw new ResourceNotFoundException("User not found");
	        }
	            User user = oUser.get();
	            String name = user.getUsername();
	            logger.info("Role fetched for user {}: {}", username, name);
	            return name;
	        
	}
	@Override
	  public ProfileResponse getProfile(String token) {
	    logger.info("Updating profile for token");
	        String username = jwtTokenProvider.getUsername(token);
	        Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);
	        if (oUser.isEmpty()) {
	            logger.warn("User not found for token");
	            throw new ResourceNotFoundException("User is not available");
	        }
	        User user = oUser.get();
	        
	        ProfileResponse profile = new ProfileResponse();
	        String role = user.getRole().getName();
	        profile.setRole(role);
	        if (role.equalsIgnoreCase("role_admin")) {
	            Admin admin = adminRepository.findByUser(user);
	            profile.setName(admin.getName());
	            profile.setAddress("N/A");
	            profile.setPhoneNumber(admin.getPhoneNumber());
	       } else if (role.equalsIgnoreCase("role_employee")) {
	            Employee employee = employeeRepository.findByUser(user).get();
	            profile.setName(employee.getName());
	            profile.setAddress(employee.getAddress());
	            profile.setPhoneNumber(employee.getPhoneNumber());
	            } else if (role.equalsIgnoreCase("role_agent")) {
	            Agent agent = agentRepository.findByUser(user);
	            profile.setName(agent.getName());
	            profile.setAddress(agent.getAddress());
	            profile.setPhoneNumber(agent.getPhoneNumber());
	        } else if (role.equalsIgnoreCase("role_customer")) {
	            Customer customer = customerRepository.findByUser(user);
	            profile.setName(customer.getName());
	            profile.setAddress(customer.getAddress());
	            profile.setPhoneNumber(customer.getPhoneNumber());
	        }
	        profile.setEmail(user.getEmail());
	        profile.setUsername(user.getUsername());
	    return profile;
	  }
	@Override
	  public String getUsername(String token) {
	    String username = jwtTokenProvider.getUsername(token);
	        Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);
	        if (oUser.isEmpty()) {
	            logger.warn("User not found for token");
	            throw new ResourceNotFoundException("User not found");
	        }
	      
	        User user = oUser.get();    
	        return user.getUsername();
	  }

}
