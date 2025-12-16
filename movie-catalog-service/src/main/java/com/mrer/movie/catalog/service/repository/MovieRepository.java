package com.mrer.movie.catalog.service.repository;

import com.mrer.movie.catalog.service.Model.MovieInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MovieRepository extends JpaRepository<MovieInfo, UUID> {
}
