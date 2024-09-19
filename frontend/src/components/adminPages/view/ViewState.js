import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Dropdown, DropdownButton, Button } from 'react-bootstrap';
import { useNavigate, useSearchParams } from 'react-router-dom';

import { verifyAdmin } from '../../../services/AuthService';
import { getAllStates, activateState, deactivateState } from '../../../services/AdminService';

import SearchBar from '../../../sharedComponents/SearchBar';
import Pagination from '../../../sharedComponents/Pagination';
import PageSize from '../../../sharedComponents/PageSize';
import TableData from '../../../sharedComponents/TableData2';
import PageDropdown from '../../../sharedComponents/PageDropDown';
import NewToast, { showToast } from '../../../sharedComponents/NewToast';
import BackButton from '../../../sharedComponents/BackButton';
import Loader from '../../../sharedComponents/Loader';

import Header from '../../layout/Header';
import Footer from '../../layout/Footer';

import sanitizeData from '../../../utils/helpers/SanitizedData';
import NewButton from '../../../sharedComponents/NewButton';

const ViewState = () => {
  const [isAdmin, setIsAdmin] = useState(null);
  const [loading, setLoading] = useState(true);
  const [data, setData] = useState([]);
  const [noOfPages, setNoOfPages] = useState(0);

  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const searchQuery = searchParams.get('searchQuery') || '';
  const currentPage = parseInt(searchParams.get('page'), 10) || 1;
  const size = parseInt(searchParams.get('size'), 10) || 10;
  const sortBy = searchParams.get('sortBy') || '';
  const direction = searchParams.get('direction') || '';



  const fetchData = async () => {
    setLoading(true);
    try {
      const params = {
        searchQuery: searchQuery,
        page: currentPage - 1,
        size,
        sortBy,
        direction
      };

      const responseData = await getAllStates(params);
      const sanitizedData = sanitizeData(responseData.content);
      setData(sanitizedData);
      setNoOfPages(responseData.totalPages);
    } catch (error) {
      showToast('Error in Fetching States', 'error');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const checkAdminStatus = async () => {
      try {
        const isAdmin = await verifyAdmin();
        if (isAdmin) {
          setIsAdmin(true);
          fetchData();
        } else {
          showToast('Unauthorized Access! Please Login', 'error');
          setTimeout(() => {
            navigate('/SecureLife.com/login');
          }, 1000);
        }
      } catch (error) {
        showToast('Internal Server Error', 'error');
        setTimeout(() => {
          navigate('/SecureLife.com/login');
        }, 1000);
      }
    };

    checkAdminStatus();
  }, [navigate]);

  useEffect(() => {
    if (isAdmin) {
      fetchData();
    }
  }, [searchQuery, currentPage, size, sortBy,direction, isAdmin]);

  if (isAdmin === null) {
    return null;
  }

  if (loading) {
    return <Loader />;
  }

  const activeState = async (id) => {
    try {
      await activateState(id);
      showToast('State Activated successfully', 'success');
      setTimeout(()=>{
        fetchData();
      },1000);
    } catch (error) {
      showToast('Error in activating state', 'error');
    }
  };

  const deleteState = async (id) => {
    try {
      await deactivateState(id);
      showToast('State Deactivated successfully', 'success');
      setTimeout(()=>{
        fetchData();
      },1000);
    } catch (error) {
      showToast('Error in deactivating state', 'error');
    }
  };

  const getCitiesByState = (id) =>{
    navigate(`/SecureLife.com/admin/states/${id}/cities`)
  }

  const update = (id) =>{
    navigate(`/SecureLife.com/admin/states/${id}/cities`)
  }

  const handleSearch = (newSearchQuery) => {
    if (newSearchQuery !== searchQuery) {
      setLoading(true);
      setSearchParams({
        searchQuery: newSearchQuery,
        page: 1,
        size,
        sortBy,
        direction
      });
    }
  };

  const handlePageChange = (page) => {
    setLoading(true);
    setSearchParams({
      searchQuery,
      page,
      size,
      sortBy,
      direction
    });
  };

  const handleSizeChange = (newSize) => {
    setLoading(true);
    setSearchParams({
      searchQuery,
      page: 1,
      size: newSize,
      sortBy,
      direction
    });
  };

  const handleSortChange = (newSort) =>{
    if(sortBy != newSort){
      setLoading(true);
      setSearchParams({
        searchQuery,
        page: 1,
        size,
        sortBy:newSort,
        direction
      });
    } 
  }
  const handleDirectionChange = (newDirection) => {
    if(direction != newDirection){
      setLoading(true);
      setSearchParams({
        searchQuery,
        page: 1,
        size,
        sortBy,
        direction:newDirection
      });  
    }
  }

  const sortOptions = [
    { label: 'State ID', value: 'stateId' },
    { label: 'Name', value: 'name' },
  ];

  const handleReset = () => {
    setSearchParams({
      searchQuery: '',
      page: 1,
      size: 10,
      sortBy: '',
      direction: '',
    });
  };


  const handleAdd = () => {
    navigate('/SecureLife.com/admin/states/new');
  };

  return (
    <Container fluid className="bg-light text-dark d-flex flex-column min-vh-100 px-0">
      <Header />
      <Container fluid className="py-5" style={{ backgroundColor: 'rgba(230, 242, 255, 0.5)' }}>
        <Row className="m-5">
          <Col xs={6} md={6}>
            <h2>States Data</h2>
          </Col>
          <Col xs={6} md={6} style={{ textAlign: 'right' }}>
            <NewButton text={"Add New State"} handleButton={handleAdd} />
          </Col>
        </Row>
        <Row className="m-5">
            <Col md={2} className='mb-3'>
              <DropdownButton title={sortBy ? sortBy : "Sort By"} variant="outline-secondary">
                {sortOptions.map(option => (
                  <Dropdown.Item key={option.value} onClick={() => handleSortChange(option.value)}>
                    {option.label}
                  </Dropdown.Item>
                ))}
              </DropdownButton>
            </Col>
            <Col md={1} className='mb-3'>
              <DropdownButton title={direction ? direction : "Direction"} variant="outline-secondary">
                <Dropdown.Item onClick={() => handleDirectionChange('asc')}>Asc</Dropdown.Item>
                <Dropdown.Item onClick={() => handleDirectionChange('desc')}>Desc</Dropdown.Item>
              </DropdownButton>
            </Col>
            <Col md={4} className="page-size-container-center">
              <SearchBar onSearch={handleSearch} defaultValue={searchQuery} />
            </Col>
            <Col md={1} className='mb-3'>
              <Button variant="secondary" onClick={handleReset}>Reset</Button>
            </Col>
          <Col md={4} className="page-size-container-right d-flex justify-content-end">
            <PageDropdown noOfPages={noOfPages} currentPage={currentPage} setPageNo={handlePageChange} />
            <PageSize size={size} setSize={handleSizeChange} />
          </Col>
        </Row>
        <div>
          <Row className="m-5">
            <TableData data={data} fetchData={fetchData} status={"State"} activateRow={activeState} deactivateRow={deleteState} 
              getInsideData={getCitiesByState}/>
          </Row>
          <Row className="m-5">
            <Col xs={6} md={6}>
              <Pagination noOfPages={noOfPages} currentPage={currentPage} setPageNo={handlePageChange} />
            </Col>
            <Col xs={6} md={6} style={{ textAlign: 'right' }}>
              <BackButton />
            </Col>
          </Row>
        </div>
      </Container>
      <Footer />
      <NewToast />
    </Container>
  );
};

export default ViewState;
