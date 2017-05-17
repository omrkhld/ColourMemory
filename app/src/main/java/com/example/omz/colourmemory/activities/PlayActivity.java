package com.example.omz.colourmemory.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.omz.colourmemory.fragments.HighscoresDialog;
import com.example.omz.colourmemory.models.Card;
import com.example.omz.colourmemory.models.Highscore;
import com.example.omz.colourmemory.R;
import com.example.omz.colourmemory.adapters.CardAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class PlayActivity extends AppCompatActivity {

    public static final String TAG = "PlayActivity";
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.textPoints) TextView pointsText;
    @BindView(R.id.buttonHighscores) Button highscoresButton;

    List<Card> cards;
    GridLayoutManager glm;
    int points, prevPos;
    Card prevCard;
    Realm realm;
    AlertDialog scoreDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        points = 0;
        pointsText.setText(" " + String.valueOf(points));
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
                FragmentManager fm = getSupportFragmentManager();
                HighscoresDialog dialog = HighscoresDialog.newInstance("High Scores");
                dialog.show(fm, "dialog_highscores");
            }
        });

        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        realm.close();
        try {
            if (scoreDialog != null && scoreDialog.isShowing()) {
                scoreDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                pointsText.setText(" " + String.valueOf(points));
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
                pointsText.setText(" " + String.valueOf(points));
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
            showAddDialog();
        }
    }

    private void showAddDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_score_title_text);
        builder.setMessage("Your score is " + points + ".");

        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText userField = new EditText(this);
        userField.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        userField.setHint(R.string.enter_name_text);
        userField.setLayoutParams(linearLayoutParams);
        linearLayoutParams.setMargins(70, 0, 100, 0);
        linearLayout.addView(userField);
        userField.setFocusable(true);
        userField.setSingleLine();
        builder.setView(linearLayout);
        builder.setCancelable(false);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setNegativeButton(null, null);

        scoreDialog = builder.create();
        scoreDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button positiveButton = scoreDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String input = userField.getText().toString().trim();
                        if (input.length() == 0) {
                            userField.setError(getString(R.string.error_enter_name_text));
                        } else {
                            realm.beginTransaction();
                            Highscore highscore = realm.createObject(Highscore.class);
                            highscore.setScore(points);
                            highscore.setName(input);
                            realm.commitTransaction();

                            Intent intent = new Intent(getApplicationContext(), HighscoresActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
        scoreDialog.show();
    }
}
