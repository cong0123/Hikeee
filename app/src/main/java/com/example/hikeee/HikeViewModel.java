package com.example.hikeee;

// HikeViewModel.java
import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class HikeViewModel extends AndroidViewModel {

    private HikeDbHelper hikeDbHelper;
    private LiveData<List<Hike>> hikesLiveData;

    public HikeViewModel(Application application) {
        super(application);
        hikeDbHelper = new HikeDbHelper(application);
        hikesLiveData = hikeDbHelper.getAllHikesLiveData();
    }

    LiveData<List<Hike>> getHikesLiveData() {
        return hikesLiveData;
    }

    public void insertHike(Hike hike) {
        hikeDbHelper.addHike(hike);
    }
}


