package com.jci.master.solution.vizualization.sequence;

/*
 * Class representing a sequence span.
 * Sequence span is used by sequence transformer.
 */

import lombok.*;

@Data
public class SequenceSpan {

    private String group;
    private int start;
    private int duration;
}
