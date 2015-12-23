package com.github.iweinzierl.moviedatabase.search.controller;

import com.github.iweinzierl.moviedatabase.search.domain.Movie;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import de.inselhome.moviesearch.api.domain.MoviePreview;
import de.inselhome.moviesearch.tmdb.TmdbSearchProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

@RestController
public class SearchController {

    private static Logger LOG = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private TmdbSearchProvider tmdbSearchProvider;

    @RequestMapping(path = "/api/movie/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
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

                    return movie;
                }
            });

            return ResponseEntity.ok(searchResult);
        }

        return ResponseEntity.notFound().build();
    }
}
