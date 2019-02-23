package io.github.nanangrustianto.pilates;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;



public class BookingClass extends AppCompatActivity {


    Spinner spinner;


    ArrayList<String> arrayGrupKelas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_class);


        spinner = (Spinner) findViewById(R.id.spinner);
        //spinner.getBackground().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
        spinner.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                ((TextView) spinner.getSelectedView()).setTextColor(Color.BLACK);
            }
        });


        Log.d("dataLog", getData(AppConfig.PATH_TO_GROUP_CLASS));
        try {
            JSONArray jsonArray = new JSONArray(getData(AppConfig.PATH_TO_GROUP_CLASS));
            for (int i = 0; i < jsonArray.length(); i++) {
                arrayGrupKelas.add(jsonArray.getJSONObject(i).getString("nama_class"));

            }
            ArrayAdapter<String> adapterKelas = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayGrupKelas);
            spinner.setAdapter(adapterKelas);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,arrayGrupKelas
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);


    }

    private String getData(String url) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        String result = "";
        try {
            HttpParams myParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(myParams, 15000);
            HttpConnectionParams.setSoTimeout(myParams, 15000);
            DefaultHttpClient httpClient= new DefaultHttpClient(myParams);

            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);

            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  result;
    }
}
