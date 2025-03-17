package com.example.testmicro.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {
    private static final Logger logger = LogManager.getLogger(BaseController.class);

    @Value("${versione}")
    private String version;
    @RequestMapping(value = "getVersion", method= RequestMethod.GET)
    public String getVersion() {
        logger.info("Versione: " + version);
        return version;
    }

    @RequestMapping(value = "checkRunning", method=RequestMethod.GET)
    public String sayHello(String test) {
        logger.info("Richiesto un hello world");
        return "Running...";
    }

}