package com.example.moviemate.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviemate.repositories.MostPopularTVShowRepositories;
import com.example.moviemate.responses.TVShowResponse;

public class MostPopularTVShowViewModel extends ViewModel {

    private MostPopularTVShowRepositories mostPopularTVShowRepositories;
    public MostPopularTVShowViewModel(){
        mostPopularTVShowRepositories = new MostPopularTVShowRepositories();
    }

    public LiveData<TVShowResponse> getMostPopularTVShow(int page){
        return mostPopularTVShowRepositories.getMostPopularTVShow(page);
    }

}
