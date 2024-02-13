package com.example.noteapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.noteapp.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class EditFragment extends Fragment {

    private static final String ARG_HEADER = "header";
    private static final String ARG_CONTENT = "content";

    public EditFragment() {
        // Required empty public constructor
    }

    public static EditFragment newInstance(String header, String content) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_HEADER, header);
        args.putString(ARG_CONTENT, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText editText = view.findViewById(R.id.contentfield);
        final TextView textView = view.findViewById(R.id.heading_view);
        final Button buttonSave = view.findViewById(R.id.savebutton);
        final Button buttonBackHome = view.findViewById(R.id.back_home);

        if (getArguments() != null) {
            String header = getArguments().getString(ARG_HEADER);
            textView.setText(header);

            String content = getArguments().getString(ARG_CONTENT);
            editText.setText(content);
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String header = textView.getText().toString();
                String filename = header + ".txt";
                String content = editText.getText().toString().trim(); // Retrieve content from EditText

                if (!content.isEmpty()) {
                    File dir = requireActivity().getFilesDir();
                    File file = new File(dir, filename);
                    file.delete();

                    try {
                        FileOutputStream fOut = requireActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                        fOut.write(content.getBytes());
                        fOut.close();
                        TextView statusView = view.findViewById(R.id.status);
                        statusView.setText("SAVED!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    editText.setError("Content can't be empty");
                }
            }
        });

        buttonBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
