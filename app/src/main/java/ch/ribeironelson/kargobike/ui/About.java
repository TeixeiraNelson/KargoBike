package ch.ribeironelson.kargobike.ui;

import ch.ribeironelson.kargobike.R;

import android.os.Bundle;

public class About extends BaseActivity {
    private static final String TAG = "About Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_about, frameLayout);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
