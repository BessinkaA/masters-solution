package com.jci.master.solution.vizualization.controller;

import com.jci.master.solution.vizualization.zipkin.*;
import lombok.extern.slf4j.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.*;

@RestController
@Slf4j
public class TraceController {

    @Resource
    ZipkinService zipkinService;

    @GetMapping(value = "/traces")
    public String getTraces(Model model) {
        log.info("Getting all traces...");
        ZipkinElement[][] traces = zipkinService.getTraces();

        model.addAttribute("traces", traces);

        return "tracesView";
    }


    @GetMapping(value = "/trace/{traceId}")
    public String getTraceById(@RequestParam("traceId") String traceId, Model model) {
        log.info("Getting trace by ID: {}", traceId);
        ZipkinElement[] traceById = zipkinService.getTraceById(traceId);

        model.addAttribute("trace", traceById);

        return "traceView";
    }
}
