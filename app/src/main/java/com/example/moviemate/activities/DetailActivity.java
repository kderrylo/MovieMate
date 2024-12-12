package com.example.moviemate.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.moviemate.R;
import com.example.moviemate.adapters.ImageSliderAdapter;
import com.example.moviemate.databinding.ActivityDetailBinding;
import com.example.moviemate.viewmodels.TVShowDetailViewModel;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding activityDetailBinding;
    private TVShowDetailViewModel tvShowDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        tvShowDetailViewModel = new ViewModelProvider(this).get(TVShowDetailViewModel.class);
        getTVShowDetails();
    }
    private void getTVShowDetails(){
        activityDetailBinding.setIsLoading(true);
        String tvShowId = String.valueOf(getIntent().getIntExtra("id", -1));
        tvShowDetailViewModel.getTVShowDetails(tvShowId).observe(
                this, tvShowDetailResponse -> {
                    activityDetailBinding.setIsLoading(false);
                    if(tvShowDetailResponse.getTvShowDetails() != null){
                        if(tvShowDetailResponse.getTvShowDetails().getPictures() != null){
                            loadImageSlider(tvShowDetailResponse.getTvShowDetails().getPictures());
                        }
                    }
                }
        );
    }

    private void loadImageSlider(String[] sliderImages){
        activityDetailBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityDetailBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        activityDetailBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityDetailBinding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupSliderIndicators(sliderImages.length);
        activityDetailBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSlideIndicator(position);
            }
        });
    }

    private void setupSliderIndicators(int count){
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);

        for(int i=0; i<indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.background_slider_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            activityDetailBinding.layoutSliderIndicators.addView(indicators[i]);
        }
        activityDetailBinding.layoutSliderIndicators.setVisibility(View.VISIBLE);
        setCurrentSlideIndicator(0);
    }

    private void setCurrentSlideIndicator(int position){
        int childCount = activityDetailBinding.layoutSliderIndicators.getChildCount();
        for(int i=0; i<childCount; i++){
            ImageView imageView = (ImageView) activityDetailBinding.layoutSliderIndicators.getChildAt(i);

            if(i==position){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_active)
                );
            }
            else{
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_inactive)
                );
            }
        }
    }
}