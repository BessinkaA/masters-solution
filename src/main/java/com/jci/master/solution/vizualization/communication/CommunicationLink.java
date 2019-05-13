package com.jci.master.solution.vizualization.communication;


import lombok.*;

/**
 * Class representing communication link in communication diagram.
 */
@Data
public class CommunicationLink {

    private int from;
    private int to;
    private String text;
}
