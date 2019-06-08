package com.example.wemiftalk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.wemiftalk.Chat.MediaAdapter;
import com.example.wemiftalk.Chat.MessageAdapter;
import com.example.wemiftalk.Chat.MessageObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView mChat,mMedia;
    private RecyclerView.Adapter mChatAdapter,mMediaAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager,mMediaLayoutManager;

    ArrayList<MessageObject> messageList;

    String chatID;

    DatabaseReference mChatDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatID=getIntent().getExtras().getString("chatID"); //odebranie nazwy czatu z main Activity

        mChatDb=FirebaseDatabase.getInstance().getReference().child("chat").child(chatID);

        Button mSend = findViewById(R.id.send);
        Button mAddMedia = findViewById(R.id.addMedia);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        mAddMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        }); //listner do media
        initializeMessage();
        initializeMedia();
        getChatMessages();
    }



    private void getChatMessages() { //metoda pozwalająca na pobieranie i wyświetlanie wiadomości, tylko jeden Listner używany
        mChatDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { //kiedy zostanie dodany child - message - na to zwraca uwagę
                if(dataSnapshot.exists()){
                    String  text = "",
                            creatorID="";
                    if(dataSnapshot.child("text").getValue() != null)
                        text=dataSnapshot.child("text").getValue().toString();
                    if(dataSnapshot.child("creator").getValue() != null)
                        creatorID=dataSnapshot.child("creator").getValue().toString();

                    MessageObject mMessage = new MessageObject(dataSnapshot.getKey(),creatorID,text); //stworzenie obiektu message z zawartością pobraną powyżej
                    messageList.add(mMessage); //dodanie message do listy wiadomości
                    mChatLayoutManager.scrollToPosition(messageList.size()-1); // scrolowanie listy do ostatniej wiadomości
                    mChatAdapter.notifyDataSetChanged();

                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void sendMessage(){
        EditText newMessage = findViewById(R.id.messageInput);

        if(!newMessage.getText().toString().isEmpty()){
            DatabaseReference newMessageDb= mChatDb.push(); //push - stworzenie nowej wiadomości

            Map newMessageMap = new HashMap<>();
            newMessageMap.put("text",newMessage.getText().toString());
            newMessageMap.put("creator", FirebaseAuth.getInstance().getUid());

            newMessageDb.updateChildren(newMessageMap);

        }

        newMessage.setText(null); //po wysłaniu kasuje pole tekstowe

    }



    private void initializeMessage() { //RecyclerView
        messageList=new ArrayList<>();
        mChat= findViewById(R.id.messageList);
        mChat.setNestedScrollingEnabled(false); //płynne przewijanie
        mChat.setHasFixedSize(false);
        mChatLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false); //późniejsze dodanie ozdobników, teraz false
        mChat.setLayoutManager(mChatLayoutManager); //przypisuje menadżera do layoutu
        mChatAdapter = new MessageAdapter(messageList);
        mChat.setAdapter(mChatAdapter);

    }

    int PICK_IMAGE_INTENT=1;
    ArrayList<String> mediaUriList = new ArrayList<>();

    private void initializeMedia() { //RecyclerView
        messageList=new ArrayList<>();
        mMedia= findViewById(R.id.mediaList);
        mMedia.setNestedScrollingEnabled(false); //płynne przewijanie
        mMedia.setHasFixedSize(false);
        mMediaLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.HORIZONTAL, false); //późniejsze dodanie ozdobników, teraz false
        mMedia.setLayoutManager(mMediaLayoutManager); //przypisuje menadżera do layoutu
        mMediaAdapter = new MediaAdapter(getApplicationContext(),mediaUriList);
        mMedia.setAdapter(mMediaAdapter);

    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Wybierz Zdjęcia"),PICK_IMAGE_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == PICK_IMAGE_INTENT){
                if(data.getClipData()== null) { //jeden obraz
                    mediaUriList.add(data.getData().toString()); //przy wybraniu jednego zdjęcia dodaje jego Uri do listy
                }else{
                    for(int i=0;i<data.getClipData().getItemCount(); i++){
                        mediaUriList.add(data.getClipData().getItemAt(i).getUri().toString()); //dodanie wszystkich wybranych zdjęć do listy
                    }
                }

                mMediaAdapter.notifyDataSetChanged(); //powiadomienie o zmianie dnaych, aby nastąpiło natychmiastowe dodanie zdjęć
            }
        }
    }
}
