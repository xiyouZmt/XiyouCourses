package com.zmt.boxin.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.zmt.boxin.Application.App;
import com.zmt.boxin.NetworkThread.GetSession;
import com.zmt.boxin.R;
import com.zmt.boxin.Utils.ClearEditText;
import com.zmt.boxin.Utils.IsConnected;
import com.zmt.boxin.Utils.PasswordEditText;
import com.zmt.boxin.Utils.RequestUrl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login) Button login;
    @BindView(R.id.remember) CheckBox checkbox;
    @BindView(R.id.username) ClearEditText username;
    @BindView(R.id.password) PasswordEditText password;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    private App app;
    private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 标题栏透明
         */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_login);
        initViews();
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("username", "");
        String pwd = sharedPreferences.getString("password", "");
        username.setText(name);
        password.setText(pwd);
    }

    public final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            Intent intent = new Intent();
            switch (msg.what){
                case 0x000 :
                    Snackbar.make(coordinatorLayout, "登陆成功!", Snackbar.LENGTH_SHORT).show();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    intent.putExtra("content", msg.obj.toString());
                    startActivity(intent);
                    progressdialog.dismiss();
                    break;
                case 0x111 :
                    progressdialog.dismiss();
                    Snackbar.make(coordinatorLayout, "用户名或密码错误!", Snackbar.LENGTH_SHORT).show();
                    break;
                case 0x222 :
                    progressdialog.dismiss();
                    Snackbar.make(coordinatorLayout, "服务端连接失败,请重新登陆!", Snackbar.LENGTH_SHORT).show();
                    break;
                case 0x333 :
                    progressdialog.dismiss();
                    Snackbar.make(coordinatorLayout, "网络连接错误!", Snackbar.LENGTH_SHORT).show();
                    break;
                case 0x444 :
                    progressdialog.dismiss();
                    Toast.makeText(LoginActivity.this, "你还没有对本学期的课程进行评价, 请先评价", Toast.LENGTH_SHORT).show();
                    intent.setClass(LoginActivity.this, com.zmt.boxin.Activity.WebView.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    @OnClick(R.id.login)
    public void onClick(View v){
        IsConnected connected = new IsConnected();
        if(username.getText().toString().equals("")){
            Snackbar.make(coordinatorLayout, "用户名不能为空!", Snackbar.LENGTH_SHORT).show();
        } else if(password.getText().toString().equals("")){
            Snackbar.make(coordinatorLayout, "密码不能为空!", Snackbar.LENGTH_SHORT).show();
        } else {
            String number = username.getText().toString();
            String pwd = password.getText().toString();
            if(checkbox.isChecked()){
                /**
                 * 记住密码
                 */
                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", number);
                editor.putString("password", pwd);
                editor.apply();
            }
            if(!connected.checkNetwork(this)){
                Snackbar.make(coordinatorLayout, " 网络未连接，请先连接网络!", Snackbar.LENGTH_SHORT).show();
            } else {
                progressdialog.show();
                RequestUrl url = new RequestUrl();
                GetSession thread = new GetSession(url.cookieUrl, number, pwd, handler, app);
                Thread t = new Thread(thread, "NetWorkThread");
                t.start();
            }
        }
    }


    public void initViews(){
        app = (App)getApplication();
        ButterKnife.bind(this);
        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("正在登陆...");
        progressdialog.setCancelable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
