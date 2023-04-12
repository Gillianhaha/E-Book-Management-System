package com.group.eBookManagementSystem.service;

import com.group.eBookManagementSystem.model.Customer;
import com.group.eBookManagementSystem.repository.BookRepository;
import com.group.eBookManagementSystem.repository.CustomerRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BookRepository bookRepository;

    public void addCustomer(Customer customer) throws Exception {
        String userName = customer.getUserName();
        if (existByUserName(userName)) {
            throw new IllegalArgumentException(String.format("UserName Occupied: %s", userName));
        }

        verifyBookList(customer);

        setCustomerRole(customer);

        customerRepository.save(customer);
    }

    private void setCustomerRole(Customer customer) {
        List<Customer> customerList = (List<Customer>) getCustomers();
        if (customerList.size() == 0) {
            customer.setRole(Customer.Role.ADMIN);
        } else {
            customer.setRole(Customer.Role.USER);
        }
    }

    private void verifyBookList(Customer customer) {
        List<Integer> bookIdList = customer.getMyLibrary();
        for (Integer bookId : bookIdList) {
            if (!bookRepository.existsById(bookId)) {
                throw new IllegalArgumentException(String.format("Book %d does not exist.", bookId));
            }
        }
    }

    public List<Customer> getCustomers() {
        return (List<Customer>) customerRepository.findAll();
    }

    public Customer findCustomerByUserName(String userName) {
        if (existByUserName(userName)) {
            return customerRepository.findCustomerByUserName(userName);
        } else {
            throw new IllegalArgumentException(String.format("User %s does not exist.", userName));
        }
    }

    public void updateCustomer(Customer customer) {
        String userName = customer.getUserName();
        if (!existByUserName(userName)) {
            throw new IllegalArgumentException(String.format("User %s does not exist.", userName));
        }

        verifyBookList(customer);

        customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(String userName) {
        if (existByUserName(userName)) {
            customerRepository.deleteCustomerByUserName(userName);
        } else {
            throw new IllegalArgumentException(String.format("User %s does not exist.", userName));
        }
    }

    public void logInUser(String userName, String password) {
        if (existByUserName(userName)) {
            if (!password.equals(customerRepository.findCustomerByUserName(userName).getPassword())) {
                throw new IllegalArgumentException("UserName and Password don't match");
            }
        } else {
            throw new IllegalArgumentException(String.format("User %s does not exist.", userName));
        }
    }

    public boolean existByUserName(String userName) {
        return customerRepository.existsById(userName);
    }

    public String getAdminName() {
        List<Customer> customerList = (List<Customer>) getCustomers();
        for (Customer customer : customerList) {
            if (customer.getRole().equals(Customer.Role.ADMIN)) {
                return customer.getUserName();
            }
        }
        throw new IllegalArgumentException("No Admin found");
    }

    public void removeBookForAllCustomers(Integer bookId) {
        if (bookRepository.existsById(bookId)) {
            List<Customer> customerList = (List<Customer>) getCustomers();
            for (Customer customer : customerList) {
                List<Integer> myLibrary = customer.getMyLibrary();
                myLibrary.remove(bookId);
                customer.setMyLibrary(myLibrary);
                customerRepository.save(customer);
            }
        } else {
            throw new IllegalArgumentException(String.format("Book with bookId %s does not exist.", bookId));
        }
    }
}
