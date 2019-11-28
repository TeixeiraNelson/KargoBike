package ch.ribeironelson.kargobike.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.util.DeliveriesRecyclerViewAdapter;
import ch.ribeironelson.kargobike.viewmodel.DeliveriesListViewModel;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.util.List;

public class About extends BaseActivity {
    private static final String TAG = "About Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_about, frameLayout);
        navigationView.setCheckedItem(R.id.nav_about);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_home);
    }
}
