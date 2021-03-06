package com.github.iweinzierl.moviedatabase.search.controller;

import com.github.iweinzierl.moviedatabase.search.domain.Movie;
import com.github.iweinzierl.springbootlogging.annotation.HttpLogging;
import com.github.iweinzierl.springbootlogging.annotation.LoggingOption;
import com.google.common.base.Strings;
import de.inselhome.moviesearch.api.domain.MoviePreview;
import de.inselhome.moviesearch.tmdb.TmdbSearchProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class SearchController {

    private static Logger LOG = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private TmdbSearchProvider tmdbSearchProvider;

    @HttpLogging(options = {LoggingOption.REQUEST, LoggingOption.RESPONSE})
    @RequestMapping(path = "/api/movie", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity search(@RequestParam(value = "title", required = true) String title) {
        LOG.info("Search movie with title: {}", title);

        List<MoviePreview> moviePreviews = tmdbSearchProvider.search(title);

        if (moviePreviews != null && !moviePreviews.isEmpty()) {
            Collection<Movie> searchResult = moviePreviews
                    .parallelStream()
                    .map(moviePreview -> {
                        Movie movie = new Movie();
                        movie.setId(moviePreview.getMovieId());
                        movie.setCoverUrl(moviePreview.getCover());
                        movie.setTitle(moviePreview.getTitle());
                        movie.setDescription(moviePreview.getDescription());
                        movie.setLength(moviePreview.getLength());
                        movie.setPublished(moviePreview.getPublishDate());
                        movie.setGenres(((moviePreview.getGenres().parallelStream().collect(Collectors.toSet()))));

                        return movie;
                    })
                    .collect(Collectors.toList());

            LOG.info("Found {} search results for: {}", searchResult.size(), title);
            if (LOG.isDebugEnabled()) {
                for (Movie movie : searchResult) {
                    LOG.debug("  -> {}", movie.getTitle());
                }
            }

            return ResponseEntity.ok(searchResult);
        }

        return ResponseEntity.notFound().build();
    }

    @HttpLogging(options = {LoggingOption.REQUEST, LoggingOption.RESPONSE})
    @RequestMapping(path = "/api/movie/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity get(@PathVariable String id) {
        if (Strings.isNullOrEmpty(id)) {
            return ResponseEntity.badRequest().build();
        }

        de.inselhome.moviesearch.api.domain.Movie movie = tmdbSearchProvider.get(id);

        if (movie != null) {
            LOG.info("Found movie by id '{}': {}", id, movie);

            Movie result = new Movie();
            result.setId(movie.getId());
            result.setCoverUrl(movie.getCoverUrl());
            result.setDescription(movie.getDescription());
            result.setGenres(movie.getGenres());
            result.setLength(movie.getLength());
            result.setOriginalTitle(movie.getOriginalTitle());
            result.setPublished(movie.getPublished());
            result.setTitle(movie.getTitle());

            return ResponseEntity.ok(result);
        } else {
            LOG.warn("Did not find movie by id: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
