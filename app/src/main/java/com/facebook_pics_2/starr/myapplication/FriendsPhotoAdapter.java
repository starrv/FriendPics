package com.facebook_pics_2.starr.myapplication;

import android.app.Activity;
import android.content.Context;;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.concurrent.ExecutionException;


/**
 * Created by starr on 3/23/2017.
 */

class FriendsPhotoAdapter  extends ArrayAdapter implements AdapterView.OnItemClickListener, TextWatcher
{
    private int resource;
    private Activity activity;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }

    private static class ViewHolder
    {
        ImageView image;
        TextView progess_view;
        Drawable drawable;
    }
    FriendsPhotoAdapter(Context context, int resource, Object[] objects)
    {
        super(context, resource, objects);
        this.resource=resource;
    }

    FriendsPhotoAdapter(Context context, int resource, Object[] objects, Activity activity)
    {
        this(context, resource, objects);
        this.activity=activity;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        ViewHolder viewHolder;
            Photo photo=(Photo)getItem(position);
       // Log.v("VERBOSE/PHOTO","creating photo "+photo.getPosition()+"with link "+photo.getLink());
            if(convertView==null)
             {
                viewHolder=new ViewHolder();
                LayoutInflater inflater=LayoutInflater.from(getContext());
                convertView=inflater.inflate(resource,parent,false);
                viewHolder.image=(ImageView)convertView.findViewById(R.id.pic);
                 viewHolder.progess_view=(TextView)convertView.findViewById(R.id.progress);
                convertView.setTag(viewHolder);
             }
            else
            {
                viewHolder=(ViewHolder)convertView.getTag();
            }
    //    viewHolder.image.setImageResource(R.drawable.friend_pics_2);
        viewHolder.progess_view.setText("Photo "+(position+1));
        viewHolder.image.setImageDrawable(photo.getDrawable());
        viewHolder.image.setVisibility(View.VISIBLE);
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Photo photo=(Photo)getItem(position);
        Toast.makeText(getContext(),"YOU JUST CLICKED PHOTO WITH LINK "+photo.getLink(),Toast.LENGTH_SHORT).show();
        Log.v("VERBOSE","YOU JUST CLICKED PHOTO "+position+" : "+photo);
    }



}
