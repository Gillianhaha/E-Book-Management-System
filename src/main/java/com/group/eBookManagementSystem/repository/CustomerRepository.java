package com.group.eBookManagementSystem.repository;

import com.group.eBookManagementSystem.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String> {

    Customer findCustomerByUserName(String userName);

    Customer deleteCustomerByUserName(String userName);

//    Customer findCustomerByFirstName(String firstName);

}
