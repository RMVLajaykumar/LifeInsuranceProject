package com.insurance.interfaces;

import com.insurance.request.AdminTransactionRequest;
import com.insurance.request.AmountRequest;
import com.insurance.request.PaymentRequestDto;
import com.stripe.exception.StripeException;

public interface IPaymentService {

	String processPayment(String token, PaymentRequestDto paymentRequestDto) throws StripeException;

//	Double calculatePayment(String token, AmountRequest amountRequest);
//
//	String refundAmount(String token, AdminTransactionRequest adminTransactionRequest) throws StripeException;

	double calculateTotalAmount(double installmentAmount, String policy_id);

}
