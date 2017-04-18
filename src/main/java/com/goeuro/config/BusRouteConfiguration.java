package com.goeuro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

/**
 * Created by melihgurgah on 15/04/2017.
 */

@Configuration
public class BusRouteConfiguration {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(BusRouteConfiguration.class.getName());
    private Map<Integer, HashSet<Integer>> mapping;

    @Value("file:#{systemProperties.routeMap}")
    public Resource file;

    @PostConstruct
    public void init() {
        LOGGER.info("map init started");
        readForMap();
        WatchFile myRunnable = new WatchFile();
        Thread t = new Thread(myRunnable);
        t.start();
    }

    @Bean(name = "routeMap")
    public Map<Integer, HashSet<Integer>> getRouteMap() throws IOException {
        return mapping;
    }

    private void readForMap() {
        if (mapping != null) {
            mapping.clear();
        } else {
            mapping = new HashMap<>();
        }
        try {
            InputStream is = file.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;

            int total = Integer.valueOf(br.readLine().trim());

            while (total > 0) {
                line = br.readLine();
                System.out.println(line);
                parseLine(line, mapping);
                total--;
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("map creation finished");
    }

    private void parseLine(String line, Map<Integer, HashSet<Integer>> routeMap) {
        if (line == null || line.trim().isEmpty()) return;

        String[] values = line.trim().split(" ");
        if (values.length < 3) return;

        Set<Integer> stations = new HashSet<>(values.length - 2);
        for (int i = 2; i < values.length; i++) {
            stations.add(Integer.valueOf(values[i - 1]));
            addThemToo(stations, Integer.valueOf(values[i]), routeMap);
        }
    }

    private void addThemToo(Set<Integer> stations, int arrival, Map<Integer, HashSet<Integer>> routeMap) {
        for (int st : stations) {
            addToMap(st, arrival, routeMap);
        }
    }

    private void addToMap(int departure, int arrival, Map<Integer, HashSet<Integer>> routeMap) {
        HashSet<Integer> set = routeMap.get(departure);
        if (set == null) {
            set = new HashSet<>();
            routeMap.put(departure, set);
        }
        set.add(arrival);
    }

    private class WatchFile implements Runnable {

        public void run() {
            try {
                fileWatcher();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }

        private void fileWatcher() throws IOException {

            final Path path = FileSystems.getDefault().getPath(file.getFile().getParentFile().getAbsolutePath());
            try {
                final WatchService watchService = FileSystems.getDefault().newWatchService();
                final WatchKey watchKey = path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
                while (true) {
                    final WatchKey wk = watchService.take();
                    for (WatchEvent<?> event : wk.pollEvents()) {
                        //we only register "ENTRY_MODIFY" so the context is always a Path.
                        final Path changed = (Path) event.context();
                        if (changed.endsWith(file.getFilename())) {
                            readForMap();
                            LOGGER.info("File has changed");
                        }
                    }
                    // reset the key
                    boolean valid = wk.reset();
                    if (!valid) {
                        LOGGER.info("Key has been unregistered");
                    }
                }
            } catch (InterruptedException ex) {
                LOGGER.info("error" + ex.getMessage());
            }
        }
    }

}
