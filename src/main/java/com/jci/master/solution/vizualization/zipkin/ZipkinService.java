package com.jci.master.solution.vizualization.zipkin;

import org.springframework.stereotype.*;
import org.springframework.web.client.*;

@Service
public class ZipkinService {

    RestTemplate restTemplate = new RestTemplate();

    public ZipkinElement[] getTraceById(String traceId) {
        return restTemplate.getForObject("http://localhost:9411/api/v2/trace/" + traceId, ZipkinElement[].class);
    }

    public ZipkinElement[][] getTraces() {
        return restTemplate.getForObject("http://localhost:9411/api/v2/traces", ZipkinElement[][].class);
    }
}


