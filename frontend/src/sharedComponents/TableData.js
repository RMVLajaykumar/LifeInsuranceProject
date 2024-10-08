import React from 'react';
import { Table, Button, Row } from 'react-bootstrap';

const TableData = ({
  data,
  onEdit,
  onToggleStatus,
  customActionButton,
  isCustomerTable = false,
  isViewPolicyTable = false,
  isViewInstallmentTable = false,
  onClaim,
  onWithdraw,
  onInstallment,
  onPayment,
  nextPendingInstallmentId,
  downloadReceipt
}) => {
  if (!data || data.length === 0) {
    return <p>No data available</p>;
  }

  const headers = Object.keys(data[0]);

  return (
    <Table striped bordered>
      <thead>
        <tr>
          {headers.map((header, index) => (
            <th key={index}>{header}</th>
          ))}
          {!isViewPolicyTable && !isViewInstallmentTable && <th>Edit</th>}
          {!isViewPolicyTable && !isViewInstallmentTable && <th>Status</th>}
          {isViewPolicyTable && <th>Claim</th>}
          {isViewPolicyTable && <th>Withdraw</th>}
          {isViewPolicyTable && <th>View Installment</th>}
          {isViewInstallmentTable && <th>Payment</th>}
          {isCustomerTable && <th>Verify Documents</th>}
        </tr>
      </thead>
      <tbody>
        {data.map((row, rowIndex) => (
          <tr key={rowIndex}>
            {headers.map((header, colIndex) => (
              <td key={colIndex}>
                {typeof row[header] === 'object' && row[header] !== null
                  ? JSON.stringify(row[header])
                  : typeof row[header] === 'boolean'
                  ? row[header] ? 'YES' : 'NO'
                  : row[header]}
              </td>
            ))}

            {!isViewPolicyTable && !isViewInstallmentTable && (
              <td>
                <Button variant="info" onClick={() => onEdit(row.agentId || row.customerId)}>
                  Edit
                </Button>
              </td>
            )}

            {!isViewPolicyTable && !isViewInstallmentTable && (
              <td>
                <Button
                  variant={row.active ? 'danger' : 'success'}
                  onClick={() => onToggleStatus(row.agentId || row.customerId, row.active)}
                >
                  {row.active ? 'Deactivate' : 'Activate'}
                </Button>
              </td>
            )}

{isViewPolicyTable && !isViewInstallmentTable && (
  <>
    <td>
      <Button 
        variant="primary" 
        className="mx-1" 
        onClick={() => onClaim(row.policyId)}
        disabled={row.status === "CANCELLED" || row.status === "EXPIRED"||  row.status === "CLAIMED"}
      >
        Claim
      </Button>
    </td>
    <td>
      <Button 
        variant="warning" 
        className="mx-1" 
        onClick={() => onWithdraw(row.policyId)}
        disabled={row.status === "CANCELLED" || row.status === "EXPIRED"}
      >
        Withdraw
      </Button>
    </td>
    <td>
      <Button 
        variant="success" 
        className="mx-1" 
        onClick={() => onInstallment(row.policyId)}
        disabled={row.status === "CANCELLED" || row.status === "EXPIRED"}
      >
        Installment
      </Button>
    </td>
  </>
)}

            {isViewInstallmentTable && (
              <td>
                {row.transactionStatus === 'SUCCESSFUL' ? (
                    <Button
                      variant="success"
                      
                      onClick={() => downloadReceipt(row.transactionId)}
                    >
                      Receipt
                    </Button>
                ) : nextPendingInstallmentId === row.installmentId ? (
                    <Button
                      variant="primary"
                      onClick={() => onPayment(row.installmentAmount)}
                    >
                      Payment
                    </Button>
                ) : (
                    <Button variant="primary" disabled>
                      Payment
                    </Button>
                )}
              </td>
            )}

            {isCustomerTable && (
              <td>
                {customActionButton && customActionButton(row.customerId)}
              </td>
            )}
          </tr>
        ))}
      </tbody>
    </Table>
  );
};

export default TableData;
