package paymentcloud.creaj.sv.com.comercio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by alexg on 11/05/2018.
 */

public class Login extends AppCompatActivity {

    String em,pw;
    private ProgressDialog pDialog;
    final protected static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //TextView tv2 = this.findViewById(R.id.tv2);

        Button btnLogin = this.findViewById(R.id.btn_login);

        final EditText username = this.findViewById(R.id.input_email);
        final EditText password = this.findViewById(R.id.input_balance);
        final Button readNFCButton = this.findViewById(R.id.readNFC);


        pDialog = new ProgressDialog(this);

        pDialog.setMessage("Espere un momento");
        pDialog.setCancelable(false);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                em=username.getText().toString();
                pw= password.getText().toString();
                if(em.length()>4&&pw.length()>2){
                    LoginServer();
                }
                else{
                    Snackbar.make(v, "Ingrese los datos por favor", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

        boolean isLogged = false;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        isLogged = preferences.getBoolean("is_logged",false);
        if(isLogged){
            Intent i = new Intent(this, Main2Activity.class);
            startActivity(i);
            finish();
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


    public void LoginServer(){
        showpDialog();
        try {
            final String _response = "";
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://koalafood.com/api/paymentgateway/v2/public/LoginBusiness";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("----respuesta",response.toString());
                            try{

                                JSONArray ja =new JSONArray(response);
                                JSONObject profile = new JSONObject(ja.get(0).toString());
                                String id = profile.getString("user_id");

                                String name = profile.getString("name");
                                String mail = profile.getString("mail");
                                String phone = profile.getString("id_wallet");

                                Log.d("uid",id.toString());
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                final SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("user_id", String.valueOf(id));

                                editor.putString("user_name", String.valueOf(name));
                                editor.putString("user_mail", String.valueOf(mail));
                                editor.putString("id_wallet", String.valueOf(phone));

                                editor.putBoolean("is_logged", true);
                                editor.commit();
                                hidepDialog();
                                Intent i = new Intent(Login.this, Main2Activity.class);
                                startActivity(i);
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


                    params.put("email", em.toString());
                    params.put("pwd", pw.toString());


                    Log.d("PARAMETROS",params.toString());
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
