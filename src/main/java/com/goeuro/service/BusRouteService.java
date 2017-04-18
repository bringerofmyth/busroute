package com.goeuro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by melihgurgah on 14/04/2017.
 */
@Service
public class BusRouteService {

    private static final Logger LOGGER = Logger.getLogger(BusRouteService.class.getName());

    @Autowired
    @Qualifier("routeMap")
    private Map<Integer, HashSet<Integer>> routeMap;

    public Map<Integer, HashSet<Integer>> getRouteMap() {
        return routeMap;
    }

    public boolean isExists(int departure, int arrival) {
        if (routeMap == null || routeMap.isEmpty()) return false;
        HashSet<Integer> set = routeMap.get(departure);

        return !(set == null || !set.contains(arrival));
    }
}
