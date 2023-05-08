package com.example.frontendfinal.sketchIt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Toast;

import com.example.frontendfinal.R;

import java.util.ArrayList;

/**
 * This class takes in all of the guesses and the drawing, creates buttons for each guess, and gets the player's final guess
 * and sets it as guess
 *
 * TODO - Implement timer
 */

public class SketchItGuessListActivity extends AppCompatActivity {

    private int numOptions;
    private ArrayList<String> guesses = new ArrayList<String>(); //The original guesses sent in
    private ArrayList<String> guessesUpdated = new ArrayList<String>(); //The new guesses after duplicates are taken out
    private static String guess = "";
    private ImageView drawing;
    private byte[] image;
    private Bitmap bitmap;
    private String correctPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sketch_it_guess_list);

        LinearLayout l_layout = (LinearLayout) findViewById(R.id.linear_layout);
        l_layout.setOrientation(LinearLayout.VERTICAL); // or HORIZONTAL

        drawing = findViewById(R.id.drawing);

        //Get the items given to us (Guesses and the Drawing)
        Bundle extras = getIntent().getExtras();
        if (extras!= null){
            guesses = getIntent().getStringArrayListExtra("guesses");
            image = getIntent().getByteArrayExtra("drawing"); //TODO not gonna work
            correctPrompt = getIntent().getStringExtra("prompt");
        }
        bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        drawing.setImageBitmap(bitmap);

        //Updating the list so no duplicate answers
        guessesUpdated = removeDuplicates(guesses);
        numOptions = guessesUpdated.size();

        for (int i = 0; i < numOptions; i++) {
            Button button = new Button(this);
            button.setText(guessesUpdated.get(i));
            button.setTextSize(20);
            Typeface typeface = ResourcesCompat.getFont(this, R.font.norwester);
            button.setTypeface(typeface);
            //Creates the id so we can find the button
            button.setId(i);
            l_layout.addView(button);
            Space space = new Space(this);
            if(i != numOptions - 1){
                space.getLayoutParams().height = 100;
            }

            button.setOnClickListener((View.OnClickListener) this);
        }
        // Let players vote for 10 seconds
        CountDownTimer c = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                // Do nothing
            }
            @Override
            public void onFinish() {
                Intent i = new Intent(SketchItGuessListActivity.this, SketchItGameActivity.class);
                i.putExtra("start", "false");
                i.putExtra("guesslist", guess);
                i.putExtra("prompt", correctPrompt);
                startActivity(i);
            }
        }.start();
    }

    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {
        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();
        // Traverse through the first list
        for (T element : list) {
            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }
        // return the new list
        return newList;
    }

    public void onClick(View v) {
        int guessNum = v.getId();
        guess = guesses.get(guessNum);
    }

    public String getGuess(){
        return guess;
    }
}