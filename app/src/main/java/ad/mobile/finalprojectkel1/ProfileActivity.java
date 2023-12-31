package ad.mobile.finalprojectkel1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private FirebaseAuth mAuth;
    private Button btLogout;
    private Button btEdit;
    private TextView tvEmail;

    private TextView etNIDN;
    private TextView etPhone;
    private Spinner spGender;
    private TextView etDate;

    private TextView etDisplayName;

    private String url = "https://final-project-papb-de61c-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private DatabaseReference db;
    private DatabaseReference appDb;

    private User user;
    private TextView tvDisplayName;
    private View backdropLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.backdropLoading = findViewById(R.id.loadingPanel);

        this.btLogout = findViewById(R.id.btLogout);
        this.btLogout.setOnClickListener(this);

        this.btEdit = findViewById(R.id.btEditUser);
        this.btEdit.setOnClickListener(this);

        this.tvEmail = findViewById(R.id.tvEmailUser);
        this.tvDisplayName = findViewById(R.id.tvNamaUser);

        this.mAuth = FirebaseAuth.getInstance();

        this.tvEmail.setText(this.mAuth.getCurrentUser().getEmail());
        this.tvDisplayName.setText(this.mAuth.getCurrentUser().getDisplayName());


        Query query = FirebaseDatabase.getInstance(url)
                .getReference()
                .child("user")
                .orderByChild("email")
                .equalTo(this.mAuth.getCurrentUser().getEmail())
                .limitToFirst(1);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()) {
                    // TODO: handle the post

                    Map<User, Object> map = (Map<User, Object>) snap.getValue();

                    user = new User(map.get("nidn").toString(),
                            map.get("gender").toString(),
                            map.get("birthDate").toString(),
                            map.get("phone").toString(),
                            map.get("email").toString(),
                            snap.getKey());

                    etNIDN.setText(user.getNIDN());
                    etPhone.setText(user.getPhone());
                    spGender.setSelection(Integer.parseInt(user.getGender()));
                    etDate.setText(user.getBirthDate());
                    etDisplayName.setText(mAuth.getCurrentUser().getDisplayName());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", error.toException());
                // ...
            }
        });

        this.etNIDN = findViewById(R.id.etNIDN);
        this.etPhone = findViewById(R.id.etNomorTeleponUser);
        this.spGender = findViewById(R.id.spGender);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.gender_array,
                R.layout.spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner.
        spGender.setAdapter(adapter);

        spGender.setOnItemSelectedListener(this);

        this.etDate = findViewById(R.id.etDate);
        this.etDisplayName = findViewById(R.id.etUsername);

        this.etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment(etDate);
                newFragment.show(ProfileActivity.this.getSupportFragmentManager(), "datePicker");
            }
        });


        this.db = FirebaseDatabase.getInstance(url).getReference();
        this.appDb = this.db.child("user");
    }

    void showLogoutDialog() {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.logout_dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button btConfirmLogout = (Button) dialog.findViewById(R.id.btConfirmLogout);


        btConfirmLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backdropLoading.setVisibility(View.VISIBLE);

                mAuth.signOut();

                dialog.dismiss();

                backdropLoading.setVisibility(View.GONE);

                Intent intent = new Intent(ProfileActivity.this, RegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                ProfileActivity.this.startActivity(intent);
            }
        });

        dialog.show();
    }


    void showEditDialog() {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.edit_user_dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button btConfirmUpdate = (Button) dialog.findViewById(R.id.btConfirmUpdate);


        btConfirmUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backdropLoading.setVisibility(View.VISIBLE);

                Handler h = new Handler(Looper.getMainLooper());
                Thread editProfileThread = new Thread(() -> {
                    try {
                        user.setEmail(tvEmail.getText().toString());
                        user.setNIDN(etNIDN.getText().toString());
                        user.setPhone(etPhone.getText().toString());
                        user.setBirthDate(etDate.getText().toString());

                        appDb.child(user.getId()).setValue(user);

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(etDisplayName.getText().toString())
                                .build();

                        mAuth.getCurrentUser().updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        backdropLoading.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            tvDisplayName.setText(mAuth.getCurrentUser().getDisplayName());
                                            Log.d("TAG", "User profile updated.");
                                        }


                                        h.post(() -> {
                                            Toast.makeText(ProfileActivity.this,"Sukses mengupdate data anda",Toast.LENGTH_LONG).show();
                                        });
                                    }
                                });

                    }catch(Exception e) {

                    }
                });

                editProfileThread.start();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btLogout) {
            showLogoutDialog();
        } else if(v.getId() == R.id.btEditUser) {
            showEditDialog();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            this.spGender.setSelection(position);
            this.user.setGender(String.valueOf(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}