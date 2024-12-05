package com.example.moviemate.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviemate.repositories.TVShowDetailRepositories;
import com.example.moviemate.responses.TVShowDetailResponse;

public class TVShowDetailViewModel extends ViewModel {

    private TVShowDetailRepositories tvShowDetailRepositories;

    public TVShowDetailViewModel() {
        tvShowDetailRepositories = new TVShowDetailRepositories();
    }

    public LiveData<TVShowDetailResponse> getTVShowDetails(String tvShowID){
        return tvShowDetailRepositories.getTVShowDetails(tvShowID);
    }
}
