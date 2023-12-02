package com.example.hikeee;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.List;

public class ObservationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ObservationAdapter observationAdapter;
    private List<Observation> observationList;
    private ObservationDbHelper dbHelper;
    private long hikeId;
    private static final int EDIT_OBSERVATION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);
        setTitle("Observation");
        // Khởi tạo RecyclerView và Adapter
        recyclerView = findViewById(R.id.recyclerViewObservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        observationAdapter = new ObservationAdapter(this);

        // Lấy danh sách quan sát từ cơ sở dữ liệu
        dbHelper = new ObservationDbHelper(this);
        hikeId = getIntent().getLongExtra("HikeId", -1); // Lấy ID chuyến đi từ Intent
        observationList = dbHelper.getObservationsForHike(hikeId);

        // Hiển thị danh sách quan sát trong RecyclerView
        observationAdapter.setObservationList(observationList);
        recyclerView.setAdapter(observationAdapter);

        // Thiết lập nút quay về trang chi tiết chuyến đi
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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

   //update
    private void updateObservationList() {
        ObservationDbHelper dbHelper = new ObservationDbHelper(this);
        long hikeId = getIntent().getLongExtra("HikeId", -1);
        observationList = dbHelper.getObservationsForHike(hikeId);

        observationAdapter.setObservationList(observationList);
        observationAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateObservationList();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_OBSERVATION_REQUEST_CODE && resultCode == RESULT_OK) {
            updateObservationList();
        }
    }

}
