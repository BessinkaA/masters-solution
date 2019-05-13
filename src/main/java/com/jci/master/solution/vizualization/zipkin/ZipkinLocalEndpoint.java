package com.jci.master.solution.vizualization.zipkin;

/*
 * Class representing Zipkin Local endpoint
 */

import lombok.*;

@Data
public class ZipkinLocalEndpoint {

    private String ipv4;
    private String serviceName;
}