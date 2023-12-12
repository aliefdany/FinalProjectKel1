package ad.mobile.finalprojectkel1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

public class NavbarFragment extends Fragment implements View.OnClickListener {

    private INavbar activity;
    private View layout;
    private ImageButton btAddMhs;
    private ImageButton btAccount;

    public NavbarFragment() {
        // doesn't do anything special
    }


    public NavbarFragment(INavbar activity){
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.layout = inflater.inflate(R.layout.navigation_fragment, container, false);

        this.btAddMhs = this.layout.findViewById(R.id.btAdd);
        this.btAccount = this.layout.findViewById(R.id.btAccount);

        this.btAddMhs.setOnClickListener(this);
        this.btAccount.setOnClickListener(this);

        return this.layout;
    }

    @Override
    public void onClick(View view) {

        if( view.getId() == R.id.btAdd) {
            Intent i = new Intent((Context) this.activity,AddStudentActivity.class);
            startActivity(i);
        } else if(view.getId() == R.id.btAccount) {
            Intent i = new Intent((Context) this.activity,ProfileActivity.class);
            startActivity(i);
        }
    }
}
