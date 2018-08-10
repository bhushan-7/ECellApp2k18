package nitrr.ecell.e_cell.signin.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import nitrr.ecell.e_cell.R;
import nitrr.ecell.e_cell.activities.HomeActivity;
import nitrr.ecell.e_cell.model.AuthenticationResponse;
import nitrr.ecell.e_cell.model.LoginDetails;
import nitrr.ecell.e_cell.restapi.ApiServices;
import nitrr.ecell.e_cell.restapi.AppClient;
import nitrr.ecell.e_cell.utils.PrefUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class login_activity extends AppCompatActivity implements View.OnClickListener {

    private EditText EditText_Email, EditText_Password;
    private TextInputLayout Layout_Email, Layout_Password;
    private Button Sign_in;
    private TextView ForgetPassword;
    private String Email, Password;
    private ProgressBar SignInProgressBar;
    private LoginDetails loginDetails;
    private PrefUtils prefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initview();
    }

    private void initview() {
        loginDetails = new LoginDetails();
        prefUtils = new PrefUtils(this);
        Typeface raleway = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/raleway.ttf");
        EditText_Email = findViewById(R.id.inputusername);
        Layout_Email = findViewById(R.id.layout_email);
        Layout_Password = findViewById(R.id.layout_password);
        EditText_Password = findViewById(R.id.inputpassword);
        Sign_in = findViewById(R.id.signinbutton);
        ForgetPassword = findViewById(R.id.forgetpassword);
        SignInProgressBar = findViewById(R.id.signinprogressbar);

        Sign_in.setOnClickListener(this);
        ForgetPassword.setOnClickListener(this);

        Layout_Password.setTypeface(raleway);
    }

    private void apiCall() {
        setData();
        ApiServices apiServices = AppClient.getInstance().createService(ApiServices.class);
        Call<AuthenticationResponse> call = apiServices.sendLoginDetails(loginDetails);
        call.enqueue(new Callback<AuthenticationResponse>() {

            @Override
            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                if (response.isSuccessful()) {
                    AuthenticationResponse jsonResponse = response.body();
                    if (null != jsonResponse && jsonResponse.getSuccess()) {
                        Toast.makeText(login_activity.this, jsonResponse.getMessage(), Toast.LENGTH_LONG).show();
                        Toast.makeText(login_activity.this, jsonResponse.getToken(), Toast.LENGTH_LONG).show();

                        prefUtils.saveAccessToken("Token " + jsonResponse.getToken());


                        Intent intent = new Intent(login_activity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(login_activity.this, response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<AuthenticationResponse> call, Throwable throwable) {
                Toast.makeText(login_activity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setData() {
        Email = EditText_Email.getText().toString().trim();
        Password = EditText_Password.getText().toString().trim();
        loginDetails.setEmail(Email);
        loginDetails.setPassword(Password);

    }

    private boolean checkinput() {
        String str[] = {EditText_Email.getText().toString().trim(), EditText_Password.getText().toString().trim()};

        for (String s : str)
            if (s.equals(""))
                return false;

        return true;
    }

    @Override
    public void onClick(View v) {

        if (v == Sign_in) {
            if (checkinput()) {
                if (!Patterns.EMAIL_ADDRESS.matcher(EditText_Email.getText().toString()).matches()) {
                    Toast.makeText(login_activity.this, "Invalid email-id", Toast.LENGTH_LONG).show();
                } else {
                    apiCall();
                }
            } else {
                Toast.makeText(login_activity.this, "Required fields can't be empty.", Toast.LENGTH_LONG).show();
            }
        } else if (v == ForgetPassword) {

            Toast.makeText(login_activity.this, "Reset password (on progress)", Toast.LENGTH_LONG).show();
        }

    }
}
