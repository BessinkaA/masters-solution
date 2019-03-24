package com.jci.master.solution.vizualization;

import com.google.gson.annotations.*;
import lombok.*;

import java.util.*;

@Data
public class CommunicationDiagram {

    private ArrayList<CommunicationGroup> nodeDataArray = new ArrayList<>();
    private ArrayList<Link> linkDataArray = new ArrayList<>();

}
