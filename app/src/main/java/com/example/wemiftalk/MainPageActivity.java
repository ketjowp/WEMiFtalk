package com.example.wemiftalk;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.wemiftalk.Chat.ChatListAdapter;
import com.example.wemiftalk.Chat.ChatObject;
import com.example.wemiftalk.User.UserObject;
import com.example.wemiftalk.Utils.SendNotification;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.ArrayList;

public class  MainPageActivity extends AppCompatActivity {

    private RecyclerView mChatList;
    private RecyclerView.Adapter mChatListAdapter;
    private RecyclerView.LayoutManager mChatListLayoutManager;

    ArrayList<ChatObject> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        OneSignal.startInit(this).init();
        OneSignal.setSubscription(true);
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("notificationKey").setValue(userId);
            }
        });
        OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);

        new SendNotification("message 1", "heading 1", null);

        Fresco.initialize(this);




        Button mLogout = findViewById(R.id.logout);
        Button mFindUser = findViewById(R.id.findUser);

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneSignal.setSubscription(false);
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);// wyjscie do ekranu startowego
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//wyczyszczenie informacji z inych activity
                startActivity(intent);
                finish();
                return;
            }
        });

        mFindUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FindUserActivity.class));
            }
        });

        getPermisions();
        initializeRecyclerView();
        getUserChatList();
    }

    private void getUserChatList(){
        DatabaseReference mUserChatDB = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat");

        mUserChatDB.addValueEventListener(new ValueEventListener() { //cały czas nasłuchuje zmiany wartości, nie tylko na klik
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //kiedy pojawi się nowy czat, dodaj go do listy
                if(dataSnapshot.exists()){
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        ChatObject mChat= new ChatObject(childSnapshot.getKey());
                        boolean exists = false;
                        for(ChatObject mChatIterator : chatList){
                            if (mChatIterator.getChatId().equals(mChat.getChatId())){
                                exists = true;

                            }
                        }
                        if(exists)
                            continue;
                        chatList.add(mChat);
                        getChatData(mChat.getChatId());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getChatData(String chatId) {
        DatabaseReference mChatDB = FirebaseDatabase.getInstance().getReference().child("chat").child(chatId).child("info");
        mChatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String chatId ="";

                    if (dataSnapshot.child("id").getValue() != null)
                        chatId = dataSnapshot.child("id").getValue().toString();

                    for(DataSnapshot userSnapshot : dataSnapshot.child("users").getChildren()){  //sprawdza uzytkowników w danym czacie
                        for(ChatObject mChat : chatList) {
                            if(mChat.getChatId().equals(chatId)){
                                UserObject mUser = new UserObject(userSnapshot.getKey());
                                mChat.addUserToArrayList(mUser);
                                getUserData(mUser);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getUserData(UserObject mUser) {
        DatabaseReference mUserDb = FirebaseDatabase.getInstance().getReference().child("user").child(mUser.getUid()); //przewijamy sie przez uzytkownikow i pobieramy info o nich
        mUserDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserObject mUser = new UserObject(dataSnapshot.getKey());

                if(dataSnapshot.child("notificationKey").getValue() != null)
                    mUser.setNotificationKey(dataSnapshot.child("notificationKey").getValue().toString());

                for(ChatObject mChat : chatList){
                    for(UserObject mUserIt : mChat.getUserObjectArrayList()){
                        if(mUserIt.getUid().equals(mUser.getUid())){
                            mUserIt.setNotificationKey(mUser.getNotificationKey());
                        }
                    }
                }
                mChatListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeRecyclerView() {
        chatList = new ArrayList<>();
        mChatList= findViewById(R.id.chatList);
        mChatList.setNestedScrollingEnabled(false); //płynne przewijanie
        mChatList.setHasFixedSize(false);
        mChatListLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false); //późniejsze dodanie ozdobników, teraz false
        mChatList.setLayoutManager(mChatListLayoutManager); //przypisuje menadżera do layoutu
        mChatListAdapter = new ChatListAdapter(chatList);
        mChatList.setAdapter(mChatListAdapter);


    }

    private void getPermisions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//tylko w niektórych wersjach androida
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS},1);//pytanie o dostęp o kontaktów
        }
    }
}
