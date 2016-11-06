package com.example.hyc.movieshow.Movies;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by hyc on 16-11-5.
 */

public class MovieBinds
{
    public static String IMG_BASE_URL = "http://image.tmdb.org/t/p/";

    //
    public enum PICSTYLE
    {
        w92("w92"),
        w154("w154"),
        w185("w185"),
        w342("w342"),
        w500("w500"),
        w780("w780");

        private String mType = "w154";

        PICSTYLE(String type)
        {
            mType = type;
        }

        public String getType()
        {
            return mType;
        }

    }
    public MovieBinds(){

    }
    @BindingAdapter({
        "bind:imgsrc"
    })
    public static void setSrc(ImageView imageView, String src)
    {
        String path = IMG_BASE_URL + PICSTYLE.w154.getType() + "/" + src;

        Glide.with(imageView.getContext()).load(path).asBitmap().fitCenter().placeholder(android.R.drawable
            .stat_sys_download).into
            (imageView);
    }
}
