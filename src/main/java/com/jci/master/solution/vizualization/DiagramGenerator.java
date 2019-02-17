package com.jci.master.solution.vizualization;

import org.apache.commons.io.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;

public class DiagramGenerator {

    void generate(String diagramJson) throws IOException {

        URL url = getClass().getResource("/sequence.html");
        String diagramTemplate = IOUtils.toString(url, StandardCharsets.UTF_8);

        String diagramReplaced = diagramTemplate.replace("${DIAGRAM_JSON}", diagramJson);

        File file = new File("src/main/resources/diagram.html");
        FileUtils.write(file, diagramReplaced, StandardCharsets.UTF_8);

    }
}
