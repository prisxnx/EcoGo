package com.sp.navdrawertest;
import android.media.Image;
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

public class RvAdapterTwo extends RecyclerView.Adapter<RvAdapterTwo.MyHolder> {
    private ArrayList<postInfo> postInfoList;
    public RvAdapterTwo(ArrayList<postInfo> postInfoList) {
        this.postInfoList = postInfoList;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_row, parent, false);
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
        postInfoList.clear();
        postInfoList.addAll(newPostInfoList);
        notifyDataSetChanged();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView Username,StateCountry,DOV,Caption,SiteName;
        ImageView SiteImage;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            Username = itemView.findViewById(R.id.add_usersname);
            StateCountry = itemView.findViewById(R.id.add_SC);
            DOV = itemView.findViewById(R.id.add_dov);
            Caption = itemView.findViewById(R.id.add_captions);
            SiteName = itemView.findViewById(R.id.add_name);
            SiteImage = itemView.findViewById(R.id.siteimage);

        }

        public void bind(postInfo post) {
                Username.setText(post.getCurrentUserID());
                StateCountry.setText(post.getPostState());
                DOV.setText(post.getPostDOV());
                Caption.setText(post.getPostCaption());
                SiteName.setText(post.getPostSiteName());
                Picasso.get().load(post.getPostImage()).into(SiteImage);
                itemView.setVisibility(View.VISIBLE);
            }
    }
}