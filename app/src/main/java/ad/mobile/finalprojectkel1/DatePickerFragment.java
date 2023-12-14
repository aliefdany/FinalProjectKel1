package ad.mobile.finalprojectkel1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private TextView etDate;
    private Calendar c;

    public DatePickerFragment(TextView etDate) {
        this.etDate = etDate;
        this.c = Calendar.getInstance();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.

        int year = this.c.get(Calendar.YEAR);
        int month = this.c.get(Calendar.MONTH);
        int day = this.c.get(Calendar.DAY_OF_MONTH);

        String[] splittedDate = this.etDate.getText().toString().split("/");

        if(splittedDate.length == 3) {
            year = Integer.valueOf(splittedDate[0]);
            month = Integer.valueOf(splittedDate[1]);
            day = Integer.valueOf(splittedDate[2]);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), R.style.datepicker, this, year, month, day);

        // Create a new instance of DatePickerDialog and return it.
        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date the user picks.

        this.etDate.setText(String.valueOf(year) + "/" + String.valueOf(month) + "/" + String.valueOf(day));
    }
}
