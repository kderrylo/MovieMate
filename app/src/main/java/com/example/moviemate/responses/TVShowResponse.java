package com.example.moviemate.responses;

import com.example.moviemate.models.TVShow;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TVShowResponse {

    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int pages;

    @SerializedName("tv_shows")
    private List<TVShow> tvShows;

    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }

    public List<TVShow> getTvShows() {
        return tvShows;
    }
}
