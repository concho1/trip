package com.goott.trip.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class Image {
    private String imgKey;
    private String Url;
    private Timestamp CreateAt;
}
