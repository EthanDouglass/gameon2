package com.example.frontendfinal;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class IncorrectFragment extends Fragment {

    private TextView pointsTextView;
    private TextView playerDisplayName;
    private int currentPoints;
    private String displayName;
    private CountDownTimer countDownTimer;


    public IncorrectFragment() {
        displayName = Player.getDisplayName();
        currentPoints = Player.getCurrentGamePts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_incorrect, container, false);
        
        pointsTextView = view.findViewById(R.id.CurrentPts);
        playerDisplayName = view.findViewById(R.id.DisplayName);

        pointsTextView.setText(String.valueOf(currentPoints));
        playerDisplayName.setText(displayName);

        return view;
    }

}
