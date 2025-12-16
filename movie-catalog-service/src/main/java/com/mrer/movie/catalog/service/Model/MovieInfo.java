package com.mrer.movie.catalog.service.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "movie_info")
@AllArgsConstructor
@NoArgsConstructor
public class MovieInfo {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    private String name;
    private String description;
    private String path;
}
