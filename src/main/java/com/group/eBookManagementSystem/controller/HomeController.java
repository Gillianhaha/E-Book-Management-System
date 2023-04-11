package com.group.eBookManagementSystem.controller;

import com.group.eBookManagementSystem.model.Book;
import com.group.eBookManagementSystem.model.Customer;
import com.group.eBookManagementSystem.service.BookService;
import com.group.eBookManagementSystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/addCustomer")
    public String addCustomer(@RequestBody Customer customer) throws Exception {
        customerService.addCustomer(customer);
        return "Add new customer successfully!";
    }

    @GetMapping("/listCustomers")
    public Iterable<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/findCustomerByUserName/{userName}")
    public Customer findCustomerByUserName(@PathVariable String userName) {
        return customerService.findCustomerByUserName(userName);
    }

    //    @PostMapping("/updateCustomerByID/{id}")
//    public String updateCustomer(@PathVariable Integer id, @RequestParam String firstName, @RequestParam String lastName,@RequestParam String passwaord,@RequestParam String email) {
//        customerService.updateCustomer(id, firstName, lastName, email);
//
//        return "Updated customer given ID: " + id.toString();
//    }
    @PostMapping("/updateCustomerByUserName/{userName}")
    public String updateCustomer(@PathVariable String userName, @RequestBody Customer customer) {
        customerService.updateCustomer(userName, customer);

        return "Updated customer with given UserName: " + userName;
    }

    @PostMapping("/deleteCustomer")
    public String deleteCustomer(@RequestParam String userName) {
        customerService.deleteCustomer(userName);
        return "Delete the customer successfully!";
    }

    @Autowired
    private BookService bookService;

    @PostMapping("/addBook")
    public String addBook(@RequestBody Book book) {
        bookService.addBook(book);
        return "Add new book successfully!";
    }

    @GetMapping("/listBooks")
    public Iterable<Book> getBooks() {
        return bookService.getBooks();
    }

    @GetMapping("/findBookByID/{id}")
    public Book findBookById(@PathVariable Integer id) {
        return bookService.findBookById(id);
    }

    //    @PostMapping("/updateBookByID/{id}")
//    public String updateBook(@PathVariable Integer id, @RequestParam String bookName, @RequestParam String author, @RequestParam String subject, @RequestParam String content) {
//        bookService.updateBook(id, bookName, author, subject, content);
//
//        return "Updated book with given ID: " + id.toString();
//    }
    @PostMapping("/updateBookByID/{id}")
    public String updateCustomer(@PathVariable Integer id, @RequestBody Book book) {
        bookService.updateBook(id, book);

        return "Updated book given with BookID: " + id;
    }

    @PostMapping("/deleteBook")
    public String deleteBook(@RequestParam Integer id) {
        bookService.deleteBook(id);
        return "Delete the book successfully!";
    }

    @PostMapping("/rateBook/{id}")
    public String rateBook(@PathVariable Integer id, @RequestParam Integer rate) {
        bookService.rateBook(id, rate);

        return "Rated book given ID: " + id.toString();
    }

    @PostMapping("/addBookToCustomer/{userName}")
    public String addBookToCustomer(@PathVariable String userName, @RequestParam Integer bookID) {
        customerService.addBookToCustomer(userName, bookID);

        return "Add book with ID: " + bookID.toString() + " to Customer with ID: " + userName;
    }

    @PostMapping("/deleteBookFromCustomer/{userName}")
    public String deleteBookToCustomer(@PathVariable String userName, @RequestParam Integer bookID) {
        customerService.deleteBookFromCustomer(userName, bookID);

        return "Delete book with ID: " + bookID.toString() + " to Customer with ID: " + userName;
    }

    @GetMapping("/login")
    public String login(@RequestParam String userName, @RequestParam String password) {
        return Boolean.toString(customerService.verifyUser(userName, password));
    }

    @GetMapping("/getAdminName")
    public String getAdminName() {
        return customerService.getAdminName();
    }
}
