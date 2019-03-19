package com.jci.master.solution.vizualization;

import com.google.gson.annotations.*;
import lombok.*;

import java.util.*;

@Data
public class FlowDiagram {

    @SerializedName("class")
    private String className = "go.GraphLinksModel";
    private boolean copiesArrays = true;
    private boolean copiesArrayObjects = true;
    private ArrayList<FlowGroup> nodeDataArray = new ArrayList<>();
    private ArrayList<Link> linkDataArray = new ArrayList<>();

}
