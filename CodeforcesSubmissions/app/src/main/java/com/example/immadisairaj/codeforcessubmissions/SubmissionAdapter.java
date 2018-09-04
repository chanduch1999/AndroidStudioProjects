package com.example.immadisairaj.codeforcessubmissions;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubmissionAdapter extends RecyclerView.Adapter<SubmissionAdapter.SubmissionViewHolder> {

    private List<Result> submissionList;

    public SubmissionAdapter(List<Result> submissionList) {
        this.submissionList = submissionList;
    }

    public SubmissionAdapter() {
    }

    public void clear() {
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public SubmissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubmissionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycle_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubmissionViewHolder holder, int position) {

        String qName, qCode, tags, lang, verdict, time, memory;

        qName = submissionList.get(position).getProblem().getName();
        holder.mQName.setText(qName);

        String id = submissionList.get(position).getProblem().getContestId() + "";
        if (id.equals("null")) {
            qCode = submissionList.get(position).getProblem().getIndex();
        } else {
            qCode = id +
                    submissionList.get(position).getProblem().getIndex();
        }
        holder.mQCode.setText(qCode);

        List<String> tagsList = submissionList.get(position).getProblem().getTags();
        if (!tagsList.isEmpty()) {
            StringBuilder tagsBuilder = new StringBuilder(tagsList.get(0));
            for (int i = 1; i < tagsList.size(); i++)
                tagsBuilder.append(", ").append(tagsList.get(i));
            tags = tagsBuilder.toString();
        } else
            tags = "no tags";
        holder.mTags.setText(tags);

        lang = submissionList.get(position).getProgrammingLanguage();
        holder.mLang.setText(lang);

        verdict = submissionList.get(position).getVerdict();
        holder.mVerdict.setText(verdict);
        if(verdict.equals("OK"))
            holder.mVerdict.setTextColor(Color.parseColor("#228B22"));
        else
            holder.mVerdict.setTextColor(Color.RED);

        time = "Time: " + submissionList.get(position).getTimeConsumedMillis().toString() + " ms";
        holder.mTime.setText(time);

        memory = "Memory: " + submissionList.get(position).getMemoryConsumedBytes().toString() + " B";
        holder.mMemory.setText(memory);
    }

    @Override
    public int getItemCount() {
        return submissionList.size();
    }

    public class SubmissionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_qName)
        TextView mQName;
        @BindView(R.id.tv_qCode)
        TextView mQCode;
        @BindView(R.id.tv_tags)
        TextView mTags;
        @BindView(R.id.tv_lang)
        TextView mLang;
        @BindView(R.id.tv_verdict)
        TextView mVerdict;
        @BindView(R.id.tv_time)
        TextView mTime;
        @BindView(R.id.tv_memory)
        TextView mMemory;

        public SubmissionViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }
}
