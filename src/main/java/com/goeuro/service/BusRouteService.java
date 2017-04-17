package com.goeuro.service;

import com.goeuro.BusRouteApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by melihgurgah on 14/04/2017.
 */
@Service
public class BusRouteService {

    private static final Logger LOGGER = Logger.getLogger( BusRouteService.class.getName() );
    @Autowired
    private ResourceLoader resourceLoader;

    private Map<Integer, TreeSet<Integer>> routeMap;

    @PostConstruct
    private void init() throws IOException {
        initializeMap(BusRouteApplication.getFilename());
    }

    public void initializeMap(String filename) {
        LOGGER.info("map initializing");
        routeMap = new HashMap<>();

        Resource resource = resourceLoader.getResource("file:" + filename);
        try {
            InputStream is = resource.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {
                parseLine(line);
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("map build finished");
    }

    public boolean isExists(int departure, int arrival) {
        if (routeMap == null || routeMap.isEmpty()) return false;
        TreeSet<Integer> set = routeMap.get(departure);

        return !(set == null || !set.contains(arrival));
    }


    private void parseLine(String line) {
        if (line == null || line.trim().isEmpty()) return;

        String[] values = line.trim().split(" ");
        if (values.length < 3) return;

        List<Integer> stations = new ArrayList<>(values.length - 2);
        for (int i = 2; i < values.length; i++) {
            stations.add(Integer.valueOf(values[i - 1]));
            addThemToo(stations, Integer.valueOf(values[i]));
        }
    }

    private void addThemToo(List<Integer> stations, int arrival) {
        for (int st : stations) {
            addToMap(st, arrival);
        }
    }

    private void addToMap(int departure, int arrival) {
        TreeSet<Integer> set = routeMap.get(departure);
        if (set == null) {
            set = new TreeSet<>();
            routeMap.put(departure, set);
        }
        set.add(arrival);
    }

    public Map<Integer, TreeSet<Integer>> getRouteMap() {
        return routeMap;
    }


}
