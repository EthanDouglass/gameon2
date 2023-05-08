package com.example.frontendfinal.sketchIt;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.frontendfinal.R;

public class SketchItGuessWaitFragment extends Fragment {
    private View v;
    private TextView t;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.guesswaitfragment, container, false);

        return v;
    }
}
