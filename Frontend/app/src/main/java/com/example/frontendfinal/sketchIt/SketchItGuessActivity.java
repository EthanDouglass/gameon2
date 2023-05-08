package com.example.frontendfinal.sketchIt;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.frontendfinal.R;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class is where players will see the drawing and type their guess as to what the prompt is
 *
 * If they guess it correct, tell them they're correct and pass that info to the SketchItGameActivity
 *
 * TODO - Add 15s timer in the corner and implement timer
 */
public class SketchItGuessActivity extends AppCompatActivity {

    private static String playerGuess; // Stores the users guess for the drawing
    private byte[] drawing;
    private ImageView image;
    private Button guessButton;
    private EditText guessText;
    private String drawPlayerId;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sketch_it_guess);

        //Bundle extras = getIntent().getExtras();
        //if (extras != null) {
            drawing = getIntent().getByteArrayExtra("drawing");
            drawPlayerId = getIntent().getStringExtra("playerid");

        //}

        // TODO Figure out why image will not load
        //Bitmap bitmap = BitmapFactory.decodeByteArray(drawing, 0, drawing.length);
        //image = (ImageView) (findViewById(R.id.imageDrawing2));
        //image.setImageBitmap(bitmap);

        guessButton = findViewById(R.id.GuessButton);
        guessText = findViewById(R.id.GuessText);

        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerGuess = guessText.getText().toString();
                Intent m = new Intent(SketchItGuessActivity.this, SketchItGameActivity.class);
                m.putExtra("start", "false");
                m.putExtra("guess", playerGuess);
                m.putExtra("playerid", drawPlayerId);
                startActivity(m);
                finish();
            }
        });

    }

    public static String getGuess() {
        return playerGuess;
    }
}
