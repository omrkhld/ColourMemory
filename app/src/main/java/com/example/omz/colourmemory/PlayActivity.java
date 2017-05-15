package com.example.omz.colourmemory;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayActivity extends AppCompatActivity {

    public static final String TAG = "PlayActivity";
    @BindView(R.id.gridPlay) GridView playGrid;
    @BindView(R.id.textPoints) TextView pointsText;
    @BindView(R.id.buttonHighscores) Button highscoresButton;
    int points, card1, card2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        points = 0;
        card1 = 0;
        card2 = 0;

        highscoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Load Realm here
            }
        });

        //randomize list of cards here
        ArrayList<Integer> mThumbIds = new ArrayList<>(Arrays.asList(
                R.drawable.colour1, R.drawable.colour2,
                R.drawable.colour3, R.drawable.colour4,
                R.drawable.colour5, R.drawable.colour6,
                R.drawable.colour7, R.drawable.colour8,
                R.drawable.colour1, R.drawable.colour2,
                R.drawable.colour3, R.drawable.colour4,
                R.drawable.colour5, R.drawable.colour6,
                R.drawable.colour7, R.drawable.colour8));
        long seed = System.nanoTime();
        Collections.shuffle(mThumbIds, new Random(seed));

        playGrid.setAdapter(new CardAdapter(this, mThumbIds));

        playGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                final CardAdapter adapter = (CardAdapter) playGrid.getAdapter();
                if (card1 == 0) {
                    card1 = (Integer) adapter.getItem(position);
                    adapter.selectCard(position);
                } else {
                    card2 = (Integer) adapter.getItem(position);
                    adapter.selectCard(position);
                    if (card1 == card2) {
                        points += 2;
                        new CountDownTimer(1000, 1000) {
                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                adapter.removeCardsCorrect();
                                pointsText.setText(String.valueOf(points));
                            }
                        }.start();
                    } else {
                        points -= 1;
                        new CountDownTimer(1000, 1000) {
                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                adapter.removeCardsWrong();
                                pointsText.setText(String.valueOf(points));
                            }
                        }.start();
                    }
                    card1 = 0;
                    card2 = 0;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void gameOver(CardAdapter.GameOverEvent event) {
        Log.i(TAG, "Game over");
        //Check and Update leaderboard
    }
}
