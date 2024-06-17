package com.goott.trip.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    private String imgKey;
    private Timestamp createdAt;
    private String url;
}
