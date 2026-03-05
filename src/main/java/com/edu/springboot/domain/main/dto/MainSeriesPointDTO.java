package com.edu.springboot.domain.main.dto;

import lombok.Data;

@Data
public class MainSeriesPointDTO {
    /** YYYY-MM-DD */
    private String date;
    private int count;
}
