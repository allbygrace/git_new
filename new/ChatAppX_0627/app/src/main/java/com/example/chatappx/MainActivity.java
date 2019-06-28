package com.example.chatappx;
/*
import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
*/

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.github.library.bubbleview.BubbleTextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.github.library.bubbleview.BubbleTextView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;


public class MainActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE =1;
    private FirebaseListAdapter<ChatMessage> adapter;
    RelativeLayout activity_main;
    FloatingActionButton fab;
    EmojiconEditText emojiconEditText;
    ImageView emojiButton,submitButton;
    EmojIconActions emojIconActions;




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out)
        {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(activity_main, "You've been signed out. !", Snackbar.LENGTH_SHORT).show();
                    finish();
                }
           });
        }

         return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_REQUEST_CODE)
        {
            if(requestCode == RESULT_OK)
            {
               Snackbar.make(activity_main, "Successdul sign in. Welcome! ", Snackbar.LENGTH_SHORT).show();
               displaayChatMessage();
            }
            else
            {
                Snackbar.make(activity_main, "Unable to sign in. Please try again later. !", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //fab = (FloatingActionButton)findViewById(R.id.fab);

        /*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input =(EditText)findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input.selectAll();
            }
        });
    */
        activity_main =(RelativeLayout)findViewById(R.id.activity_main);

        //Add Emoji
        emojiButton = (ImageView)findViewById(R.id.emoji_button);
        submitButton = (ImageView)findViewById(R.id.submit_button);
        emojiconEditText = (EmojiconEditText)findViewById(R.id.emojicon_edit_text);
        emojIconActions = new EmojIconActions(getApplicationContext(),activity_main,emojiButton,emojiconEditText);
        emojIconActions.ShowEmojicon();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("ChatMessage")
                        .push().setValue(new ChatMessage(emojiconEditText.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));

                emojiconEditText.setTag("");
                emojiconEditText.requestFocus();
            }
        });



        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE);
        }
        else
        {
            Snackbar.make(activity_main,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
        }

        displaayChatMessage();
    }
    private void displaayChatMessage() {

        ListView ListOfMessage = (ListView)findViewById(R.id.list_of_message);

        Query query = FirebaseDatabase.getInstance().getReference();
        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(query,ChatMessage.class)
                .setLayout(R.layout.list_item)
                .build();


        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull ChatMessage model, int position) {
                TextView messageText, messageUser, messageTime;
                messageText = (TextView) findViewById(R.id.message_text);
                messageUser = (TextView) findViewById(R.id.message_user);
                messageTime = (TextView) findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                //messageTime.setText(DateFormat.format("mm-dd-yyyy (HH:mm:ss)", model.getMessageTime()));
                messageTime.setText(DateFormat.format("mm-dd-yyyy (HH:mm:ss)", model.getMessageTime()));

            }
        };
      /*
        adapter = new FirebaseListAdapter<ChatMessage> (this, ChatMessage.class,R.layout.list_item, FirebaseDatabase.getInstance().getReference())

        {

            @Override
            protected void populateView(@NonNull View v, @NonNull ChatMessage model, int position) {

                TextView messageText, messageUser, messageTime;
                messageText = (TextView) findViewById(R.id.message_text);
                messageUser = (TextView) findViewById(R.id.message_user);
                messageTime = (TextView) findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                //messageTime.setText(DateFormat.format("mm-dd-yyyy (HH:mm:ss)", model.getMessageTime()));
                messageTime.setText(DateFormat.format("mm-dd-yyyy (HH:mm:ss)", model.getMessageTime()));

            }

         */

        adapter.startListening();
        ListOfMessage.setAdapter(adapter);

    }

    @Override
    protected void onStop() {
        if (adapter != null)
        adapter.stopListening();
        super.onStop();
    }
}
