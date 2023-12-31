package ad.mobile.finalprojectkel1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;


    private FirebaseAuth mAuth;
    private TextView loginTextButton;
    private EditText etEmail;
    private EditText etPassword;
    private Button btMasuk;
    private Button btGoogle;
    private TextView tvDaftar;

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    private ActivityResultLauncher<IntentSenderRequest> launcher;
    private View backdropLoading;

    private String url = "https://final-project-papb-de61c-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private DatabaseReference db;
    private DatabaseReference appDb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.etEmail = findViewById(R.id.etEmailLogin);
        this.etPassword = findViewById(R.id.etPasswordLogin);

        this.btMasuk = findViewById(R.id.btDaftar);
        this.btMasuk.setOnClickListener(this);

        this.btGoogle = findViewById(R.id.btGoogleLogin);
        this.btGoogle.setOnClickListener(this);

        this.backdropLoading = findViewById(R.id.loadingPanel);



        this.tvDaftar = findViewById(R.id.tvDaftar);

        this.tvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        this.db = FirebaseDatabase.getInstance(url).getReference();

        this.appDb = this.db.child("user");

        this.launcher = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK){
                        try {
                            backdropLoading.setVisibility(View.VISIBLE);
                            SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                            String idToken = credential.getGoogleIdToken();
                            if (idToken != null) {
                                AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);



                                mAuth.signInWithCredential(firebaseCredential)
                                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {

                                                    FirebaseUser user = mAuth.getCurrentUser();

                                                    Query query = appDb
                                                            .orderByChild("email")
                                                            .equalTo(user.getEmail())
                                                            .limitToFirst(1);

                                                    query.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if(!snapshot.hasChildren()) {
                                                                String id = UUID.randomUUID().toString();

                                                                User dbUser = new User("",
                                                                        "0",
                                                                        "",
                                                                        "",
                                                                        user.getEmail(),
                                                                        id);
                                                                appDb.child(id).setValue(dbUser);
                                                            }

                                                            updateUI(user);
                                                            backdropLoading.setVisibility(View.GONE);
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            // Getting Post failed, log a message
                                                            Log.w("TAG", "loadPost:onCancelled", error.toException());
                                                            // ...
                                                        }
                                                    });

                                                    // Sign in success, update UI with the signed-in user's information
                                                    Log.d("TAG", "signInWithCredential:success");
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
    public void onClick(View v) {
        if(v.getId() == R.id.btDaftar) {
            this.backdropLoading.setVisibility(View.VISIBLE);

            Handler h = new Handler(Looper.getMainLooper());
            Thread signinThread = new Thread(() -> {
                try {
                    mAuth.signInWithEmailAndPassword(this.etEmail.getText().toString(), this.etPassword.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TAG", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }

                                    h.post(() -> {
                                        backdropLoading.setVisibility(View.GONE);
                                    });
                                }
                            });
                }catch(Exception e) {

                }
            });

            signinThread.start();

        } else if (v.getId() == R.id.btGoogleLogin) {
            this.backdropLoading.setVisibility(View.VISIBLE);

            Handler h = new Handler(Looper.getMainLooper());
            Thread signinGoogleThread = new Thread(() -> {
                try {
                    oneTapClient = Identity.getSignInClient(LoginActivity.this);
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
                            .addOnSuccessListener(LoginActivity.this, new OnSuccessListener<BeginSignInResult>() {
                                @Override
                                public void onSuccess(BeginSignInResult result) {
                                    launcher.launch(new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build());
                                    h.post(() -> {
                                        backdropLoading.setVisibility(View.GONE);
                                    });
                                }
                            })
                            .addOnFailureListener(LoginActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // No Google Accounts found. Just continue presenting the signed-out UI.
                                    Log.d("TAG", e.getLocalizedMessage());
                                    h.post(() -> {
                                        backdropLoading.setVisibility(View.GONE);
                                    });
                                }
                            });
                }catch(Exception e) {

                }
            });

            signinGoogleThread.start();

        }

    }

    public void updateUI(FirebaseUser account){
        Log.d("TAG", "update ui executed");
        if(account != null){
            Toast.makeText(this,"You Signed In successfully",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,MainActivity.class));

        }else {
            Toast.makeText(this,"You Didnt signed in",Toast.LENGTH_LONG).show();
        }

    }
}