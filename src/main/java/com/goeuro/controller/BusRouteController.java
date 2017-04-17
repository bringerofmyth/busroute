package com.goeuro.controller;

import com.goeuro.model.RouteResponse;
import com.goeuro.service.BusRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

/**
 * Created by melihgurgah on 14/04/2017.
 */
@RestController
public class BusRouteController {

    private static final Logger LOGGER = Logger.getLogger( BusRouteService.class.getName() );
    @Qualifier("routeService")
    @Autowired
    private BusRouteService routeService;

    @RequestMapping("/direct")
    public RouteResponse getHasDirectRoute(@RequestParam("dep_sid") int departureStation,
                                           @RequestParam("arr_sid") int arrivalStation) {

        LOGGER.info("get info");
        boolean isDirect = routeService.isExists(departureStation, arrivalStation);

        RouteResponse response = new RouteResponse();
        response.setDep_sid(departureStation);
        response.setArr_sid(arrivalStation);
        response.setDirect_bus_route(isDirect);

        return response;
    }
}