package com.facebook_pics_2.starr.myapplication;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.ProfilePictureView;

/**
 * Created by starr on 3/7/2017.
 */

public class FriendsAdapter extends ArrayAdapter implements AdapterView.OnItemClickListener
{
    int resource;
    Activity activity;

    private static class ViewHolder
    {
        TextView friendName;
        ProfilePictureView friendImage;
    }
    public FriendsAdapter(Context context, int resource, Object[] objects)
    {
        super(context, resource, objects);
        this.resource=resource;
    }

    public FriendsAdapter(Context context, int resource, Object[] objects, Activity activity)
    {
        this(context, resource, objects);
        this.activity=activity;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Friend friend=(Friend)getItem(position);
        ViewHolder viewHolder;
        if(convertView==null)
        {
            viewHolder=new ViewHolder();
            LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView=inflater.inflate(resource,parent,false);
            viewHolder.friendName=(TextView)convertView.findViewById(R.id.friend_name);
            viewHolder.friendImage=(ProfilePictureView)convertView.findViewById(R.id.friend_image);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder)convertView.getTag();
        }
      //  Log.v("VERBOSE/VIEW","Friend: "+viewHolder.friendName.getText());
        viewHolder.friendName.setText(friend.getName());
        viewHolder.friendImage.setProfileId(friend.getProfileId());
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
     //   Log.v("VERBOSE","HELLO YALL I CLICKED ITEM AT POSITION "+position);
        MainActivity mainActivity=(MainActivity)activity;
        Friend friend=(Friend)getItem(position);
      //  Log.v("VERBOSE",friend.getName()+"'s profile id is "+friend.getProfileId());
        mainActivity.getFriendPics(friend);
    }
}
