package com.jci.master.solution.vizualization;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    public static void main(String[] args) throws IOException {
//        SequenceJsonTransformer jsonTransformer = new SequenceJsonTransformer();
//        String diagramJson = jsonTransformer.transform();
//
//        DiagramGenerator diagramGenerator = new DiagramGenerator();
//        diagramGenerator.generate(diagramJson);
//
//    }
}
