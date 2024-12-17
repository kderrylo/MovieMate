package com.example.moviemate.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviemate.repositories.SearchTVShowRepositories;
import com.example.moviemate.responses.TVShowResponse;

public class SearchTVShowViewModel extends ViewModel {
    private SearchTVShowRepositories searchTVShowRepositories;

    public SearchTVShowViewModel() {
        searchTVShowRepositories = new SearchTVShowRepositories();
    }

    public LiveData<TVShowResponse> searchTVShow(String query, int page){
        return searchTVShowRepositories.searchTVShow(query, page);
    }
}
