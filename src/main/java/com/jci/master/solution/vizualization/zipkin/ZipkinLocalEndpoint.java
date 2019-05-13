package com.jci.master.solution.vizualization.zipkin;


import lombok.*;

/**
 * Class representing Zipkin Local endpoint
 */
@Data
public class ZipkinLocalEndpoint {

    private String ipv4;
    private String serviceName;
}