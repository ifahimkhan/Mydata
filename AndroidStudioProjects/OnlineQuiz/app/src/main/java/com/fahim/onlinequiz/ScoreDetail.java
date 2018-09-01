package com.fahim.onlinequiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.fahim.onlinequiz.Interface.ItemClickListener;
import com.fahim.onlinequiz.Model.QuestionScore;
import com.fahim.onlinequiz.ViewHolder.RankingViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ScoreDetail extends AppCompatActivity {

    String viewUser = "";
    FirebaseDatabase database;
    DatabaseReference question_score;

    RecyclerView scorelist;
    RecyclerView.LayoutManager layoutManager;


    FirebaseRecyclerAdapter<QuestionScore,RankingViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_detail);

        database=FirebaseDatabase.getInstance();
        question_score=database.getReference("Question_score");

        scorelist= (RecyclerView) findViewById(R.id.score_list);
        scorelist.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        scorelist.setLayoutManager(layoutManager);

        if (getIntent() != null) {
            viewUser = getIntent().getStringExtra("viewUser");
            if(!viewUser.isEmpty()){
                loadScoreDetail(viewUser);
                Toast.makeText(this, viewUser+"'s Score Details", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadScoreDetail(String viewUser) {

        adapter=new FirebaseRecyclerAdapter<QuestionScore, RankingViewHolder>(
                QuestionScore.class,
                R.layout.layout_ranking,
                RankingViewHolder.class,
                question_score.orderByChild("user").equalTo(viewUser)
        ) {
            @Override
            protected void populateViewHolder(RankingViewHolder viewHolder, QuestionScore model, int position) {

                viewHolder.textName.setText(model.getCategoryName());
                viewHolder.textScore.setText(model.getScore());
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLong) {

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        scorelist.setAdapter(adapter);
    }
}
