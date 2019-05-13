package com.jci.master.solution.vizualization.sequence;

/*
 * Transformer class for sequence diagram
 */

import com.google.gson.*;
import com.jci.master.solution.vizualization.*;
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

    /**
     * Method to transform a list of Zipkin elements to a string representing sequence diagram
     *
     * @param zipkinOutput
     *         list of Zipkin elements
     *
     * @return string representing sequence diagram
     *
     * @throws IOException
     *         exception
     */
    public String transform(ZipkinElement[] zipkinOutput) throws IOException {
        // getting service names from the list of Zipkin elements
        List<String> serviceNames = Stream.of(zipkinOutput)
                                          .sorted(Comparator.comparing(ZipkinElement::getTimestamp))
                                          .map(x -> x.getLocalEndpoint().getServiceName())
                                          .distinct()
                                          .collect(Collectors.toList());

        // sorting zipkin elements by timestamp
        List<ZipkinElement> zipkinElementsByTimestamp = Stream.of(zipkinOutput)
                                                              .sorted(Comparator.comparing(ZipkinElement::getTimestamp))
                                                              .collect(Collectors.toList());

        SequenceDiagram sequenceDiagram = new SequenceDiagram();

        int time = processData(zipkinElementsByTimestamp, sequenceDiagram);

        // create sequence groups
        for (int i = 0; i < serviceNames.size(); i++) {
            String serviceName = serviceNames.get(i);
            SequenceGroup sequenceGroup = new SequenceGroup();
            sequenceGroup.setKey(serviceName);
            sequenceGroup.setText(serviceName);
            sequenceGroup.setDuration(time + 1);
            sequenceGroup.setIsGroup(true);
            int x = i * 150;
            sequenceGroup.setLoc(x + " 0");

            sequenceDiagram.getNodeDataArray().add(sequenceGroup);
        }


        try (Writer writer = new FileWriter("src/main/resources/sequence.json")) {
            gson.toJson(sequenceDiagram, writer);
        }
        return gson.toJson(sequenceDiagram);
    }

    /**
     * Entry method to start recursive population of sequence diagram calls
     *
     * @param zipkinElementsByTimestamp
     *         list of zipkin elements by timestamp
     * @param sequenceDiagram
     *         sequence diagram
     *
     * @return duration of the root call
     */
    private int processData(List<ZipkinElement> zipkinElementsByTimestamp, SequenceDiagram sequenceDiagram) {

        // find the first element of a trace (root element)
        ZipkinElement rootElement = zipkinElementsByTimestamp.stream()
                                                             .filter(x -> x.getParentId() == null)
                                                             .filter(x -> x.getKind().equals("SERVER"))
                                                             .findAny()
                                                             .get();
        return processElement(zipkinElementsByTimestamp, rootElement, 0, sequenceDiagram);
    }

    /**
     * Method to draw a callspan for a given service and request/response arrows for its child calls
     *
     * @param zipkinElementsByTimestamp
     *         list of zipkin elements sorted by timestamp
     * @param element
     *         zipkin element
     * @param time
     *         time
     * @param sequenceDiagram
     *         sequence diagram
     *
     * @return time of the last response for a given service
     */
    private int processElement(List<ZipkinElement> zipkinElementsByTimestamp, ZipkinElement element, int time, SequenceDiagram sequenceDiagram) {
        // find if this element has any child elements
        List<ZipkinElement> childElements = zipkinElementsByTimestamp.stream()
                                                                     .filter(x -> Objects.equals(x.getParentId(), element
                                                                             .getId()))
                                                                     .filter(x -> x.getKind().equals("SERVER"))
                                                                     .collect(Collectors.toList());

        int from = time;

        time++;
        for (int i = 0; i < childElements.size(); i++) {
            ZipkinElement childServerElement = childElements.get(i);
            ZipkinElement childClientElement = zipkinElementsByTimestamp.stream()
                                                                        .filter(x -> x.getKind().equals("CLIENT"))
                                                                        .filter(x -> x.getId()
                                                                                      .equals(childServerElement.getId()))
                                                                        .findAny()
                                                                        .orElse(null);

            addClientToServerLink(childClientElement, childServerElement, sequenceDiagram, time);
            time = processElement(zipkinElementsByTimestamp, childServerElement, time, sequenceDiagram);
            addServerToClientLink(childClientElement, childServerElement, sequenceDiagram, time);
            if (i < childElements.size() - 1) {
                time += 2;
            } else {
                time++;
            }
        }

        SequenceSpan span = new SequenceSpan();
        span.setStart(from);
        span.setDuration(time - from);
        span.setGroup(element.getLocalEndpoint().getServiceName());
        sequenceDiagram.getNodeDataArray().add(span);

        return time;
    }

    /**
     * Method to add server to client link
     *
     * @param clientElement
     *         Zipkin client element
     * @param serverElement
     *         Zipkin server element
     * @param sequenceDiagram
     *         sequence diagram
     * @param time
     *         time
     */
    private void addServerToClientLink(ZipkinElement clientElement, ZipkinElement serverElement, SequenceDiagram sequenceDiagram, int time) {

        if (clientElement != null) {
            Link link = new Link();
            link.setFrom(serverElement.getLocalEndpoint().getServiceName());
            link.setTo(clientElement.getLocalEndpoint().getServiceName());
            link.setTime(time);

            sequenceDiagram.getLinkDataArray().add(link);
        }

    }

    /**
     * Method to add client to server link
     *
     * @param clientElement
     *         Zipkin client element
     * @param serverElement
     *         Zipkin server element
     * @param sequenceDiagram
     *         sequence diagram
     * @param time
     *         time
     */
    public void addClientToServerLink(ZipkinElement clientElement, ZipkinElement serverElement, SequenceDiagram sequenceDiagram, int time) {

        if (clientElement != null) {
            Link link = new Link();
            link.setFrom(clientElement.getLocalEndpoint().getServiceName());
            link.setTo(serverElement.getLocalEndpoint().getServiceName());
            link.setText(serverElement.getName());
            link.setTime(time);

            sequenceDiagram.getLinkDataArray().add(link);
        }
    }
}

