package ch.ribeironelson.kargobike.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.database.entity.UserEntity;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;
import ch.ribeironelson.kargobike.viewmodel.UserViewModel;

public class AccountActivity extends AppCompatActivity {
    private static final String TAG = "UserActivity";

    private boolean isEditable;

    private EditText lastname;
    private EditText firstname;
    private EditText email;
    private Spinner role;

    private UserViewModel viewModel;
    private UserEntity user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Users");
        setContentView(R.layout.activity_recyclerview);

        initiateView();

        UserViewModel.Factory factory = new UserViewModel.Factory(
                getApplication(),
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );
        viewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);
        viewModel.getUser().observe(this, accountEntity -> {
            if (accountEntity != null) {
                user = accountEntity;
                updateContent();
            }
        });

    }
    private void initiateView() {
        isEditable=false;
        firstname = findViewById(R.id.FirstnameData);
        lastname = findViewById(R.id.LastnameData);
        email = findViewById(R.id.EmailData);
        role = findViewById(R.id.RoleSpinner);
    }

    private void switchEditableMode() {
        if (!isEditable) {
            LinearLayout linearLayout = findViewById(R.id.recyclerView);
            linearLayout.setVisibility(View.VISIBLE);
            firstname.setEnabled(true);
            lastname.setEnabled(true);
            email.setEnabled(true);
            role.setEnabled(true);

        } else {
            saveChanges(
                    firstname.getText().toString(),
                    lastname.getText().toString(),
                    email.getText().toString(),
                    role.getSelectedItem().toString()
            );
            LinearLayout linearLayout = findViewById(R.id.recyclerView);
            firstname.setEnabled(false);
            lastname.setEnabled(false);
            email.setEnabled(false);
            role.setEnabled(false);

        }
        isEditable = !isEditable;
    }

    private void saveChanges(String firstname, String lastname, String email, String role) {

        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        //user.setIdRole(role);

        viewModel.updateUser(user, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "update: success");
                setResponse(true);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "update: failure", e);
                setResponse(false);
            }
        });
    }
    private void setResponse(Boolean response) {
        if (response) {
            updateContent();
        }
    }
    private void updateContent() {
        if (user != null) {
            firstname.setText(user.getFirstname());
            lastname.setText(user.getLastname());
            email.setText(user.getEmail());
            //role à faire
        }
    }

}
