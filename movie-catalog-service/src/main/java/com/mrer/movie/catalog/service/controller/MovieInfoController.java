package com.mrer.movie.catalog.service.controller;


import com.mrer.movie.catalog.service.Model.MovieInfo;
import com.mrer.movie.catalog.service.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie-info")
public class MovieInfoController {

    private final MovieRepository movieRepository;

    @PostMapping("/save")
    public List<MovieInfo> saveAll(@RequestBody List<MovieInfo> movielist){
        return movieRepository.saveAll(movielist);
    }

    @GetMapping("/list")
    public List<MovieInfo> getAll(){
        return movieRepository.findAll();
    }

    @GetMapping("/find-path-Id/{movieInfoId}")
    public String findPathById(@PathVariable UUID movieInfoId){
        var videoInfoOptional = movieRepository.findById(movieInfoId);
        return videoInfoOptional.map(movieInfo -> movieInfo.getPath()).orElse(null);
    }
}
