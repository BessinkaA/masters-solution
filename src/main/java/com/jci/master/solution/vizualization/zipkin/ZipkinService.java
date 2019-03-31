package com.jci.master.solution.vizualization.zipkin;

import com.jci.master.solution.vizualization.ui.*;
import com.jci.master.solution.vizualization.ui.Filter;
import com.sun.tools.javac.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;

@Service
public class ZipkinService {

    RestTemplate restTemplate = new RestTemplate();

    public ZipkinElement[] getTraceById(String traceId) {
        String url = "http://localhost:9411/api/v2/trace/" + traceId;
        return restTemplate.getForObject(url, ZipkinElement[].class);
    }

    public ZipkinElement[][] getTraces(Filter filter) {
        String url = "http://localhost:9411/api/v2/traces?limit=5";
        if(StringUtils.isNotBlank(filter.getService())){
            url = url + "&serviceName=" + filter.getService();
        }

        if (StringUtils.isNotBlank(filter.getLookback())){
            url = url + "&lookback=" + filter.getLookback();
        }
        return restTemplate.getForObject(url, ZipkinElement[][].class);
    }
}


