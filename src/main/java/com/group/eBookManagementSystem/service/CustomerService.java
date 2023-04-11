package com.group.eBookManagementSystem.service;

import com.group.eBookManagementSystem.model.Customer;
import com.group.eBookManagementSystem.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);
    @Autowired
    private CustomerRepository customerRepository;

    public void addCustomer(Customer customer) throws Exception {
        if (!checkExist(customer.getUserName())) {
            List<Customer> customerList = (List<Customer>) getCustomers();
            if (customerList.size() == 0) {
                customer.setRole(Customer.Role.ADMIN);
            } else {
                customer.setRole(Customer.Role.USER);
            }
            customerRepository.save(customer);
        } else {
            throw new Exception("UserName Occupied.");
        }
    }

    public Iterable<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    public Customer findCustomerByUserName(String userName) {
        return customerRepository.findCustomerByUserName(userName);
    }

//    public void updateCustomer(String userName, String firstName, String lastName, String email) {
//        Customer customer = customerRepository.findCustomerByUserName(userName);
//        customer.setFirstName(firstName);
//        customer.setLastName(lastName);
//        customer.setEmail(email);
//
//        customerRepository.save(customer);
//    }

    public void updateCustomer(String userName, Customer customer) {
        customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(String userName) {
        LOG.info("userName:", userName);
        customerRepository.deleteCustomerByUserName(userName);
    }

    public void addBookToCustomer(String userName, Integer bookID) {
        Customer customer = findCustomerByUserName(userName);
        List<Integer> library = customer.getMyLibrary();
        library.add(bookID);
        customer.setMyLibrary(library);

        customerRepository.save(customer);
    }

    public void deleteBookFromCustomer(String userName, Integer bookID) {
        Customer customer = findCustomerByUserName(userName);
        List<Integer> library = customer.getMyLibrary();
        library.remove(bookID);
        customer.setMyLibrary(library);

        customerRepository.save(customer);
    }

    public boolean verifyUser(String userName, String password) {
        boolean res = password.equals(customerRepository.findCustomerByUserName(userName).getPassword());
        LOG.info(String.format("Result of verify user %b", res));
        return res;
    }

    public boolean checkExist(String userName) {
        return customerRepository.existsById(userName);
    }

    public String getAdminName() {
        List<Customer> customerList = (List<Customer>) getCustomers();
        for (Customer customer : customerList) {
            if (customer.getRole().equals(Customer.Role.ADMIN)) {
                return customer.getUserName();
            }
        }
        return "";
    }

    public void removeBookForAllCustomers(Integer bookID) {
        List<Customer> customerList = (List<Customer>) getCustomers();
        for (Customer customer : customerList) {
            List<Integer> myLibrary = customer.getMyLibrary();
            myLibrary.remove(bookID);
            customer.setMyLibrary(myLibrary);
            customerRepository.save(customer);
        }
    }
}
