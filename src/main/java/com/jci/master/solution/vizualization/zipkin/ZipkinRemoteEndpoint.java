package com.jci.master.solution.vizualization.zipkin;

/*
 * Class representing Zipkin Remote endpoint
 */

import lombok.*;

@Data
public class ZipkinRemoteEndpoint {

    private String ipv4;
    private int port;
}



