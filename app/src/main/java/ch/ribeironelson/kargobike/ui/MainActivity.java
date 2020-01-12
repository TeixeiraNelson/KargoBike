package ch.ribeironelson.kargobike.ui;

import ch.ribeironelson.kargobike.R;

import android.os.Bundle;

public class MainActivity extends BaseActivity {
    private static final String TAG = "Main Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
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
