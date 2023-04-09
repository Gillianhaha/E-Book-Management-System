package com.group.eBookManagementSystem.service;

import com.group.eBookManagementSystem.repository.CustomerRepository;
import com.group.eBookManagementSystem.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CustomerService {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);
    @Autowired
    private CustomerRepository customerRepository;

    public void addCustomer(String firstName, String lastName, String password, String email, String role) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPassword(password);
        customer.setEmail(email);
        customer.setMyLibrary(List.of(1,2,3));
        if(Objects.equals(role, "Admin")) {
            customer.setRole(Customer.Role.ADMIN);
        }
        else {
            customer.setRole(Customer.Role.USER);
        }
        customerRepository.save(customer);
    }

    public Iterable<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    public Customer findCustomerById(Integer id) {
        return customerRepository.findCustomerById(id);
    }

    public void updateCustomer(Integer id, String firstName,String lastName,String email) {
        Customer customer = customerRepository.findCustomerById(id);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);

        customerRepository.save(customer);
    }
    public void updateCustomer(Integer id, Customer customer){
        customerRepository.save(customer);
    }

    public void deleteCustomer(Integer id){
        customerRepository.deleteCustomerById(id);
    }
    public void addBook(Integer customerID,Integer bookID){
        Customer customer = findCustomerById(customerID);
        List<Integer> library = customer.getMyLibrary();
        library.add(bookID);
        customer.setMyLibrary(library);

        customerRepository.save(customer);
    }
    public void deleteBook(Integer customerID,Integer bookID){
        Customer customer = findCustomerById(customerID);
        List<Integer> library = customer.getMyLibrary();
        library.remove(bookID);
        customer.setMyLibrary(library);

        customerRepository.save(customer);
    }

    public boolean verifyUser(Integer id, String password){
        boolean res = password.equals(customerRepository.findCustomerById(id).getPassword());
        LOG.info(String.format("Result of verify user %b",res));
        return res;
    }
}
