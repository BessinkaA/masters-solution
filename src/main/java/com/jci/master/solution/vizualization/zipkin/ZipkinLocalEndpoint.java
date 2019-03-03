package com.jci.master.solution.vizualization.zipkin;

import lombok.*;

@Data
public class ZipkinLocalEndpoint {

    private String ipv4;
    private String serviceName;
}