package com.example.noteapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.noteapp.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainFragment extends Fragment {

    public static final String EXTRA_MESSAGE = "com.example.notes.MESSAGE";

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ArrayList<String> arrayList = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, arrayList);
        final ListView listView = view.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        File files = requireContext().getFilesDir();
        String[] array = files.list();
        assert array != null;
        for (String filename : array) {
            filename = filename.replace(".txt", "");
            adapter.add(filename);
        }

        Button button = view.findViewById(R.id.savebutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextHeading = view.findViewById(R.id.editTextTextPersonName);
                EditText editTextContent = view.findViewById(R.id.contentfield);
                String heading = editTextHeading.getText().toString().trim();
                String content = editTextContent.getText().toString().trim();
                if (!heading.isEmpty()) {
                    if (!content.isEmpty()) {
                        try {
                            FileOutputStream fileOutputStream = requireContext().openFileOutput(heading + ".txt", Context.MODE_PRIVATE); //heading will be the filename
                            fileOutputStream.write(content.getBytes());
                            fileOutputStream.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        adapter.add(heading);
                    } else {
                        editTextContent.setError("Content can't be empty!");
                    }
                } else {
                    editTextHeading.setError("Heading can't be empty!");
                }
                editTextContent.setText("");
                editTextHeading.setText("");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = listView.getItemAtPosition(position).toString();

                // Create a new instance of NoteFragment and pass the selected item as an argument
                NoteFragment noteFragment = NoteFragment.newInstance(item);

                // Perform a fragment transaction to replace the current fragment with the NoteFragment
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, noteFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
