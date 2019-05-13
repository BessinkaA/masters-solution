package com.jci.master.solution.vizualization.ui;

/*
 * Filter class. Represents filtering criteria used when searching for traces.
 */

import lombok.*;
import org.apache.commons.lang3.time.*;
import org.springframework.format.annotation.*;

import java.util.*;

@Data
public class Filter {

    private String lookback;
    private String service;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date from;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date to;

    private boolean ascOrder = false;

    public Filter() {
        from = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        to = DateUtils.addDays(from, 1);
    }
}
