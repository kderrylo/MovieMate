package com.example.moviemate.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.text.HtmlCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.moviemate.R;
import com.example.moviemate.adapters.EpisodeAdapter;
import com.example.moviemate.adapters.ImageSliderAdapter;
import com.example.moviemate.databinding.ActivityDetailBinding;
import com.example.moviemate.databinding.LayoutEpisodesBottomSheetBinding;
import com.example.moviemate.viewmodels.TVShowDetailViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding activityDetailBinding;
    private TVShowDetailViewModel tvShowDetailViewModel;
    private BottomSheetDialog episodesBottomSheetDialog;
    private LayoutEpisodesBottomSheetBinding layoutEpisodesBottomSheetBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void doInitialization(){
        tvShowDetailViewModel = new ViewModelProvider(this).get(TVShowDetailViewModel.class);
        activityDetailBinding.arrowBack.setOnClickListener(view -> onBackPressed());
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
                        activityDetailBinding.setTvShowImageURL(
                                tvShowDetailResponse.getTvShowDetails().getImagePath()
                        );
                        activityDetailBinding.imageTVShow.setVisibility(View.VISIBLE);
                        activityDetailBinding.setDescription(
                                String.valueOf(
                                        HtmlCompat.fromHtml(
                                                tvShowDetailResponse.getTvShowDetails().getDescription(),
                                                HtmlCompat.FROM_HTML_MODE_LEGACY
                                        )
                                )
                        );
                        activityDetailBinding.textDescription.setVisibility(View.VISIBLE);
                        activityDetailBinding.textReadMore.setVisibility(View.VISIBLE);
                        activityDetailBinding.textReadMore.setOnClickListener(view -> {
                            if(activityDetailBinding.textReadMore.getText().toString().equals("Read More")){
                                activityDetailBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                                activityDetailBinding.textDescription.setEllipsize(null);
                                activityDetailBinding.textReadMore.setText(R.string.read_less);
                            }
                            else{
                                activityDetailBinding.textDescription.setMaxLines(4);
                                activityDetailBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                activityDetailBinding.textReadMore.setText(R.string.read_more);
                            }
                        });
                        activityDetailBinding.setRating(
                                String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        Double.parseDouble(tvShowDetailResponse.getTvShowDetails().getRating())
                                )
                        );
                        if (tvShowDetailResponse.getTvShowDetails().getGenres() != null){
                            activityDetailBinding.setGenre(tvShowDetailResponse.getTvShowDetails().getGenres()[0]);
                        }
                        else{
                            activityDetailBinding.setGenre("N/A");
                        }
                        activityDetailBinding.setRuntime(tvShowDetailResponse.getTvShowDetails().getRuntime() + "Min");
                        activityDetailBinding.viewDivider1.setVisibility(View.VISIBLE);
                        activityDetailBinding.layoutMisc.setVisibility(View.VISIBLE);
                        activityDetailBinding.viewDivider2.setVisibility(View.VISIBLE);
                        activityDetailBinding.buttonWebsite.setOnClickListener(view -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(tvShowDetailResponse.getTvShowDetails().getUrl()));
                            startActivity(intent);
                        });
                        activityDetailBinding.buttonWebsite.setVisibility(View.VISIBLE);
                        activityDetailBinding.buttonEpisodes.setVisibility(View.VISIBLE);
                        activityDetailBinding.buttonEpisodes.setOnClickListener(view -> {
                            if(episodesBottomSheetDialog == null){
                                episodesBottomSheetDialog = new BottomSheetDialog(DetailActivity.this);
                                layoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(
                                        LayoutInflater.from(DetailActivity.this),
                                        R.layout.layout_episodes_bottom_sheet,
                                        findViewById(R.id.episodeContainer),
                                        false
                                );
                                episodesBottomSheetDialog.setContentView(layoutEpisodesBottomSheetBinding.getRoot());

                                layoutEpisodesBottomSheetBinding.episodeRecyclerView.setAdapter(
                                        new EpisodeAdapter(tvShowDetailResponse.getTvShowDetails().getEpisodes())
                                );
                                layoutEpisodesBottomSheetBinding.textTitle.setText(
                                        String.format("Episodes | %s", getIntent().getStringExtra("name"))
                                );
                                layoutEpisodesBottomSheetBinding.imageClose.setOnClickListener(view1 -> episodesBottomSheetDialog.dismiss());
                            }

                            // ---Optional Section--- //
                            FrameLayout frameLayout = episodesBottomSheetDialog.findViewById(
                                    com.google.android.material.R.id.design_bottom_sheet
                            );
                            if(frameLayout != null){
                                BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                                bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }
                            // ---Optional Section End---//

                            episodesBottomSheetDialog.show();
                        });
                        loadBasicTVShowDetails();
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

    private void loadBasicTVShowDetails(){
        activityDetailBinding.setTvShowName(getIntent().getStringExtra("name"));
        activityDetailBinding.setNetworkCountry(
                getIntent().getStringExtra("network") + "(" +
                        getIntent().getStringExtra("country") + ")"
        );
        activityDetailBinding.setStatus(getIntent().getStringExtra("status"));
        activityDetailBinding.setStartedDate(getIntent().getStringExtra("startDate"));
        activityDetailBinding.textName.setVisibility(View.VISIBLE);
        activityDetailBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        activityDetailBinding.textStatus.setVisibility(View.VISIBLE);
        activityDetailBinding.textStarted.setVisibility(View.VISIBLE);
    }
}