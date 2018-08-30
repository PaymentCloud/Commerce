package paymentcloud.creaj.sv.com.comercio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class Summary extends AppCompatActivity {
     ProgressDialog pDialog;

    TextView txtCharged, txtCredit;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        txtCharged = findViewById(R.id.txtCharged);
        txtCredit = findViewById(R.id.txtAmount);
        pDialog = new ProgressDialog(this);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(i);
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String pnfc_tag = prefs.getString("pnfc_tag", null);

        actLeer(pnfc_tag);
    }




    public void actLeer(final String nfctag){//4

        showpDialog();
        try {
            final String _response = "";
            RequestQueue queue = Volley.newRequestQueue(Summary.this);
            String url = "http://koalafood.com/api/paymentgateway/v2/public/getMyWallet";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("----respuesta",response.toString());
                            try{
                                JSONArray ja =new JSONArray(response);
                                JSONObject profile = new JSONObject(ja.get(0).toString());
                                String current = profile.getString("current_balance");
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                String charged = prefs.getString("charged", null);

                                txtCredit.setText("Tu Saldo restante: $" +current);
                                txtCharged.setText("Saldo debitado fue: $" +charged);
                                hidepDialog();

                            } catch (Exception s){
                                Log.d("ERROR",s.toString());
                                hidepDialog();
                                Toast.makeText(getApplicationContext(), "No se pudo realizar operacion, intenta nuevamente", Toast.LENGTH_SHORT).show();
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
                    params.put("pnfc_tag", nfctag);
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



        pDialog.setMessage("Espere un momento");
        pDialog.setCancelable(false);
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
