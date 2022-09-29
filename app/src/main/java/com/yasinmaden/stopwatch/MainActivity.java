package com.yasinmaden.stopwatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText    editTextScoreList;
    TextView    counterText;
    Button      btnStart, btnRestart, btnLap, btnPause;
    Switch      switchLapText, switchDarkMode;

    Runnable    r;
    Handler     h;

    int         counter, scoreListCounter;
    String      newScore, scoreList, score;
    Boolean     differentScoreState, counterState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextScoreList = findViewById(R.id.editTextScoreList);
        counterText = findViewById(R.id.textViewCounter);
        btnStart = findViewById(R.id.btnStart);
        btnRestart = findViewById(R.id.btnRestart);
        btnLap = findViewById(R.id.btnLap);
        btnPause = findViewById(R.id.btnPause);

        switchLapText = findViewById(R.id.switchLapText);
        switchDarkMode = findViewById(R.id.switchDarkMode);

        editTextScoreList.setFocusable(true);
        editTextScoreList.setFocusableInTouchMode(true);
        editTextScoreList.setEnabled(true);
        editTextScoreList.setCursorVisible(true);
        editTextScoreList.setKeyListener(null);
        editTextScoreList.setBackgroundColor(Color.TRANSPARENT);

        btnPause.setVisibility(View.GONE);

        counter = 0;
        scoreListCounter = 0;
        newScore = "";
        scoreList = "";
        score = "";

        counterState = false;
        differentScoreState = false;
    }

    public void darkMode(View view) {
        switchLapText.setChecked(false);
        editTextScoreList.setText("");
        if (switchDarkMode.isChecked()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
    public void disableSameScore(View view) {
        differentScoreState = !differentScoreState;
    }
    public String getTabSpace(int x) {
        String  s = "";

        while (x-- > 0)
            s += "\t";
        return (s);
    }

    public void start(View view) {
        counterState = true;
        switchDarkMode.setVisibility(View.GONE);
        btnStart.setVisibility(View.GONE);
        btnPause.setVisibility(View.VISIBLE);

        h = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                int hours = counter / 3600;
                int minutes = (counter % 3600) / 60;
                int secs = counter % 60;
                counterText.setText(String.format(Locale.getDefault(),"%d:%02d:%02d", hours, minutes, secs));
                h.postDelayed(r, 1000);
                counter++;
            }
        };

        h.post(r);
    }
    public void pause(View view) {
        btnPause.setVisibility(View.GONE);
        btnStart.setVisibility(View.VISIBLE);
        h.removeCallbacks(r);
    }

    public void restart(View view) {
        if (counterState)
            h.removeCallbacks(r);
        counterState = false;
        counter = 0;
        counterText.setText("0:00:00");
        btnPause.setVisibility(View.GONE);
        btnStart.setVisibility(View.VISIBLE);
        switchDarkMode.setVisibility(View.VISIBLE);
        scoreList = "";
        scoreListCounter = 0;
        editTextScoreList.setText("");
    }

    public void lap(View view) {
        setScoreList(differentScoreState, 30);
    }

    public void setScoreList(Boolean isDifferent, int tabSize) {
        if (isDifferent) {
            if (counterText.getText().toString().equals("0:00:00")) {
                if (scoreListCounter == 0) {
                    scoreListCounter++;
                    score = counterText.getText().toString();
                    newScore = counterText.getText().toString();
                    newScore = "#" + scoreListCounter + getTabSpace(tabSize) + newScore + "\n";
                    scoreList = newScore.concat(scoreList);
                    editTextScoreList.setText(scoreList);
                }
                else
                    return;
            }
            else {
                if (counterText.getText().toString().equals(score))
                    return;
                score = counterText.getText().toString();
                scoreListCounter++;
                newScore = counterText.getText().toString();
                newScore = "#" + scoreListCounter + getTabSpace(tabSize) + newScore + "\n";
                scoreList = newScore.concat(scoreList);
                editTextScoreList.setText(scoreList);
            }

        } else {
            scoreListCounter++;
            score = counterText.getText().toString();
            newScore = counterText.getText().toString();
            newScore = "#" + scoreListCounter + getTabSpace(tabSize) + newScore + "\n";
            scoreList = newScore.concat(scoreList);
            editTextScoreList.setText(scoreList);
        }

    }
}