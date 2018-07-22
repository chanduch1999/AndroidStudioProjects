package com.example.immad.quiz;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionsViewHolder> {
    private String[] Questions;

    QuestionsAdapter(String[] questions) {
        Questions = questions;
    }

    @NonNull
    @Override
    public QuestionsAdapter.QuestionsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new QuestionsAdapter.QuestionsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.relative, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionsAdapter.QuestionsViewHolder questionsViewHolder, int i) {
        String qus = (i + 1) + " Question";
        String question = Questions[i];

        questionsViewHolder.question.setText(qus);
        questionsViewHolder.answered.setText(question);
        questionsViewHolder.correct.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return Questions.length;
    }

    class QuestionsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_question)
        TextView question;

        @BindView(R.id.tv_answered)
        TextView answered;

        @BindView(R.id.tv_correct)
        TextView correct;

        QuestionsViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
