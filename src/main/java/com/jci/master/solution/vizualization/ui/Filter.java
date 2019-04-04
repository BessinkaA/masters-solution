package com.jci.master.solution.vizualization.ui;

import lombok.*;
import org.springframework.format.annotation.*;

import java.util.*;

@Data
public class Filter {

    private String lookback;
    private String service;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date from;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date to;
}
