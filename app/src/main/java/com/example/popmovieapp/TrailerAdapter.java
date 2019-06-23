package com.example.popmovieapp;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popmovieapp.Objects.Trailer;
import com.squareup.picasso.Picasso;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private final itemClickListener mitemClickListener;

    private Trailer[] trailersData;

    public TrailerAdapter(Trailer[] trailer_data,itemClickListener clickListener){
        this.trailersData=trailer_data;

        this.mitemClickListener=clickListener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
        View view=inflater.inflate(R.layout.trailer_item,viewGroup,false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder trailerViewHolder, int i) {

        Picasso.get()
                .load(Uri.parse("https://img.youtube.com/vi/"+trailersData[i].getKey()+"/sddefault.jpg"))
                .error(R.drawable.not_found)
                .placeholder(R.drawable.loading)
                .into(trailerViewHolder.imgVTrailer);
    trailerViewHolder.textViewTrailerName.setText(trailersData[i].getName());
    trailerViewHolder.trailerSite.setText(trailersData[i].getSite());
    trailerViewHolder.trailerSize.setText(trailersData[i].getSize());
    }

    @Override
    public int getItemCount() {
        if (trailersData==null){return 0;}
        else return trailersData.length;
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgVTrailer;
        TextView textViewTrailerName,trailerSize,trailerSite;
        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            trailerSite=itemView.findViewById(R.id.tvTrailerSite);
            trailerSize=itemView.findViewById(R.id.tv_trailerSize);
            imgVTrailer=itemView.findViewById(R.id.iv_Trailer);
            textViewTrailerName=itemView.findViewById(R.id.tv_TrailerName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mitemClickListener.onItemClick(getAdapterPosition());
        }
    }
    public interface itemClickListener{
        void onItemClick(int clickedItem);
    }
}
