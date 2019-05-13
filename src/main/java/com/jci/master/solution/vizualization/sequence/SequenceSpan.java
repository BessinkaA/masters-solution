package com.jci.master.solution.vizualization.sequence;


import lombok.*;

/*
 * Class representing a sequence span.
 * Sequence span is used by sequence transformer.
 */
@Data
public class SequenceSpan {

    private String group;
    private int start;
    private int duration;
}
