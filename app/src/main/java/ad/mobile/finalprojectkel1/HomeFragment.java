package ad.mobile.finalprojectkel1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    private INavbar activity;

    private View layout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.layout = inflater.inflate(R.layout.home_fragment, container, false);

        return this.layout;
    }

}
