package paymentcloud.creaj.sv.com.customerspcloud;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    protected static final char[] hexArray = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    int accion = 0;
    String em;
    private NfcAdapter mNfcAdapter;
    private ProgressDialog pDialog;
    String pw;
    EditText username;

    class C04311 implements OnClickListener {
        C04311() {
        }

        public void onClick(View v) {
            Login.this.accion = 4;
        }
    }

    class C05733 implements Listener<String> {
        C05733() {
        }

        public void onResponse(String response) {
            Log.d("----respuesta", response.toString());
            try {
                JSONObject profile = new JSONObject(new JSONArray(response).get(0).toString());
                String id = profile.getString("user_id");
                String name = profile.getString("name");
                String mail = profile.getString("mail");
                String phone = profile.getString("id_wallet");
                String balance = profile.getString("balance");
                Log.d("balance", balance);
                Editor editor = PreferenceManager.getDefaultSharedPreferences(Login.this.getApplicationContext()).edit();
                editor.putString("user_id", String.valueOf(id));
                editor.putString("user_name", String.valueOf(name));
                editor.putString("user_mail", String.valueOf(mail));
                editor.putString("id_wallet", String.valueOf(phone));
                editor.putString("balance", String.valueOf(balance));
                editor.putBoolean("is_logged", true);
                editor.commit();
                Login.this.hidepDialog();
                Login.this.startActivity(new Intent(Login.this.getApplicationContext(), Customer.class));
                Login.this.finish();
            } catch (Exception s) {
                Log.d("ERROR", s.toString());
                if (response.equals("{'login': 'false'}")) {
                    Login.this.hidepDialog();
                    Toast.makeText(Login.this.getApplicationContext(), "Error en los datos ingresados, intenta nuevamente", 0).show();
                }
            }
        }
    }

    class C05744 implements ErrorListener {
        C05744() {
        }

        public void onErrorResponse(VolleyError error) {
            Log.d("respuesta error", error.toString());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_login);
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        this.username = (EditText) findViewById(R.id.input_email);
        final EditText password = (EditText) findViewById(R.id.input_password);
        ((Button) findViewById(R.id.readNFC)).setOnClickListener(new C04311());
        this.pDialog = new ProgressDialog(this);
        this.pDialog.setMessage("Espere un momento");
        this.pDialog.setCancelable(false);
        btnLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Login.this.em = Login.this.username.getText().toString();
                Login.this.pw = password.getText().toString();
                if (Login.this.em.length() <= 4 || Login.this.pw.length() <= 2) {
                    Snackbar.make(v, (CharSequence) "Ingrese los datos por favor", 0).setAction((CharSequence) "Action", null).show();
                } else {
                    Login.this.LoginServer();
                }
            }
        });
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("is_logged", false)) {
            startActivity(new Intent(this, Customer.class));
            finish();
        }
        initNFC();
    }

    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter("android.nfc.action.TAG_DISCOVERED");
        IntentFilter ndefDetected = new IntentFilter("android.nfc.action.NDEF_DISCOVERED");
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{new IntentFilter("android.nfc.action.TECH_DISCOVERED"), tagDetected, ndefDetected};
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(536870912), 0);
        if (this.mNfcAdapter != null) {
            this.mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, (String[][]) null);
        }
    }

    private void initNFC() {
        this.mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    protected void onNewIntent(Intent intent) {
        Tag tag = (Tag) intent.getParcelableExtra("android.nfc.extra.TAG");
        if (tag == null) {
            return;
        }
        if (this.accion == 0) {
            Toast.makeText(this, "Etiqueta detectada, seleccione una acción", 0).show();
            return;
        }
        Ndef ndef = Ndef.get(tag);
        Tag myTag = (Tag) intent.getParcelableExtra("android.nfc.extra.TAG");
        Log.i("tag ID ", myTag.getId().toString());
        String vtag = bytesToHex(myTag.getId());
        Log.i("tax ", vtag);
        if (this.accion == 1) {
        }
        if (this.accion == 2) {
        }
        if (this.accion == 3) {
        }
        if (this.accion == 4) {
            this.username.setText(vtag);
        }
        if (ndef != null) {
        }
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[(bytes.length * 2)];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[(j * 2) + 1] = hexArray[v & 15];
        }
        return new String(hexChars);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void LoginServer() {
        showpDialog();
        try {
            String _response = "";
            Volley.newRequestQueue(this).add(new StringRequest(1, "http://koalafood.com/api/paymentgateway/v2/public/LoginCustomer", new C05733(), new C05744()) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap();
                    params.put("email", Login.this.em.toString());
                    params.put("pwd", Login.this.pw.toString());
                    Log.d("PARAMETROS", params.toString());
                    return params;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), 1).show();
        }
    }

    public String md5(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (byte b : messageDigest) {
                hexString.append(Integer.toHexString(b & 255));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void showpDialog() {
        if (!this.pDialog.isShowing()) {
            this.pDialog.show();
        }
    }

    private void hidepDialog() {
        if (this.pDialog.isShowing()) {
            this.pDialog.dismiss();
        }
    }
}
