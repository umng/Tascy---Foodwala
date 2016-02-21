package xyz.umng.tascy.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.umng.tascy.R;
import xyz.umng.tascy.helper.ParseUtils;

/**
 * Created by Umang on 2/15/2016.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private String facebook_login_email = "";
    private String facebook_login_first_name = "";
    private String facebook_login_last_name = "";

    ProgressDialog progressDialog;

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.facebook_login) Button _facebookLoginButton;
    @Bind(R.id.link_signup) TextView _signupLink;
    @Bind(R.id.link_reset_password) TextView _resetPasswordLink;

    ParseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ParseUtils.subscribeWithEmail("");

        ButterKnife.bind(this);

        _emailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _loginButton.setEnabled(true);
            }
        });

        _passwordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _loginButton.setEnabled(true);
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        _resetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start the ResetPassword activity
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        _facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookLogin();
            }
        });
    }

    public void facebookLogin()
    {
        Log.d(TAG, "Facebook Login");

        List<String> permissions = Arrays.asList("public_profile", "email");

        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                    _loginButton.setEnabled(false);
                    _facebookLoginButton.setEnabled(false);
                    _signupLink.setEnabled(false);
                    _resetPasswordLink.setEnabled(false);
                    showPD("Getting Details...", false);
                    getUserDetailsFromFB();
                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                    showPD("Logging In...", false);
                    gotoMain("Welcome back");
                }
            }
        });
    }

    private void getUserDetailsFromFB() {
        // Suggested by https://disqus.com/by/dominiquecanlas/
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,first_name,last_name");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
         /* handle the result */
                        try {
                            facebook_login_email = response.getJSONObject().getString("email");
                            facebook_login_first_name = response.getJSONObject().getString("first_name");
                            facebook_login_last_name = response.getJSONObject().getString("last_name");
                            saveNewUser();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("getUserDetailsFromFB", e.getMessage());
                            progressDialog.dismiss();
                        }
                    }
                }
        ).executeAsync();
    }

    private void saveNewUser() {
        user = ParseUser.getCurrentUser();
        user.setUsername(facebook_login_email);
        user.setEmail(facebook_login_email);
        user.put("firstName", facebook_login_first_name);
        user.put("lastName", facebook_login_last_name);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    gotoMain("Welcome to Tascy");
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getBaseContext(), "Invalid Facebook Login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void showPD(String message, boolean cancelable)
    {
        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(cancelable);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void gotoMain(String toastText)
    {
        ParseUtils.subscribeWithEmail(user.getCurrentUser().getEmail().trim());
        Toast.makeText(LoginActivity.this, toastText, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        onLoginSuccess();
        progressDialog.dismiss();
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

//        _loginButton.setEnabled(false);

        showPD("Authenticating...", true);

        final String email = _emailText.getText().toString().trim();
        String password = _passwordText.getText().toString().trim();

        // TODO: Implement your own authentication logic here.

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);

        user.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null)
                {
                    gotoMain("Logged In");
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Invalid Email or Password", Toast.LENGTH_LONG).show();
                    onLoginFailed();
                    progressDialog.dismiss();
                }
            }
        });

    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_SIGNUP) {
//            if (resultCode == RESULT_OK) {
//
//                // TODO: Implement successful signup logic here
//                // By default we just finish the Activity and log them in automatically
//                this.finish();
//            }
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString().trim();
        String password = _passwordText.getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            _passwordText.setError("between 4 and 20 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
