package com.sp.navdrawertest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.auth.User;
import com.sp.navdrawertest.postInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyHolder> {
    private ArrayList<postInfo> postInfoList;

    public RvAdapter(ArrayList<postInfo> postInfoList) {
        this.postInfoList = postInfoList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.bind(postInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        return postInfoList.size();
    }

    public void setPostInfoList(ArrayList<postInfo> newPostInfoList) {
        postInfoList=newPostInfoList;
        notifyDataSetChanged();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        TextView rvTitle, rvType;
        ImageView rvCardImage;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            rvTitle = itemView.findViewById(R.id.rvTitle);
            rvType = itemView.findViewById(R.id.rvType);
            rvCardImage = itemView.findViewById(R.id.rvCardImage);
        }

        public void bind(postInfo post) {
            // Check if the user is "ecogoadmin" before setting data
            if ("ecogoadmin".equals(post.getCurrentUserID())) {
                rvTitle.setText(post.getPostSiteName());
                rvType.setText(post.getPostCaption());
                Picasso.get().load(post.getPostImage()).into(rvCardImage);
                itemView.setVisibility(View.VISIBLE);
            } else {
                // If the user is not "ecogoadmin", you might want to hide the view or handle it differently
                itemView.setVisibility(View.GONE);
            }
        }
    }
}
