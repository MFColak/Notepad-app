package com.notepad;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class NoteView extends RecyclerView.ViewHolder {

    View nView;

    TextView textTitle,textTime;
    CardView noteCard;
    public NoteView(@NonNull View itemView) {
        super(itemView);

        nView = itemView;

        textTitle= nView.findViewById(R.id.note_title);
        textTime = nView.findViewById(R.id.note_time);
        noteCard = nView.findViewById(R.id.notcard);
    }

    public void setNotBaslik(String title){
        textTitle.setText(title);
    }

    public void setNotZaman(String time){
        textTime.setText(time);
    }
}
