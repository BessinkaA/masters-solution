package com.jci.master.solution.vizualization.flow;


import lombok.*;

/*
 * Class representing flow group.
 * Flow group is a part of flow diagram.
 */
@Data
public class FlowGroup {

    private String key;
    private String text;
    private String category = "Source";
}
