package com.example.voiceassistent;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MessageHolder extends RecyclerView.ViewHolder {

    protected TextView messageText;
    protected TextView messageDate;

    public MessageHolder(View itemView) {
        super(itemView);
        messageText = itemView.findViewById(R.id.messageText);
        messageDate = itemView.findViewById(R.id.messageDateTime);
    }

    public void BindData(Message message){
        DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        messageText.setText(message.text);
        messageDate.setText(fmt.format(message.dateTime));
    }
}
