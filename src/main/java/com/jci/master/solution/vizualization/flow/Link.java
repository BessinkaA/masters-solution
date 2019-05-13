package com.jci.master.solution.vizualization.flow;


import lombok.*;

/**
 * Class representing links for flow diagram
 */
@Data
public class Link {
    private String from;
    private String to;
    private String text;
    private int time;
}
