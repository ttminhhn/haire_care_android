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

import java.util.concurrent.TimeUnit;

public class EnterOtpActivity extends AppCompatActivity {
    public static  final  String TAG = EnterOtpActivity.class.getName();
    private FirebaseAuth mAuth;
    private EditText txtOtpCode;
    private Button btnSendOtp;
    private TextView tvSendOtpAgain;
    private String mPhoneNumber;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mForceResendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);

        getDataIntent();

        initUI();

        mAuth = FirebaseAuth.getInstance();

        SetListenerActivity();

    }

    private void SetListenerActivity() {
        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strOtpCode = txtOtpCode.getText().toString().trim();
                if(strOtpCode!= null)
                {
                    onClicksendOtpCode(strOtpCode);
                }
                else
                {
                    Toast.makeText(  EnterOtpActivity.this,
                            "You can not send wrong otp",Toast.LENGTH_SHORT).show();
                }

            }
        });
        tvSendOtpAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkCode())
                {
                    Toast.makeText(  EnterOtpActivity.this,
                            "You can not send wrong otp",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    onClicksendOtpCodeAgain();
                }
            }
        });
    }

    private void setTitleToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Enter Otp Activity");
        }
    }

    private void initUI() {
        txtOtpCode = findViewById(R.id.txtOtpCode);
        btnSendOtp = findViewById(R.id.btnSendOtpCode);
        tvSendOtpAgain = findViewById(R.id.sendOtpAgain);
    }

    private void getDataIntent(){
        mPhoneNumber = getIntent().getStringExtra("phone_number");
        mVerificationId = getIntent().getStringExtra("verification_id");
    }

    private void onClicksendOtpCodeAgain() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mPhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setForceResendingToken(mForceResendingToken)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(  EnterOtpActivity.this,
                                        "VerificationFailed",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);
                                mVerificationId = verificationId;
                                mForceResendingToken = forceResendingToken;

                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void onClicksendOtpCode(String strOtpCode) {

        if(mVerificationId != null)
        {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,strOtpCode);
            signInWithPhoneAuthCredential(credential);
        }
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

                            goToSetPassActivity(user.getPhoneNumber());

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(  EnterOtpActivity.this,
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
    private boolean checkCode() {
        if (txtOtpCode.getText().toString().isEmpty()) {
            btnSendOtp.setEnabled(true);
            return true;
        } else {
            btnSendOtp.setEnabled(false);
            return false;
        }
    }
    private void goToSetPassActivity(String phoneNumber) {
        Intent intent = new Intent( this, SetPassWordActivity.class);
        intent.putExtra("phone_number",phoneNumber);
        startActivity(intent);
        finish();
    }
}