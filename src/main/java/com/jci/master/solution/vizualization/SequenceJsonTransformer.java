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

        addServerToClientLinks(zipkinElementsByTimestamp, sequenceDiagram);
        addClientToServerLinks(zipkinElementsByTimestamp, sequenceDiagram);
        addSequenceSpans(zipkinElementsByTimestamp, sequenceDiagram);

        try (Writer writer = new FileWriter("src/main/resources/sequence.json")) {
            gson.toJson(sequenceDiagram, writer);
        }
        return gson.toJson(sequenceDiagram);
    }

    private void addSequenceSpans(List<ZipkinElement> zipkinElementsByTimestamp, SequenceDiagram sequenceDiagram) {


    }

    private void addClientToServerLinks(List<ZipkinElement> zipkinElementsByTimestamp, SequenceDiagram sequenceDiagram) {
        for (int i = 0; i < zipkinElementsByTimestamp.size(); i++) {
            ZipkinElement element = zipkinElementsByTimestamp.get(i);

            if (element.getKind().equals("CLIENT")) {
                continue;
            }

            ZipkinElement clientElement = zipkinElementsByTimestamp.stream()
                                                                   .filter(x -> x.getKind().equals("CLIENT"))
                                                                   .filter(x -> x.getId().equals(element.getId()))
                                                                   .findAny()
                                                                   .orElse(null);


            if(clientElement != null){
                Link link = new Link();
                link.setFrom(element.getLocalEndpoint().getServiceName());
                link.setTo(clientElement.getLocalEndpoint().getServiceName());
                link.setTime(i+1);

                sequenceDiagram.getLinkDataArray().add(link);
            }
        }
    }

    public void addServerToClientLinks(List<ZipkinElement> zipkinElementsByTimestamp, SequenceDiagram sequenceDiagram) {
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
            link.setText(serverElement.getName());
            link.setTime(i+1);

            sequenceDiagram.getLinkDataArray().add(link);
        }
    }
}

