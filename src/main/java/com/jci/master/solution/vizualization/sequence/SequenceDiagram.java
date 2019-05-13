package com.jci.master.solution.vizualization.sequence;

/*
 * Class representing sequence diagram
 */

import com.google.gson.annotations.*;
import com.jci.master.solution.vizualization.*;
import lombok.*;

import java.util.*;

@Data
public class SequenceDiagram {

    @SerializedName("class")
    private String className = "go.GraphLinksModel";
    private ArrayList<Object> nodeDataArray = new ArrayList<>();
    private ArrayList<Link> linkDataArray = new ArrayList<>();
}
