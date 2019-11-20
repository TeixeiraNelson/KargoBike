package ch.ribeironelson.kargobike.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import ch.ribeironelson.kargobike.R;

import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected FrameLayout frameLayout;

    protected DrawerLayout drawerLayout;

    protected NavigationView navigationView;

    protected static int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        frameLayout = findViewById(R.id.flContent);

        drawerLayout = findViewById(R.id.base_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.base_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        BaseActivity.position = 0;
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == BaseActivity.position) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }
        BaseActivity.position = id;
        Intent intent = null;

        navigationView.setCheckedItem(id);

        if (id == R.id.nav_home) {
            intent=new Intent (BaseActivity.this, MainActivity.class);

        } else if (id == R.id.nav_shops) {
            intent=new Intent (BaseActivity.this, About.class);
        } else if (id == R.id.nav_articles) {
            intent=new Intent (BaseActivity.this, About.class);
        } else if (id == R.id.nav_about) {
            intent=new Intent (BaseActivity.this, About.class);
        }
        if (intent != null) {
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION
            );
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
