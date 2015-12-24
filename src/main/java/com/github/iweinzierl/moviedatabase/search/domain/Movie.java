package com.github.iweinzierl.moviedatabase.search.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.util.Set;

public class Movie implements de.inselhome.moviesearch.api.domain.Movie {

    private String id;

    private String title;
    private String originalTitle;

    private String description;
    private String cover;
    private Set<String> genres;
    private int length;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate publishDate;

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    @Override
    public String getOriginalTitle() {
        return originalTitle;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public String getCover() {
        return cover;
    }

    @Override
    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    @Override
    public Set<String> getGenres() {
        return genres;
    }

    @Override
    public void setLength(Integer length) {
        this.length = length;
    }

    @Override
    public Integer getLength() {
        return length;
    }

    @Override
    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public LocalDate getPublishDate() {
        return publishDate;
    }
}
