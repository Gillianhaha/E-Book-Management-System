package com.group.eBookManagementSystem.service;

import com.group.eBookManagementSystem.repository.BookRepository;
import com.group.eBookManagementSystem.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public void addBook(String bookName, String author, String subject, String content) {
        Book book = new Book();
        book.setBookName(bookName);
        book.setAuthor(author);
        book.setSubject(subject);
        book.setContent(content);

        bookRepository.save(book);
    }

    public Iterable<Book> getBooks() {
        return bookRepository.findAll();
    }

    public Book findBookById(Integer id) {
        return bookRepository.findBookById(id);
    }

    public void updateBook(Integer id, String bookName,String author,String subject,String content) {
        Book book = bookRepository.findBookById(id);
        book.setBookName(bookName);
        book.setAuthor(author);
        book.setSubject(subject);
        book.setContent(content);
        bookRepository.save(book);
    }

    public void deleteBook(Integer id){
        bookRepository.deleteBookById(id);
    }
    public void rateBook(Integer id, Integer rate) {
        Book book = bookRepository.findBookById(id);
        book.addRate(rate);
        bookRepository.save(book);
    }

}
//        int[] old = book.getRate();
//        int[] newRate = new int[old.length + 1];
//        for (int i = 0; i < old.length; i++) {
//            newRate[i] = old[i];
//        }
//        newRate[old.length] = rate;
//        book.setRate(newRate);