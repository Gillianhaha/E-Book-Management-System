package com.group.eBookManagementSystem.service;

import com.group.eBookManagementSystem.model.Book;
import com.group.eBookManagementSystem.repository.BookRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    private static final Logger LOG = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CustomerService customerService;

    public void addBook(Book book) {
        if (book.getId() == null) {
            bookRepository.save(book);
        } else {
            throw new IllegalArgumentException(String.format("Book %d already exists.", book.getId()));
        }
    }

    public List<Book> getBooks() {
        return (List<Book>) bookRepository.findAll();
    }

    public Book findBookById(Integer id) {
        if (existByBookId(id)) {
            return bookRepository.findBookById(id);
        } else {
            throw new IllegalArgumentException(String.format("Book %d does not exist.", id));
        }
    }

    public void updateBook(Book book) {
        if (existByBookId(book.getId())) {
            bookRepository.save(book);
        } else {
            throw new IllegalArgumentException(String.format("Book %d does not exist.", book.getId()));
        }
    }

    @Transactional
    public void deleteBook(Integer id) {
        if (existByBookId(id)) {
            customerService.removeBookForAllCustomers(id);
            bookRepository.deleteBookById(id);
        } else {
            throw new IllegalArgumentException(String.format("Book %d does not exist.", id));
        }
    }

    public void rateBook(Integer id, Integer rate) {
        if (existByBookId(id)) {
            Book book = bookRepository.findBookById(id);
            book.addRate(rate);
            bookRepository.save(book);
        } else {
            throw new IllegalArgumentException(String.format("Book %d does not exist.", id));
        }
    }

    public boolean existByBookId(Integer id) {
        return bookRepository.existsById(id);
    }

}
