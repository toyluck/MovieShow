package com.example.hyc.movieshow.widges;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hyc.movieshow.R;

/**
 * Created by hyc on 16-11-8.
 */

public class ImageTextContainer extends LinearLayout
{

    private Drawable mIcon;
    private String   subTitle;
    private Drawable mColor;
    private TextView mTextView;

    public ImageTextContainer(Context context)
    {
        this(context, null);
    }

    public ImageTextContainer(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ImageTextContainer(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ImageTextContainer, 0, 0);
        mColor = ta.getDrawable(R.styleable.ImageTextContainer_background_color);
        subTitle = ta.getString(R.styleable.ImageTextContainer_subtitle);
        mIcon = ta.getDrawable(R.styleable.ImageTextContainer_tagicon);
        ta.recycle();
        LayoutInflater.from(context).inflate(R.layout.widge_imagetext_container, this, true);

        init();
    }

    private void init()
    {
        ViewGroup container = (ViewGroup) findViewById(R.id.ctl_image_text_container);
        ImageView icon      = (ImageView) findViewById(R.id.iv_icon);
        mTextView = (TextView) findViewById(R.id.tv_subtitle);

        container.setBackground(mColor);
        icon.setImageDrawable(mIcon);
        mTextView.setText(subTitle);
    }

    public void setSubTitle(String subTitle)
    {
        this.subTitle = subTitle;
        mTextView.setText(subTitle);

    }
}
