package com.example.testmicro.controller;

import com.example.testmicro.delegate.TestDelegate;
import com.example.testmicro.protocol.GetLinesResponse;
import com.example.testmicro.utils.CostantiTest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.example.testmicro.model.Point;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

@RestController
public class TestMicroController {
    private static final Logger logger = LogManager.getLogger(TestMicroController.class);
    private final List<Point> points = new ArrayList<>();
    @Operation(summary = "Add a point to the space", description = "Adds a new point with given x and y coordinates.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Point added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid point format")
    })
    @RequestMapping(value= "/point",  method= RequestMethod.POST)
    public synchronized String addPoint(@RequestBody Point point) {
        if (point == null || point.getX() == null || point.getY() == null) {
            return "Invalid point format";
        }
        points.add(point);
        return "Point added successfully";
    }

    @Operation(summary = "Get all points in the space", description = "Retrieves all the points currently stored in the space.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of points returned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Point.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @RequestMapping(value= "/space",  method= RequestMethod.GET)
    public synchronized List<Point> getAllPoints() {
        return new ArrayList<>(points);
    }

    @Operation(summary = "Remove all points from the space", description = "Clears the space, removing all stored points.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All points removed successfully")
    })
    @RequestMapping(value= "/space",  method= RequestMethod.DELETE)
    public synchronized String clearSpace() {
        points.clear();
        return "All points removed";
    }

    @Operation(summary = "Get lines passing through at least n points", description = "Finds and returns the lines that pass through at least 'n' points in the space.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lines retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetLinesResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @RequestMapping(value= "/lines",  method = {RequestMethod.GET})
    public synchronized GetLinesResponse getLines(@RequestParam(value = "n") Integer n) {
        logger.info("Client request received");
        logger.info(n);
        GetLinesResponse response = new GetLinesResponse();
        String description = "";
        short resultCode = -1;
        try {
            if (n != null) {
                if(n == 0 || n == 1 || points.size() < 2){
                    resultCode = 101;
                    description = "The minimum number of points for a segment is 2. Please insert enough points.";
                }else if(n>points.size()){
                    resultCode = 102;
                    description = "n is greater than the number of points present.";
                }else {
                    Map<String, Object> result = TestDelegate.findLines(Integer.valueOf(n), points);
                    resultCode = (short) result.get(CostantiTest.ESITO);
                    description = (String) result.get(CostantiTest.RESULTDESC);
                    Integer lines = (Integer) result.get(CostantiTest.TOTALE_PERMUTAZIONI);
                }
            } else {
                resultCode = 100;
                response.setResultCode(resultCode);
                response.setResultDesc("The number of points cannot be empty or null.");
                return response;
            }
        } catch (Exception e) {
            description = "Error occurred during getLines";
            logger.error(description, e.getMessage());
            response.setResultDesc(description);
            response.setResultCode((short) -1);
            return response;
        }
        response.setResultDesc(description);
        response.setResultCode(resultCode);
        logger.info("Sending response to client");
        logger.info(response.toString());
        return response;
    }
}
