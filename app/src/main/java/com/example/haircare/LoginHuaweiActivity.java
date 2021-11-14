package com.example.haircare;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.huawei.hmf.tasks.Task;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;

public class LoginHuaweiActivity extends AppCompatActivity {

    AccountAuthParams authParams;
    AccountAuthService authService;
    private Button btnLoginHuawei;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_huawei);
        initUI();
        btnLoginHuawei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInId();
            }
        });
    }
    ActivityResultLauncher<Intent> signInIDResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //xu ly ket qua tra ve khi dang nhap
                    Intent data = result.getData();
                    //id-token signIn
                    Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
                    if(authAccountTask.isSuccessful()){
                        Toast.makeText(LoginHuaweiActivity.this, "Đăng nhập thành công:", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginHuaweiActivity.this, HomeActivity.class));
                    }else {
                        Toast.makeText(LoginHuaweiActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    private void signInId(){
        authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setIdToken().createParams();
        authService= AccountAuthManager.getService(LoginHuaweiActivity.this,authParams);
        signInIDResult.launch(authService.getSignInIntent());
    }
    private void initUI(){
       btnLoginHuawei = findViewById(R.id.btnLoginHuawei);
    }
}