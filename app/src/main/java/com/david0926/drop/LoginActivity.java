package com.david0926.drop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.david0926.drop.Retrofit.DROPRetrofit;
import com.david0926.drop.Retrofit.LoginModel;
import com.david0926.drop.Retrofit.DROPRetrofitService;
import com.david0926.drop.databinding.ActivityLoginBinding;
import com.david0926.drop.util.TokenCache;
import com.david0926.drop.util.UserCache;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import gun0912.tedkeyboardobserver.TedKeyboardObserver;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setActivity(this);
        binding.setOnProgress(false);

        //scroll to bottom when keyboard up
        new TedKeyboardObserver(this).listen(isShow -> {
            binding.scrollLogin.smoothScrollTo(0, binding.scrollLogin.getBottom());
        });

        //sign in button clicked
        binding.btnLoginSignin.setOnClickListener(view -> {

            binding.setOnProgress(true);
            hideKeyboard(this);

            String id = binding.getId(), pw = binding.getPw();

            if (TextUtils.isEmpty(id) || TextUtils.isEmpty(pw)) //empty field
                showErrorMsg("아이디와 비밀번호를 입력하세요.");
            else signIn(id, pw);

        });

        //sign up button clicked
        binding.btnLoginRegi.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            overridePendingTransition(R.anim.slide_up, R.anim.slide_up_before);
        });

    }

    private void signIn(String id, String pw) {

        DROPRetrofitService mRetrofitAPI = DROPRetrofit.getInstance(this).getDropService();
        Call<ResponseBody> mCallResponse = mRetrofitAPI.Login(new LoginModel(id,pw));

        mCallResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String body = response.body().string();

                    JSONObject dataObject = new JSONObject(body).getJSONObject("data");
                    JSONObject tokenObject = dataObject.getJSONObject("token");
                    JSONObject userObject = dataObject.getJSONObject("user");

                    Logger.addLogAdapter(new AndroidLogAdapter());
                    Logger.d(tokenObject.toString());

                    TokenCache.setToken(LoginActivity.this, tokenObject.toString());
                    UserCache.setUser(LoginActivity.this, userObject.toString());

                    finishSignIn();
                } catch(Exception e) {
                    showErrorMsg("아이디 또는 비밀번호가 일치하지 않습니다.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showErrorMsg("서버가 응답하지 않습니다.");
            }
        });
    }

    private void finishSignIn() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_down_before, R.anim.slide_down);
        finish();
    }

    private void showErrorMsg(String msg) {
        binding.setOnProgress(false);
        binding.txtLoginError.setVisibility(View.VISIBLE);
        binding.txtLoginError.setText(msg);
        binding.txtLoginError.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
    }

    private void hideKeyboard(Activity activity) {
        View v = activity.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
