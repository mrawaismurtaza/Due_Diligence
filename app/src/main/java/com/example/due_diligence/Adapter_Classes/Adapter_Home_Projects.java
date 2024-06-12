package com.example.due_diligence.Adapter_Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.due_diligence.ModelClasses.Project;
import com.example.due_diligence.R;

import java.util.List;

public class Adapter_Home_Projects extends RecyclerView.Adapter<Adapter_Home_Projects.MyViewHolder> {

    private List<Project> projects;
    private Context context;
    private OnItemClickListener mListener;

    public Adapter_Home_Projects(Context context, List<Project> projects) {
        this.context = context;
        this.projects = projects;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_card, parent, false);
        return new MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Project project = projects.get(position);
        holder.projectNameTextView.setText(project.getName());
        // If you want to set an image, uncomment the line below and replace `project.getImage()` with your actual image resource
        // holder.projectImageView.setImageResource(project.getImage());
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView projectNameTextView;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            projectNameTextView = itemView.findViewById(R.id.projectname);

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
