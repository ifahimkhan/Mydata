package com.fahim.onlinequiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fahim.onlinequiz.Common.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Playing extends AppCompatActivity implements View.OnClickListener {

    final static long INTERVAL = 1000;
    final static long TIMEOUT = 7000;
    int progressValue = 0;
    CountDownTimer countDownTimer;

    int index = 0, score = 0, thisQuestion = 0, totalQuestion = 0, correctAnswer;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference questions;

    ProgressBar progressBar;
    ImageView question_image;
    Button btnA, btnB, btnC, btnD;
    TextView textScore, textQuestionNum, question_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");

        textScore = (TextView) findViewById(R.id.textScore);
        textQuestionNum = (TextView) findViewById(R.id.textTotalQuestion);
        question_text = (TextView) findViewById(R.id.question_text);
        question_image= (ImageView) findViewById(R.id.question_image);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnA = (Button) findViewById(R.id.btnAnswerA);
        btnB = (Button) findViewById(R.id.btnAnswerB);
        btnC = (Button) findViewById(R.id.btnAnswerC);
        btnD = (Button) findViewById(R.id.btnAnswerD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        countDownTimer.cancel();
        if (index < totalQuestion) {
            TextView clickButton = (TextView) view;
            if (clickButton.getText().equals(Common.questionList.get(index).getCorrectAnswer())) {

                score += 10;
                correctAnswer++;
                showQuestion(++index);
            } else {
                Bundle datasend = new Bundle();
                datasend.putInt("SCORE", score);
                datasend.putInt("TOTAL", totalQuestion);
                datasend.putInt("CORRECT", correctAnswer);
                startActivity(new Intent(this, Done.class).putExtras(datasend));
                finish();

            }
            textScore.setText(String.format("%d", score));
        }

    }

    private void showQuestion(int index) {

        if (index < totalQuestion) {
            thisQuestion++;
            textQuestionNum.setText(String.valueOf(thisQuestion) + " / " + totalQuestion);
            progressBar.setProgress(0);
            progressValue = 0;

            if (Common.questionList.get(index).getIsImageQuestion().equals("true")) {

                Picasso.with(getApplicationContext())
                        .load(Common.questionList.get(index).getQuestion())
                        .into(question_image);
                question_image.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);

            } else {
                question_image.setVisibility(View.INVISIBLE);
                question_text.setVisibility(View.VISIBLE);
                question_text.setText(Common.questionList.get(index).getQuestion());


            }

            btnA.setText(Common.questionList.get(index).getAnswerA());
            btnB.setText(Common.questionList.get(index).getAnswerB());
            btnC.setText(Common.questionList.get(index).getAnswerC());
            btnD.setText(Common.questionList.get(index).getAnswerD());

            countDownTimer.start();

        }else{
            Bundle datasend = new Bundle();
            datasend.putInt("SCORE", score);
            datasend.putInt("TOTAL", totalQuestion);
            datasend.putInt("CORRECT", correctAnswer);
            startActivity(new Intent(this, Done.class).putExtras(datasend));
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        totalQuestion=Common.questionList.size();
        countDownTimer=new CountDownTimer(TIMEOUT,INTERVAL) {
            @Override
            public void onTick(long milisec) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {

                countDownTimer.cancel();
                showQuestion(++index);
            }
        };
        showQuestion(index);
    }
}
