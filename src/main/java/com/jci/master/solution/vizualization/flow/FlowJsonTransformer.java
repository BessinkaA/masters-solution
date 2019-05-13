package com.jci.master.solution.vizualization.flow;


import com.google.gson.*;
import com.jci.master.solution.vizualization.zipkin.*;
import lombok.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

/**
 * Transformer class responsible for transformation of zipkin elements to a string representing flow diagram
 */
@NoArgsConstructor
public class FlowJsonTransformer {
    Gson gson = new Gson();

    /**
     * Method to transform a list of zipkin elements into a string representing flow diagram
     *
     * @param zipkinOutput
     *         list of Zipkin elements
     *
     * @return string representing flow diagram
     *
     * @throws IOException
     *         exception
     */
    public String transform(ZipkinElement[] zipkinOutput) throws IOException {

        // get names of the services in the trace
        List<String> serviceNames = Stream.of(zipkinOutput)
                                          .sorted(Comparator.comparing(ZipkinElement::getTimestamp))
                                          .map(x -> x.getLocalEndpoint().getServiceName())
                                          .distinct()
                                          .collect(Collectors.toList());

        // sort zipkin elements by timestamp
        List<ZipkinElement> zipkinElementsByTimestamp = Stream.of(zipkinOutput)
                                                              .sorted(Comparator.comparing(ZipkinElement::getTimestamp))
                                                              .collect(Collectors.toList());

        FlowDiagram flowDiagram = new FlowDiagram();

        // create flow groups
        for (String serviceName : serviceNames) {
            FlowGroup flowGroup = new FlowGroup();
            flowGroup.setKey(serviceName);
            flowGroup.setText(serviceName);

            flowDiagram.getNodeDataArray().add(flowGroup);
        }

        // make connections between clients and servers and link them
        for (int i = 0; i < zipkinElementsByTimestamp.size(); i++) {
            ZipkinElement element = zipkinElementsByTimestamp.get(i);

            if (element.getKind().equals("SERVER")) {
                continue;
            }

            ZipkinElement serverElement = zipkinElementsByTimestamp.stream()
                                                                   .filter(x -> x.getKind().equals("SERVER"))
                                                                   .filter(x -> x.getId().equals(element.getId()))
                                                                   .findAny()
                                                                   .get();

            Link link = new Link();
            link.setFrom(element.getLocalEndpoint().getServiceName());
            link.setTo(serverElement.getLocalEndpoint().getServiceName());

            flowDiagram.getLinkDataArray().add(link);
        }

        try (Writer writer = new FileWriter("src/main/resources/flow.json")) {
            gson.toJson(flowDiagram, writer);
        }
        return gson.toJson(flowDiagram);
    }
}

