package com.hanprojects.gymweightconverter;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    boolean pounds = true; // If user input is in LBS, true. If use input is in KGS, false.
    float input = 0f;
    float conversion = 2.205f; // Divide lbs -> kgs, Multiply kgs -> lbs
    float solution = 0f;
    int roundedSoln = 0;

    ImageButton lbBarBtn; // Used in onItemSelected()
    ImageButton kgBarBtn;

    String inputStr; // Used in OnClick()
    EditText inputWeight;
    TextView txtSolution;
    TextView txtDisplayAnswer;
    Button btnConvert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize Widgets
        Spinner spinUnit = findViewById(R.id.spinUnit);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinUnit.setAdapter(adapter);
        spinUnit.setOnItemSelectedListener(this);

        btnConvert = findViewById(R.id.btnConvert);
        Button btnClear = findViewById(R.id.btnClear);

        ImageButton lb2_5Btn = findViewById(R.id.lb2_5Btn);
        ImageButton lb5Btn = findViewById(R.id.lb5Btn);
        ImageButton lb10Btn = findViewById(R.id.lb10Btn);
        ImageButton lb25Btn = findViewById(R.id.lb25Btn);
        ImageButton lb35Btn = findViewById(R.id.lb35Btn);
        ImageButton lb45Btn = findViewById(R.id.lb45Btn);

        ImageButton kg2_5Btn = findViewById(R.id.kg2_5Btn);
        ImageButton kg5Btn = findViewById(R.id.kg5Btn);
        ImageButton kg10Btn = findViewById(R.id.kg10Btn);
        ImageButton kg15Btn = findViewById(R.id.kg15Btn);
        ImageButton kg20Btn = findViewById(R.id.kg20Btn);
        ImageButton kg25Btn = findViewById(R.id.kg25Btn);

        lbBarBtn = findViewById(R.id.lbBarBtn);
        kgBarBtn = findViewById(R.id.kgBarBtn);
        kgBarBtn.setVisibility(View.GONE);

        // Setting up the main EditText
        inputWeight = findViewById(R.id.inputWeight);
        inputStr = inputWeight.getText().toString(); // text/string value (NULL AT START)

        // Convert
        btnConvert.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        // Plate Increment
        lb2_5Btn.setOnClickListener(this);
        lb5Btn.setOnClickListener(this);
        lb10Btn.setOnClickListener(this);
        lb35Btn.setOnClickListener(this);
        lb25Btn.setOnClickListener(this);
        lb45Btn.setOnClickListener(this);

        kg2_5Btn.setOnClickListener(this);
        kg5Btn.setOnClickListener(this);
        kg10Btn.setOnClickListener(this);
        kg15Btn.setOnClickListener(this);
        kg20Btn.setOnClickListener(this);
        kg25Btn.setOnClickListener(this);

        // Bar Increment
        lbBarBtn.setOnClickListener(this);
        kgBarBtn.setOnClickListener(this);

        // To maintain variables upon rotation
        if(savedInstanceState != null) {
            input = savedInstanceState.getFloat("input");
            setNewWeight();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat("input", input);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.rate) {
            String message = getString(R.string.about);
            String message2 = getString(R.string.about2);
            String finalMessage = message + "\n\n" + message2;

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Thanks for using my app!").create();
            alert.setMessage(finalMessage);
            alert.setPositiveButton("OK, Rate!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + getPackageName())));
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                }
            });
            alert.setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // String unitText = parent.getItemAtPosition(position).toString(); To use if I want to
        // get "LBS" or "KGS" directly from array

        LinearLayout linearLB = findViewById(R.id.linearLayoutLB);
        LinearLayout linearLB2 = findViewById(R.id.linearLayoutLB2);
        LinearLayout linearLB3 = findViewById(R.id.linearLayoutLB3);
        LinearLayout linearKG = findViewById(R.id.linearLayoutKG);
        LinearLayout linearKG2 = findViewById(R.id.linearLayoutKG2);
        LinearLayout linearKG3 = findViewById(R.id.linearLayoutKG3);

        String message; // Initialize message
        if(position == 0){
            message = "LBS to KGS";
            pounds = true;
            linearLB.setVisibility(View.VISIBLE);
            linearLB2.setVisibility(View.VISIBLE);
            linearLB3.setVisibility(View.VISIBLE);
            lbBarBtn.setVisibility(View.VISIBLE);

            linearKG.setVisibility(View.GONE);
            linearKG2.setVisibility(View.GONE);
            linearKG3.setVisibility(View.GONE);
            kgBarBtn.setVisibility(View.GONE);
        } else {
            message = "KGS to LBS";
            pounds = false;
            linearKG.setVisibility(View.VISIBLE);
            linearKG2.setVisibility(View.VISIBLE);
            linearKG3.setVisibility(View.VISIBLE);
            kgBarBtn.setVisibility(View.VISIBLE);

            linearLB.setVisibility(View.GONE);
            linearLB2.setVisibility(View.GONE);
            linearLB3.setVisibility(View.GONE);
            lbBarBtn.setVisibility(View.GONE);
        }
        onClick(btnConvert);
        Toast.makeText(parent.getContext(), message, Toast.LENGTH_SHORT).show(); /// should be message
    }

    @Override public void onNothingSelected(AdapterView<?> parent) {} // Not Used

    // Convert new input to string and set it in the EditText input
    public void setNewWeight() {
        inputStr = Float.toString(input);
        inputWeight.setText(inputStr);
    }

    @Override
    public void onClick(View v) {
        int inputLen = inputStr.trim().length();
        if(inputLen != 0) {
            input = Float.parseFloat(inputStr);
        } else {
            input = 0f; // If the input is empty
        }

        switch (v.getId()) {
            case R.id.btnConvert:
                txtSolution = findViewById(R.id.txtSolution); // Full solution
                txtDisplayAnswer = findViewById(R.id.txtDisplayAnswer); // Rounded number
                String answer;
                String fullSolution;
                String rounded;

                if (inputLen == 0) {
                    String invalid = "Enter a value!";
                    Toast.makeText(getApplicationContext(), invalid, Toast.LENGTH_SHORT).show();
                } else {
                    if (pounds) { // If converting from Lbs to Kgs
                        // For txtSolution
                        solution = input / conversion;
                        answer = String.valueOf(solution);
                        fullSolution = answer + " KGS";
                        txtSolution.setText(fullSolution);

                        // For txtDisplayAnswer
                        roundedSoln = (int) solution;
                        int answerLen = answer.length();
                        char firstDecimal = '0'; // If solution is whole number, decimal is 0
                        for (int i = 0; i < answerLen; ++i) {
                            if (answer.charAt(i) == '.') firstDecimal = answer.charAt(i + 1);
                        }
                        rounded = roundedSoln + " KGS";
                        if (!(firstDecimal >= 48 && firstDecimal <= 52)) { // Using Ascii values
                            rounded = (roundedSoln + 1) + " KGS";
                        }
                        txtDisplayAnswer.setText(rounded); // Bigger, Integer Value
                    } else { // If converting from Kgs to Lbs
                        // For txtSolution
                        solution = input * conversion;
                        answer = String.valueOf(solution);
                        fullSolution = answer + " LBS";
                        txtSolution.setText(fullSolution);

                        // For txtDisplayAnswer
                        roundedSoln = (int) solution;
                        int answerLen = answer.length();
                        char firstDecimal = '0'; // If solution is whole number, decimal is 0
                        for (int i = 0; i < answerLen; ++i) {
                            if (answer.charAt(i) == '.') firstDecimal = answer.charAt(i + 1);
                        }
                        rounded = roundedSoln + " LBS";
                        if (!(firstDecimal >= 48 && firstDecimal <= 52)) {
                            rounded = (roundedSoln + 1) + " LBS";
                        }
                        txtDisplayAnswer.setText(rounded);
                    }
                }
                inputWeight.onEditorAction(EditorInfo.IME_ACTION_DONE); // When convert is pressed, lower keyboard
                break;
            case R.id.btnClear:
                input = 0f;
                break;
            case R.id.lb2_5Btn:
            case R.id.kg2_5Btn:
                input += 2.5f; // Add to existing input number value
                break;
            case R.id.lb5Btn:
            case R.id.kg5Btn:
                input += 5.0f;
                break;
            case R.id.lb10Btn:
            case R.id.kg10Btn:
                input += 10.0f;
                break;
            case R.id.kg15Btn:
                input += 15.0f;
                break;
            case R.id.kg20Btn:
            case R.id.kgBarBtn:
                input += 20.0f;
                break;
            case R.id.lb25Btn:
            case R.id.kg25Btn:
                input += 25.0f;
                break;
            case R.id.lb35Btn:
                input += 35.0f;
                break;
            case R.id.lb45Btn:
            case R.id.lbBarBtn:
                input += 45.0f;
                break;
            default:
                break;
        }
        setNewWeight();
    }
}
