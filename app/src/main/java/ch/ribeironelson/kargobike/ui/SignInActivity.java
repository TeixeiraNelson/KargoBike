package ch.ribeironelson.kargobike.ui;

import androidx.appcompat.app.AppCompatActivity;
import ch.ribeironelson.kargobike.R;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SignInActivity extends AppCompatActivity {

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

        if(!isAnyEditTextEmpty()){
            // TODO : CREATE USER AND INSERT IN DATABASE + FIREBASE
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
}
