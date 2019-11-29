package ch.ribeironelson.kargobike.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.database.entity.UserEntity;
import ch.ribeironelson.kargobike.database.repository.UserRepository;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class SignInActivity extends AppCompatActivity {

    private final String TAG = "SignInActivity";

    private EditText firstname_et;
    private EditText lastname_et;
    private EditText phone_et;
    private EditText email_et;
    private EditText password_et;
    private EditText password2_et;

    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String password;
    private String password2;

    private UserEntity userToAdd ;

    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Kargo Bike");
        getSupportActionBar().setSubtitle("Register here");

        firstname_et = findViewById(R.id.kargobike_reg_firstname);
        lastname_et = findViewById(R.id.kargobike_reg_lastname);
        phone_et = findViewById(R.id.kargobike_reg_phone);
        email_et = findViewById(R.id.kargobike_reg_email);
        password_et = findViewById(R.id.kargobike_reg_password);
        password2_et = findViewById(R.id.kargobike_reg_password2);

        UserEntity userToAdd ;

        registerBtn = findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(v -> verifyUserInputs());
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void verifyUserInputs(){
        firstname = firstname_et.getText().toString();
        lastname = lastname_et.getText().toString();
        phone = phone_et.getText().toString();
        email = email_et.getText().toString();
        password = password_et.getText().toString();
        password2 = password2_et.getText().toString();

        userToAdd = new UserEntity(firstname, lastname, email, phone );

        System.out.println("edit text :"+isAnyEditTextEmpty());
        System.out.println("is existing:"+isUserAlreadyRegister(userToAdd.getEmail()));

        if(!isAnyEditTextEmpty() && !isUserAlreadyRegister(userToAdd.getEmail())){
            UserRepository.getInstance().insert(userToAdd, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Insert user : success");
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "Insert user : failure");
                }
            });
        }if(isUserAlreadyRegister(userToAdd.getEmail())){
            Toast.makeText(this, getString(R.string.error_user_already_exist), Toast.LENGTH_LONG ).show();
        }if(isAnyEditTextEmpty()){
            Toast.makeText(this, getString(R.string.error_text_empty), Toast.LENGTH_LONG).show();
        }

    }

    private boolean isAnyEditTextEmpty() {
        if(firstname.length()<1){
            firstname_et.setError("You must enter a first name !");
            return true;
        }

        if(lastname.length()<1){
            lastname_et.setError("You must enter a last name !");
            return true;
        }

        if(phone.length()<1){
            phone_et.setError("You must enter a phone number !");
            return true;
        }

        if(email.length()<1){
            email_et.setError("You must enter an email !");
            return true;
        }

        if(password.length()<1){
            password_et.setError("You must enter a password !");
            return true;
        }

        if(password2.length()<1){
            password2_et.setError("You must enter a password confirmation !");
            return true;
        }

        return false;
    }

    private boolean isUserAlreadyRegister(String email){
        LiveData<List<UserEntity>> users =  UserRepository.getInstance().getAllUsers();
        List<UserEntity> usersList = users.getValue() ;

        if(usersList != null){
                return usersList.contains(email);
        }

        return false ;
    }
}
