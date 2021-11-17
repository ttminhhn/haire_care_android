package com.example.haircare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.haircare.Model.Config;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;


import org.json.JSONArray;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private  static  final String TAG = LoginActivity.class.getName();
    private EditText txtPhoneNumber;
    private Button btnLogin;
    private FirebaseAuth mAuth;

    private TextView tvSigninWithOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //setTitleToolbar();

        initUI();

        mAuth = FirebaseAuth.getInstance();

        SetListenerActivity();
    }

    private void SetListenerActivity() {
        tvSigninWithOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHuaweiActivity();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetNext();
            }
        });
    }

    private void SetNext()
    {
        String strPhoneNumber = txtPhoneNumber.getText().toString().trim();
        String url = Config.LOCALHOST + "GetCheckUser.php?UserName=" +strPhoneNumber;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null
                , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length() == 0) {
                    onClickVerifyPhoneNumber(strPhoneNumber);
                } else {
                    try {
                        //String pass = response.getJSONObject(0).getString("Password");
                        //goToLoginWithPass(pass);
                        Toast.makeText(  LoginActivity.this,
                                "The verification code entered was invalid",Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });

        requestQueue.add(request);
    }


    private void onClickVerifyPhoneNumber(String strPhoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(strPhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(  LoginActivity.this,
                                        "Verification Failed",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);

                                goToEnterOtpActivity(strPhoneNumber,verificationId);

                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI

                            goToMainActivity(user.getPhoneNumber());

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(  LoginActivity.this,
                                                "The verification code entered was invalid",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void goToMainActivity(String phoneNumber) {
        Intent intent = new Intent( this, MainActivity.class);
               intent.putExtra("phone_number",phoneNumber);
               startActivity(intent);

    }

    private void goToHuaweiActivity() {
        Intent intent = new Intent( this, LoginHuaweiActivity.class);
        startActivity(intent);

    }

    private void goToEnterOtpActivity(String strPhoneNumber, String verificationId) {
        Intent intent = new Intent( this, EnterOtpActivity.class);
        intent.putExtra("phone_number",strPhoneNumber);
        intent.putExtra("verification_id",verificationId);
        startActivity(intent);
    }

    private void goToLoginWithPass( String pass)
    {

    }

    private void initUI(){
        txtPhoneNumber = findViewById(R.id.txtPhoneNummber);
        btnLogin = findViewById(R.id.btnLogin);
        tvSigninWithOther = findViewById(R.id.SigninWithOther);
    }
}