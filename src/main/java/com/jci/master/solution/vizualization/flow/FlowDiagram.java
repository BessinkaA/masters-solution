package com.jci.master.solution.vizualization.flow;


import com.google.gson.annotations.*;
import lombok.*;

import java.util.*;

/**
 * Class representing flow diagram
 */
@Data
public class FlowDiagram {

    @SerializedName("class")
    private String className = "go.GraphLinksModel";
    private boolean copiesArrays = true;
    private boolean copiesArrayObjects = true;
    private ArrayList<FlowGroup> nodeDataArray = new ArrayList<>();
    private ArrayList<Link> linkDataArray = new ArrayList<>();

}
