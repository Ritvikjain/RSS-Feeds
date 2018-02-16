package com.example.user.rssfeed;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> titles;
    ArrayList<String> links;
    ArrayList<String> desp;
    ArrayList<String> pub;
    SwipeRefreshLayout srl;
    String category;
    String fname;
    private RecyclerView recyclerView;
    private List<Feed> feedList = new ArrayList<>();
    private FeedAdapter feedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        titles=new ArrayList<String>();
        links=new ArrayList<String>();
        desp=new ArrayList<String>();
        pub=new ArrayList<String>();

        category=getIntent().getExtras().getString("category");
        //Toast.makeText(this, category+".txt", Toast.LENGTH_SHORT).show();

        srl=findViewById(R.id.swiperefresh);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isConnected()) {
                    feedList.clear();
                    titles.clear();
                    links.clear();
                    desp.clear();
                    pub.clear();

                    new ProcessInBackground().execute();
                    feedAdapter = new FeedAdapter(feedList);
                    recyclerView.setAdapter(feedAdapter);

                    Toast.makeText(MainActivity.this, "Feeds Updated", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Check Your Internet Connection!!!...", Toast.LENGTH_SHORT).show();
                    srl.setRefreshing(false);
                }
            }
        });


        recyclerView = findViewById(R.id.rv);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());




        Toast.makeText(this, "Swipe down to refresh feeds", Toast.LENGTH_SHORT).show();

        if (isConnected())
        {
            new ProcessInBackground().execute();
        }else {
            try {
                FileInputStream fis=openFileInput(category+".txt");
                ObjectInputStream ois=new ObjectInputStream(fis);
                StoreData sd=(StoreData) ois.readObject();
                titles=sd.getData();
                links=sd.getLinks();
                desp=sd.getDesp();
                pub=sd.getPub();
                for(int i=0;i<titles.size();i++){
                    Feed feeds = new Feed(titles.get(i), desp.get(i), links.get(i), pub.get(i));
                    feedList.add(feeds);
                }
                feedAdapter = new FeedAdapter(feedList);
                recyclerView.setAdapter(feedAdapter);


                //Toast.makeText(this, "Object Retrieved", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        feedAdapter = new FeedAdapter(feedList);
        recyclerView.setAdapter(feedAdapter);

    }

    public boolean isConnected()
    {
        ConnectivityManager connec=(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState()== NetworkInfo.State.CONNECTED||
                connec.getNetworkInfo(0).getState()== NetworkInfo.State.CONNECTING||
                connec.getNetworkInfo(1).getState()== NetworkInfo.State.CONNECTED||
                connec.getNetworkInfo(1).getState()== NetworkInfo.State.CONNECTING)
        {
            //Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (connec.getNetworkInfo(0).getState()== NetworkInfo.State.DISCONNECTED||
                connec.getNetworkInfo(1).getState()== NetworkInfo.State.DISCONNECTED)
        {
            //Toast.makeText(this, "not connected", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }

    public InputStream getInputStream(URL url)
    {
        try{
            return url.openConnection().getInputStream();
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public class ProcessInBackground extends AsyncTask<Integer,Void,Exception>
    {
        ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
        Exception exception=null;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            progressDialog.setMessage("Rss feed loading... Please wait...");
            progressDialog.show();
        }

        @Override
        protected Exception doInBackground(Integer... integers) {

            try
            {
                URL url;
                if (category.equalsIgnoreCase("topstories")) {
                    url = new URL("http://feeds.feedburner.com/ndtvnews-top-stories");
                    fname="topstories.txt";
                }
                else if (category.equalsIgnoreCase("india"))
                {
                    url = new URL("http://feeds.feedburner.com/ndtvnews-india-news");
                    fname="india.txt";
                }
                else if (category.equalsIgnoreCase("sports"))
                {
                    url = new URL("http://feeds.feedburner.com/ndtvsports-latest");
                    fname="sports.txt";
                }
                else {
                    url = new URL("http://feeds.feedburner.com/ndtvprofit-latest");
                    fname="business.txt";
                }

                XmlPullParserFactory factory=XmlPullParserFactory.newInstance();

                factory.setNamespaceAware(false);
                XmlPullParser xpp=factory.newPullParser();
                xpp.setInput(getInputStream(url),"UTF_8");
                boolean insideItem=false;
                int eventType=xpp.getEventType();
                while (eventType!=XmlPullParser.END_DOCUMENT)
                {
                    if (eventType==XmlPullParser.START_TAG)
                    {
                        if (xpp.getName().equalsIgnoreCase("item"))
                        {
                            insideItem=true;
                        }
                        else if (xpp.getName().equalsIgnoreCase("title"))
                        {
                            if (insideItem)
                            {
                                titles.add(xpp.nextText());
                            }
                        }
                        else if (xpp.getName().equalsIgnoreCase("link"))
                        {
                            if (insideItem)
                            {
                                links.add(xpp.nextText());
                            }
                        }
                        else if (xpp.getName().equalsIgnoreCase("description"))
                        {
                            if (insideItem)
                            {
                                desp.add(xpp.nextText());
                            }
                        }
                        else if (xpp.getName().equalsIgnoreCase("pubDate"))
                        {
                            if (insideItem)
                            {
                                pub.add(xpp.nextText());
                            }
                        }
                    }
                    else if (eventType==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item"))
                    {
                        insideItem=false;
                    }
                    eventType=xpp.next();
                }
                for(int i=0;i<titles.size();i++){
                    Feed feeds = new Feed(titles.get(i), desp.get(i), links.get(i), pub.get(i));
                    feedList.add(feeds);
                }

            }
            catch (MalformedURLException e)
            {
                exception = e;
            }
            catch (XmlPullParserException e)
            {
                exception=e;
            }
            catch (IOException e)
            {
                exception=e;
            }

            return exception;
        }

        @Override
        protected void onPostExecute(Exception s)
        {
            super.onPostExecute(s);

            try {
                new File(fname).delete();
                FileOutputStream fos=openFileOutput(fname,MODE_PRIVATE);

                ObjectOutputStream oos=new ObjectOutputStream(fos);
                StoreData sd1=new StoreData(titles,links,desp,pub);

                oos.writeObject(sd1);
                oos.close();
                fos.close();
                //Toast.makeText(MainActivity.this, "Object Stored", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            feedAdapter = new FeedAdapter(feedList);
            recyclerView.setAdapter(feedAdapter);

            progressDialog.cancel();
            srl.setRefreshing(false);
        }
    }

}
