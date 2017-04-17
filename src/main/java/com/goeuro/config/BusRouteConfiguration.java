package com.goeuro.config;

import com.goeuro.service.BusRouteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by melihgurgah on 15/04/2017.
 */
@Configuration
public class BusRouteConfiguration {

    @Bean
    BusRouteService routeService (){
        return new BusRouteService();
    }
}
