package com.example.topquiz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topquiz.Model.Question;
import com.example.topquiz.Model.QuestionBank;

import java.util.Arrays;
import java.util.List;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private static boolean mEnableTouchEvents = true;
    private int mScore;
    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";

    private TextView mQuestionTextView;
    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private Button mAnswerButton4;

    private int mRemainingQuestionCount;

    private QuestionBank mQuestionBank = generateQuestionBank();

    public static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO";
    public static final String SHARED_PREF_USER_INFO_SCORE = "SHARED_PREF_USER_INFO_SCORE";

    private QuestionBank generateQuestionBank() {
        Question question1 = new Question(
                "Who is the creator of Android?",
                Arrays.asList(
                        "Andy Rubin",
                        "Steve Wozniak",
                        "Jake Wharton",
                        "Paul Smith"
                ),
                0
        );

        Question question2 = new Question(
                "When did the first man land on the moon?",
                Arrays.asList(
                        "1958",
                        "1962",
                        "1967",
                        "1969"
                ),
                3
        );

        Question question3 = new Question(
                "What is the house number of The Simpsons?",
                Arrays.asList(
                        "42",
                        "101",
                        "666",
                        "742"
                ),
                3
        );

        return new QuestionBank(Arrays.asList(question1, question2, question3));
    }

    private void displayQuestion(final Question question) {
        // Set the text for the question text view and the four buttons
        List<String> currentQuestion_choiceList = question.getChoiceList();
        mQuestionTextView.setText(question.getQuestion());
        mAnswerButton1.setText(currentQuestion_choiceList.get(0));
        mAnswerButton2.setText(currentQuestion_choiceList.get(1));
        mAnswerButton3.setText(currentQuestion_choiceList.get(2));
        mAnswerButton4.setText(currentQuestion_choiceList.get(3));
    }

    @Override
    public void onBackPressed() {
        //**** START using SharedPreferences ****//
        SharedPreferences preferences = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SHARED_PREF_USER_INFO_SCORE, String.valueOf(mScore));
        editor.apply();
        //**** END using SharedPreferences ****//
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        init();
        displayQuestion(mQuestionBank.getCurrentQuestion());
    }

    public void init() {
        mQuestionTextView = findViewById(R.id.game_activity_textview_question);
        mAnswerButton1 = findViewById(R.id.game_activity_button_1);
        mAnswerButton2 = findViewById(R.id.game_activity_button_2);
        mAnswerButton3 = findViewById(R.id.game_activity_button_3);
        mAnswerButton4 = findViewById(R.id.game_activity_button_4);

        // Use the same listener for the four buttons.
        // The view id value will be used to distinguish the button triggered
        mAnswerButton1.setOnClickListener(this);
        mAnswerButton2.setOnClickListener(this);
        mAnswerButton3.setOnClickListener(this);
        mAnswerButton4.setOnClickListener(this);

        mRemainingQuestionCount = 3;
        mScore = 0;
        mEnableTouchEvents = true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
        //return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        mEnableTouchEvents = false;
        int index;
        if (v == mAnswerButton1) {
            index = 0;
        } else if (v == mAnswerButton2) {
            index = 1;
        } else if (v == mAnswerButton3) {
            index = 2;
        } else if (v == mAnswerButton4) {
            index = 3;
        } else {
            throw new IllegalStateException("Unknown clicked view : " + v);
        }
        if (index == mQuestionBank.getCurrentQuestion().getAnswerIndex()) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            mScore++;
        } else {
            Toast.makeText(this, "Faut!", Toast.LENGTH_SHORT).show();
        }
        //affichage de la nouvelle question ou revenir au menu pricipale
        mRemainingQuestionCount--;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // If this is the last question, ends the game.
                // Else, display the next question.
                if (mRemainingQuestionCount > 0) {
                    displayQuestion(mQuestionBank.getNextQuestion());
                    mEnableTouchEvents = true;
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);

                    builder.setTitle("Well done!")
                            .setMessage("Your score is " + mScore)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //**** START using SharedPreferences ****//
                                    SharedPreferences preferences = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString(SHARED_PREF_USER_INFO_SCORE, String.valueOf(mScore));
                                    editor.apply();
                                    //**** END using SharedPreferences ****//
//                            Intent intent = new Intent();
//                            intent.putExtra("result", String.valueOf(mScore));
//                            setResult(78, intent);
                                    //finish();
                                    GameActivity.super.onBackPressed();
                                }
                            })
                            .setNegativeButton("Annuler",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            dialog.dismiss();
                                            //**** START using SharedPreferences ****//
                                            SharedPreferences preferences = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString(SHARED_PREF_USER_INFO_SCORE, String.valueOf(mScore));
                                            editor.apply();
                                            //**** END using SharedPreferences ****//
                                        }
                                    }
                            )
                            .create()
                            .show();
                }
            }
        }, 2_000); // LENGTH_SHORT is usually 2 second long

    }
}