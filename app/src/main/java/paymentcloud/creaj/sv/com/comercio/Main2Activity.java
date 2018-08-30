package paymentcloud.creaj.sv.com.comercio;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.preference.PreferenceManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {


    String em,pw;
    TextView txtbussinessid;
    private ProgressDialog pDialog;
    final protected static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    private NfcAdapter mNfcAdapter;
    int accion =0;
    String name, user_id;
    EditText  credittocharge;
    EditText summary;
    TextView username;

    int count = 0;
    int preciotem = 0;
    int precio1 = 50;

    int count2 = 0;
    int preciotem2 = 0;
    int precio2 = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button btnCharge = this.findViewById(R.id.btn_charge);

        username = this.findViewById(R.id.input_NFC);
        credittocharge = this.findViewById(R.id.input_balance);
        summary = this.findViewById(R.id.input_summary);

        final Button readNFCButton = this.findViewById(R.id.readNFC);

        //Preferences
        boolean isLogged = false;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        isLogged = preferences.getBoolean("is_logged",false);
        name = preferences.getString("name", null);
        user_id = preferences.getString("user_id", null);


        readNFCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accion=4;

            }
        });

        btnCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (username.length()>3 && credittocharge.length()>0){

                        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                        builder.setMessage("Deseas hacer efectivo este cobro?")
                                .setCancelable(false)
                                .setPositiveButton("Cobrar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Chargewallet(credittocharge.getText().toString(),username.getText().toString(),user_id);
                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }else{
                        Toast.makeText(Main2Activity.this, "Datos no validos ", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){

                }
            }
        });


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Espere un momento");
        pDialog.setCancelable(false);

        initNFC();

        final Button btn1product1 = findViewById(R.id.btn1product1);
        btn1product1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                btn1product1.setText("Cantidad X"+String.valueOf(count));

                preciotem += precio1;

                credittocharge.setText(String.valueOf(preciotem + preciotem2));

            }
        });


        final Button btn1product2 = findViewById(R.id.btn1product2);
        btn1product2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count2++;
                btn1product2.setText("Cantidad X"+String.valueOf(count2));

                preciotem2 += precio2;

                credittocharge.setText(String.valueOf(preciotem + preciotem2));

            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(mNfcAdapter!= null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

    }
    private void initNFC(){

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if(tag != null) {
            if(accion==0){
                Toast.makeText(this, "Etiqueta detectada, seleccione una acción", Toast.LENGTH_SHORT).show();
            }
            else{

                Ndef ndef = Ndef.get(tag);
                Tag myTag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                Log.i("tag ID ", myTag.getId().toString());
                String vtag= bytesToHex(myTag.getId());
                Log.i("tax ", vtag);

                if(accion==1){

                }
                if(accion==2){

                }
                if(accion==3){

                }
                if(accion==4){
                    username.setText(vtag);
                }

                if (ndef == null) {
                    return;
                }

            }




        }
    }

    public void Chargewallet(final String credit, final String nfc, final String businessid){
        showpDialog();
        try {
            final String _response = "";
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://koalafood.com/api/paymentgateway/v2/public/chargeWallet";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("----respuesta",response.toString());
                            hidepDialog();

                            try{

                                JSONObject profile = new JSONObject(response);

                                String amount = profile.getString("amount");
                                String pnfc_tag = profile.getString("pnfc_tag");
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                final SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("charged", String.valueOf(amount));
                                editor.putString("pnfc_tag", String.valueOf(pnfc_tag));
                                editor.commit();
                                Intent i = new Intent(Main2Activity.this, Lottie.class);
                                startActivity(i);

                                hidepDialog();


                            } catch (Exception s){
                                Log.d("ERROR",s.toString());
                                if(response.equals("{'login': 'false'}")){
                                    hidepDialog();
                                    Toast.makeText(getApplicationContext(), "Error en los datos ingresados, intenta nuevamente", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // _response.setText("That didn't work!");
                    Log.d("respuesta error",error.toString());
                }
            }) {
                //adding parameters to the request
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("amount", credit);
                    params.put("user_id", businessid);
                    params.put("pnfc_tag", nfc);
                    params.put("custom_description", summary.getText().toString());
                    return params;
                }
            };
            queue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }



    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void onBackPressed() {
        if (Integer.valueOf(Integer.valueOf(0).intValue() + 1).intValue() != 1) {
            finish();
        }
    }

}
