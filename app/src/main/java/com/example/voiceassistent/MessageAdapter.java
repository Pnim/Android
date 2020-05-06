package com.example.voiceassistent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter implements Serializable {
    public List<Message> messages = new ArrayList<>();
    private static final int HELPER = 0;
    private static final int USER = 1;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == USER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_user_message, parent, false);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_helper_message, parent, false);
        }
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MessageHolder)holder).BindData(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public int getItemViewType(int index) {
        Message message = messages.get(index);
        if(message.wasSend) {
            return USER;
        }
        else {
            return HELPER;
        }
    }
}
