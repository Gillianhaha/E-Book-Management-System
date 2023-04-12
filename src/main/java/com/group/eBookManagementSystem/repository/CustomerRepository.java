package com.group.eBookManagementSystem.repository;

import com.group.eBookManagementSystem.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String> {

    Customer findCustomerByUserName(String userName);

    @Transactional
    void deleteCustomerByUserName(String userName);

}
