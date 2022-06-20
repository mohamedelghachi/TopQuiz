package com.example.topquiz.Controller;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topquiz.GameActivity;
import com.example.topquiz.Model.User;
import com.example.topquiz.R;

public class MainActivity extends AppCompatActivity {

    private TextView mGreetingTextView;
    private TextView mLastNameTextView;
    private TextView mScoreSharedPreferencesTextView;
    private TextView mScoreTextView;
    private EditText mNameEditText;
    private Button mPlayButton;

    private User mUser = new User();

    private static final int GAME_ACTIVITY_REQUEST_CODE = 42;
    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    public static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO";
    public static final String SHARED_PREF_USER_INFO_NAME = "SHARED_PREF_USER_INFO_NAME";
    public static final String SHARED_PREF_USER_INFO_SCORE = "SHARED_PREF_USER_INFO_SCORE";

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d("MainActivity", "onActivityResult :: ");
                    if (result.getResultCode() == 78) {
                        // There are no request codes
                        Intent intent = result.getData();
                        if (intent != null) {
                            String data = intent.getStringExtra("result");
                            mScoreTextView.setText("Votre score est : "+data);
                        }
                    }
                    //**** START reading SharedPreferences ****//
                    String firstName = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getString(SHARED_PREF_USER_INFO_NAME, null);
                    if(firstName != null){
                        mLastNameTextView.setText("Dérnier nom : "+firstName);
                        mNameEditText.setText(firstName);
                        mNameEditText.setSelection(firstName.length());
                        String score = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getString(SHARED_PREF_USER_INFO_SCORE, null);
                        mScoreSharedPreferencesTextView.setText("Dérnier score : "+score);
                    }
                    //**** END reading SharedPreferences ****//
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGreetingTextView = findViewById(R.id.main_textview_greeting);
        mLastNameTextView = findViewById(R.id.main_textview_lastname);
        mScoreSharedPreferencesTextView = findViewById(R.id.main_textview_scoreSharePreferences);

        mScoreTextView = findViewById(R.id.main_textview_score);
        mNameEditText = findViewById(R.id.main_edittext_name);
        mPlayButton = findViewById(R.id.main_button_play);

        //mPlayButton.setEnabled(false);
        //**** START reading SharedPreferences ****//
        String firstName = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getString(SHARED_PREF_USER_INFO_NAME, null);
        if(firstName != null){
            mLastNameTextView.setText("Dérnier nom : "+firstName);
            mNameEditText.setText(firstName);
            mNameEditText.setSelection(firstName.length());
            String score = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getString(SHARED_PREF_USER_INFO_SCORE, null);
            mScoreSharedPreferencesTextView.setText("Dérnier score : "+score);
        }else{
            mPlayButton.setEnabled(false);
        }
        //**** END reading SharedPreferences ****//
        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPlayButton.setEnabled(!s.toString().isEmpty());
//                if(s.length()>=1){
//                    mPlayButton.setEnabled(true);
//                }else{
//                    mPlayButton.setEnabled(false);
//                }
            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mémoriser le nom de l'utilisateur
                mUser.setFirstName(mNameEditText.getText().toString());
                //**** START using SharedPreferences ****//
                SharedPreferences preferences = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(SHARED_PREF_USER_INFO_NAME, mUser.getFirstName());
                editor.apply();
                mLastNameTextView.setText("Dérnier nom : "+mUser.getFirstName());
                //**** END using SharedPreferences ****//
                Intent gameActivityIntent = new Intent(MainActivity.this, GameActivity.class);
                //startActivity(gameActivityIntent);
                //startActivityForResult(gameActivityIntent,GAME_ACTIVITY_REQUEST_CODE);
                someActivityResultLauncher.launch(gameActivityIntent);
            }
        });

    }
}