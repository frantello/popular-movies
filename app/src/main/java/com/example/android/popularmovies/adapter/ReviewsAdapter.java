package com.example.android.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Review;

import java.util.List;

/**
 * Reviews adapter.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<Review> reviews;

    private OnClickReviewListener listener;

    public ReviewsAdapter(List<Review> reviews, OnClickReviewListener listener) {
        this.reviews = reviews;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ViewHolder(inflater.inflate(R.layout.item_review, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Review review = reviews.get(position);

        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Review review;

        TextView author;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            author = (TextView) itemView.findViewById(R.id.review_author);
        }

        public void bind(Review review) {
            this.review = review;

            author.setText(review.getAuthor());
        }

        @Override
        public void onClick(View v) {

            listener.readReview(review);
        }
    }

    public interface OnClickReviewListener {

        void readReview(Review review);
    }
}
