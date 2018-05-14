package com.facebook_pics_2.starr.myapplication;

import android.graphics.drawable.Drawable;

/**
 * Created by starr on 3/24/2017.
 */

public class Photo
{
    private String link;
    private int height;
    private int width;
    private int position;
    private Drawable drawable;

    public Photo(String link, int height, int width)
    {
        this.link=link;
        this.height=height;
        this.width=width;
    }

    public Photo(String link, int height, int width, int position)
    {
       this(link, height, width);
        this.position=position;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "link='" + link + '\'' +
                ", height=" + height +
                ", width=" + width +
                '}';
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
