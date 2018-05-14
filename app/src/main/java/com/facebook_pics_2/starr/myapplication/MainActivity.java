package com.facebook_pics_2.starr.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Scene;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
{
    TextView text;
    CallbackManager callbackManager;
    LoginButton loginButton;
    AccessToken accessToken;
    AccessTokenTracker accessTokenTracker;
    TextView user_name;
    TextView friend_subtitle;
    ListView friend_list;
    ListView friend_pics_list;
    ProfilePictureView user_image;
    Activity thisActivity;
    JSONObject friend_obj;
    JSONArray friend_json_array;
    Bundle savedInstanceState;

    @Override
    protected void onRestart()
    {
        Log.v("VERBOSE","activity restarted");
        super.onRestart();
        getLoginProfile();
    }

    @Override
    protected void onResume()
    {
        Log.v("VERBOSE","activity resumed");
        super.onResume();
        getLoginProfile();
    }

    @Override
    protected void onStart()
    {
        Log.v("VERBOSE","activity started");
        super.onStart();
        getLoginProfile();
    }

    private void setUpLogin()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(callbackManager==null)callbackManager = CallbackManager.Factory.create();
        if(accessTokenTracker==null)accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null)
                {
                    resetOnLogOut();
                }
                else
                {
                    registerLogin();
                }
                // Set the access token using
                // currentAccessToken when it's loaded or set.

            }
        };
     /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        if(loginButton==null)loginButton = (LoginButton) findViewById(R.id.login_button);
        // Callback registration
        if(callbackManager==null)callbackManager = CallbackManager.Factory.create();
        registerLogin();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState=savedInstanceState;
        thisActivity=this;
        Log.v("VERBOSE", "activity created");
        setContentView(R.layout.activity_main);
        setUpLogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        resetOnLogOut();
    }

    private void resetOnLogOut()
    {
        if(user_image!=null)user_image.setVisibility(View.GONE);
        if(user_name!=null)user_name.setText(R.string.welcome);
        if(friend_list!=null) friend_list.setVisibility(View.GONE);
        if(friend_subtitle!=null)friend_subtitle.setVisibility(View.GONE);
    }

    private void getLoginProfile()
    {
        loginButton.setReadPermissions(Arrays.asList("public_profile"));
        loginButton.setReadPermissions(Arrays.asList("user_status"));
        loginButton.setReadPermissions(Arrays.asList("user_friends"));
        loginButton.setReadPermissions(Arrays.asList("user_posts"));
        loginButton.setReadPermissions(Arrays.asList("user_photos"));

        Log.v("VERBOSE", "logged in");
        Log.v("VERBOSE", "REQUEST TO LOG IN SUCCESSFUL");
        Log.v("VERBOSE","access token: "+accessToken);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.GRAPH_API_DEBUG_INFO);

        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        if (object == null)
                        {
                            Log.v("VERBOSE", "no data");
                        }
                        else
                        {
                            if(user_name==null)user_name = (TextView) findViewById(R.id.user_name);
                            if(user_image==null)user_image = (ProfilePictureView) findViewById(R.id.user_image);
                            user_image.setVisibility(View.VISIBLE);
                            try
                            {
                                user_name.setText(object.getString("name"));
                                user_image.setProfileId(object.getString("id"));
                                Log.v("VERBOSE","OBJECT: "+object.toString());
                                friend_obj = object.getJSONObject("friends");
                                Log.v("VERBOSE","JSON OBJECT FRIENDS: "+friend_obj.toString());
                                friend_json_array = friend_obj.getJSONArray("data");
                                Log.v("VERBOSE","JSON ARRAY DATA: "+friend_json_array.toString());
                                listFriends();
                            }
                            catch (JSONException jse)
                            {
                                Log.e("ERROR", jse.getMessage());

                                if(jse.getMessage().equals("No value for name") || (jse.getMessage().equals("No value for id")))
                                {

                                    LoginManager.getInstance().logInWithReadPermissions(thisActivity,Arrays.asList("user_profile"));
                                }

                                if(jse.getMessage().equals("No value for friends"))
                                {

                                    LoginManager.getInstance().logInWithReadPermissions(thisActivity,Arrays.asList("user_friends"));
                                }
                                if(jse.getMessage().equals("No value for photos"))
                                {

                                    LoginManager.getInstance().logInWithReadPermissions(thisActivity,Arrays.asList("user_photos"));
                                    LoginManager.getInstance().logInWithReadPermissions(thisActivity,Arrays.asList("user_posts"));
                                }
                                if(jse.getMessage().equals("No value for name") || jse.getMessage().equals("No value for photos") || (jse.getMessage().equals("No value for id")) || (jse.getMessage().equals("No value for friends")))
                                {
                                    registerLogin();
                                }
                                else
                                {
                                    LoginManager.getInstance().logOut();
                                }
                            }

                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,picture,name,photos,friends");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void registerLogin()
    {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                getLoginProfile();
            }

            @Override
            public void onCancel() {
                // App code
                Log.v("VERBOSE", "log in cancelled");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.v("VERBOSE","log in failed");
            }


        });

    }

    private void listFriends()
    {
        if(friend_list==null)friend_list = (ListView) findViewById(R.id.friend_list);
        if(friend_subtitle==null)friend_subtitle=(TextView)findViewById(R.id.friend_subtitle);
        friend_subtitle.setVisibility(View.VISIBLE);
        friend_list.setVisibility(View.VISIBLE);
        Friend[] friend_array=new Friend[friend_json_array.length()];
        Friend currentFriend=null;
        JSONObject user=null;
        try
        {
            for(int i=0; i<friend_array.length; i++)
            {
                user=(JSONObject)friend_json_array.get(i);
                currentFriend=new Friend(user.getString("name"),user.getString("id"));
               // Log.v("VERBOSE/FRIEND",currentFriend.toString());
                friend_array[i]=currentFriend;
            }
        }
        catch(JSONException jse)
        {
            Log.e("ERROR",jse.getMessage());
        }
        FriendsAdapter adapter=new FriendsAdapter(getApplicationContext(),R.layout.friends,friend_array,this);
        friend_list.setAdapter(adapter);
        friend_list.setOnItemClickListener(adapter);
    }

    private JSONObject findFriend(String id) throws JSONException
    {
        JSONObject object=null;
        for(int i=0; i<=friend_json_array.length();i++)
        {
            object=friend_json_array.getJSONObject(i);
            if(object.getString("id").equals(id))
            {
                return object;
            }
        }
        return null;
    }

    public void getFriendPics(Friend friend)
    {
        setContentView(R.layout.processing);
        String id=friend.getProfileId();
        final Friend friend_holder=friend;
        new GraphRequest(
                accessToken,
                "/"+id+"/photos?fields=images&limit=15&type=uploaded",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response)
                    {
                            /* handle the result */
                        if(response==null)
                        {
                            Log.e("ERROR","NO respones");
                            LoginManager.getInstance().logOut();
                            return;
                        }
                        JSONObject json_object=response.getJSONObject();
                        JSONArray photo_json_array_data=null;
                        JSONArray photo_json_array_images=null;
                        JSONObject photo_object=null;
                        Photo[] photos=null;
                        JSONObject data_object=null;
                        try
                        {
                            photo_json_array_data=json_object.getJSONArray("data");
                            photos=new Photo[photo_json_array_data.length()];
                            Log.v("VERBOSE","creating "+photos.length+" photos");
                            for(int i=0; i<photos.length;i++)
                            {
                                data_object=photo_json_array_data.getJSONObject(i);
                                photo_json_array_images=data_object.getJSONArray("images");
                                for(int j=0; j<photo_json_array_images.length();j++)
                                {
                                    photo_object=photo_json_array_images.getJSONObject(j);
                                    if((photo_object.getInt("height")<=600 && photo_object.getInt("width")<=600))
                                    {
                                        photos[i]=new Photo(photo_object.getString("source"),photo_object.getInt("height"),photo_object.getInt("width"));
                                        fetchImage(photos[i]);
                                     //  Log.v("VERBOSE","NEW PHOTO OBJECT AT POSITION "+i+": "+photos[i]);
                                        break;
                                    }
                                    else if(j==photo_json_array_images.length()-1)
                                    {
                                        photos[i]=new Photo(photo_object.getString("source"),photo_object.getInt("height"),photo_object.getInt("width"));
                                        fetchImage(photos[i]);
                                     //   Log.v("VERBOSE","NEW PHOTO OBJECT AT POSITION "+i+": "+photos[i]);
                                    }
                                }
                            }

                            FriendsPhotoAdapter photoAdapter=new FriendsPhotoAdapter(getApplicationContext(),R.layout.pics,photos,thisActivity);
                            setContentView(R.layout.friend_pics_layout);
                            friend_pics_list=(ListView)findViewById(R.id.friend_pics_list);
                            friend_pics_list.setAdapter(photoAdapter);
                            friend_pics_list.setOnItemClickListener(photoAdapter);

                        }
                        catch(JSONException jse)
                        {
                            Log.e("ERROR",jse.getMessage());
                            Toast.makeText(getApplicationContext(),friend_holder.getName()+" has no photos to display",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ).executeAsync();
          JSONObject friend_json_object=null;

    }


    private class FBImageTask extends AsyncTask<Photo,Integer,Drawable>
    {

        FBImageTask(){}

        @Override
        protected void onPreExecute()

        {
           // Log.v("VERBOSE","Changing content view....");
        }

        @Override
        protected synchronized void onPostExecute(Drawable drawable)
        {
               Log.v("VERBOSE/FINISHED","FINISHED");
            //   viewHolder.progess_view.setVisibility(View.GONE);
        }

        @Override
        protected Drawable doInBackground(Photo... params)
        {

            publishProgress(-1);
            Photo photo = params[0];
            try
            {
                publishProgress(photo.getPosition());
                URL url = new URL(photo.getLink());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                   /* fb_image = BitmapFactory.decodeStream(connection.getInputStream());

                fb_image = Bitmap.createScaledBitmap(fb_image, photo.getWidth(), photo.getHeight(), true);
*/
                //    publishProgress("almost ready....");
                // return fb_image;

                //   Log.v("VERBOSE/PHOTO","finishing photo "+photo.getPosition()+"with link "+photo.getLink());
                return Drawable.createFromStream(connection.getInputStream(),photo.getLink());
            }
            catch (IOException e)
            {
                Log.e("ERROR", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
          /* super.onProgressUpdate(values);
            Log.v("VERBOSE/PROGESS","publish progress for picture "+values[0]);
                if(values[0]==-1)
                {
                    setContentView(R.layout.processing);
                    text=(TextView)findViewById(R.id.process);
                }
                else
                {
                    text.setText("Processing picture "+values[0]);
                }
*/
        }

    }

    private void fetchImage(Photo photo)
    {
        FBImageTask task=new FBImageTask();
        task.execute(photo);
        Drawable m=null;
        try
        {
            m=task.get();
        }
        catch (InterruptedException e)
        {
            Log.e("ERROR",e.getMessage());
        }
        catch (ExecutionException e)
        {
            Log.e("ERROR",e.getMessage());
        }
        photo.setDrawable(m);
    }

    @Override
    public void onBackPressed()
    {
        if(findViewById(R.id.user_name)==null)
        {
            setContentView(R.layout.activity_main);
            user_image=(ProfilePictureView)findViewById(R.id.user_image);
            user_name=(TextView)findViewById(R.id.user_name);
            friend_list=(ListView)findViewById(R.id.friend_list);
            getLoginProfile();
            listFriends();
        }
        else
        {
            super.onBackPressed();
        }
    }
}