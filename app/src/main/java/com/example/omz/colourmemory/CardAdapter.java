package com.example.omz.colourmemory;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by Omz on 15/5/2017.
 */

public class CardAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Integer> mThumbIds;
    private ArrayList<Integer> selected;
    private ArrayList<Integer> temp;

    public CardAdapter(Context c, ArrayList<Integer> i) {
        mContext = c;
        mThumbIds = i;
        selected = new ArrayList<>();
        temp = new ArrayList<>();
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public Object getItem(int position) {
        return mThumbIds.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public int getSelected() {
        return selected.size();
    }

    @Override
    public boolean isEnabled(int position) {
        if (temp.contains(position) || selected.contains(position)) {
            return false;
        } else {
            return true;
        }
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }

        if (!temp.isEmpty()) {
            if (temp.contains(position)) {
                imageView.setImageResource(mThumbIds.get(position));
            } else {
                imageView.setImageResource(R.drawable.card_bg);
            }
        } else {
            if (!selected.isEmpty()) {
                if (selected.contains(position)) {
                    imageView.setVisibility(View.GONE);
                } else {
                    imageView.setImageResource(R.drawable.card_bg);
                }
            } else {
                imageView.setImageResource(R.drawable.card_bg);
            }
        }

        return imageView;
    }

    public void selectCard(int position) {
        temp.add(position);
        notifyDataSetChanged();
    }

    public void removeCardsCorrect() {
        selected.addAll(temp);
        temp.clear();
        if (selected.size() == 16) {
            EventBus.getDefault().post(new GameOverEvent());
        }
        notifyDataSetChanged();
    }

    public void removeCardsWrong() {
        temp.clear();
        notifyDataSetChanged();
    }

    public static class GameOverEvent {
    }
}
