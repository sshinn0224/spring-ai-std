package com.example.springai.ch4.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Hotel {
    private String city;
    private List<String> names;
}
