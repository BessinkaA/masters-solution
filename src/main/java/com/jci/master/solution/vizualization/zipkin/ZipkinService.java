package com.jci.master.solution.vizualization.zipkin;

/*
 * Service class responsible for interactions with Zipkin server
 */

import com.jci.master.solution.vizualization.ui.*;
import lombok.extern.slf4j.*;
import org.apache.commons.lang3.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;

import java.util.*;
import java.util.stream.*;

import static java.util.stream.Collectors.*;

@Service
@Slf4j
public class ZipkinService {

    RestTemplate restTemplate = new RestTemplate();

    /**
     * Method to get trace by ID from Zipkin server
     *
     * @param traceId
     *         ID of the trace
     *
     * @return a list of Zipkin elements that represent the trace
     */
    public ZipkinElement[] getTraceById(String traceId) {
        String url = "http://localhost:9411/api/v2/trace/" + traceId;
        return restTemplate.getForObject(url, ZipkinElement[].class);
    }

    /**
     * Method to get traces from Zipkin server by criteria
     *
     * @param filter
     *         Filter filter
     *
     * @return list of traces
     */
    public List<Trace> getTraces(Filter filter) {
        String url = "http://localhost:9411/api/v2/traces?limit=10";
        if (StringUtils.isNotBlank(filter.getService())) {
            url = url + "&serviceName=" + filter.getService();
        }

        if ("custom".equals(filter.getLookback())) {
            long startTs = filter.getFrom().getTime();
            long endTs = filter.getTo().getTime();
            long lookback = endTs - startTs;
            url = url + "&endTs=" + endTs + "&lookback=" + lookback;
        } else if (StringUtils.isNotBlank(filter.getLookback())) {
            url = url + "&lookback=" + filter.getLookback();
        }

        Comparator<Trace> comparator = Comparator.comparing(Trace::getTimestamp);
        if (!filter.isAscOrder()) {
            comparator = comparator.reversed();
        }

        log.info("URL: {}", url);
        ZipkinElement[][] traces = restTemplate.getForObject(url, ZipkinElement[][].class);

        return Stream.of(traces)
                     .map(this::toTrace)
                     .sorted(comparator)
                     .collect(toList());
    }

    /**
     * Method to convert a list of Zipkin elemnts into a trace
     *
     * @param e
     *         Zipkin element
     *
     * @return trace
     */
    protected Trace toTrace(ZipkinElement[] e) {
        Trace trace = new Trace();
        trace.setTraceId(e[0].getTraceId());
        trace.setTimestamp(e[0].getTimestamp());

        List<String> services = Stream.of(e)
                                      .map(el -> el.getLocalEndpoint() == null ? "" : el.getLocalEndpoint()
                                                                                        .getServiceName())
                                      .distinct()
                                      .sorted()
                                      .collect(toList());

        trace.setServices(StringUtils.join(services, ", "));
        if (services.size() > 1) {
            trace.setServicesShort(services.get(0));
            trace.setExpandServices(true);
            trace.setServicesLink("+ " + (services.size() - 1) + " more");
        } else {
            trace.setServicesShort(trace.getServices());
            trace.setExpandServices(false);
        }
        return trace;
    }
}


