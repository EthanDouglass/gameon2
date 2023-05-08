package com.example.frontendfinal.sketchIt;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.frontendfinal.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class SketchItActivity extends AppCompatActivity {
    /**
     * This is the activity for drawing in the SketchIt activity. This is only for drawing for players, and then saving it
     *
     *
     * GAME IDEA
     * Every person gets 60 seconds to draw their respective prompt. Then, the game goes through
     * every person's drawing 1 at a time and every player takes turns writing their guess as to what that
     * drawing really is. Then, all of the player guesses are given as choices, and every player must guess
     * what the actual prompt is vs the prompts the other players guessed.
     * THIS is the game "Drawful" in jackbox
     */

    private ImageView imageView;
    private TextView promptView;
    private float floatStartX = -1, floatStartY = -1, floatEndX = -1, floatEndY = -1;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint = new Paint();
    //private Button black;
    private ImageButton blue;
    private ImageButton red;
    private ImageButton orange;
    private ImageButton yellow;
    private ImageButton green;
    private ImageButton purple;

    //private Button next;
    private OutputStream test;
    private final int RED = Color.rgb(246,0,0); //0xF60000
    private final int ORANGE = Color.rgb(255,140,0); //0xFF8C00
    private final int YELLOW = Color.rgb(255,238,0); //0xFFEE00
    private final int GREEN = Color.rgb(77,233,76); //0x4DE94C
    private final int BLUE = Color.rgb(55,131,255); //0x3783FF
    private final int PURPLE = Color.rgb(72,21,170); //0x4815AA
    private String drawPrompt;
    private CountDownTimer countDownTimer;
    private TextView drawCountdown;
    private static byte[] byteArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sketch_it);

        ActivityCompat.requestPermissions(this
                ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        imageView = findViewById(R.id.imageView);
        promptView = findViewById(R.id.promptText);

        //black = findViewById(R.id.buttonBlack);
        blue = (ImageButton)findViewById(R.id.buttonBlue);
        red = (ImageButton)findViewById(R.id.buttonRed);
        orange = (ImageButton)findViewById(R.id.buttonOrange);
        yellow = (ImageButton)findViewById(R.id.buttonYellow);
        green = (ImageButton)findViewById(R.id.buttonGreen);
        purple = (ImageButton)findViewById(R.id.buttonPurple);

        //next = findViewById(R.id.buttonShowImage);

        drawCountdown = findViewById(R.id.drawCountdown);



        //Drawing Logic Time!
        Bundle extras = getIntent().getExtras();
        if (extras!= null){
            drawPrompt = extras.getString("prompt");
            promptView.setText(drawPrompt);
        }

        countDownTimer = new CountDownTimer(5000, 1000) { //TODO - CHANGE for actual game
            @Override
            public void onTick (long millisUntilFinished) {
                // Update text for the readyTextView every second
                int secondsLeft = (int) millisUntilFinished / 1000;
                drawCountdown.setText(getString(R.string.drawing_countdown, secondsLeft));
            }

            @Override
            public void onFinish() {
                //bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background); //Might be the fix? Change ic_launher_background if needed
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray(); //This byte array is the image! We can send this out to other players and open it on a receive
                //System.out.println(byteArray);
                //String byteString = new String(byteArray, StandardCharsets.UTF_8);
                //System.out.println(byteString);
                //byte[] test = byteString.getBytes(StandardCharsets.UTF_8);
                //System.out.println("Converted - " + test);
                try {
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//                //TESTING
//                Intent j = new Intent(SketchItActivity.this, SketchItGuessActivity.class);
//                j.putExtra("drawing", byteArray);
//                startActivity(j);
//                //TESTING ^^^

                //bitmap.recycle();
                Intent i = new Intent(SketchItActivity.this, SketchItGameActivity.class);
                i.putExtra("start", "false");
                i.putExtra("drawing", byteArray);
                i.putExtra("prompt", drawPrompt);
                startActivity(i);
            }
        }.start();



        /**
         * Below are the button calls for the drawing activity
         */
//        black.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                paint.setColor(Color.BLACK);
//            }
//        });

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint.setColor(BLUE);
            }
        });
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint.setColor(RED);
            }
        });

        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint.setColor(ORANGE);
            }
        });

        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint.setColor(YELLOW);
            }
        });

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint.setColor(GREEN);
            }
        });

        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint.setColor(PURPLE);
            }
        });


//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(SketchItActivity.this, ShowImageActivity.class);
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] byteArray = stream.toByteArray(); //This byte array is the image! We can send this out to other players and open it on a receive
//                i.putExtra("image", byteArray);
//                try {
//                    stream.close();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                bitmap.recycle();
//                startActivity(i);
//            }
//        });

    }

    private void drawPaintSketchImage(){

        if (bitmap == null){
            bitmap = Bitmap.createBitmap(imageView.getWidth(),
                    imageView.getHeight(),
                    Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            paint.setColor(RED);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(8);
        }
        canvas.drawLine(floatStartX,
                floatStartY-220,
                floatEndX,
                floatEndY-220,
                paint);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            floatStartX = event.getX();
            floatStartY = event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE){
            floatEndX = event.getX();
            floatEndY = event.getY();
            drawPaintSketchImage();
            floatStartX = event.getX();
            floatStartY = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP){
            floatEndX = event.getX();
            floatEndY = event.getY();
            drawPaintSketchImage();
        }
        return super.onTouchEvent(event);
    }

    public static byte[] getDrawing(){
        return byteArray;
    }

}
