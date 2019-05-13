package com.jci.master.solution.vizualization.communication;



import lombok.*;

import java.util.*;

/**
 * Class representing communication diagram
 */
@Data
public class CommunicationDiagram {

    private ArrayList<CommunicationGroup> nodeDataArray = new ArrayList<>();
    private ArrayList<CommunicationLink> linkDataArray = new ArrayList<>();

}
