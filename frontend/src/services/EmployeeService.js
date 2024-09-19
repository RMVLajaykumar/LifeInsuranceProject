import axios from 'axios';
import { UnAuthorized, NotFoundError, ValidationError, InternalServerError } from '../utils/errors/Error';

const getToken = () => localStorage.getItem('token');

const handleErrors = (error) => {
  if (error.response) {
    const { status } = error.response;
    if (status === 403) {
      throw new UnAuthorized("User is unauthorized");
    } else if (status === 404) {
      throw new NotFoundError("Resource not found");
    } else if (status === 400) {
      throw new ValidationError("Invalid data provided");
    } else if (status === 500) {
      throw new InternalServerError("Internal server error");
    }
  }
  throw new ValidationError("An error occurred while processing your request");
};

export const addAgent = async (agentDetails) => {
  const token = getToken();
  if (!token) throw new UnAuthorized("User is not logged in");

  try {
    const response = await axios.post('http://localhost:8081/SecureLife.com/agents', agentDetails, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    });

    return response.data;
  } catch (error) {
    handleErrors(error);
  }
};

export const getAllAgents = async (params) => {
  const token = getToken();
  if (!token) throw new UnAuthorized("User is not logged in");

  try {
    const response = await axios.get('http://localhost:8081/SecureLife.com/agents', {
      params,
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    handleErrors(error);
  }
};

export const updateAgent = async (agentId, agentDetails, headers = {}) => {
  const token = getToken();
  if (!token) throw new UnAuthorized("User is not logged in");

  try {
    const response = await axios.put(`http://localhost:8081/SecureLife.com/agent/${agentId}`, agentDetails, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
        ...headers,
      },
    });
    return response.data;
  } catch (error) {
    handleErrors(error);
  }
};

export const toggleAgentStatus = async (agentId, isActive) => {
  const token = getToken();
  if (!token) throw new UnAuthorized("User is not logged in");

  const endpoint = isActive
    ? `http://localhost:8081/SecureLife.com/agent/${agentId}/active`
    : `http://localhost:8081/SecureLife.com/agent/${agentId}/deactivate`;

  try {
    const response = await axios.put(endpoint, {}, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    handleErrors(error);
  }
};

export const getAllCustomers = async (params) => {
  const token = getToken();
  if (!token) throw new UnAuthorized("User is not logged in");

  try {
    const response = await axios.get('http://localhost:8081/SecureLife.com/customers', {
      params,
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    handleErrors(error);
  }
};

export const getCustomerDocuments = async (customerId) => {
  const token = getToken();
  if (!token) throw new UnAuthorized("User is not logged in");

  try {
    const response = await axios.get(`http://localhost:8081/SecureLife.com/customer/${customerId}/documents`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    handleErrors(error);
  }
};

export const downloadDocument = async (documentId, documentNameFromUI) => {
  const token = getToken();
  if (!token) throw new UnAuthorized("User is not logged in");

  try {
    const response = await axios.get(`http://localhost:8081/SecureLife.com/document/${documentId}/download`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      responseType: 'blob',
    });

    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;

    const contentDisposition = response.headers['content-disposition'];
    let fileName = documentNameFromUI;

    if (contentDisposition) {
      const fileNameMatch = contentDisposition.match(/filename="?(.+)"?/);
      if (fileNameMatch && fileNameMatch.length > 1) {
        fileName = fileNameMatch[1];
      }
    }

    const contentType = response.headers['content-type'];
    if (!fileName.includes('.')) {
      if (contentType.includes('jpeg') || contentType.includes('jpg')) {
        fileName += '.jpeg';
      } else if (contentType.includes('pdf')) {
        fileName += '.pdf';
      } else if (contentType.includes('png')) {
        fileName += '.png';
      } else if (contentType.includes('application/msword')) {
        fileName += '.doc';
      } else if (contentType.includes('application/vnd.openxmlformats-officedocument.wordprocessingml.document')) {
        fileName += '.docx';
      }
    }

    link.setAttribute('download', fileName);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  } catch (error) {
    handleErrors(error);
  }
};

export const approveCustomer = async (customerId) => {
  const token = getToken();
  if (!token) throw new UnAuthorized("User is not logged in");

  try {
    const response = await axios.put(`http://localhost:8081/SecureLife.com/customer/${customerId}/approve`, {}, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    handleErrors(error);
  }
};

export const rejectCustomer = async (customerId) => {
  const token = getToken();
  if (!token) throw new UnAuthorized("User is not logged in");

  try {
    const response = await axios.put(`http://localhost:8081/SecureLife.com/customer/${customerId}/reject`, {}, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    handleErrors(error);
  }
};

export const updateCustomer = async (customerId, customerDetails, headers = {}) => {
  const token = getToken();
  if (!token) throw new UnAuthorized("User is not logged in");

  try {
    const response = await axios.put(`http://localhost:8081/SecureLife.com/customer/${customerId}/update`, customerDetails, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
        ...headers,
      },
    });
    return response.data;
  } catch (error) {
    handleErrors(error);
  }
};

export const toggleCustomerStatus = async (customerId, isActive) => {
  const token = getToken();
  if (!token) throw new UnAuthorized("User is not logged in");

  const endpoint = isActive
    ? `http://localhost:8081/SecureLife.com/customer/${customerId}/activate`
    : `http://localhost:8081/SecureLife.com/customer/${customerId}/deactivate`;

  try {
    const response = await axios.put(endpoint, {}, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    handleErrors(error);
  }
};

export const getCustomerById = async (customerId) => {
  const token = getToken();
  if (!token) throw new UnAuthorized("User is not logged in");

  try {
    const response = await axios.get(`http://localhost:8081/SecureLife.com/customers/${customerId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    handleErrors(error);
  }
};

export const getCommissionByAgentId = async (agentId, page = 0, size = 5, sortBy = 'policyId', direction = 'asc') => {
  const token = getToken();
  if (!token) throw new UnAuthorized("User is not logged in");

  try {
    const response = await axios.get(`http://localhost:8081/SecureLife.com/agent/${agentId}/commissions`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      params: {
        page,
        size,
        sortBy,
        direction,
      },
    });

    return response.data;
  } catch (error) {
    handleErrors(error);
  }
};
