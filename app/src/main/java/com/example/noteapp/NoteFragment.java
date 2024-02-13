package com.example.noteapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class NoteFragment extends Fragment {
    private static final String ARG_MESSAGE = "message";
    private String message;

    public NoteFragment() {
        // Required empty public constructor
    }

    public static NoteFragment newInstance(String message) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            message = getArguments().getString(ARG_MESSAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String FILE_NAME = message + ".txt";
        try {
            FileInputStream fis = getActivity().openFileInput(FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            StringBuilder whole = new StringBuilder(); // Use StringBuilder for efficiency
            while ((line = reader.readLine()) != null) {
                if (whole.length() == 0) {
                    whole.append(line);
                } else {
                    whole.append("\n").append(line);
                }
            }
            reader.close();
            String content = whole.toString();
            Log.d("NoteFragment", "Content read from file: " + content); // Add this line to check the content
            TextView textView1 = view.findViewById(R.id.content);
            textView1.setText(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button delete = view.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File dir = getActivity().getFilesDir();
                File file = new File(dir, FILE_NAME);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                file.delete();
                startActivity(intent);
            }
        });

        Button edit = view.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the content to EditFragment
                TextView textView1 = view.findViewById(R.id.content);
                String content = textView1.getText().toString();
                EditFragment editFragment = EditFragment.newInstance(message, content);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, editFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        Button button = view.findViewById(R.id.back_home2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), MainActivity.class);
                startActivity(intent1);
            }
        });
    }
}
