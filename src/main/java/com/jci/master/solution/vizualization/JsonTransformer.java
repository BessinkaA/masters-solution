package com.jci.master.solution.vizualization;

import com.google.gson.*;
import lombok.*;
import org.apache.commons.io.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

@NoArgsConstructor
class JsonTransformer {

    String transform() throws IOException {

        // TODO look at one more time
        URL url = getClass().getResource("/zipkin.json");
        String json = IOUtils.toString(url, StandardCharsets.UTF_8);

        Gson gson = new Gson();
        ZipkinElement[] zipkinOutput = gson.fromJson(json, ZipkinElement[].class);
        List<String> serviceNames = Stream.of(zipkinOutput)
                                     .map(x -> x.getLocalEndpoint().getServiceName())
                                     .distinct()
                                     .collect(Collectors.toList());

        SequenceDiagram sequenceDiagram = new SequenceDiagram();

        for (int i = 0; i < serviceNames.size(); i++) {
            String serviceName = serviceNames.get(i);
            SequenceGroup sequenceGroup = new SequenceGroup();
            sequenceGroup.setKey(serviceName);
            sequenceGroup.setText(serviceName);
            sequenceGroup.setDuration(9);
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
}

