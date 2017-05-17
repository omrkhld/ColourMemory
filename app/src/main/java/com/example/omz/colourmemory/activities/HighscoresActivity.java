package com.example.omz.colourmemory.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.omz.colourmemory.models.Highscore;
import com.example.omz.colourmemory.R;
import com.example.omz.colourmemory.adapters.HighscoresRealmAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class HighscoresActivity extends AppCompatActivity {

    public static final String TAG = "HighscoresActivity";
    @BindView(R.id.highscores_recyclerview)
    RecyclerView recyclerView;

    private Realm realm;
    public RealmResults<Highscore> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);
        ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();
        results = realm.where(Highscore.class).findAllSorted("score", Sort.DESCENDING);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new HighscoresRealmAdapter(this, results));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
