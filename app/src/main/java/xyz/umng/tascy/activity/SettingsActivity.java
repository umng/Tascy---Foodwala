package xyz.umng.tascy.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import xyz.umng.tascy.R;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    ProgressDialog pd;
    private EditText inputEmail, pwd, fName, lName, phone, room;
    Spinner hostel, block;
    Button editDetails, saveDetails;
    ParseUser user = ParseUser.getCurrentUser();

    String userDetailsObjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        inputEmail = (EditText) findViewById(R.id.activity_account_email);
        pwd = (EditText) findViewById(R.id.activity_account_password);
        fName = (EditText) findViewById(R.id.activity_account_firstName);
        lName = (EditText) findViewById(R.id.activity_account_lastName);
        phone = (EditText) findViewById(R.id.activity_account_phone);
        editDetails = (Button) findViewById(R.id.activity_account_EditDetailsButton);
        saveDetails = (Button) findViewById(R.id.activity_account_accountButton);

        pd = new ProgressDialog(SettingsActivity.this,
                R.style.AppTheme_Dark_Dialog);
        pd.setIndeterminate(true);
        pd.setCancelable(true);
        pd.setMessage("Loading...");
        pd.show();

        user.fetchInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // Success!
                } else {
                    // Failure!
                    Toast.makeText(SettingsActivity.this, "Can't connect to internet\n Try again later", Toast.LENGTH_LONG);
                }
                pd.dismiss();
            }
        });

        inputEmail.setText(user.getEmail());
        pwd.setText("12345678");
        fName.setText(user.getString("firstName"));
        lName.setText(user.getString("lastName"));
        phone.setText(user.getString("phone"));

        inputEmail.setEnabled(false);
        pwd.setEnabled(false);
        fName.setEnabled(false);
        lName.setEnabled(false);
        phone.setEnabled(false);

        editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fName.setEnabled(true);
                lName.setEnabled(true);
                phone.setEnabled(true);
                editDetails.setVisibility(View.GONE);
                saveDetails.setVisibility(View.VISIBLE);
            }
        });

        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();

                user.put("firstName", fName.getText().toString().trim());
                user.put("lastName", lName.getText().toString().trim());
                user.put("phone", phone.getText().toString().trim());
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null)
                        {
                            Toast.makeText(SettingsActivity.this, "Details Updated Successfully", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(SettingsActivity.this, "Can't connect to the internet\n Try again later", Toast.LENGTH_LONG).show();
                        }
                        pd.dismiss();
                    }
                });
            }
        });
    }
}
