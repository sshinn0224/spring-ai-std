package com.example.springai.ch4.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReviewClassification {

    public enum Sentiment {
        POSITIVE, NEUTRAL, NEGATIVE
    }

    private String review;
    private Sentiment classification;
}
