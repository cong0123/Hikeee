package com.example.hikeee;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private HikeAdapter adapter;
    private HikeDbHelper hikeDbHelper;
    private List<Hike> hikes;

    private static final int REQUEST_CODE_ADD_HIKE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        hikeDbHelper = new HikeDbHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        hikes = hikeDbHelper.getAllHikes();

        adapter = new HikeAdapter(this, hikes);
        recyclerView.setAdapter(adapter);

        // Thiết lập thanh tìm kiếm


        if (!hikes.isEmpty()) {
            adapter.setOnItemClickListener(position -> {
                if (position >= 0 && position < hikes.size()) {
                    Hike clickedHike = hikes.get(position);

                    Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
                    intent.putExtra("Hike", clickedHike);
                    startActivity(intent);
                }
            });
        }

        FloatingActionButton addHikeButton = findViewById(R.id.addHikeButton);
        addHikeButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddHikeActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_HIKE);
        });
        ImageButton compassButton = findViewById(R.id.imageButton);
        compassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện khi người dùng nhấn vào nút "La Bàn"
                openCompassActivity();
            }
        });
    }
    private void openCompassActivity() {

        Intent compassIntent = new Intent(this, CompassActivity.class);
        startActivity(compassIntent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater() .inflate(R.menu.menu_home, menu);
        SearchManager searchManager = (SearchManager)   getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_HIKE && resultCode == RESULT_OK) {
            updateData();
        }
    }

    private void updateData() {
        // Lấy lại dữ liệu mới và cập nhật RecyclerView hoặc adapter
        List<Hike> updatedData = hikeDbHelper.getAllHikes();
        adapter.updateData(updatedData);

        // Cập nhật danh sách hikes với dữ liệu mới
        hikes.clear();
        hikes.addAll(updatedData);

        // Thông báo cho adapter cần cập nhật
        adapter.notifyDataSetChanged();
    }



    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }



}
