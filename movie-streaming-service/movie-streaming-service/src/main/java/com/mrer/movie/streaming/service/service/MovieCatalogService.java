package com.mrer.movie.streaming.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class MovieCatalogService {

    public static final String CATALOG_SERVICE = "http://movie-catalog-service";

    @Autowired
    private RestTemplate restTemplate;

    public String getMoviePath(UUID pathById){
        var response = restTemplate.getForEntity(CATALOG_SERVICE+"/movie-info/find-path-Id/{movieInfoId}", String.class, pathById);
        return response.getBody();
    }
}
