package com.github.iweinzierl.moviedatabase.search.controller;

import com.github.iweinzierl.moviedatabase.search.domain.Movie;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import de.inselhome.moviesearch.api.domain.MoviePreview;
import de.inselhome.moviesearch.tmdb.TmdbSearchProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

@RestController
public class SearchController {

    private static Logger LOG = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private TmdbSearchProvider tmdbSearchProvider;

    @RequestMapping(path = "/api/movie", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity search(@RequestParam(value = "title", required = true) String title) {
        LOG.info("Search movie with title: {}", title);

        List<MoviePreview> moviePreviews = tmdbSearchProvider.search(title);

        if (moviePreviews != null && !moviePreviews.isEmpty()) {
            Collection<Movie> searchResult = Collections2.transform(moviePreviews, new Function<MoviePreview, Movie>() {
                @Nullable
                @Override
                public Movie apply(@Nullable MoviePreview input) {
                    Movie movie = new Movie();
                    movie.setId(input.getMovieId());
                    movie.setCover(input.getCover());
                    movie.setTitle(input.getTitle());
                    movie.setDescription(input.getDescription());

                    return movie;
                }
            });

            LOG.info("Found {} search results for: {}", searchResult.size(), title);
            return ResponseEntity.ok(searchResult);
        }

        return ResponseEntity.notFound().build();
    }

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
            result.setCover(movie.getCover());
            result.setDescription(movie.getDescription());
            result.setGenres(movie.getGenres());
            result.setLength(movie.getLength());
            result.setOriginalTitle(movie.getOriginalTitle());
            result.setPublishDate(movie.getPublishDate());
            result.setTitle(movie.getTitle());

            return ResponseEntity.ok(result);
        } else {
            LOG.warn("Did not find movie by id: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
