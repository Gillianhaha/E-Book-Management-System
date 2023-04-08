package com.group.eBookManagementSystem.repository;

import com.group.eBookManagementSystem.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Customer findCustomerById(Integer id);
    Customer deleteCustomerById(Integer id);
//    Customer findCustomerByFirstName(String firstName);

}
