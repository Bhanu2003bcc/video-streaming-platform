package com.mrer.movie.streaming.service.controller;

import com.mrer.movie.streaming.service.model.StreamResponse;
import com.mrer.movie.streaming.service.service.MovieCatalogService;
import com.mrer.movie.streaming.service.service.StreamingAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping
public class VideoStreamController {

    @Autowired
    private MovieCatalogService movieCatalogService;

    public static final Logger log = Logger.getLogger(VideoStreamController.class.getName());

    public static final String VIDEO_DIRECTORY = "/home/lol/MicroService";

    private final StreamingAccessService streamingService;

    public VideoStreamController(StreamingAccessService streamingService) {
        this.streamingService = streamingService;
    }

    @GetMapping("/stream/{videoPath}")
    public ResponseEntity<InputStreamResource> streamVideo(
            @PathVariable String videoPath) throws FileNotFoundException {

        File file = Paths.get(VIDEO_DIRECTORY, videoPath).toFile();

        System.out.println("Resolved path: " + file.getAbsolutePath());

        if(file.exists()){
            InputStreamResource inputStreamResource =
                    new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("video/mp4"))
                    .body(inputStreamResource);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/stream/with-id/{videoInfoId}")
    public ResponseEntity<InputStreamResource> streamVideoById(
            @PathVariable UUID videoInfoId) throws FileNotFoundException {
        String moviePath = movieCatalogService.getMoviePath(videoInfoId);
        log.log(Level.INFO, "Resolve movie path = {0}", moviePath);
        return streamVideo(moviePath);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(){
        return ResponseEntity.ok().build();
    }

    // For short time url
    @GetMapping("/stream/{movieId}")
    public ResponseEntity<StreamResponse> stream(
            @PathVariable String movieId,
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-USER-ROLE") String role) throws AccessDeniedException {

        StreamResponse response =
                streamingService.generateStreamUrl(movieId, userId, role);

        return ResponseEntity.ok(response);
    }
}

