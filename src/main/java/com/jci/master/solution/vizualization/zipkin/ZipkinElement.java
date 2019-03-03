package com.jci.master.solution.vizualization.zipkin;

import lombok.*;

@Data
public class ZipkinElement {

    private String traceId;
    private String parentId;
    private String id;
    private String kind;
    private String name;
    private long timestamp;
    private int duration;
    private Boolean shared;
    private ZipkinLocalEndpoint localEndpoint;
    private ZipkinRemoteEndpoint remoteEndpoint;
    private ZipkinTags zipkinTags;

    public Boolean getShared() {
        if (shared == null){
            return false;
        }
        return shared;
    }
}
