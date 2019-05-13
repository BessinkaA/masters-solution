package com.jci.master.solution.vizualization.sequence;


import com.google.gson.annotations.*;
import com.jci.master.solution.vizualization.flow.*;
import lombok.*;

import java.util.*;

/**
 * Class representing sequence diagram
 */
@Data
public class SequenceDiagram {

    @SerializedName("class")
    private String className = "go.GraphLinksModel";
    private ArrayList<Object> nodeDataArray = new ArrayList<>();
    private ArrayList<Link> linkDataArray = new ArrayList<>();
}
