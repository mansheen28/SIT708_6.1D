package com.example.personalizedlearningexperience;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewResult extends AppCompatActivity {

    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_result);
        rv = findViewById(R.id.recyclerViewResults);
        String[] ques = getIntent().getStringArrayExtra("ques");
        findViewById(R.id.buttonBackToMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewResult.this, Dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        List<String[]> list = new ArrayList<>();
        int i = 1;
        for (String que : ques) {
            String[] sp = que.split("@", 2);
            list.add(new String[]{i + ". " + sp[0], sp[1]});
            i++;
        }
        AdapterListResult questionAdapter = new AdapterListResult(list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(questionAdapter);
    }

    private class AdapterListResult extends RecyclerView.Adapter<AdapterListResult.TaskViewHolder> {

        List<String[]> questionsAnswers;

        public AdapterListResult(List<String[]> questionsAnswers) {
            this.questionsAnswers = questionsAnswers;
        }

        @NonNull
        @Override
        public AdapterListResult.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
            return new AdapterListResult.TaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterListResult.TaskViewHolder holder, int position) {
            String[] questions = questionsAnswers.get(position);
            holder.bind(questions);
        }

        @Override
        public int getItemCount() {
            return questionsAnswers.size();
        }

        public class TaskViewHolder extends RecyclerView.ViewHolder {
            TextView ques, ans;

            public TaskViewHolder(@NonNull View itemView) {
                super(itemView);
                ques = itemView.findViewById(R.id.tv_question);
                ans = itemView.findViewById(R.id.tv_answer);
            }

            public void bind(String[] questions) {
                ques.setText(questions[0]);
                ans.setText(questions[1]);
            }
        }
    }
}