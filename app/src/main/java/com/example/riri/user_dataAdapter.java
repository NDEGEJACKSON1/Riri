package com.example.riri;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;


public class user_dataAdapter extends RecyclerView.Adapter<user_dataAdapter.ViewHolder> {

    private final List<user_dataInfos> user_dataInfos;

    public user_dataAdapter(List<com.example.riri.user_dataInfos> user_dataInfos){
        this.user_dataInfos = user_dataInfos;
    }

    @NonNull
    @Override
    public user_dataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View userdata_list_view = layoutInflater.inflate(R.layout.user_record_view,parent,false);

        return new ViewHolder(userdata_list_view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull user_dataAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(user_dataInfos.get(position).title);
        holder.description.setText(user_dataInfos.get(position).description);
        holder.time.setText(user_dataInfos.get(position).time + " " + user_dataInfos.get(position).date);
        holder.time_left_label.setText(user_dataInfos.get(position).getRemainedTime()[0]);
        holder.time_unit_label.setText(user_dataInfos.get(position).getRemainedTime()[1]);

        holder.remove_reminder.setOnClickListener(view -> {
            ((home_page)view.getContext()).remove_edit_item("remove",position,user_dataInfos.get(position).title,
                    user_dataInfos.get(position).description,user_dataInfos.get(position).time,user_dataInfos.get(position).date);
        });

        holder.edit_reminder.setOnClickListener(view -> {
            ((home_page)view.getContext()).remove_edit_item("edit",position,user_dataInfos.get(position).title,
                    user_dataInfos.get(position).description,user_dataInfos.get(position).time,user_dataInfos.get(position).date);
        });

    }


    @Override
    public int getItemCount() {
        return user_dataInfos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final MaterialTextView title,description,time,time_left_label,time_unit_label;
        public final MaterialButton edit_reminder,remove_reminder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.title_tag_view);
            this.description = itemView.findViewById(R.id.description_tag_view);
            this.time = itemView.findViewById(R.id.time_date_tag_view);
            this.time_left_label = itemView.findViewById(R.id.time_left_label);
            this.time_unit_label = itemView.findViewById(R.id.time_unit_label);

            this.edit_reminder = itemView.findViewById(R.id.edit_reminder);
            this.remove_reminder = itemView.findViewById(R.id.remove_reminder);
        }
    }

}
