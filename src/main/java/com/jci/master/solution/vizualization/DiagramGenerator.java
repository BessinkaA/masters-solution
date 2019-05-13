package com.jci.master.solution.vizualization;


import org.apache.commons.io.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;

/**
 * Class responsible for diagram generation.
 */
public class DiagramGenerator {

    /**
     * Method to insert a diagram into diagram template
     *
     * @param diagramJson
     *         string representation of the diagram
     * @param diagramType
     *         diagram type
     *
     * @return string representing diagram template with the actual diagram
     *
     * @throws IOException
     *         exception
     */
    public String generate(String diagramJson, String diagramType) throws IOException {

        URL url = getClass().getResource(diagramType);
        String diagramTemplate = IOUtils.toString(url, StandardCharsets.UTF_8);

        String diagramReplaced = diagramTemplate.replace("${DIAGRAM_JSON}", diagramJson);

        File file = new File("src/main/resources/diagram.html");
        FileUtils.write(file, diagramReplaced, StandardCharsets.UTF_8);

        return diagramReplaced;
    }
}
