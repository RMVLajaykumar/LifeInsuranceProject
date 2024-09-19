import React, { useEffect, useState } from 'react';
import { Modal, Button, Spinner } from 'react-bootstrap';
import { calculateTotalPaymentAmount } from '../../services/CustomerService';
import { errorToast } from '../../sharedComponents/MyToast';

const CalculateAmountModal = ({ show, handleClose, installmentAmount, onProceed }) => {
  const [totalAmount, setTotalAmount] = useState(null);
  const [isFetchingTotal, setIsFetchingTotal] = useState(true); 

  
  useEffect(() => {
    const fetchTotalAmount = async () => {
      if (!installmentAmount) return;

      try {
        setIsFetchingTotal(true);
        console.log('Fetching total amount for installment:', installmentAmount);
        const total = await calculateTotalPaymentAmount(installmentAmount);
        console.log('Total amount received:', total);
        setTotalAmount(total);
      } catch (error) {
        console.error('Error fetching total amount:', error);
        errorToast('Failed to calculate total amount.');
        setTotalAmount('Error');
      } finally {
        setIsFetchingTotal(false); 
      }
    };

    fetchTotalAmount();
  }, [installmentAmount]);

  return (
    <Modal show={show} onHide={handleClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Installment Amount</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p>Here is the total amount including tax:</p>
        {isFetchingTotal ? (
          <Spinner animation="border" /> 
        ) : totalAmount === 'Error' ? (
          <p>Error calculating total amount</p>
        ) : (
          <p><strong>{totalAmount}</strong> INR</p>
        )}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Cancel
        </Button>
        <Button
          variant="primary"
          onClick={() => onProceed(totalAmount)} 
          disabled={isFetchingTotal || totalAmount === 'Error'}
        >
          Proceed to Payment
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default CalculateAmountModal;
