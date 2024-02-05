package com.sp.navdrawertest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.auth.User;
import com.sp.navdrawertest.postInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyHolder> {
    private ArrayList<adminInfo> adminInfoList;
    private OnItemClickListener onItemClickListener;

    public RvAdapter(ArrayList<adminInfo> adminInfoList) {
        this.adminInfoList = adminInfoList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.bind(adminInfoList.get(position));

        holder.rvCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (onItemClickListener != null) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(adminInfoList.get(adapterPosition));
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return adminInfoList.size();
    }

    public void setAdminInfoList(ArrayList<adminInfo> newAdminInfoList) {
        adminInfoList.clear();
        adminInfoList.addAll(newAdminInfoList);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onMapReady(GoogleMap googleMap);

        void onCardClick(int position);

        void onCardClick(adminInfo adminInfo);

        void onRouteDraw(List<List<LatLng>> route);

        void onItemClick(adminInfo adminInfo);
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        CardView rvCard;
        TextView rvTitle, rvType, rvSC;
        ImageView rvCardImage;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            rvTitle = itemView.findViewById(R.id.rvTitle);
            rvType = itemView.findViewById(R.id.rvType);
            rvSC = itemView.findViewById(R.id.rvSC);
            rvCardImage = itemView.findViewById(R.id.rvCardImage);
            rvCard = itemView.findViewById(R.id.rvCard);
        }

        public void bind(adminInfo post) {
            rvTitle.setText(post.getPostSiteName());
            rvType.setText(post.getPostCaption());
            rvSC.setText(post.getPostState());
            Picasso.get().load(post.getPostImage()).into(rvCardImage);
            itemView.setVisibility(View.VISIBLE);
        }
    }
}