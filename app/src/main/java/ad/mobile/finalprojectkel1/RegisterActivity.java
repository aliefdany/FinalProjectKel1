package ad.mobile.finalprojectkel1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;

    private TextView loginTextButton;

    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;

    private Button btDaftar;

    private Button btGoogle;
    private ActivityResultLauncher<IntentSenderRequest> launcher;

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private View backdropLoading;

    private String url = "https://final-project-papb-de61c-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private DatabaseReference db;
    private DatabaseReference appDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.loginTextButton = findViewById(R.id.tvMasuk);
        this.etEmail = findViewById(R.id.etEmailRegister);
        this.etPassword = findViewById(R.id.etPasswordRegister);
        this.etConfirmPassword = findViewById(R.id.etConfirmPassword);

        this.btDaftar = findViewById(R.id.btDaftar);
        this.btDaftar.setOnClickListener(this);

        this.btGoogle = findViewById(R.id.btGoogleRegister);
        this.btGoogle.setOnClickListener(this);

        this.backdropLoading = findViewById(R.id.loadingPanel);

        this.db = FirebaseDatabase.getInstance(url).getReference();

        this.appDb = this.db.child("user");


        Context ctx = this;
        this.loginTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, LoginActivity.class);
                startActivity(i);
            }
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        this.launcher = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK){
                        try {
                            SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                            String idToken = credential.getGoogleIdToken();

                            backdropLoading.setVisibility(View.VISIBLE);
                            if (idToken != null) {
                                AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                                mAuth.signInWithCredential(firebaseCredential)
                                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    Log.d("TAG", "signInWithCredential:success");
                                                    FirebaseUser user = mAuth.getCurrentUser();

                                                    String id = UUID.randomUUID().toString();

                                                    User dbUser = new User("","","","", user.getEmail(), id);
                                                    appDb.child(id).setValue(dbUser);

                                                    updateUI(user);

                                                    backdropLoading.setVisibility(View.GONE);
                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    Log.w("TAG", "signInWithCredential:failure", task.getException());
                                                    updateUI(null);
                                                    backdropLoading.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                            }
                        } catch (ApiException e) {
                            // ...
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateUI(currentUser);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btDaftar) {
            mAuth.createUserWithEmailAndPassword(this.etEmail.getText().toString(), this.etPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                String id = UUID.randomUUID().toString();

                                User dbUser = new User("","","","", user.getEmail(), id);
                                appDb.child(id).setValue(dbUser);

                                updateUI(user);
                                backdropLoading.setVisibility(View.GONE);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                                backdropLoading.setVisibility(View.GONE);
                            }
                        }
                    });

            this.backdropLoading.setVisibility(View.VISIBLE);
        } else if(v.getId() == R.id.btGoogleRegister) {
            Log.d("Whatt", "hello");
            oneTapClient = Identity.getSignInClient(RegisterActivity.this);
            signInRequest = BeginSignInRequest.builder()
                    .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                            .setSupported(true)
                            // Your server's client ID, not your Android client ID.
                            .setServerClientId(getString(R.string.default_web_client_id))
                            // Show all accounts on the device.
                            .setFilterByAuthorizedAccounts(false)
                            .build())
                    .build();

            oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener(RegisterActivity.this, new OnSuccessListener<BeginSignInResult>() {
                        @Override
                        public void onSuccess(BeginSignInResult result) {
                            launcher.launch(new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build());
                            backdropLoading.setVisibility(View.GONE);
                        }
                    })
                    .addOnFailureListener(RegisterActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // No Google Accounts found. Just continue presenting the signed-out UI.
                            Log.d("TAG", e.getLocalizedMessage());
                            backdropLoading.setVisibility(View.GONE);
                        }
                    });

            this.backdropLoading.setVisibility(View.VISIBLE);


        }


    }

    public void updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this,"You Signed In successfully",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,MainActivity.class));

        }else {
            Toast.makeText(this,"You Didnt signed in",Toast.LENGTH_LONG).show();
        }

    }
}