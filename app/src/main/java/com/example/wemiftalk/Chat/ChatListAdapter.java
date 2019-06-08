package com.example.wemiftalk.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wemiftalk.ChatActivity;
import com.example.wemiftalk.R;
import com.example.wemiftalk.Chat.ChatObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    ArrayList<ChatObject> chatList;

    public ChatListAdapter(ArrayList<ChatObject> chatList){
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,null,false); //przekazanie chat item do listview
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT); // służy do ograniczenia rozmiaru Recycler View
        layoutView.setLayoutParams(lp);

        ChatListViewHolder rcv = new ChatListViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListViewHolder holder, final int position) {
        holder.mTitle.setText(chatList.get(position).getChatId());

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(v.getContext(), ChatActivity.class); //po wybraniu czatu zostanie przesłana o nim informacja (pakiet informacji - Bundle)
                Bundle bundle = new Bundle();
                bundle.putString("chatID",chatList.get(holder.getAdapterPosition()).getChatId());
                intent.putExtras(bundle);
                v.getContext().startActivity(intent); //uruchomienie ChatActivity
            }

        });


    }

    @Override
    public int getItemCount() { //uwaga, tutaj wywala błąd, już nie, bo zrobiona inicjalizacja listy w OnCreate
            return chatList.size();

    }


    public class ChatListViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitle;
        public ConstraintLayout mLayout; //zmiana Constraint zamiast Linear
        public ChatListViewHolder(View view){
            super(view);
            mTitle = view.findViewById(R.id.title);
            mLayout = view.findViewById((R.id.layout));
        }
    }
}