package com.example.moviemate.responses;

import com.example.moviemate.models.TVShowDetail;
import com.google.gson.annotations.SerializedName;

public class TVShowDetailResponse {

    @SerializedName("tvShow")
    private TVShowDetail tvShowDetails;

    public TVShowDetail getTvShowDetails() {
        return tvShowDetails;
    }
}
