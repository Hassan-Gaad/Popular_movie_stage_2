package com.example.popmovieapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popmovieapp.Objects.Reviews;

public class reviewsAdapter extends RecyclerView.Adapter<reviewsAdapter.ReviewViewHolder> {

    private final itemClickListenerReview mitemClickListener;

   private Reviews[] reviewsData;
    public reviewsAdapter(Reviews[] reviews_Data,itemClickListenerReview clickListener){
        this.mitemClickListener=clickListener;
        this.reviewsData=reviews_Data;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
        View view=inflater.inflate(R.layout.reviews_item,viewGroup,false);
        return new ReviewViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int i) {
        reviewViewHolder.tvAuthor.setText(reviewsData[i].getAuthor());
        reviewViewHolder.tvContent.setText(reviewsData[i].getContent());
        reviewViewHolder.imageViewUser.setImageResource(R.drawable.user);

    }

    @Override
    public int getItemCount() {
        if (reviewsData==null){return 0;}
        else return reviewsData.length;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView tvAuthor,tvContent;
        CardView cardView;
        ImageView imageViewUser;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor=itemView.findViewById(R.id.textViewAuthorReviewer);
            tvContent=itemView.findViewById(R.id.textViewContentReviewer);
            cardView=itemView.findViewById(R.id.cardView);
            imageViewUser=itemView.findViewById(R.id.imageViewAvatar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mitemClickListener.onReviewItemClicked(getAdapterPosition());
            tvContent.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        }
    }

    public interface itemClickListenerReview{
        void onReviewItemClicked(int clickedItem);

    }
}
