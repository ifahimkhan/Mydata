package com.fahim.onlinequiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fahim.onlinequiz.Common.Common;
import com.fahim.onlinequiz.Model.QuestionScore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Done extends AppCompatActivity {
    Button btnTryAgain;
    TextView textViewResultScore, getTextResultQuestion;
    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference question_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_score");

        textViewResultScore = (TextView) findViewById(R.id.textTotalScore);
        getTextResultQuestion = (TextView) findViewById(R.id.textTotalQuestion);
        progressBar = (ProgressBar) findViewById(R.id.doneProgressBar);

        btnTryAgain = (Button) findViewById(R.id.btnTryAgain);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Home.class));
                finish();
            }
        });

        Bundle extra =getIntent().getExtras();

        if(extra!=null){

            int score =extra.getInt("SCORE");
            int totalQuestion=extra.getInt("TOTAL");
            int correctAnswer=extra.getInt("CORRECT");

            textViewResultScore.setText(String.format("SCORE : %d",score));
            getTextResultQuestion.setText(String.format("PASSED : %d / %d", correctAnswer,totalQuestion));

            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswer);

            //uploading to database
            question_score.child(String.format("%s_%s", Common.currentUser.getUsername(),Common.categoryId))
                    .setValue(new QuestionScore(String.format("%s_%s",Common.currentUser.getUsername(),Common.categoryId),
                            Common.currentUser.getUsername(),
                            String.valueOf(score),
                            Common.categoryId,
                            Common.categoryName
                    ));
        }

    }
}
