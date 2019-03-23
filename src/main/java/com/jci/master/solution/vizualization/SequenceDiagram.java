package com.jci.master.solution.vizualization;

import com.google.gson.annotations.*;
import lombok.*;

import java.util.*;

@Data
public class SequenceDiagram {

    @SerializedName("class")
    private String className = "go.GraphLinksModel";
    private ArrayList<Object> nodeDataArray = new ArrayList<>();
    private ArrayList<Link> linkDataArray = new ArrayList<>();
}
