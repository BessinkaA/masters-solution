package com.jci.master.solution.vizualization.sequence;


import lombok.*;

/**
 * Class representing sequence group.
 * Sequence group is used by sequence transformer
 */
@Data
public class SequenceGroup {

    private String key;
    private String text;
    private Boolean isGroup;
    private String loc;
    private int duration;
}
