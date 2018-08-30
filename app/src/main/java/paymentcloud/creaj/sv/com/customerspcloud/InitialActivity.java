package paymentcloud.creaj.sv.com.customerspcloud;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKit.InitializeCallback;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitActivity.ResponseType;
import com.facebook.accountkit.ui.AccountKitConfiguration.AccountKitConfigurationBuilder;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;
import com.facebook.accountkit.ui.SkinManager.Skin;
import com.facebook.accountkit.ui.SkinManager.Tint;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.KenBurnsView.TransitionListener;
import com.flaviofaria.kenburnsview.Transition;

public class InitialActivity extends AppCompatActivity {
    public static int APP_REQUEST_CODE = 3301;
    public static String APP_TAG = "AccountKit";
    private KenBurnsView kbv;
    private Button mBtnEmailLogin;
    private Button mBtnPhoneLogin;
    private LinearLayout mLytLoading;

    class C04293 implements OnClickListener {
        C04293() {
        }

        public void onClick(View view) {
            InitialActivity.this.goToLogin(false);
        }
    }

    class C04304 implements OnClickListener {
        C04304() {
        }

        public void onClick(View view) {
            InitialActivity.this.goToLogin(true);
        }
    }

    class C05711 implements TransitionListener {
        C05711() {
        }

        public void onTransitionStart(Transition transition) {
        }

        public void onTransitionEnd(Transition transition) {
        }
    }

    class C05722 implements InitializeCallback {
        C05722() {
        }

        public void onInitialized() {
            InitialActivity.this.mLytLoading.setVisibility(8);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_initial);
        Log.d("Cycle", "Oncreate");
        getObjects();
        setProperties();
        this.kbv.setTransitionListener(new C05711());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) {
            String responseMessage;
            AccountKitLoginResult loginResult = (AccountKitLoginResult) data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null) {
                responseMessage = loginResult.getError().getErrorType().getMessage();
            } else if (loginResult.wasCancelled()) {
                responseMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    responseMessage = "Success: " + loginResult.getAccessToken().getAccountId();
                } else {
                    responseMessage = String.format("Success:%s...", new Object[]{loginResult.getAuthorizationCode().substring(0, 10)});
                }
                goToMyLoggedInActivity();
            }
            log(responseMessage);
        }
    }

    private void getObjects() {
        this.mBtnEmailLogin = (Button) findViewById(R.id.btnEmailLogin);
        this.mBtnPhoneLogin = (Button) findViewById(R.id.btnPhoneLogin);
        this.mLytLoading = (LinearLayout) findViewById(R.id.lytLoading);
        this.kbv = (KenBurnsView) findViewById(R.id.image);
    }

    private void setProperties() {
        AccountKit.initialize(getApplicationContext(), new C05722());
        this.mBtnEmailLogin.setOnClickListener(new C04293());
        this.mBtnPhoneLogin.setOnClickListener(new C04304());
        if (AccountKit.getCurrentAccessToken() != null) {
            goToMyLoggedInActivity();
        }
    }

    public void goToLogin(boolean isSMSLogin) {
        LoginType loginType = isSMSLogin ? LoginType.PHONE : LoginType.EMAIL;
        Intent intent = new Intent(getApplicationContext(), AccountKitActivity.class);
        AccountKitConfigurationBuilder configurationBuilder = new AccountKitConfigurationBuilder(loginType, ResponseType.TOKEN);
        configurationBuilder.setUIManager(new SkinManager(Skin.CONTEMPORARY, getResources().getColor(R.color.colorPrimary), R.drawable.background, Tint.BLACK, 0.1d));
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    private void goToMyLoggedInActivity() {
        startActivity(new Intent(getApplicationContext(), SecondActivity.class));
        finish();
    }

    private void log(String msj) {
        Log.println(3, APP_TAG, msj);
    }
}
