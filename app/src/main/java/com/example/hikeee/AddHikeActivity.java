package com.example.hikeee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddHikeActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText locationEditText;
    private EditText dateEditText;
    private EditText lengthEditText;
    private EditText difficultyEditText;
    private EditText descriptionEditText;

    private RadioGroup radioGroupParking;
    private RatingBar tripRatingBar;
    private RadioGroup radioGroupRestCamp;

    private HikeDbHelper hikeDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hike);
        setTitle("Add Hike");
        hikeDbHelper = new HikeDbHelper(this);

        nameEditText = findViewById(R.id.editTextName);
        locationEditText = findViewById(R.id.editTextLocation);
        dateEditText = findViewById(R.id.editTextDate);
        lengthEditText = findViewById(R.id.editTextLength);
        difficultyEditText = findViewById(R.id.editTextDifficulty);
        descriptionEditText = findViewById(R.id.editTextDescription);

        radioGroupParking = findViewById(R.id.radioGroupParking);
        tripRatingBar = findViewById(R.id.ratingBarTrip);
        radioGroupRestCamp = findViewById(R.id.radioGroupRestCamp);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveHike());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        dateEditText.setFocusable(false);
        dateEditText.setOnClickListener(v -> showDatePicker());

        difficultyEditText.setFocusable(false);
        difficultyEditText.setOnClickListener(v -> showDifficultyDialog());

        lengthEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing needed here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Nothing needed here
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().replaceAll("[^\\d.]", ""); // Chỉ giữ số và dấu chấm
                lengthEditText.removeTextChangedListener(this);
                lengthEditText.setText(text);
                lengthEditText.setSelection(text.length());
                lengthEditText.addTextChangedListener(this);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Xử lý khi nút "Home" được nhấn
                backToHome();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void backToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish(); // Đóng hiện tại Activity để tránh quay lại nó khi nhấn nút "Back"
    }
    private void saveHike() {
        String name = nameEditText.getText().toString();
        String location = locationEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String length = lengthEditText.getText().toString();
        String difficulty = difficultyEditText.getText().toString();

        if (name.isEmpty() || location.isEmpty() || date.isEmpty() || length.isEmpty() || difficulty.isEmpty()) {
            Toast.makeText(this, "Please complete all information", Toast.LENGTH_SHORT).show();
            return;
        }

        String description = descriptionEditText.getText().toString();

        int selectedParkingId = radioGroupParking.getCheckedRadioButtonId();
        RadioButton radioButtonSelectedParking = findViewById(selectedParkingId);
        String parkingArea = (radioButtonSelectedParking != null) ? radioButtonSelectedParking.getText().toString() : "No";

        int selectedRestCampId = radioGroupRestCamp.getCheckedRadioButtonId();
        RadioButton radioButtonSelectedRestCamp = findViewById(selectedRestCampId);
        String restingSpots = (radioButtonSelectedRestCamp != null) ? radioButtonSelectedRestCamp.getText().toString() : "No";

        String rating = String.valueOf(tripRatingBar.getRating());

        Hike hike = new Hike(name, location, date, length, difficulty, description, parkingArea, rating, restingSpots);
        hikeDbHelper.addHike(hike);

        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, dayOfMonth);

                    if (selected.after(calendar) || selected.equals(calendar)) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        dateEditText.setText(selectedDate);
                    } else {
                        Toast.makeText(AddHikeActivity.this, "Please select current or future date", Toast.LENGTH_SHORT).show();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showDifficultyDialog() {
        String[] difficultyLevels = {"Easy", "Normal", "Hard", "Professional"};
        showListDialog("Choose difficulty", difficultyLevels, difficultyEditText);
    }

    private void showListDialog(String title, String[] listItems, EditText targetEditText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setItems(listItems, (dialog, which) -> {
                    String selected = listItems[which];
                    targetEditText.setText(selected);
                });

        builder.create().show();
    }

}