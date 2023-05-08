package com.example.frontendfinal;


import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import android.app.Fragment;

/**
 * Change Log --
 *
 * Changed some of the correct vs incorrect logic around to make to easier to understand
 * Started a more complex points system, where points are awarded based on when they answer
 *    This value is saved to "answerPts", which is called back in the FactOrCap Activity and added to the player's points
 */


@SuppressLint("ValidFragment")
public class QuestionScreenFragment extends Fragment {
     private TextView questionTextView;
     private Button trueButton;
     private Button falseButton;
     private CountDownTimer countDownTimer;

    private TextView pointsTextView;
    private TextView playerDisplayName;

     private String question;
     private String correct_answer;
    private String temp_answer;
    private boolean earnedPoints;

    private int answerPts = 0;

    private final int currentPoints;
    private final String displayName;
    private long startTime;
    private long tempTime;

    public QuestionScreenFragment(String question, String answer) {
         this.question = question;
         this.correct_answer = answer;
        displayName = Player.getDisplayName();
        currentPoints = Player.getCurrentGamePts();
     }

     @Nullable
     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_question_sreen, container, false);

         questionTextView = view.findViewById(R.id.question_text_view);
         trueButton = view.findViewById(R.id.true_button);
         falseButton = view.findViewById(R.id.false_button);
         pointsTextView = view.findViewById(R.id.CurrentPts);
         playerDisplayName = view.findViewById(R.id.DisplayName);

         pointsTextView.setText(String.valueOf(currentPoints));

         //Set the current player's DisplayName
         playerDisplayName.setText(displayName);


         // Set the question text
         questionTextView.setText(question);


         //This sets what time the player enters the question - for points calculation
         startTime = System.currentTimeMillis();
         tempTime = System.currentTimeMillis() + 5000;

         // Set click listeners for the true & false buttons
         trueButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                tempTime = System.currentTimeMillis();
                setAnswer("true"); //tempAnswer to true
             }
         });
         falseButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 tempTime = System.currentTimeMillis();
                 setAnswer("false"); //tempAnswer to false
             }
         });

         // Create and start the countdown timer
         countDownTimer = new CountDownTimer(5000, 1000) {

             @Override
             public void onTick(long l) {
                 // Do nothing while waiting for the user to answer
             }

             @Override
             public void onFinish() {
                // If the user hasn't answered yet, treat it as the wrong answer
                 if (temp_answer == null) {
                     onAnswerSelected("no answer");
                 }
                 onAnswerSelected(temp_answer);
             }
         }.start();

         return view;
     }

     private void onAnswerSelected (String answer) {
        if (answer != null) {
            if (answer.equals(correct_answer.toLowerCase())) {
                earnedPoints = true;
                tempTime = tempTime - startTime;
                answerPts = (5000 - (int) tempTime) / 10;
                loadFragment(new CorrectFragment(answerPts));
            } else {
                earnedPoints = false;
                answerPts = 0;
                loadFragment(new IncorrectFragment());
            }

            // Cancel the countDownTimer
            countDownTimer.cancel();
        } else {
            earnedPoints = false;
            answerPts = 0;
            loadFragment(new IncorrectFragment());
        }
     }

     public void setAnswer(String b) {
         temp_answer = b;
     }

     public boolean correctCheck() {
        return earnedPoints;
     }

     //This returns how many points the player earned during the question based off speed
     public int pointsEarned(){
        return answerPts;
     }

     @Override
    public void onDestroyView() {
         super.onDestroyView();
         // Cancel the countdown timer to prevent leaks
         countDownTimer.cancel();
     }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.factorcapframe, fragment);
        fragmentTransaction.commit();
    }
}
