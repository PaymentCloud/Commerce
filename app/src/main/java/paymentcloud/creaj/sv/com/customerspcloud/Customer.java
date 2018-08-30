package paymentcloud.creaj.sv.com.customerspcloud;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Customer extends AppCompatActivity {
    Button btnLogout, btnHistorial;

    TextView e1;
    TextView e2;
    TextView e3;
    TextView e4;


    class C04261 implements OnClickListener {
        C04261() {
        }

        public void onClick(View view) {
            PreferenceManager.getDefaultSharedPreferences(Customer.this.getApplicationContext()).edit().remove("is_logged").commit();
            Customer.this.startActivity(new Intent(Customer.this, Login.class));
            Customer.this.finish();
        }
    }

    class C04272 implements Runnable {
        C04272() {
        }

        public void run() {
            Log.d("Servicio", "Servicesdasd");
            PreferenceManager.getDefaultSharedPreferences(Customer.this.getApplicationContext()).edit().remove("is_logged").commit();
        }
    }

    class C04283 implements Runnable {
        C04283() {
        }

        public void run() {
            Log.d("Servicio", "Servicesdasd");
            PreferenceManager.getDefaultSharedPreferences(Customer.this.getApplicationContext()).edit().remove("is_logged").commit();
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.customer);
        getObjects();
        this.btnLogout.setOnClickListener(new C04261());

        btnHistorial = findViewById(R.id.btnHistorial);
        btnHistorial.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HistorialActivity.class);
                startActivity(i);
            }
        });


    }

    private void getObjects() {
        this.e1 = (TextView) findViewById(R.id.txtName);
        this.e2 = (TextView) findViewById(R.id.txtEmail);
        this.e3 = (TextView) findViewById(R.id.txtWallet);
        this.e4 = (TextView) findViewById(R.id.txtBalance);
        this.btnLogout = (Button) findViewById(R.id.btnLogout);

        setData();
    }

    private void setData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String name = prefs.getString("user_name", null);
        String email = prefs.getString("user_mail", null);
        String wallet = prefs.getString("id_wallet", null);
        String balance = prefs.getString("balance", null);
        this.e1.setText("Hola! " + name);
        this.e2.setText(email);
        this.e3.setText("# " + wallet);
        this.e4.setText("$" + balance);
    }

    protected void onPause() {
        new Handler().postDelayed(new C04272(), 2000);
        super.onStop();
        super.onPause();
    }

    protected void onStop() {
        new Handler().postDelayed(new C04283(), 2000);
        super.onStop();
    }

    public void onBackPressed() {
        if (Integer.valueOf(Integer.valueOf(0).intValue() + 1).intValue() != 1) {
            finish();
        }
    }
}
