package com.fahim.onlinequiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fahim.onlinequiz.Common.Common;
import com.fahim.onlinequiz.Interface.ItemClickListener;
import com.fahim.onlinequiz.Interface.RankingCallBack;
import com.fahim.onlinequiz.Model.QuestionScore;
import com.fahim.onlinequiz.Model.Ranking;
import com.fahim.onlinequiz.ViewHolder.RankingViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RankingFragment extends Fragment {
    View myfragment;
    RecyclerView rankingList;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference question_score, rankingTbl;
    int sum = 0;

    public static RankingFragment newInstance() {
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_score");
        rankingTbl = database.getReference("Ranking");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myfragment = inflater.inflate(R.layout.fragment_ranking, container, false);
        rankingList = myfragment.findViewById(R.id.ranking_list);
        rankingList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        rankingList.setLayoutManager(layoutManager);
        updateScore(Common.currentUser.getUsername(), new RankingCallBack<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                rankingTbl.child(ranking.getUserName())
                        .setValue(ranking);
                showRanking();

            }
        });
        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(
                Ranking.class,
                R.layout.layout_ranking,
                RankingViewHolder.class,
                rankingTbl.orderByChild("score")
        ) {
            @Override
            protected void populateViewHolder(RankingViewHolder viewHolder, final Ranking model, int position) {
                viewHolder.textName.setText(model.getUserName());
                viewHolder.textScore.setText(String.valueOf(model.getScore()));

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLong) {

                        startActivity(new Intent(getActivity(),ScoreDetail.class).putExtra("viewUser",model.getUserName()));
                    }
                });

            }
        };
        adapter.notifyDataSetChanged();
        rankingList.setAdapter(adapter);
        return myfragment;
    }

    private void showRanking() {
        rankingTbl.orderByChild("score")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Ranking local = data.getValue(Ranking.class);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    private void updateScore(final String username, final RankingCallBack<Ranking> callback) {

        question_score.orderByChild("user").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            QuestionScore questionScore = data.getValue(QuestionScore.class);
                            sum += Integer.parseInt(questionScore.getScore());
                        }
                        Ranking ranking = new Ranking(username, sum);
                        callback.callBack(ranking);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}