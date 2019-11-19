package ch.ribeironelson.kargobike.ui;

import androidx.appcompat.app.AppCompatActivity;
import ch.ribeironelson.kargobike.R;

import android.os.Bundle;

public class About extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.about);
        navigationView.setCheckedItem(R.id.nav_about);
        getLayoutInflater().inflate(R.layout.activity_about, frameLayout);
    }
}
