package com.example.omz.colourmemory;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayActivity extends AppCompatActivity {

    public static final String TAG = "PlayActivity";
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.textPoints) TextView pointsText;
    @BindView(R.id.buttonHighscores) Button highscoresButton;

    List<Card> cards;
    GridLayoutManager glm;
    int points, prevPos;
    Card prevCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        points = 0;
        prevCard = new Card();

        cards = new ArrayList<>(Arrays.asList(
                new Card(R.drawable.colour1), new Card(R.drawable.colour2),
                new Card(R.drawable.colour3), new Card(R.drawable.colour4),
                new Card(R.drawable.colour5), new Card(R.drawable.colour6),
                new Card(R.drawable.colour7), new Card(R.drawable.colour8),
                new Card(R.drawable.colour1), new Card(R.drawable.colour2),
                new Card(R.drawable.colour3), new Card(R.drawable.colour4),
                new Card(R.drawable.colour5), new Card(R.drawable.colour6),
                new Card(R.drawable.colour7), new Card(R.drawable.colour8)));
        Collections.shuffle(cards);

        glm = new GridLayoutManager(this, 4);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(glm);

        CardAdapter adapter = new CardAdapter(this, cards);
        recyclerView.setAdapter(adapter);

        highscoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Load Realm here
            }
        });
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void cardClicked(final CardAdapter.ClickEvent event) {
        final Card card = cards.get(event.position);
        card.setIsRevealed(true);
        recyclerView.getAdapter().notifyItemChanged(event.position);

        if (prevCard.getImgRes() == 0) {
            prevCard = card;
            prevPos = event.position;
        } else {
            if (prevCard.getImgRes() == card.getImgRes()) {
                cardsMatch(event, card);
            } else {
                cardsNotMatch(event, card);
            }
        }
    }

    public void cardsMatch(final CardAdapter.ClickEvent event, final Card card) {
        points += 2;
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                pointsText.setText(String.valueOf(points));
                prevCard.setIsMatched(true);
                card.setIsMatched(true);
                cards.set(prevPos, prevCard);
                cards.set(event.position, card);
                recyclerView.getAdapter().notifyItemChanged(prevPos);
                recyclerView.getAdapter().notifyItemChanged(event.position);
                prevCard = new Card();
                checkGameOver();
            }
        }.start();
    }

    public void cardsNotMatch(final CardAdapter.ClickEvent event, final Card card) {
        points -= 1;
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                pointsText.setText(String.valueOf(points));
                prevCard.setIsRevealed(false);
                card.setIsRevealed(false);
                cards.set(prevPos, prevCard);
                cards.set(event.position, card);
                recyclerView.getAdapter().notifyItemChanged(prevPos);
                recyclerView.getAdapter().notifyItemChanged(event.position);
                prevCard = new Card();
            }
        }.start();
    }

    public void checkGameOver() {
        boolean gameOver = true;

        for (Card c : cards) {
            if (!c.getIsMatched()) {
                gameOver = false;
            }
        }

        if (gameOver) {
            Log.e(TAG, "Game over");
        }
    }
}
