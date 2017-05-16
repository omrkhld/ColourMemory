package com.example.omz.colourmemory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Omz on 16/5/2017.
 */

public class HighscoresRealmAdapter extends RealmRecyclerViewAdapter<Highscore, HighscoresRealmAdapter.ViewHolder> {

    public static final String TAG = "HighscoresAdapter";
    private final Context mContext;

    public HighscoresRealmAdapter(Context c, OrderedRealmCollection<Highscore> data) {
        super(c, data, true);
        setHasStableIds(true);
        this.mContext = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_highscore, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Highscore obj = getData().get(position);

        holder.name.setText(obj.getName());
        holder.rank.setText(String.valueOf(position+1));
        holder.score.setText(String.valueOf(obj.getScore()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        @BindView(R.id.rank) TextView rank;
        @BindView(R.id.name) TextView name;
        @BindView(R.id.score) TextView score;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }
}
