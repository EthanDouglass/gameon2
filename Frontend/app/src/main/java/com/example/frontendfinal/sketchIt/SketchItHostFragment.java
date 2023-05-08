package com.example.frontendfinal.sketchIt;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.frontendfinal.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SketchItHostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressLint("ValidFragment")
public class SketchItHostFragment extends Fragment {


    private ImageView drawing;
    private View view;
    private Bitmap bitmap;

    @SuppressLint("ValidFragment")
    SketchItHostFragment() {
    }


    @SuppressLint("StringFormatInvalid")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sketch_it_host, container, false);

        // Set the initial text for the readyTextView
        drawing = view.findViewById(R.id.drawing);
        byte[] temp = SketchItGameActivity.getDrawing();
        bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
        drawing.setImageBitmap(bitmap);


        return view;
    }
}