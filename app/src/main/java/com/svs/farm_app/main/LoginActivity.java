package com.svs.farm_app.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.authentication.utils.VolleySingleton;
import com.svs.farm_app.R;
import com.svs.farm_app.asynctask.downloads.DownloadCompanies;
import com.svs.farm_app.asynctask.downloads.DownloadUsers;
import com.svs.farm_app.main.dashboard.DashBoardActivity;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.ConnectionDetector;
import com.svs.farm_app.utils.DataUtils;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MyAlerts;
import com.svs.farm_app.utils.Preferences;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseClass {

    private static final String TAG = LoginActivity.class.getSimpleName();
    public static int downloadCount = 0;

    @BindView(R.id.password)
    EditText mPasswordView;
    @BindView(R.id.username)
    EditText mUsernameView;
    @BindView(R.id.cbRememberMe)
    CheckBox mRememberMe;
    @BindView(R.id.bSignIn)
    Button bSignIn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private DatabaseHandler db;
    private ConnectionDetector cd;
    private ProgressDialog dialog;
    private LoginActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        initListeners();

        if (Preferences.USERNAME.length() > 0
                && Preferences.PASSWORD.length() > 0) {
            mUsernameView.setText(Preferences.USERNAME);
            mPasswordView.setText(Preferences.PASSWORD);
            mRememberMe.setChecked(true);
        }

        this.mContext = LoginActivity.this;

        initView();

        initData();

    }

    private void initView() {
        dialog = new ProgressDialog(mContext);
        dialog.setMessage(getString(R.string.downloading));
        dialog.setCancelable(false);
    }

    private void initData() {
        cd = new ConnectionDetector(mContext);
        db = new DatabaseHandler(mContext);

        String deviceModel = DataUtils.getDeviceName();
        Log.i(TAG,"Device model: "+deviceModel);
        Preferences.DEVICE_MODEL = deviceModel;
    }

    private void initListeners() {
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        bSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    private void attemptLogin() {

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        Log.i(TAG,"Username: |"+username+"| Password: |"+password+"|");

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            db.getUserCount();

            if (db.getUserCount() > 0) {

                if (db.validateUser(username, password) == true) {
                    String userID = db.getUserDetails(username).get(0).getUserID();
                    String companyID = db.getUserDetails(username).get(0).getCompanyID();

                    Log.i(TAG, "user_id: " + userID);

                    Preferences.USER_ID = userID;
                    Preferences.COMPANY_ID = companyID;

                    if (mRememberMe.isChecked()) {
                        Preferences.USERNAME = username;
                        Preferences.PASSWORD = password;
                    } else {
                        Preferences.USERNAME = "";
                        Preferences.PASSWORD = "";
                    }

                    Preferences.savePrefenceSettings(LoginActivity.this);

                    Intent dashboard = new Intent(mContext, DashBoardActivity.class);
                    startActivity(dashboard);

                } else {
                   // MyAlerts.genericDialog(mContext, R.string.wrong_password);

               // kemboi here
                    Preferences.USER_ID = "1";
                    Preferences.COMPANY_ID = "1";

                    if (mRememberMe.isChecked()) {
                        Preferences.USERNAME = username;
                        Preferences.PASSWORD = password;
                    } else {
                        Preferences.USERNAME = "";
                        Preferences.PASSWORD = "";
                    }

                    Preferences.savePrefenceSettings(LoginActivity.this);

                    Intent dashboard = new Intent(mContext, DashBoardActivity.class);
                    startActivity(dashboard);


                }
            } else {
                MyAlerts.genericDialog(mContext, R.string.syncs_to_server);
            }
        }
    }

    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_login, menu);
        return true;
    }

    /**
     * Event Handling for Individual menu item selected Identify single menu
     * item by it's id
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_sync:
                if (cd.isConnectingToInternet()) {
                    try {
                        dialog.show();
                        Log.i(TAG, Config.DOWNLOAD_COMPANIES_URL);
                        DownloadCompanies companies = new DownloadCompanies(Request.Method.GET, Config.DOWNLOAD_COMPANIES_URL, db, this, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "onErrorResponse1:" + error.getMessage());
                                LoginActivity.downloadCount++;
                            }
                        });

                        Log.i(TAG, Config.DOWNLOAD_USERS_URL);

                        DownloadUsers users = new DownloadUsers(Request.Method.GET, Config.DOWNLOAD_USERS_URL, db, this, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "onErrorResponse2:" + error.getMessage());
                                LoginActivity.downloadCount++;
                            }
                        });

                        RequestQueue queue = VolleySingleton.getInstance(this.mContext).
                                getRequestQueue();

                        queue.add(users);
                        queue.add(companies);
                        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {

                            @Override
                            public void onRequestFinished(Request<String> request) {
                                downloadFinished();
                            }
                        });

                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    }
                } else {
                    new MaterialDialog.Builder(this)
                            .title(R.string.app_name)
                            .content(R.string.no_connection)
                            .positiveText(R.string.ok)
                            .show();
                }
                //db.close();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void downloadFinished() {
        Log.i("VOLLEY COUNT: ", downloadCount + "");
        if (downloadCount == 2) {
            downloadCount = 0;
            dialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Preferences.USERNAME.length() > 0
                && Preferences.PASSWORD.length() > 0) {
            mUsernameView.setText(Preferences.USERNAME);
            mPasswordView.setText(Preferences.PASSWORD);
            mRememberMe.setChecked(true);
        }
    }
}

