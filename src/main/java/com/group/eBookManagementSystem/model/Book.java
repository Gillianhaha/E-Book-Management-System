package com.group.eBookManagementSystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String bookName;
    private String subject;
    private String author;
    private String content;
    private Integer sum = 0;
    private Integer count = 0;
    private Integer rate = 0;

    public void addRate(Integer number) {
        this.sum += number;
        this.count += 1;
        this.rate = this.sum / this.count;
    }

}