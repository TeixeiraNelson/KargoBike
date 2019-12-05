package ch.ribeironelson.kargobike.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.database.entity.CheckpointEntity;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.database.entity.UserEntity;
import ch.ribeironelson.kargobike.database.repository.CheckpointRepository;
import ch.ribeironelson.kargobike.database.repository.UserRepository;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;
import ch.ribeironelson.kargobike.viewmodel.UsersListViewModel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    static final int GOOGLE_SIGN = 123;
    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private static GoogleSignInClient mGoogleSignInClient;

    private Button kargobikeLogin;
    private Button googleLoginBtn;
    private Button logoutBtn;

    private RelativeLayout kargobike_login_form;
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView signinTxtview;
    private TextView forgotPassword;

    private ProgressBar progressBar;

    private UsersListViewModel usersListViewModel;
    private List<UserEntity> users;
    private UserEntity userToAdd;


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
        signinTxtview = findViewById(R.id.signin_txtview);
        forgotPassword = findViewById(R.id.forgotpass_txtview);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        googleLoginBtn.setOnClickListener(v -> SignInGoogle());
        kargobikeLogin.setOnClickListener(v -> KargoBikeLogin());
        signinTxtview.setOnClickListener(v -> startSignInForm());
        forgotPassword.setOnClickListener(v -> forgotPassword());

        //Get the list of all users to check it
        UsersListViewModel.Factory factory = new UsersListViewModel.Factory(
                getApplication());
        usersListViewModel = ViewModelProviders.of(this, factory).get(UsersListViewModel.class);
        usersListViewModel.getAllUsers().observe(this, userEntities -> {
            if (userEntities != null) {
                users = userEntities;
            }
        });
    }

    private void forgotPassword() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Password reset");
        alert.setMessage("Please enter your email address");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setHint("Example@test.com");
        alert.setView(input);

        alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = input.getText().toString();
                Toast.makeText(LoginActivity.this, "An email to reset your password has been sent !",
                        Toast.LENGTH_SHORT).show();
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Password reset successfully sent to : " + emailAddress);
                                }
                            }
                        });

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        final AlertDialog dialog = alert.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button negativeButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                Button positiveButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);

                negativeButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                positiveButton.setTextColor(getResources().getColor(R.color.colorPrimary));


                negativeButton.invalidate();
                positiveButton.invalidate();
            }
        });

        dialog.show();
    }

    private void startSignInForm(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);

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

            if(email.length() < 1){
                emailEditText.setError("You must enter an email !");
                return;
            }

            if(password.length() < 1){
                passwordEditText.setError("You must enter a password !");
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.INVISIBLE);
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                logUserAuthentication(user);
                                addUserToDB(user);
                                startActivity();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Wrong email and/or password !", Toast.LENGTH_SHORT).show();
                                logUserAuthentication(null);
                            }

                            // ...
                        }
                    });
        }

    }

    private void startActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
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

                        addUserToDB(user);

                        startActivity();

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

    private void addUserToDB(FirebaseUser user) {
        userToAdd = new UserEntity();
        userToAdd.setIdUser(user.getUid());
        userToAdd.setEmail(user.getEmail());
        userToAdd.setPhoneNumber(user.getPhoneNumber());
        userToAdd.setFirstname(user.getDisplayName().substring(0,user.getDisplayName().indexOf(" ")));
        userToAdd.setLastname(user.getDisplayName().substring(user.getDisplayName().indexOf(" ")+1));

        if(!isUserAlreadyRegisterInDB(userToAdd.getEmail())) {
            UserRepository.getInstance().insertUID(userToAdd,user.getUid(), new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Insert user: "+userToAdd.getFirstname()+" in database : success");
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "Insert user in database : failure");
                }
            });
        }
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


    public static void Logout() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut();
    }

    private boolean isUserAlreadyRegisterInDB(String email){

        for(int i = 0 ; i != users.size() ; i++){
            if(users.get(i).getEmail().equals(email)){
                return true ;
            }
        }
        return false ;
    }

}
