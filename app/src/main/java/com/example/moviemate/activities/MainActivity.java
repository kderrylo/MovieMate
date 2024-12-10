package com.example.moviemate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviemate.R;
import com.example.moviemate.adapters.PosterAdapter;
import com.example.moviemate.adapters.TVShowAdapter;
import com.example.moviemate.databinding.ActivityMainBinding;
import com.example.moviemate.listeners.TVShowListeners;
import com.example.moviemate.models.TVShow;
import com.example.moviemate.viewmodels.MostPopularTVShowViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TVShowListeners {
    TextView username;
    FirebaseAuth auth;
    FirebaseFirestore fstore;
    String userID;

    private ActivityMainBinding activityMainBinding;
    private MostPopularTVShowViewModel viewModel;
    private List<TVShow> tvShows = new ArrayList<>();
    private List<TVShow> posterList = new ArrayList<>();
    private TVShowAdapter tvShowsAdapter;
    private PosterAdapter posterAdapter;
    private int currentPage = 1;
    private int totalAvailablePage = 1;
    private boolean isFetchedTopRecommendationTVShows = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
            finish();
            return;
        }

        userID = auth.getCurrentUser().getUid();

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.tvShowRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        activityMainBinding.tvShowRecyclerView.setLayoutManager(gridLayoutManager);

        tvShowsAdapter = new TVShowAdapter(tvShows, this);
        activityMainBinding.tvShowRecyclerView.setAdapter(tvShowsAdapter);

        posterAdapter = new PosterAdapter(posterList);
        activityMainBinding.pagerPoster.setAdapter(posterAdapter);

        activityMainBinding.tvShowRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentPage += 1;
                getMostPopularTVShow();
            }
        });

        activityMainBinding.imageSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", "ImageSearch clicked");
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        viewModel = new ViewModelProvider(this).get(MostPopularTVShowViewModel.class);
        getMostPopularTVShow();

        fstore = FirebaseFirestore.getInstance();
        username = findViewById(R.id.textHelloUsername);

        DocumentReference documentReference = fstore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    username.setText(documentSnapshot.getString("username"));
                } else {
                    username.setText("User");
                }
            }
        });

        startAutoSlide();
    }

    private void getMostPopularTVShow() {
        toggleLoading();
        viewModel.getMostPopularTVShow(currentPage).observe(this, mostPopularTVShowResponse -> {
            if (mostPopularTVShowResponse != null) {
                toggleLoading();
                totalAvailablePage = mostPopularTVShowResponse.getPages();
                if (mostPopularTVShowResponse.getTvShows() != null && !mostPopularTVShowResponse.getTvShows().isEmpty()) {
                    int old = tvShows.size();
                    tvShows.addAll(mostPopularTVShowResponse.getTvShows());
                    tvShowsAdapter.notifyItemRangeInserted(old, tvShows.size());

                    if (!isFetchedTopRecommendationTVShows) {
                        fetchTopRecommendationTVShows();
                        isFetchedTopRecommendationTVShows = true;
                    }
                } else {
                    Log.e("MainActivity", "Something went wrong with the API.Try reopening the app.");
                }
            } else {
                Log.e("MainActivity", "Something went wrong with the API.Try reopening the app.");
            }
        });
    }

    private void fetchTopRecommendationTVShows() {
        if (tvShows != null && tvShows.size() > 0) {
            List<TVShow> topRecommendationTVShows = new ArrayList<>();
            List<TVShow> availableTVShows = new ArrayList<>(tvShows);
            availableTVShows.removeAll(posterList);

            if (availableTVShows.size() >= 5) {
                for (int i = 0; i < 5; i++) {
                    int randomIndex = (int) (Math.random() * availableTVShows.size());
                    topRecommendationTVShows.add(availableTVShows.get(randomIndex));
                    availableTVShows.remove(randomIndex);
                }
            } else {
                topRecommendationTVShows.addAll(availableTVShows);
            }

            tvShows.removeAll(posterList);

            if (topRecommendationTVShows != null && !topRecommendationTVShows.isEmpty()) {
                posterList.clear();
                posterList.addAll(topRecommendationTVShows);
                tvShows.removeAll(topRecommendationTVShows);
                posterAdapter.notifyDataSetChanged();
            }
        }
    }

    private void toggleLoading() {
        if (currentPage == 1) {
            if (activityMainBinding.getIsLoading() != null && activityMainBinding.getIsLoading()){
                activityMainBinding.setIsLoading(false);
            } else {
                activityMainBinding.setIsLoading(true);
            }
        }
    }

    private void startAutoSlide() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = activityMainBinding.pagerPoster.getCurrentItem();
                int nextItem = currentItem + 1;

                if (nextItem >= posterList.size()) {
                    nextItem = 0;
                }

                // Mengubah halaman ViewPager2
                activityMainBinding.pagerPoster.setCurrentItem(nextItem, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.putExtra("tvShow", tvShow);
        startActivity(intent);
    }
}