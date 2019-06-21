package com.example.arrays;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageView;
    private TextView mTextView;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.imageView);
        mTextView = (TextView) findViewById(R.id.fact);
        mButton = (Button) findViewById(R.id.fact_button);

        showRandomFact();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRandomFact();
            }
        });

    }

    public void showRandomFact(){
        shuffleFacts();
        mImageView.setImageResource(factArray[0].getmImage());
        mTextView.setText(factArray[0].getmFact());
    }

   Facts f01 = new Facts(R.drawable.pig, "Pigs are very smart.");
   Facts f02 = new Facts(R.drawable.dolphin, "Dolphins are fun animals");
   Facts f03 = new Facts(R.drawable.peacock, "Peacock refers to male only!");
   Facts f04 = new Facts(R.drawable.seahorse, "Male seahorse gets pregnant.");
   Facts f05 = new Facts(R.drawable.turtle, "Turtle can live up to 200 years.");

   Facts [] factArray = new Facts[]{
   f01,f02,f03,f04,f05
   };

   public void shuffleFacts(){
       Collections.shuffle(Arrays.asList(factArray));
   }
}
