package com.goott.trip.jhm.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class QNA {
    private int num;
    private String memberId;
    private String title;
    private String cont;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int status;
    private int qnaGroup;
    private int step;
}
