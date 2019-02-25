package io.github.nanangrustianto.pilates;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
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



public class RegisterActivity extends AppCompatActivity {


    Spinner spinner;

    ImageView viewLogo;
    ImageView viewLogin;
    ImageView viewRegister;
    ImageView viewAbout;
    ImageView devider;

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



        viewLogo = (ImageView) findViewById(R.id.imageViewLogo);
        viewLogin = (ImageView) findViewById(R.id.imageViewLogin);
        viewRegister = (ImageView) findViewById(R.id.imageViewregister);
        viewAbout = (ImageView) findViewById(R.id.imageViewAbout);
        devider = (ImageView) findViewById(R.id.imageViewdevider);

        scaleImage(viewLogo, 550); // in dp
        scaleImage(viewLogin, 100); // in dp
        scaleImage(viewRegister, 140); // in dp
        scaleImage(viewAbout, 160); // in dp
        scaleImage(devider, 210); // in dp
        setMargins(devider, 240, 170, 50, 50);
        setMargins(viewLogo, 50, 30, 50, 50);
        setMargins(viewLogin, 120, 170, 50, 50);
        setMargins(viewRegister, 280, 170, 50, 50);
        setMargins(viewAbout, 470, 170, 50, 50);
        viewLogo.getLayoutParams().width=692;
        viewLogo.requestLayout();

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

    private void scaleImage(ImageView view, int boundBoxInDp)
    {
        // Get the ImageView and its bitmap
        Drawable drawing = view.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

        // Get current dimensions
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) boundBoxInDp) / width;
        float yScale = ((float) boundBoxInDp) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        width = scaledBitmap.getWidth();
        height = scaledBitmap.getHeight();

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    private int dpToPx(int dp)
    {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }


    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
}
