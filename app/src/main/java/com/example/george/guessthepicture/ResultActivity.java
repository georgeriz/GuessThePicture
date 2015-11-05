package com.example.george.guessthepicture;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView resultText = (TextView) findViewById(R.id.result_text);

        Intent intent = getIntent();
        int nCorrect = intent.getIntExtra(GameActivity.NUMBER_CORRECT, 0);
        int nTotal = intent.getIntExtra(GameActivity.NUMBER_TOTAL, 0);

        resultText.setText(nCorrect + "/" + nTotal);
    }

    public void onPlayAgainClicked(View v){
        finish();
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}
