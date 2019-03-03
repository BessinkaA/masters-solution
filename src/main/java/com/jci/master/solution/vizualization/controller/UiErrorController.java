package com.jci.master.solution.vizualization.controller;

import lombok.extern.slf4j.*;
import org.springframework.boot.web.servlet.error.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 */
@Slf4j
@Controller
public class UiErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        log.error("Status: {}", status);
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
