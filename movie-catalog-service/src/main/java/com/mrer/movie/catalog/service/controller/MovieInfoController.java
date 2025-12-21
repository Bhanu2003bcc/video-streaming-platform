package com.mrer.movie.catalog.service.controller;


import com.mrer.movie.catalog.service.Model.MovieInfo;
import com.mrer.movie.catalog.service.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/movie-info")
public class MovieInfoController {

//    private static final Logger log = LoggerFactory.getLogger(MovieInfoController.class);
    private final MovieRepository movieRepository;

    @PostMapping("/save")
    public List<MovieInfo> saveAll(@RequestBody List<MovieInfo> movielist){
        log.info("List of movies are saving", movielist);
        return movieRepository.saveAll(movielist);
    }

    @GetMapping("/list")
    public List<MovieInfo> getAll(){
        log.info("Fetching movie info");
        return movieRepository.findAll();
    }

    @GetMapping("/find-path-Id/{movieInfoId}")
    public String findPathById(@PathVariable UUID movieInfoId){
        log.info("Fetching movie info, movieId={}", movieInfoId);
        var videoInfoOptional = movieRepository.findById(movieInfoId);
        return videoInfoOptional.map(movieInfo -> movieInfo.getPath()).orElse(null);
    }
}
