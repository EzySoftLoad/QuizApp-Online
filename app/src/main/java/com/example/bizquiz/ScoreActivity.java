package com.example.bizquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {
    private TextView scored,total;
    private Button doneBtn, shareBtn;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scored = findViewById(R.id.scored);
        total = findViewById(R.id.total);
        doneBtn = findViewById(R.id.done_btn);
        shareBtn = findViewById(R.id.share_btn);

        scored.setText(String.valueOf(getIntent().getIntExtra("score",0)));
        total.setText(String.valueOf(getIntent().getIntExtra("total",0)));

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"BizQuiz Challange");
                shareIntent.putExtra(Intent.EXTRA_TEXT,"Download the App ! ");
                startActivity(Intent.createChooser(shareIntent,"Share Via"));
            }
        });




    }
}
