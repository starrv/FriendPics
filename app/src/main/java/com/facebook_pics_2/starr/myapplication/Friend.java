package com.facebook_pics_2.starr.myapplication;

/**
 * Created by starr on 3/19/2017.
 */

public class Friend
{
    private String name;
    private String profileId;

    public Friend(String name, String profileId)
    {
        this.name=name;
        this.profileId=profileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    @Override
    public String toString()
    {
        return "name: "+name+"; id: "+profileId;
    }

}
