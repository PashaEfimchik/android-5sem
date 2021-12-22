package com.example.lab_4_balls.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_4_balls.Activities.ScoresActivity;
import com.example.lab_4_balls.Models.UserScore;
import com.example.lab_4_balls.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UserScoreRecyclerViewAdapter extends RecyclerView.Adapter<UserScoreRecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<UserScore> userScores;

    public ArrayList<UserScore> getUserScores() {
        return userScores;
    }

    public UserScoreRecyclerViewAdapter(Context context, ArrayList<UserScore> userScores) {
        this.context = context;
        this.userScores = userScores;
    }

    @NonNull
    public UserScoreRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_score_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserScoreRecyclerViewAdapter.ViewHolder holder, int position) {
        UserScore userScore = userScores.get(position);

        holder.tvScore.setText(Integer.toString(userScore.score));
        holder.tvUsername.setText(userScore.username);
        holder.tvID.setText(Integer.toString(userScore.id) + ".");
    }

    @Override
    public int getItemCount() {
        return userScores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUsername;
        public TextView tvScore;
        public TextView tvID;
        public LinearLayout ll;

        public ViewHolder(View view) {
            super(view);
            tvUsername = view.findViewById(R.id.tvUsername);
            tvScore = view.findViewById(R.id.tvScore);
            tvID = view.findViewById(R.id.tvID);
            ll = view.findViewById(R.id.ll);
        }
    }
}
