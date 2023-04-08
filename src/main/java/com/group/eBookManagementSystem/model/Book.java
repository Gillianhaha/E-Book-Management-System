package com.group.eBookManagementSystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
//    @ElementCollection
//    private int[] rate={0};