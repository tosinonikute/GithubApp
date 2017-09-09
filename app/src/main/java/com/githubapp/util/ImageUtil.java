package com.githubapp.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.githubapp.R;

/**
 * @author Tosin Onikute.
 */

public class ImageUtil {
    private Context mContext;

    public ImageUtil(Context context) {
        mContext = context;
    }

    public static void displayImage(Context context, String imageResource, @NonNull ImageView imageView) {
        Glide.with(context)
                .load(imageResource)
                .placeholder(R.drawable.place_holder)
                .into(imageView);
    }
}
