package com.example.moviemate.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.moviemate.database.TVShowDatabase;
import com.example.moviemate.models.TVShow;
import com.example.moviemate.repositories.TVShowDetailRepositories;
import com.example.moviemate.responses.TVShowDetailResponse;

import io.reactivex.Completable;

public class TVShowDetailViewModel extends AndroidViewModel {

    private TVShowDetailRepositories tvShowDetailRepositories;

    private TVShowDatabase tvShowDatabase;

    public TVShowDetailViewModel(@NonNull Application application) {
        super(application);
        tvShowDetailRepositories = new TVShowDetailRepositories();
        tvShowDatabase = TVShowDatabase.getTvShowDatabase(application);
    }

    public LiveData<TVShowDetailResponse> getTVShowDetails(String tvShowID){
        return tvShowDetailRepositories.getTVShowDetails(tvShowID);
    }

    public Completable addToWatchList(TVShow tvShow){
        return tvShowDatabase.tvShowDao().addToWatchList(tvShow);
    }
}
