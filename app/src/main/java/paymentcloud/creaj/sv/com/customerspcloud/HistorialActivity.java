package paymentcloud.creaj.sv.com.customerspcloud;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HistorialActivity extends AppCompatActivity {
        private List<Historial> movieList = new ArrayList<>();
        private RecyclerView recyclerView;
        private Adapter mAdapter;
    protected RequestQueue queue;
    private Adapter adapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_historial);


            recyclerView = (RecyclerView) findViewById(R.id.recycleview);
            queue = Volley.newRequestQueue(this);
            mAdapter = new Adapter(movieList);

            recyclerView.setHasFixedSize(true);

            // vertical RecyclerView
            // keep movie_list_row.xml width to `match_parent`
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

            // horizontal RecyclerView
            // keep movie_list_row.xml width to `wrap_content`
            // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

            recyclerView.setLayoutManager(mLayoutManager);

            // adding inbuilt divider line


            // adding custom divider line with padding 16dp
            // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.HORIZONTAL, 16));
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            recyclerView.setAdapter(mAdapter);



            prepareMovieData();
        }

        /**
         * Prepares sample data to provide data set to adapter
         */
        private void prepareMovieData() {
            StringRequest postRequest = new StringRequest(Request.Method.POST, "https://laguan099.000webhostapp.com/Historial/historial.php",
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response

                            GsonBuilder builder = new GsonBuilder();
                            Gson mGson = builder.create();

                            movieList= new ArrayList<Historial>();
                            movieList = Arrays.asList(mGson.fromJson(response, Historial[].class));
                            for (Historial post : movieList) {
                                Log.d("Response", post.getAmount());
                            }
                            adapter = new Adapter(movieList);
                            recyclerView.setAdapter(adapter);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.toString());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    Map<String, String>  params = new HashMap<String, String>();
                    String wallet = prefs.getString("id_wallet", null);
                    params.put("wallet_id", wallet);


                    return params;
                }
            };
            queue.add(postRequest);

            // notify adapter about data set changes
            // so that it will render the list with new data
            mAdapter.notifyDataSetChanged();
        }

    }
