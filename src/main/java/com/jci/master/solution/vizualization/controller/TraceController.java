package com.jci.master.solution.vizualization.controller;


import com.jci.master.solution.vizualization.*;
import com.jci.master.solution.vizualization.communication.*;
import com.jci.master.solution.vizualization.flow.*;
import com.jci.master.solution.vizualization.sequence.*;
import com.jci.master.solution.vizualization.ui.*;
import com.jci.master.solution.vizualization.zipkin.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.*;
import java.util.*;

/**
 * Controller that receives all the incoming application REST requests.
 */
@Controller
@Slf4j
public class TraceController {

    @Resource
    ZipkinService zipkinService;

    /**
     * Method that receive GET request to display traces
     *
     * @param model
     *         Model model
     *
     * @return traces view
     */
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

        // Use tracesView template to render response
        return "tracesView";
    }

    /**
     * Method that receives POST requests to get traces by criteria
     *
     * @param model
     *         Model model
     * @param filter
     *         Filter filter
     *
     * @return traces view
     */
    @PostMapping(value = "/traces")
    public String findTraces(Model model, @ModelAttribute Filter filter) {
        log.info("Getting all traces...");

        try {
            List<Trace> traces = zipkinService.getTraces(filter);
            log.info("Traces: {}", traces);
            model.addAttribute("filter", filter);
            model.addAttribute("traces", traces);
        } catch (Exception e) {
            log.error("Request failed", e);
        }

        // Use tracesView template to render response
        return "tracesView";
    }


    /**
     * Method to GET a sequence diagram for the selected trace.
     *
     * @param traceId
     *         ID of the trace
     *
     * @return String representing sequence diagram
     *
     * @throws Exception
     *         exception
     */
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

    /**
     * Method to GET a flow diagram for the selected trace.
     *
     * @param traceId
     *         ID of the trace
     *
     * @return String representing flow diagram
     *
     * @throws Exception
     *         exception
     */
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

    /**
     * Method to GET a communication diagram for the selected trace.
     *
     * @param traceId
     *         ID of the trace
     *
     * @return String representing communication diagram
     *
     * @throws Exception
     *         exception
     */
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

    /**
     * Method to display all 3 diagrams in one page
     *
     * @param model
     *         Model model
     * @param traceId
     *         ID of the trace
     *
     * @return A  view for all traces
     */
    @GetMapping(value = "/trace/all/{traceId}", produces = "text/html")
    public String getAllDiagrams(Model model, @PathVariable("traceId") String traceId) {
        model.addAttribute("traceId", traceId);

        // Use viewAll template to render response
        return "viewAll";
    }
}
