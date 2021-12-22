package com.example.lab_4_balls.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.lab_4_balls.Adapters.UserScoreRecyclerViewAdapter;
import com.example.lab_4_balls.DBHelper;
import com.example.lab_4_balls.Models.UserScore;
import com.example.lab_4_balls.R;

import java.util.ArrayList;

public class ScoresActivity extends AppCompatActivity {
    TextView tvScore;
    RecyclerView rv;
    public static int score = 0;
    public static String currentUsername = null;
    ArrayList<UserScore> userScores;

    Button btnPlayAgain;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        UserScore userScore = new UserScore(currentUsername, score);

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        if (!dbHelper.add(userScore)) {
            if (score > dbHelper.scoreByUserName(userScore)) {
                dbHelper.updateScores(userScore);
            }
        }

        userScores = (ArrayList<UserScore>) dbHelper.getTopN(100);

        setFields();
        setListeners();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setFields() {
        btnPlayAgain = findViewById(R.id.btnPlayAgain);

        tvScore = findViewById(R.id.tvScore);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        UserScoreRecyclerViewAdapter adapter = new UserScoreRecyclerViewAdapter(
                this,
                userScores) {
            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                if (currentUsername != null) {
                    super.onBindViewHolder(holder, position);
                    String aaa = holder.tvUsername.getText().toString();
                    if (holder.tvUsername.getText().toString().equals(currentUsername)) {
                        holder.ll.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.list_item_focused, null));
                    }
                }
            }
        };
        rv.setAdapter(adapter);

        tvScore.setText(Integer.toString(score));
    }

    private void setListeners() {
        btnPlayAgain.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        });
    }
}