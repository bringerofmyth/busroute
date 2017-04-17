package com.goeuro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class BusRouteApplication {

    private static String filename;

    public static void main(String[] args) throws IOException {
        String textFile;
        if (args == null || args.length < 1)
            textFile = "/Users/melihgurgah/IdeaProjects/BusRoute/src/main/resources/test.txt";
        else textFile = args[0];

        setFilename(textFile);

        SpringApplication.run(BusRouteApplication.class, args);
    }

    public static String getFilename() {
        return filename;
    }

    public static void setFilename(String filename) {
        BusRouteApplication.filename = filename;
    }
}
