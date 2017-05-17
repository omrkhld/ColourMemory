package com.example.omz.colourmemory.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.omz.colourmemory.models.Highscore;
import com.example.omz.colourmemory.R;
import com.example.omz.colourmemory.adapters.HighscoresRealmAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Omz on 16/5/2017.
 */

public class HighscoresDialog extends DialogFragment {

    public static final String TAG = "HighscoresDialog";
    @BindView(R.id.highscores_recyclerview) RecyclerView recyclerView;

    private Realm realm;
    public RealmResults<Highscore> results;

    public HighscoresDialog() {
    }

    public static HighscoresDialog newInstance(String title) {
        HighscoresDialog frag = new HighscoresDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_highscores, container);
        ButterKnife.bind(this, view);
        getDialog().setCanceledOnTouchOutside(true);

        realm = Realm.getDefaultInstance();
        results = realm.where(Highscore.class).findAllSorted("score", Sort.DESCENDING);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new HighscoresRealmAdapter(getContext(), results));
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("Highscores");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
