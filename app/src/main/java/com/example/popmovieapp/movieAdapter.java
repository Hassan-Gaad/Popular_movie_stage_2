package com.example.popmovieapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class movieAdapter extends RecyclerView.Adapter<movieAdapter.movieViewHolder> {

    private final itemClickListener mitemClickListener;
    private Movie[] movieData;

    public movieAdapter(Movie[]movies,itemClickListener clickListener){
        mitemClickListener=clickListener;
        movieData=movies;
    }

    @NonNull
    @Override
    public movieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context=viewGroup.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);

        View view=inflater.inflate(R.layout.movie_item,viewGroup, false);
        movieViewHolder movieViewHolder=new movieViewHolder(view);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull movieViewHolder movieViewHolder, int i) {
    String posterPath=movieData[i].getPoster();
        Picasso.get()
                .load(posterPath)
                .error(R.drawable.not_found)
                .placeholder(R.drawable.loading)
                .into(movieViewHolder.ivMovieItem);
    }

    @Override
    public int getItemCount() {
        if (movieData==null){return 0;}
        return movieData.length;
    }


    //  the view holder class
    class movieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
     ImageView ivMovieItem;


      public movieViewHolder (View itemView){
          super(itemView);
          ivMovieItem=itemView.findViewById(R.id.iv_movie_item);
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
