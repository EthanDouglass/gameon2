package com.example.frontendfinal.sketchIt;


import android.annotation.SuppressLint;
import android.os.Bundle;

import android.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.frontendfinal.R;

@SuppressLint("ValidFragment")
public class SketchIt1Fragment extends Fragment {

    private String prompt = "prompt";
    private TextView promptText;
    View view;
    private CountDownTimer countDownTimer;


    @SuppressLint("ValidFragment")
    SketchIt1Fragment(String prompt) {
        this.prompt = prompt;
    }

    @SuppressLint("StringFormatInvalid")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sketch_it1, container, false);
        promptText = view.findViewById(R.id.prompt);

        // Set the initial text for the readyTextView
        promptText.setText(getString(R.string.drawing_prompt, prompt));

        // Create and start the countdown timer
        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick (long millisUntilFinished) {
                // Nothing
            }

            @Override
            public void onFinish() {
                // Nothing
            }
        }.start();

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Cancel the countdown timer to prevent leaks
        countDownTimer.cancel();
    }
}
