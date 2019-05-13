package com.jci.master.solution.vizualization.communication;

/*
 * Transformer class that is responsible for a list of zipkin elements into communication diagram object
 */

import com.google.gson.*;
import com.jci.master.solution.vizualization.zipkin.*;
import lombok.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

@NoArgsConstructor
public class CommunicationJsonTransformer {
    Gson gson = new Gson();

    /**
     * Method to transform a list of zipkin elements into a String representing communication diagram
     *
     * @param zipkinOutput
     *         a list of ZipkinElements
     *
     * @return String representing communication diagram
     *
     * @throws IOException
     *         exception
     */
    public String transform(ZipkinElement[] zipkinOutput) throws IOException {

        // get all service names in from the list of Zipkin elements
        List<String> serviceNames = Stream.of(zipkinOutput)
                                          .sorted(Comparator.comparing(ZipkinElement::getTimestamp))
                                          .map(x -> x.getLocalEndpoint().getServiceName())
                                          .distinct()
                                          .collect(Collectors.toList());

        // sort Zipkin elements by timestamp
        List<ZipkinElement> zipkinElementsByTimestamp = Stream.of(zipkinOutput)
                                                              .sorted(Comparator.comparing(ZipkinElement::getTimestamp))
                                                              .collect(Collectors.toList());

        // create a map to map service name to group key (int)
        Map<String, Integer> serviceKeys = new HashMap<>();

        // create new communication diagram
        CommunicationDiagram communicationDiagram = new CommunicationDiagram();

        // create communication groups for communication diagram
        for (int i = 0; i < serviceNames.size(); i++) {
            String serviceName = serviceNames.get(i);
            serviceKeys.put(serviceName, i + 1);
            CommunicationGroup communicationGroup = new CommunicationGroup();
            communicationGroup.setKey(i + 1);
            communicationGroup.setText(serviceName);

            communicationDiagram.getNodeDataArray().add(communicationGroup);
        }

        int callCount = 1;

        // determine the connections between client and server (SERVER-CLIENT pairs)
        // and create communication links
        for (int i = 0; i < zipkinElementsByTimestamp.size(); i++) {
            ZipkinElement clientElement = zipkinElementsByTimestamp.get(i);

            if (clientElement.getKind().equals("SERVER")) {
                continue;
            }

            ZipkinElement serverElement = zipkinElementsByTimestamp.stream()
                                                                   .filter(x -> x.getKind().equals("SERVER"))
                                                                   .filter(x -> x.getId().equals(clientElement.getId()))
                                                                   .findAny()
                                                                   .get();

            CommunicationLink link = new CommunicationLink();

            link.setFrom(serviceKeys.get(clientElement.getLocalEndpoint().getServiceName()));
            link.setTo(serviceKeys.get(serverElement.getLocalEndpoint().getServiceName()));

            // adding a custom text to the description
            link.setText(callCount + ". " + serverElement.getTags()
                                                         .get("mvc.controller.class") + "\n." + serverElement.getTags()
                                                                                                             .get("mvc.controller.method") + "()");

            communicationDiagram.getLinkDataArray().add(link);
            callCount++;
        }

        try (Writer writer = new FileWriter("src/main/resources/communication.json")) {
            gson.toJson(communicationDiagram, writer);
        }
        return gson.toJson(communicationDiagram);
    }
}
