package ad.mobile.finalprojectkel1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private Button btLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.btLogout = findViewById(R.id.btLogout);
        this.btLogout.setOnClickListener(this);

        this.mAuth = FirebaseAuth.getInstance();
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
                mAuth.signOut();

                dialog.dismiss();

            }
        });

        dialog.show();
    }

    @Override
    public void onClick(View v) {
        showLogoutDialog();
    }
}