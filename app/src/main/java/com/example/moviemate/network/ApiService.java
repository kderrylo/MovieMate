package com.example.moviemate.network;

import com.example.moviemate.responses.TVShowDetailResponse;
import com.example.moviemate.responses.TVShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Call<TVShowResponse> getMostPopularTVShows(@Query("page") int page);

//    @GET("search")
//    Call<TVShowResponse> searchTVShow(@Query("q") String query, @Query("page") int page);

    @GET("show-details")
    Call<TVShowDetailResponse> getTVShowDetails(@Query("q") String tvShowId);
}
