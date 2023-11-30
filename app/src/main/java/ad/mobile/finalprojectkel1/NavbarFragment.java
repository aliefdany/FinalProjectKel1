package ad.mobile.finalprojectkel1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class NavbarFragment extends Fragment implements View.OnClickListener {

    private INavbar activity;

    private View layout;


    public NavbarFragment(INavbar activity){
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.layout = inflater.inflate(R.layout.navigation_fragment, container, false);

        return this.layout;
    }

    @Override
    public void onClick(View view) {

    }
}
