package com.abitalo.www.rss_aggregator.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.abitalo.www.rss_aggregator.R;
import com.abitalo.www.rss_aggregator.presenter.AccountPresenter;
import com.abitalo.www.rss_aggregator.util.MD5Encrypt;

/**
 * Created by sangzhenya on 2016/5/8.
 * 登录界面
 */
public class SignInView extends Fragment implements View.OnClickListener, IAccountView {
    AccountPresenter presenter = null;
    DrawerLayout drawer = null;

    View view = null;

    EditText etUserName = null;
    EditText etPassword = null;
    Button btnLogin = null;
    TextView tvForgetPassword = null;
    TextView tvSignUp = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sign_in_main, container, false);
        initView();
        return view;
    }

    private void initView() {
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("用户登录");
        } catch (Exception exception) {
            Log.e("RssListView", "Some thing wrong");
        }
        etUserName = (EditText) view.findViewById(R.id.edit_user_name);
        etPassword = (EditText) view.findViewById(R.id.edit_user_password);

        btnLogin = (Button) view.findViewById(R.id.login_login);

        tvForgetPassword = (TextView) view.findViewById(R.id.login_forget);
        tvSignUp = (TextView) view.findViewById(R.id.login_sign);

        btnLogin.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);

        drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        presenter = new AccountPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login:
                login();
                break;
            case R.id.login_forget:
                forget();
                break;
            case R.id.login_sign:
                sign();
                break;
        }
    }

    private void sign() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_content, new SignUpView(), "sign_up").commit();
    }

    private void forget() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_content, new ForgetPasswordView(), "forget_password").commit();

    }

    private void showMenu() {
        onDestroy();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_account, new UserAccountView(), "user_account").commit();
        Log.i("SignInView", "is here visited");
        getFragmentManager().beginTransaction().replace(R.id.fragment_content,
                RSSListView.newInstance("https://www.zhihu.com/rss"), "fragment_view").commit();
    }

    private void login() {
        presenter.login();
    }

    @Override
    public String getUserName() {
        return etUserName.getText().toString();
    }

    @Override
    public String getPassword() {
        return MD5Encrypt.parse(etPassword.getText().toString());
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public boolean onFailure(String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onSuccess() {
        showMenu();
        Snackbar.make(view, "Success!", Snackbar.LENGTH_SHORT).show();
        SharedPreferences mySharedPreferences = getContext().getSharedPreferences("userAuthentication",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("name", getUserName());
        editor.apply();

        return false;
    }
}
