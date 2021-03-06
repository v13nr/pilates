package io.github.nanangrustianto.pilates;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.github.nanangrustianto.pilates.adapter.VeiculoListAdapter;
import io.github.nanangrustianto.pilates.model.Veiculo;

public class MainActivity extends AppCompatActivity {

    ListView veiculosListView;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefresh;
    ArrayList<Veiculo> veiculosList;
    TextView errormessage;
    RequestQueue queue;

    private JsonParser jsonParser;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        veiculosListView = findViewById(R.id.veiculosListView);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        veiculosList = new ArrayList<>();
        errormessage = findViewById(R.id.errormessage);

        jsonParser = new JsonParser();
        gson = new Gson();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                carregarLista();

            }
        });



        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Get Dinamy Menu...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        queue = Volley.newRequestQueue(this);

        carregarLista();

    }


    public void carregarLista(){

        veiculosList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.PATH_TO_MENU_DEPAN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response
                // string.


                try{

                    JsonArray mJson = (JsonArray) jsonParser
                            .parse(response);

                    veiculosList = new ArrayList<>();


                    for (int i = 0; i < mJson.size(); i++) {
                        Veiculo object = gson.fromJson(mJson.get(i),
                                Veiculo.class);
                        veiculosList.add(object);


                    }

                    VeiculoListAdapter veiculoListAdapter = new VeiculoListAdapter(MainActivity.this,veiculosList);

                    veiculosListView.setAdapter(veiculoListAdapter);
                    errormessage.setText("Daftar Tidak dapat di load.");
                    veiculosListView.setEmptyView(errormessage);

                    progressDialog.cancel();
                    swipeRefresh.setRefreshing(false);

                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Problema dengan Server.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    progressDialog.cancel();
                    swipeRefresh.setRefreshing(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                swipeRefresh.setRefreshing(false);
                Toast.makeText(MainActivity.this,
                        "Problema dengan web service!",
                        Toast.LENGTH_LONG).show();

            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("PATH", "getVeiculos");


                return params;
            };
        };
        // Add the request to the RequestQueue.

        queue.add(stringRequest);
    }



}
