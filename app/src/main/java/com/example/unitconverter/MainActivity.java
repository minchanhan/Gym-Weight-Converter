package com.example.unitconverter;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    boolean pounds = true; // If user input is in LBS, true. If use input is in KGS, false.

    float input = 0f;
    float conversion = 2.205f; // Divide lbs -> kgs, Multiply kgs -> lbs
    float solution = 0f;
    int roundedSoln = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Widgets
        Spinner spinUnit = findViewById(R.id.spinUnit);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinUnit.setAdapter(adapter);
        spinUnit.setOnItemSelectedListener(this);

        Button btnConvert = findViewById(R.id.btnConvert);

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inputWeight = findViewById(R.id.inputWeight);
                TextView txtDisplayAnswer = findViewById(R.id.txtDisplayAnswer);
                TextView txtSolution = findViewById(R.id.txtSolution);
                ///
                String inputStr = inputWeight.getText().toString();
                int inputLen = inputStr.trim().length();

                if(inputLen == 0){
                    String invalid = "Enter a value!";
                    Toast.makeText(getApplicationContext(), invalid, Toast.LENGTH_SHORT).show();
                } else {
                    input = Float.parseFloat(inputStr);
                    String answer;
                    String display;
                    String rounded;

                    if (pounds) {
                        // For txtSolution
                        solution = input / conversion;
                        answer = String.valueOf(solution);
                        display = answer + " KGS";
                        txtSolution.setText(display);

                        // For txtDisplayAnswer
                        roundedSoln = (int) solution;
                        int answerLen = answer.length();
                        char firstDecimal = '0'; // If solution is whole number, decimal is 0
                        for(int i = 0; i < answerLen; ++i){
                            if(answer.charAt(i) == '.') firstDecimal = answer.charAt(i+1);
                        }
                        rounded = roundedSoln + " KGS";
                        if(firstDecimal >= 48 && firstDecimal <= 52){ // Using Ascii values
                            txtDisplayAnswer.setText(rounded); // Bigger, Integer Value
                        } else {
                            String roundUp = (roundedSoln + 1) + " KGS";
                            txtDisplayAnswer.setText(roundUp);
                        }
                    } else {
                        // For txtSolution
                        solution = input * conversion;
                        answer = String.valueOf(solution);
                        display = answer + " LBS";
                        txtSolution.setText(display);

                        // For txtDisplayAnswer
                        roundedSoln = (int) solution;
                        int answerLen = answer.length();
                        char firstDecimal = '0'; // If solution is whole number, decimal is 0
                        for(int i = 0; i < answerLen; ++i){
                            if(answer.charAt(i) == '.') firstDecimal = answer.charAt(i+1);
                        }
                        rounded = roundedSoln + " LBS";
                        if(firstDecimal >= 48 && firstDecimal <= 52){ // Using Ascii values
                            txtDisplayAnswer.setText(rounded); // Bigger, Integer Value
                        } else {
                            String roundUp = (roundedSoln + 1) + " LBS";
                            txtDisplayAnswer.setText(roundUp);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // String unitText = parent.getItemAtPosition(position).toString(); To use if I want to
        // get "LBS" or "KGS" directly from array

        String message; // Initialize message
        if(position == 0){
            message = "LBS to KGS";
            pounds = true;
        } else {
            message = "KGS to LBS";
            pounds = false;
        }

        Toast.makeText(parent.getContext(), message, Toast.LENGTH_SHORT).show(); /// should be message
    }
    @Override public void onNothingSelected(AdapterView<?> parent) {} // Not Used
}
// Stable Build - 123 Lines (Now let's add drop down menu with preset weights)