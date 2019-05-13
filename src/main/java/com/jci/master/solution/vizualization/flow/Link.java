package com.jci.master.solution.vizualization.flow;

/*
 * Class representing links for flow diagram
 */

import lombok.*;

@Data
public class Link {
    private String from;
    private String to;
    private String text;
    private int time;
}
