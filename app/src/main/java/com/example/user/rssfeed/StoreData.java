package com.example.user.rssfeed;

import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by User on 16-02-2018.
 */

public class StoreData implements Serializable
{
    ArrayList<String> titles;
    ArrayList<String> links;
    ArrayList<String> description;
    ArrayList<String> pub;
    StoreData(ArrayList<String> title,ArrayList<String> link,ArrayList<String> desp,ArrayList<String> pub)
    {
        titles=title;
        links=link;
        description=desp;
        this.pub=pub;
    }
    public ArrayList getData()
    {
        return titles;
    }
    public ArrayList getDesp()
    {
        return description;
    }
    public ArrayList getLinks()
    {
        return links;
    }
    public ArrayList getPub()
    {
        return pub;
    }
}
