package com.jci.master.solution.vizualization.controller;

import com.jci.master.solution.vizualization.*;
import com.jci.master.solution.vizualization.ui.*;
import com.jci.master.solution.vizualization.zipkin.*;
import lombok.extern.slf4j.*;
import org.apache.commons.io.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.*;
import java.util.*;

@Controller
@Slf4j
public class TraceController {

    @Resource
    ZipkinService zipkinService;

    @GetMapping(value = "/traces")
    public String getTraces(Model model) {
        log.info("Getting all traces...");

        try {
            Filter filter = new Filter();
            filter.setLookback("43200000");

            List<Trace> traces = zipkinService.getTraces(filter);
            log.info("Traces: {}", traces);
            model.addAttribute("traces", traces);
            model.addAttribute("filter", filter);
        } catch (Exception e) {
            log.error("Request failed", e);
        }

        return "tracesView";
    }

    @PostMapping(value = "/traces")
    public String findTraces(Model model, @ModelAttribute Filter filter) {
        log.info("Getting all traces...");

        try {
            List<Trace> traces = zipkinService.getTraces(filter);
            log.info("Traces: {}", traces);
            model.addAttribute("filter",filter);
            model.addAttribute("traces", traces);
        } catch (Exception e) {
            log.error("Request failed", e);
        }

        return "tracesView";
    }


    @ResponseBody
    @GetMapping(value = "/trace/sequence/{traceId}", produces = "text/html")
    public String getSequenceDiagram(@PathVariable("traceId") String traceId) throws Exception {
        try {
            log.info("Getting trace by ID: {}", traceId);
            ZipkinElement[] traceById = zipkinService.getTraceById(traceId);

            SequenceJsonTransformer sequenceJsonTransformer = new SequenceJsonTransformer();
            String diagramJson = sequenceJsonTransformer.transform(traceById);

            DiagramGenerator diagramGenerator = new DiagramGenerator();
            String diagramHtml = diagramGenerator.generate(diagramJson, "/sequence.html");

            return diagramHtml;
        } catch (Exception e) {
            log.error("Request failed", e);
            throw e;
        }
    }

    @ResponseBody
    @GetMapping(value = "/trace/flow/{traceId}", produces = "text/html")
    public String getFlowDiagram(@PathVariable("traceId") String traceId) throws Exception {
        try {
            log.info("Getting trace by ID: {}", traceId);
            ZipkinElement[] traceById = zipkinService.getTraceById(traceId);

            FlowJsonTransformer flowJsonTransformer = new FlowJsonTransformer();
            String diagramJson = flowJsonTransformer.transform(traceById);

            DiagramGenerator diagramGenerator = new DiagramGenerator();
            String diagramHtml = diagramGenerator.generate(diagramJson, "/flow.html");

            return diagramHtml;
        } catch (Exception e) {
            log.error("Request failed", e);
            throw e;
        }
    }

    @ResponseBody
    @GetMapping(value = "/trace/communication/{traceId}", produces = "text/html")
    public String getCommunicationDiagram(@PathVariable("traceId") String traceId) throws Exception {
        try {
            log.info("Getting trace by ID: {}", traceId);
            ZipkinElement[] traceById = zipkinService.getTraceById(traceId);

            CommunicationJsonTransformer communicationTransformer = new CommunicationJsonTransformer();
            String diagramJson = communicationTransformer.transform(traceById);

            DiagramGenerator diagramGenerator = new DiagramGenerator();
            String diagramHtml = diagramGenerator.generate(diagramJson, "/communication.html");

            return diagramHtml;
        } catch (Exception e) {
            log.error("Request failed", e);
            throw e;
        }
    }
}
