package ch.ribeironelson.kargobike.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.database.entity.UserEntity;
import ch.ribeironelson.kargobike.viewmodel.UserViewModel;

public class AccountActivity extends AppCompatActivity {

    private UserViewModel viewModel;

    private UserEntity user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("List of users");
        setContentView(R.layout.activity_recyclerview);



        UserViewModel.Factory factory = new UserViewModel.Factory(
                getApplication(),
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );
        viewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);
        viewModel.getUser().observe(this, accountEntity -> {
            if (accountEntity != null) {
                user = accountEntity;
            }
        });
    }
}
