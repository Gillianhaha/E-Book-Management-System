package com.group.eBookManagementSystem.controller;

import com.group.eBookManagementSystem.model.Book;
import com.group.eBookManagementSystem.model.Customer;
import com.group.eBookManagementSystem.service.BookService;
import com.group.eBookManagementSystem.service.CustomerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EBookManagementController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BookService bookService;

    @PostMapping("/addCustomer")
    public void addCustomer(@RequestBody Customer customer) throws Exception {
        customerService.addCustomer(customer);
    }

    @GetMapping("/listCustomers")
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/findCustomerByUserName/{userName}")
    public Customer findCustomerByUserName(@PathVariable String userName) {
        return customerService.findCustomerByUserName(userName);
    }

    @PostMapping("/updateCustomer")
    public void updateCustomer(@RequestBody Customer customer) {
        customerService.updateCustomer(customer);
    }

    @PostMapping("/deleteCustomer")
    public void deleteCustomer(@RequestParam String userName) {
        customerService.deleteCustomer(userName);
    }

    @PostMapping("/addBook")
    public void addBook(@RequestBody Book book) {
        bookService.addBook(book);
    }

    @GetMapping("/listBooks")
    public List<Book> getBooks() {
        return bookService.getBooks();
    }

    @GetMapping("/findBookById/{id}")
    public Book findBookById(@PathVariable Integer id) {
        return bookService.findBookById(id);
    }

    @PostMapping("/updateBook")
    public void updateBook(@RequestBody Book book) {
        bookService.updateBook(book);
    }

    @PostMapping("/deleteBook")
    public void deleteBook(@RequestParam Integer id) {
        bookService.deleteBook(id);
    }

    @PostMapping("/rateBook/{id}")
    public void rateBook(@PathVariable Integer id, @RequestParam Integer rate) {
        bookService.rateBook(id, rate);
    }

    @GetMapping("/login")
    public void login(@RequestParam String userName, @RequestParam String password) {
        customerService.logInUser(userName, password);
    }

    @GetMapping("/getAdminName")
    public String getAdminName() {
        return customerService.getAdminName();
    }
}
