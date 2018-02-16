package com.example.user.rssfeed;

/**
 * Created by User on 17-02-2018.
 */

public class Feed {
    private String title, desp, link, pub;
    public Feed(String title, String description, String link, String pub)
    {
        this.title = title;
        this.desp = description;
        this.link = link;
        this.pub = pub;
    }
    public String getTitle()
    {
        return this.title;
    }
    public String getDescription()
    {
        return this.desp;
    }
    public String getLink(){
        return this.link;
    }
    public String getPub(){
        return this.pub;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setDescription(String description){
        this.desp = description;
    }
    public void setLink(String link){
        this.link = link;
    }
    public void setPub(String pub){
        this.pub = pub;
    }
}
