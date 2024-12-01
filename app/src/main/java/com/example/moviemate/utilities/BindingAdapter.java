package com.example.moviemate.utilities;

import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class BindingAdapter {
    @androidx.databinding.BindingAdapter("android:imageURL")
    public static void setImageURL(ImageView imageView, String imageURL){
        try {
            imageView.setAlpha(0f);
            Picasso.get().load(imageURL).noFade().into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    imageView.animate().setDuration(300).alpha(1f).start();
                }

                @Override
                public void onError(Exception e) {
                    Log.e("BindingAdapter", "Error loading image: " + e.getMessage(), e);
                }
            });
        }
        catch (Exception e){
            Log.e("BindingAdapter", "Error in setImageURL: " + e.getMessage(), e);
        }
    }
}