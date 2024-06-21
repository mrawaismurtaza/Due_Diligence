package com.example.due_diligence.Adapter_Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.due_diligence.ModelClasses.Submission;
import com.example.due_diligence.R;

import java.util.List;

public class Adapter_Submissions extends RecyclerView.Adapter<Adapter_Submissions.ViewHolder> {

    private List<Submission> submissions;
    private Context context;
    private OnItemClickListener mListener;

    public Adapter_Submissions(List<Submission> submissions, Context context) {
        this.submissions = submissions;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.submission_card, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Submission submission = submissions.get(position);
        holder.submissionDetail.setText(submission.getDetail());
    }

    @Override
    public int getItemCount() {
        return submissions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView submissionDetail;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            submissionDetail = itemView.findViewById(R.id.submissiondetail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
