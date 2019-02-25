package io.github.nanangrustianto.pilates;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import io.github.nanangrustianto.MyTextView;


public class RegisterActivity extends AppCompatActivity {


    ImageView viewLogo;
    ImageView viewLogin;
    ImageView viewRegister;
    ImageView viewAbout;
    ImageView devider;
    Context context;

    Button registerbt;

    Dialog dialog_logout;
    MyTextView btn_no, btn_yes;

    Dialog dialog_informasi;
    Dialog dialog_informasi2;
    MyTextView btn_ok;
    MyTextView text_title;
    MyTextView text_message;

    Dialog dialog_pilih_gambar;
    MyTextView from_camera, from_galery;

    ArrayList<String> arrayGrupKelas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);





        viewLogo = (ImageView) findViewById(R.id.imageViewLogo);
        viewLogin = (ImageView) findViewById(R.id.imageViewLogin);
        viewRegister = (ImageView) findViewById(R.id.imageViewregister);
        viewAbout = (ImageView) findViewById(R.id.imageViewAbout);
        devider = (ImageView) findViewById(R.id.imageViewdevider);
        registerbt = (Button) findViewById(R.id.btSubmitRegister);

        registerbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                String[] lista = new String[2];
                lista[0]="Ya";
                lista[1]="Tidak";
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Yakin akan Register ?");
                builder.setItems(lista, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (which==0){

                      */
                            new prosesSaveRegister("1","1","1","1","1","1","1","1","1","1","1","1");
                      /*
                        }else{

                        }

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                */
            }
        });


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

    public void prosesRegister(String jenis_lapak, String nama_bank, String norek, String npwp, String nama_anggota, String nik, String hp, String email, String password, String namapasar, String namalapak, String alamat) {

        new prosesSaveRegister(jenis_lapak, nama_bank, norek, npwp, nama_anggota, nik, hp, email, password, namapasar, namalapak, alamat).execute();

    }

    public class prosesSaveRegister extends AsyncTask<String, Void, JSONObject> {

        String jenis_lapak;

        String nama_bank, password, namapasar, namalapak, alamat;

        String norek;

        String npwp, nama_anggota, nik, hp, email;

        prosesSaveRegister(String jenis_lapak, String nama_bank, String norek, String npwp, String nama_anggota, String nik, String hp, String email, String password, String namapasar, String namalapak, String alamat) {
            this.jenis_lapak = jenis_lapak;
            this.nama_bank = nama_bank;
            this.norek = norek;
            this.npwp = npwp;
            this.nik = npwp;
            this.nama_anggota = nama_anggota;
            this.hp = hp;
            this.email = email;
            this.password = password;
            this.namapasar = namapasar;
            this.namalapak = namalapak;
            this.alamat = alamat;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... urls) {

            try {
                String url = AppConfig.PATH_TO_REGISTER;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);

                MultipartEntity reqEntity = new MultipartEntity();
                reqEntity.addPart("jenis_lapak", new StringBody(jenis_lapak));
                reqEntity.addPart("nama_bank", new StringBody(nama_bank));
                reqEntity.addPart("norek", new StringBody(norek));
                reqEntity.addPart("npwp", new StringBody(npwp));


                httppost.setEntity(reqEntity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity resEntity = response.getEntity();
                InputStream is = resEntity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;


                while ((line = reader.readLine()) != null) {
                    Log.i("Line", line+"\n");
                    sb.append(line + "\n");
                }
                is.close();
                String json = sb.toString();
                System.out.println(json);

                return new JSONObject(json);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            boolean success = false;
            String message = "Proses kirim gagal.";

            try {
                success = result.isNull("success")?false:result.getBoolean("success");
                message = result.isNull("message")?"":result.getString("message");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(success) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                if(message=="Data GAGAL terkirim."){

                    text_message.setText(message, null);
                    text_title.setText(success?"BERHASIL":"GAGAL");
                    dialog_informasi.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog_informasi.show();

                } else {


                    text_message.setText("ingat email Anda : ", null);
                    text_title.setText(success?"BERHASIL":"GAGAL");
                    dialog_informasi.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog_informasi.show();


                }

            } else {
                text_message.setText(message);
                text_title.setText(success?"BERHASIL":"GAGAL");
                dialog_informasi.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog_informasi.show();
            }

        }
    }


}
