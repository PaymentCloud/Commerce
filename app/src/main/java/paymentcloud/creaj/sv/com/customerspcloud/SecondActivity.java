package paymentcloud.creaj.sv.com.customerspcloud;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;

public class SecondActivity extends AppCompatActivity {
    private Button mBtnLogout;
    private Button mBtnSignIn;
    private Toolbar mToolbar;
    private TextView mTxtAccountKitID;
    private TextView mTxtUserLoginData;
    private TextView mTxtUserLoginMode;

    class C04351 implements OnClickListener {
        C04351() {
        }

        public void onClick(View view) {
            SecondActivity.this.logout();
        }
    }

    class C04362 implements OnClickListener {
        C04362() {
        }

        public void onClick(View view) {
            SecondActivity.this.startActivity(new Intent(SecondActivity.this.getApplicationContext(), Login.class));
        }
    }

    class C05753 implements AccountKitCallback<Account> {
        C05753() {
        }

        public void onSuccess(Account account) {
            String accountKitId = account.getId();
            SecondActivity.this.log("ID: " + accountKitId);
            boolean SMSLoginMode = false;
            PhoneNumber phoneNumber = account.getPhoneNumber();
            String phoneNumberString = "";
            if (phoneNumber != null) {
                phoneNumberString = phoneNumber.toString();
                SecondActivity.this.log("Phone: " + phoneNumberString);
                SMSLoginMode = true;
            }
            String email = account.getEmail();
            SecondActivity.this.log("Email: " + email);
            SecondActivity.this.mTxtAccountKitID.setText(accountKitId);
            SecondActivity.this.mTxtUserLoginMode.setText(SMSLoginMode ? "Phone:" : "Email:");
            if (SMSLoginMode) {
                SecondActivity.this.mTxtUserLoginData.setText(phoneNumberString);
            } else {
                SecondActivity.this.mTxtUserLoginData.setText(email);
            }
        }

        public void onError(AccountKitError error) {
            SecondActivity.this.log("Error: " + error.toString());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_second);
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("is_logged", false)) {
            startActivity(new Intent(this, Customer.class));
            finish();
        }
        getObjects();
        setProperties();
    }

    private void getObjects() {
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        this.mTxtAccountKitID = (TextView) findViewById(R.id.txtAccountKitID);
        this.mTxtUserLoginMode = (TextView) findViewById(R.id.txtUserLoginMode);
        this.mTxtUserLoginData = (TextView) findViewById(R.id.txtUserLoginData);

        this.mBtnLogout = (Button) findViewById(R.id.btnLogout);
        this.mBtnSignIn = (Button) findViewById(R.id.btnSignIn);
    }

    private void setProperties() {
        setSupportActionBar(this.mToolbar);
        this.mBtnLogout.setOnClickListener(new C04351());
        this.mBtnSignIn.setOnClickListener(new C04362());
        setUserInformation();
    }

    protected void onResume() {
        super.onResume();
        Log.d("Cycle", "OnResume");
    }

    protected void onPause() {
        Log.d("Cycle", "OnPause");
        super.onPause();
    }

    protected void onStop() {
        Log.d("Cycle", "Murio");
        super.onStop();
    }

    public void setUserInformation() {
        AccountKit.getCurrentAccount(new C05753());
    }

    public void logout() {
        AccountKit.logOut();
        startActivity(new Intent(this, InitialActivity.class));
        finish();
        String a = "alex";
    }

    private void log(String msj) {
        Log.println(3, InitialActivity.APP_TAG, msj);
    }
}
