package com.example.riri;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class new_reminder extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_reminder, container, false);
        EditText[] editTexts = {view.findViewById(R.id.title), view.findViewById(R.id.description), view.findViewById(R.id.time_picker), view.findViewById(R.id.date_picker)};
        Button button = view.findViewById(R.id.submit_button);
        for (EditText ed : editTexts) {
            ed.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (ed.getId() == R.id.title && ed.getText().toString().split(" ").length >= 7) {
                        ed.setError("Please Limit Your Words to 7");
                        button.setEnabled(false);
                    } else if (ed.getId() == R.id.title && ed.getText().toString().split(" ").length <= 7) {
                        button.setEnabled(true);
                    }

                    if (ed.getId() == R.id.description && ed.getText().toString().split(" ").length >= 30) {
                        ed.setError("Please Limit Your Words to 30");
                        button.setEnabled(false);
                    } else if (ed.getId() == R.id.description && ed.getText().toString().split(" ").length <= 30) {
                        ;
                        button.setEnabled(true);
                    }
                }
            });
        }


        // Inflate the layout for this fragment
        return view;
    }


}