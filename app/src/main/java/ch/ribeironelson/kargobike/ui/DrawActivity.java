package ch.ribeironelson.kargobike.ui;

import androidx.appcompat.app.AppCompatActivity;
import ch.ribeironelson.kargobike.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.io.ByteArrayOutputStream;

public class DrawActivity extends AppCompatActivity {

    PaintView paintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        paintView = findViewById(R.id.paint_view);
        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);
        paintView.init(dm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mn= getMenuInflater();
        mn.inflate(R.menu.drawable_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.finish:
                returnSignatureAsResult();
                return true;
            case R.id.restart:
                paintView.clear();
                return true;
            case R.id.cancel:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void returnSignatureAsResult() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        paintView.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Intent intent=new Intent();
        intent.putExtra("data",byteArray);
        setResult(RESULT_OK, intent);

        finish();
    }
}
