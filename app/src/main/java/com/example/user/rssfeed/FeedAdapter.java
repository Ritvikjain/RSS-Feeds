package com.example.user.rssfeed;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by User on 17-02-2018.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>{
    private List<Feed> feedList;
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title,pub;
        public  ViewHolder(View view){
            super(view);
            title=view.findViewById(R.id.title);
            pub=view.findViewById(R.id.pub);
        }
    }
    public FeedAdapter(List<Feed> newsList){
        this.feedList=newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Feed feeds = feedList.get(position);
        holder.title.setText(feeds.getTitle());
        final ViewHolder desc = holder;
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), WebViews.class);
                intent.putExtra("URL",feeds.getLink());
                v.getContext().startActivity(intent);
            }
        });
        holder.pub.setText(feeds.getPub());
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }
}
