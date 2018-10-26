package com.example.anonymous.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class LikeTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false, length=100)
    private String memberEmail;


    @Column(nullable = false, name = "boardId")
    private long  boardId;

    @Column(length = 1)
    private int checkLike;
}
