package com.example.hikeee;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class EditObservationActivity extends AppCompatActivity {
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    private Observation observation;
    private EditText contentEditText, timeEditText, weatherEditText, trailConditionEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_observation);
        setTitle("Edit Observation");
        contentEditText = findViewById(R.id.editContent);
        timeEditText = findViewById(R.id.editTime);
        timeEditText.setOnClickListener(v -> showDatePickerDialog());
        weatherEditText = findViewById(R.id.editWeather);
        trailConditionEditText = findViewById(R.id.editTrailCondition);
        Button saveButton = findViewById(R.id.saveButton);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("Observation")) {
            observation = (Observation) intent.getSerializableExtra("Observation");

            // Hiển thị dữ liệu trên giao diện người dùng
            contentEditText.setText(observation.getContent());
            timeEditText.setText(observation.getTime());
            weatherEditText.setText(observation.getWeather());
            trailConditionEditText.setText(observation.getTrailCondition());
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Sự kiện khi nhấn nút lưu
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateObservation();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Xử lý khi nút "Home" được nhấn
                finish(); // Đóng Activity, quay lại trang trước đó
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void showDatePickerDialog() {
        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
            // Lưu giữ ngày/tháng/năm
            selectedYear = year1;
            selectedMonth = monthOfYear;
            selectedDay = dayOfMonth;

            // Hiển thị TimePickerDialog sau khi chọn
            showTimePickerDialog();
        }, year, month, day);

        datePickerDialog.show();
    }
    private void showTimePickerDialog() {
        // Lấy giờ và phút hiện tại
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Tạo TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            // Lưu giữ giờ/phút
            selectedHour = hourOfDay;
            selectedMinute = minute1;

            // Hiển thị ngày và giờ đã chọn trên EditText
            timeEditText.setText(String.format(Locale.getDefault(), "%02d/%02d/%d %02d:%02d",
                    selectedDay, selectedMonth + 1, selectedYear, selectedHour, selectedMinute));
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void updateObservation() {
        // Lấy dữ liệu từ các trường nhập
        String content = contentEditText.getText().toString();
        String time = timeEditText.getText().toString();
        String weather = weatherEditText.getText().toString();
        String trailCondition = trailConditionEditText.getText().toString();


        if (content.trim().isEmpty() || weather.trim().isEmpty() || trailCondition.trim().isEmpty()) {

            Toast.makeText(EditObservationActivity.this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return; // Thoát khỏi phương thức nếu một trong các trường trống
        }

        // Cập nhật dữ liệu quan sát
        observation.setContent(content);
        observation.setTime(time);
        observation.setWeather(weather);
        observation.setTrailCondition(trailCondition);

        // Cập nhật vào database
        ObservationDbHelper dbHelper = new ObservationDbHelper(this);
        dbHelper.updateObservation(observation);

        finish();
    }

}
