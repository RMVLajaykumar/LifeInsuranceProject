package com.insurance.interfaces;

public interface IDashboardService {

	long getTotalAdmins();

	long getTotalAgents();

	long getTotalCustomers();

	long getTotalEmployees();

	Double getMyWithdrawals(String token);

	Double getMyCommissions(String token);

	Long getSoldPolicies(String token);

	Long getCancelledPolicies(String token);

}
