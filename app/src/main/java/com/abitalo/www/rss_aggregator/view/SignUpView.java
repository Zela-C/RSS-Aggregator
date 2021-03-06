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
 * 注册界面
 */
public class SignUpView extends Fragment implements View.OnClickListener, IAccountView {
    AccountPresenter presenter = null;
    DrawerLayout drawer = null;
    View view = null;

    EditText etFullName = null;
    EditText etEmail = null;
    EditText etPassword = null;
    EditText etConfirmPassword = null;

    Button btnCreateAccount = null;
    TextView tvExist2Login = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sign_up_main, container, false);
        initView();
        return view;
    }

    private void initView() {
        try {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("用户注册");
        }catch (Exception exception){
            Log.e("RssListView", "Some thing wrong");
        }
        etFullName = (EditText) view.findViewById(R.id.sign_full_name);
        etEmail = (EditText) view.findViewById(R.id.sign_user_mail);
        etPassword = (EditText) view.findViewById(R.id.sign_user_pass);
        etConfirmPassword = (EditText) view.findViewById(R.id.sign_up_edit_confirm_password);

        btnCreateAccount = (Button) view.findViewById(R.id.sign_login);

        tvExist2Login = (TextView) view.findViewById(R.id.sign_exist);

        btnCreateAccount.setOnClickListener(this);
        tvExist2Login.setOnClickListener(this);

        drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        presenter = new AccountPresenter(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_login:
                create();
                break;
            case R.id.sign_exist:
                signIn();
                break;
        }
    }

    private void signIn() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_content, new SignInView(), "sign_in").commit();
    }

    private void create() {
        if (etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            presenter.register();
        } else {
            Snackbar.make(view, "Enter the password twice inconsistent!!", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public String getUserName() {
        return etFullName.getText().toString();
    }

    @Override
    public String getPassword() {
        return MD5Encrypt.parse(etPassword.getText().toString());
    }

    @Override
    public String getEmail() {
        return etEmail.getText().toString();
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
        SharedPreferences mySharedPreferences= getContext().getSharedPreferences("userAuthentication",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("name", getUserName());
        editor.apply();


        return false;
    }

    private void showMenu() {//将来需要使用从云端获得的用户数据作为参数，然后使用adapter根据用户数据填充菜单。。
        onDestroy();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_account, new UserAccountView(), "user_account").commit();
        Log.i("SignUpView", "is here visited");
        fragmentManager.beginTransaction().replace(R.id.fragment_content,
                RSSListView.newInstance("https://www.zhihu.com/rss"), "fragment_view").commit();
    }
}
