package ch.ribeironelson.kargobike.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import ch.ribeironelson.kargobike.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    static final int GOOGLE_SIGN = 123;
    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private Button kargobikeLogin;
    private Button googleLoginBtn;
    private Button logoutBtn;

    private RelativeLayout kargobike_login_form;
    private EditText emailEditText;
    private EditText passwordEditText;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        googleLoginBtn = findViewById(R.id.login_google_btn);
        progressBar = findViewById(R.id.progress_circular);
        logoutBtn = findViewById(R.id.logout_btn);
        kargobikeLogin = findViewById(R.id.login_kargobike);
        kargobike_login_form = findViewById(R.id.lgn_form_layout);
        emailEditText = findViewById(R.id.kargobike_lgn_email);
        passwordEditText = findViewById(R.id.kargobike_lgn_password);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        googleLoginBtn.setOnClickListener(v -> SignInGoogle());
        logoutBtn.setOnClickListener(v -> Logout());
        kargobikeLogin.setOnClickListener(v -> KargoBikeLogin());
    }

    private void KargoBikeLogin() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(kargobike_login_form.getVisibility() == View.INVISIBLE){
            kargobike_login_form.setAlpha(0f);
            kargobike_login_form.setVisibility(View.VISIBLE);
            kargobike_login_form.animate()
                    .alpha(1f)
                    .setDuration(1000);
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(LoginActivity.this, "Authentication success.",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                logUserAuthentication(user);
                                startLoginAnimation();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                logUserAuthentication(null);
                            }

                            // ...
                        }
                    });
        }

    }

    private void startLoginAnimation() {
       
    }

    void SignInGoogle(){
        progressBar.setVisibility(View.VISIBLE);

        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, GOOGLE_SIGN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GOOGLE_SIGN){

            if(resultCode == RESULT_OK){

                Task<GoogleSignInAccount> task = GoogleSignIn
                        .getSignedInAccountFromIntent(data);
                try {

                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    if(account != null){
                        firebaseAuthWithGoogle(account);
                    }
                } catch (ApiException e){
                    e.printStackTrace();
                }
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                logoutBtn.setVisibility(View.INVISIBLE);
            }

        }



    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "FirebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        Log.d(TAG, "Signin success !");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Signin Sucess ! " + user.getEmail() , Toast.LENGTH_SHORT).show();
                        logUserAuthentication(user);

                        progressBar.setVisibility(View.INVISIBLE);
                        logoutBtn.setVisibility(View.VISIBLE);
                    } else {

                        Log.w(TAG,"Signin failure !");
                        Toast.makeText(this, "Signin failed !", Toast.LENGTH_SHORT).show();
                        logUserAuthentication(null);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void logUserAuthentication(FirebaseUser user) {
        if(user != null ){
            String name = user.getDisplayName();
            String email = user.getEmail();

            Log.d(TAG," SIGN IN : " + name + " - " + email);
        } else {
            Log.d(TAG,"Authentication attempt failed!");
        }
    }


    private void Logout() {
        logoutBtn.setVisibility(View.INVISIBLE);
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> logUserAuthentication(null));
    }

}
