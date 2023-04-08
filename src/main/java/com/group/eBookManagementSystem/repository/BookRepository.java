package com.group.eBookManagementSystem.repository;

import com.group.eBookManagementSystem.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    Book findBookById(Integer id);
    Book deleteBookById(Integer id);
//    Customer findCustomerByFirstName(String firstName);

}
