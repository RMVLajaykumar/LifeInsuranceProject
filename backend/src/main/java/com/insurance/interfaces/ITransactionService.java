package com.insurance.interfaces;

import java.util.List;

import com.insurance.response.TransactionResponse;
import com.insurance.util.PagedResponse;

public interface ITransactionService {

	PagedResponse<TransactionResponse> getAllTransactions(int page, int size, String sortBy, String direction);

	List<TransactionResponse> TransctionByPolicyId(String policyid);

	PagedResponse<TransactionResponse> transactionWithParamsByPolicyId(String policyid, int page, int size, String sortBy, String direction);

}
