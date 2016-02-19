package xyz.umng.tascy.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.umng.tascy.R;

/**
 * Created by Umang on 2/18/2016.
 */
public class ResetPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ResetPasswordActivity";

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.btn_reset) Button _resetButton;
    @Bind(R.id.link_login) TextView _loginLink;

    ParseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        ButterKnife.bind(this);

        _emailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _resetButton.setEnabled(true);
            }
        });

        _resetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Login activity
                finish();
            }
        });
    }

    public void login() {
        Log.d(TAG, "Reset Password");

        if (!validate()) {
            onResetFailed();
            return;
        }

        _resetButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ResetPasswordActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = _emailText.getText().toString().trim();

        // TODO: Implement your own authentication logic here.

        user.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    progressDialog.dismiss();
                    onResetSuccess();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Invalid Email", Toast.LENGTH_LONG).show();
                    onResetFailed();
                    progressDialog.dismiss();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onResetSuccess() {
        _resetButton.setEnabled(true);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ResetPasswordActivity.this);
        builder1.setMessage("Check your email and follow instructions to set up your new password.");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        final AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void onResetFailed() {
        _resetButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        return valid;
    }
}
