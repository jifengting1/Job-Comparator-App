package edu.gatech.seclass.jobcompare6300.currentjob;

import android.widget.EditText;

public class CurrentJobValidator {
    public boolean isValidField(EditText field) {
        String str = field.getText().toString();

        if (str.isEmpty()) {
            field.setError("This field is required");
            return false;
        }

        return true;
    }
}
