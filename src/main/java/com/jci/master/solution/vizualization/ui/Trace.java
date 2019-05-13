package com.jci.master.solution.vizualization.ui;

/*
 * Trace class representing the trace and it's attributes.
 */

import lombok.*;

import java.util.*;

@Data
public class Trace {
    private String traceId;
    private Date timestamp;

    private String services;

    private String servicesShort;
    private String servicesLink;
    private boolean expandServices;
}
