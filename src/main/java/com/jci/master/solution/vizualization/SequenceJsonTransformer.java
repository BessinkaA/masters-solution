package com.jci.master.solution.vizualization;

import com.google.gson.*;
import com.jci.master.solution.vizualization.zipkin.*;
import lombok.*;
import org.apache.commons.io.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

@NoArgsConstructor
public class SequenceJsonTransformer {
    Gson gson = new Gson();

    @Deprecated
    public String transform() throws IOException {

        URL url = getClass().getResource("/zipkin.json");
        String json = IOUtils.toString(url, StandardCharsets.UTF_8);

        ZipkinElement[] zipkinOutput = gson.fromJson(json, ZipkinElement[].class);
        return transform(zipkinOutput);
    }

    public String transform(ZipkinElement[] zipkinOutput) throws IOException {
        List<String> serviceNames = Stream.of(zipkinOutput)
                                          .sorted(Comparator.comparing(ZipkinElement::getTimestamp))
                                          .map(x -> x.getLocalEndpoint().getServiceName())
                                          .distinct()
                                          .collect(Collectors.toList());

        List<ZipkinElement> zipkinElementsByTimestamp = Stream.of(zipkinOutput)
                                                              .sorted(Comparator.comparing(ZipkinElement::getTimestamp))
                                                              .collect(Collectors.toList());

        SequenceDiagram sequenceDiagram = new SequenceDiagram();

        for (int i = 0; i < serviceNames.size(); i++) {
            String serviceName = serviceNames.get(i);
            SequenceGroup sequenceGroup = new SequenceGroup();
            sequenceGroup.setKey(serviceName);
            sequenceGroup.setText(serviceName);
            sequenceGroup.setDuration(15);
            sequenceGroup.setIsGroup(true);
            int x = i * 150;
            sequenceGroup.setLoc(x + " 0");

            sequenceDiagram.getNodeDataArray().add(sequenceGroup);
        }

        processData(zipkinElementsByTimestamp, sequenceDiagram);

        try (Writer writer = new FileWriter("src/main/resources/sequence.json")) {
            gson.toJson(sequenceDiagram, writer);
        }
        return gson.toJson(sequenceDiagram);
    }

    private void processData(List<ZipkinElement> zipkinElementsByTimestamp, SequenceDiagram sequenceDiagram) {

        // find the first element of a trace (root element)
        ZipkinElement rootElement = zipkinElementsByTimestamp.stream()
                                                             .filter(x -> x.getParentId() == null)
                                                             .findAny()
                                                             .get();
        processElement(zipkinElementsByTimestamp, rootElement, 1, sequenceDiagram);

    }

    private int processElement(List<ZipkinElement> zipkinElementsByTimestamp, ZipkinElement element, int time, SequenceDiagram sequenceDiagram) {
        // find if this element has any child elements
        List<ZipkinElement> childElements = zipkinElementsByTimestamp.stream()
                                                                     .filter(x -> Objects.equals(x.getParentId(), element
                                                                             .getId()))
                                                                     .filter(x -> x.getKind().equals("SERVER"))
                                                                     .collect(Collectors.toList());

        for (ZipkinElement childElement : childElements) {
            addClientToServerLink(childElement, sequenceDiagram, time + 1);
            time = processElement(zipkinElementsByTimestamp, childElement, 1, sequenceDiagram);
            addServerToClientLink(childElement, sequenceDiagram, time);
        }

        return time;
    }

    private void addServerToClientLink(ZipkinElement clientElement, SequenceDiagram sequenceDiagram, int time) {

        if (clientElement != null) {
            Link link = new Link();
            link.setFrom(clientElement.getLocalEndpoint().getServiceName());
            link.setTo(clientElement.getLocalEndpoint().getServiceName());
            link.setTime(time);

            sequenceDiagram.getLinkDataArray().add(link);
        }

    }

    public void addClientToServerLink(ZipkinElement serverElement, SequenceDiagram sequenceDiagram, int time) {

        Link link = new Link();
        link.setFrom(serverElement.getLocalEndpoint().getServiceName());
        link.setTo(serverElement.getLocalEndpoint().getServiceName());
        link.setText(serverElement.getName());
        link.setTime(time);

        sequenceDiagram.getLinkDataArray().add(link);
    }
}

