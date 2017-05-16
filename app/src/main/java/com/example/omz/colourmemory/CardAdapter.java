package com.example.omz.colourmemory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Omz on 15/5/2017.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    public static final String TAG = "CardAdapter";
    private Context mContext;
    private List<Card> mCards;
    private boolean init = true;
    private boolean touchEnabledFlag = true;

    public CardAdapter(Context c, List<Card> l) {
        mContext = c;
        mCards = l;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Card card = mCards.get(position);
        holder.cardFront.setImageResource(card.getImgRes());

        if (init) {
            holder.cardFront.setVisibility(View.GONE);
        }

        int revealedCount = 0;
        for (Card c : mCards) {
            if (c.getIsRevealed() && !c.getIsMatched()) {
                ++revealedCount;
            }
        }

        if (card.getIsMatched()) {
            holder.cardFront.setVisibility(View.VISIBLE);
            holder.cardLayout.setOnClickListener(null);
            if (!touchEnabledFlag)  touchEnabledFlag = true;
        } else if (!card.getIsMatched()) {
            if (!card.getIsRevealed()) {
                if (!init) {
                    holder.cardFront.setVisibility(View.VISIBLE);
                    Log.e(TAG, "Flipping back");
                    animateFlip(holder.cardBack, holder.cardFront, holder.cardLayout, new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (!touchEnabledFlag) {
                                touchEnabledFlag = true;
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    Log.e(TAG, "3: " + holder.cardFront.getVisibility());
                }
                holder.cardLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (touchEnabledFlag)
                            EventBus.getDefault().post(new ClickEvent(holder.getAdapterPosition()));
                    }
                });
                if (!touchEnabledFlag)  touchEnabledFlag = true;
            } else if (card.getIsRevealed()) {
                Log.e(TAG, "Flipping forward");
                holder.cardBack.setVisibility(View.VISIBLE);
                holder.cardFront.setVisibility(View.GONE);
                animateFlip(holder.cardBack, holder.cardFront, holder.cardLayout, null);
                Log.e(TAG, "2: " + holder.cardFront.getVisibility());
                holder.cardLayout.setOnClickListener(null);
                if (revealedCount > 1) {
                    touchEnabledFlag = false;
                }
                if (init)   init = false;
            }
        }
    }

    public static void animateFlip(ImageView back, ImageView front, FrameLayout cardView, Animation.AnimationListener animationListener) {

        FlipAnimation flipAnimation = new FlipAnimation(back, front);
        Log.e(TAG, "1: " + front.getVisibility());
        if (front.getVisibility() == View.VISIBLE) {
            Log.e(TAG, "Reversed");
            flipAnimation.reverse();
        }
        flipAnimation.setAnimationListener(animationListener);
        cardView.startAnimation(flipAnimation);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_layout) FrameLayout cardLayout;
        @BindView(R.id.card_front) ImageView cardFront;
        @BindView(R.id.card_back) ImageView cardBack;

        public ViewHolder(View itemView) {
            super (itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public int getItemCount() {
        return mCards.size();
    }

    public static class ClickEvent {
        public final int position;

        public ClickEvent(int position) {
            this.position = position;
        }
    }
}
