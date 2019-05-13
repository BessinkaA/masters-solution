package com.jci.master.solution.vizualization.sequence;

/*
 * Class representing sequence group.
 * Sequence group is used by sequence transformer
 */

import lombok.*;

@Data
public class SequenceGroup {

    private String key;
    private String text;
    private Boolean isGroup;
    private String loc;
    private int duration;
}
