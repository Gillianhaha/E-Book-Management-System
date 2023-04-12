package com.group.eBookManagementSystem.repository;

import com.group.eBookManagementSystem.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {

    Book findBookById(Integer id);

    @Transactional
    void deleteBookById(Integer id);

}
