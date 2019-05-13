package com.jci.master.solution.vizualization;

/*
 * Class representing communication link in communication diagram.
 */

import lombok.*;

@Data
public class CommunicationLink {

    private int from;
    private int to;
    private String text;
}
