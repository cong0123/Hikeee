package com.example.hikeee;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddObservationActivity extends AppCompatActivity {

    private long hikeId;

    private EditText contentEditText;
    private EditText timeEditText;
    private EditText weatherEditText;
    private EditText trailConditionEditText;
    final Calendar calendar = Calendar.getInstance();
    private ObservationDbHelper observationDbHelper;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);
        setTitle("Thêm quan sát");
        observationDbHelper = new ObservationDbHelper(this);

        // Lấy ID của chuyến đi từ Intent
        Intent intent = getIntent();
        hikeId = intent.getLongExtra("HikeId", 0);

        // Hiển thị ID chuyến đi trong giao diện
        TextView hikeIdTextView = findViewById(R.id.hikeIdTextView);
        hikeIdTextView.setText("Chuyến Đi ID: " + hikeId);

        // Ánh xạ các EditText
        contentEditText = findViewById(R.id.editTextContent);
        timeEditText = findViewById(R.id.editTextTime);
        timeEditText.setOnClickListener(v -> showDatePickerDialog());

        weatherEditText = findViewById(R.id.editTextWeather);
        trailConditionEditText = findViewById(R.id.editTextTrailCondition);

        // nút "Lưu"
        Button saveButton = findViewById(R.id.saveObservationButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveObservation();
            }
        });

        // Thiết lập nút quay về trang chi tiết chuyến đi
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void showDatePickerDialog() {
        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
            // Kiểm tra xem ngày đã chọn có phải là bây giờ hoặc tương lai không
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(year1, monthOfYear, dayOfMonth);

            if (selectedCalendar.after(calendar) || selectedCalendar.equals(calendar)) {
                // Lưu giữ ngày/tháng/năm
                selectedYear = year1;
                selectedMonth = monthOfYear;
                selectedDay = dayOfMonth;

                // Hiển thị TimePickerDialog sau khi chọn ngày
                showTimePickerDialog();
            } else {

                 Toast.makeText(this, "Vui lòng chọn ngày hiện tại hoặc tương lai", Toast.LENGTH_SHORT).show();

            }
        }, year, month, day);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        // Lấy giờ và phút hiện tại
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Tạo TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            // Kiểm tra xem ngày giờ đã chọn có phải là bây giờ hoặc tương lai không
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(selectedYear, selectedMonth, selectedDay, hourOfDay, minute1);

            if (selectedCalendar.after(calendar) || selectedCalendar.equals(calendar)) {
                // Lưu giữ giờ/phút
                selectedHour = hourOfDay;
                selectedMinute = minute1;

                // Hiển thị ngày và giờ đã chọn trên EditText
                timeEditText.setText(String.format(Locale.getDefault(), "%02d/%02d/%d %02d:%02d",
                        selectedDay, selectedMonth + 1, selectedYear, selectedHour, selectedMinute));
            } else {

               Toast.makeText(this, "Vui lòng chọn giờ hiện tại hoặc tương lai", Toast.LENGTH_SHORT).show();
            }
        }, hour, minute, true);

        // Hiển thị TimePickerDialog
        timePickerDialog.show();
    }



    private void saveObservation() {
        // Lấy thông tin từ EditText
        String content = contentEditText.getText().toString();
        String time = timeEditText.getText().toString();
        String weather = weatherEditText.getText().toString();
        String trailCondition = trailConditionEditText.getText().toString();

        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(time) || TextUtils.isEmpty(weather) || TextUtils.isEmpty(trailCondition)) {

          Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return; // Không tiếp tục xử lý nếu có trường trống
        }


        // Lưu thông tin quan sát vào cơ sở dữ liệu
        long observationId = observationDbHelper.addObservation(hikeId, content, time, weather, trailCondition);


        // Quay về trang chi tiết chuyến đi
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Xử lý khi nút "Home" được nhấn
                finish(); // Đóng Activity để quay lại trang trước đó
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
