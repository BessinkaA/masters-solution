package com.jci.master.solution.vizualization;

import com.google.gson.*;
import com.jci.master.solution.vizualization.zipkin.*;
import lombok.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

@NoArgsConstructor
public class FlowJsonTransformer {
    Gson gson = new Gson();

    public String transform(ZipkinElement[] zipkinOutput) throws IOException {
        List<String> serviceNames = Stream.of(zipkinOutput)
                                          .sorted(Comparator.comparing(ZipkinElement::getTimestamp))
                                          .map(x -> x.getLocalEndpoint().getServiceName())
                                          .distinct()
                                          .collect(Collectors.toList());

        List<ZipkinElement> zipkinElementsByTimestamp = Stream.of(zipkinOutput)
                                                              .sorted(Comparator.comparing(ZipkinElement::getTimestamp))
                                                              .collect(Collectors.toList());

        FlowDiagram flowDiagram = new FlowDiagram();

        for (String serviceName : serviceNames) {
            FlowGroup flowGroup = new FlowGroup();
            flowGroup.setKey(serviceName);
            flowGroup.setText(serviceName);

            flowDiagram.getNodeDataArray().add(flowGroup);
        }

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
            link.setText("Call to a service");
            link.setTime(i + 1);

            flowDiagram.getLinkDataArray().add(link);
        }

        try (Writer writer = new FileWriter("src/main/resources/flow.json")) {
            gson.toJson(flowDiagram, writer);
        }
        return gson.toJson(flowDiagram);
    }
}

