package com.example.hikeee;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    private String selectedDate = "";
    private static final int ADD_OBSERVATION_REQUEST_CODE = 1;
    private Hike hike;
    private EditText[] editTextArray;
    private TextView[] textViewArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Chi tiết");
        // Ánh xạ các TextView và EditText trong layout
        editTextArray = new EditText[]{
                findViewById(R.id.nameEditText),
                findViewById(R.id.locationEditText),
                findViewById(R.id.dateEditText),
                findViewById(R.id.lengthEditText),
                findViewById(R.id.difficultyEditText),
                findViewById(R.id.descriptionEditText),
                findViewById(R.id.parkingAreaEditText),
                findViewById(R.id.ratingEditText),
                findViewById(R.id.restingSpotsEditText)
        };

        textViewArray = new TextView[]{
                findViewById(R.id.nameTextView),
                findViewById(R.id.locationTextView),
                findViewById(R.id.dateTextView),
                findViewById(R.id.lengthTextView),
                findViewById(R.id.difficultyTextView),
                findViewById(R.id.descriptionTextView),
                findViewById(R.id.parkingAreaTextView),
                findViewById(R.id.ratingTextView),
                findViewById(R.id.restingSpotsTextView)
        };

        Button saveButton = findViewById(R.id.saveButton);
        Button deleteButton = findViewById(R.id.deleteButton);

        // Nhận dữ liệu từ Intent
        hike = (Hike) getIntent().getSerializableExtra("Hike");

        // Hiển thị thông tin chuyến đi
        if (hike != null) {
            for (int i = 0; i < editTextArray.length; i++) {
                displayInfo(textViewArray[i], editTextArray[i], getFieldName(i), hike.getFieldValue(i));
            }

            // Thêm InputFilter cho lengthEditText
            InputFilter lengthFilter = new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    for (int i = start; i < end; i++) {
                        if (!Character.isDigit(source.charAt(i))) {
                            return "";
                        }
                    }
                    return null;
                }
            };

            editTextArray[3].setFilters(new InputFilter[]{lengthFilter});

            // Set up click listener for dateEditText
            editTextArray[2].setFocusable(false);
            editTextArray[2].setOnClickListener(v -> showDatePickerDialog());

            // Set up click listener for difficultyEditText
            editTextArray[4].setFocusable(false);
            editTextArray[4].setOnClickListener(v -> showDifficultyDialog());

            editTextArray[6].setFocusable(false);
            editTextArray[6].setOnClickListener(v -> showYesNoDialog("Khu đậu xe", editTextArray[6]));
            editTextArray[7].setFocusable(false);
            editTextArray[7].setOnClickListener(v -> showRatingDialog(editTextArray[7]));


            editTextArray[8].setFocusable(false);
            editTextArray[8].setOnClickListener(v -> showYesNoDialog("Nơi nghỉ ngơi", editTextArray[8]));

            saveButton.setOnClickListener(v -> saveHikeInfo());

            deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog());
            // Thiết lập nút quay về trang Home trên thanh tiêu đề
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        } else {
            // Chế độ thêm chuyến đi mới
            hike = new Hike("", "", "", "", "", "", "", "", "");
        }
        Button addObservationButton = findViewById(R.id.addObservationButton);
        addObservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addObservationIntent = new Intent(DetailActivity.this, AddObservationActivity.class);
                addObservationIntent.putExtra("HikeId", hike.getId());
                startActivityForResult(addObservationIntent, ADD_OBSERVATION_REQUEST_CODE);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_OBSERVATION_REQUEST_CODE && resultCode == RESULT_OK) {

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            // Xử lý khi người dùng nhấn nút quay lại
            backToHome();
            return true;
        } else if (itemId == R.id.action_view_observations) {
            // Xử lý khi người dùng nhấn nút quan sát
            Intent intent = new Intent(DetailActivity.this, ObservationActivity.class);
            intent.putExtra("HikeId", hike.getId());
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }




    private void backToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish(); // Đóng hiện tại Activity để tránh quay lại nó khi nhấn nút "Back"
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn xóa chuyến đi này không?")
                .setPositiveButton("Có", (dialog, id) -> {
                    // Người dùng đã chọn "Có", thực hiện xóa và gửi thông báo về trang chủ để cập nhật dữ liệu
                    deleteHike();
                    sendResultToHome();
                })
                .setNegativeButton("Không", (dialog, id) -> {
                    // Người dùng đã chọn "Không", đóng hộp thoại
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showYesNoDialog(String fieldName, EditText editText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn " + fieldName);

        // Danh sách các lựa chọn
        final String[] options = {"Yes", "No"};

        builder.setItems(options, (dialog, which) -> {
            String selectedOption = options[which];
            editText.setText(selectedOption);
        });

        builder.show();
    }
    private void showRatingDialog(EditText editText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn đánh giá");

        final String[] ratingOptions = {"0.5", "1.0", "1.5", "2.0", "2.5", "3.0", "3.5", "4.0", "4.5", "5.0"};

        builder.setItems(ratingOptions, (dialog, which) -> {
            // Xử lý khi người dùng chọn một lựa chọn
            String selectedRating = ratingOptions[which];
            editText.setText(selectedRating);
        });

        // Hiển thị AlertDialog
        builder.show();
    }

    private void deleteHike() {
        HikeDbHelper dbHelper = new HikeDbHelper(this);
        dbHelper.deleteHike(hike.getId());
    }

    private void sendResultToHome() {
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void displayInfo(TextView textView, EditText editText, String fieldName, String fieldValue) {
        // Hiển thị tên trường trên TextView và dữ liệu trên EditText
        textView.setText(fieldName + " : ");
        editText.setText(fieldValue);
        textView.setVisibility(View.VISIBLE);
        editText.setVisibility(View.VISIBLE);
    }

    private void saveHikeInfo() {
        // Lưu thông tin đã sửa vào  Hike
        for (int i = 0; i < editTextArray.length; i++) {
            hike.setFieldValue(i, editTextArray[i].getText().toString());
        }

        HikeDbHelper dbHelper = new HikeDbHelper(this);

        // Kiểm tra nếu là chuyến đi mới thì thêm vào cơ sở dữ liệu, ngược lại cập nhật
        if (hike.getId() == 0) {
            long insertedId = dbHelper.addHike(hike);
            hike.setId(insertedId);
        } else {
            dbHelper.updateHike(hike);
        }

        // Gửi thông báo về trang chủ để cập nhật dữ liệu
        sendResultToHome();
    }


    private String getFieldName(int index) {
        // Hàm trả về tên của trường tương ứng với chỉ mục
        switch (index) {
            case 0:
                return "Tên chuyến đi";
            case 1:
                return "Địa điểm";
            case 2:
                return "Ngày";
            case 3:
                return "Chiều dài (m)";
            case 4:
                return "Độ khó";
            case 5:
                return "Mô tả";
            case 6:
                return "Khu đậu xe";
            case 7:
                return "Đánh giá";
            case 8:
                return "Nơi nghỉ ngơi";
            default:
                return "";
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
            selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year1);
            editTextArray[2].setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showDifficultyDialog() {
        final String[] difficultyLevels = {"Dễ", "Vừa phải", "Khó", "Chuyên gia"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn độ khó");
        builder.setItems(difficultyLevels, (dialog, which) -> {
            // Xử lý khi người dùng chọn một mức độ khó
            String selectedDifficulty = difficultyLevels[which];
            editTextArray[4].setText(selectedDifficulty);
        });

        builder.show();
    }
}
