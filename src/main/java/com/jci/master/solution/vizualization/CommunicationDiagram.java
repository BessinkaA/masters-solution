package com.jci.master.solution.vizualization;

/*
 * Class representing communication diagram
 */

import lombok.*;

import java.util.*;

@Data
public class CommunicationDiagram {

    private ArrayList<CommunicationGroup> nodeDataArray = new ArrayList<>();
    private ArrayList<CommunicationLink> linkDataArray = new ArrayList<>();

}
