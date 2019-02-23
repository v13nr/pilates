package io.github.nanangrustianto.pilates;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.github.nanangrustianto.pilates.model.Veiculo;

public class HomeActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    private JsonParser jsonParser;
    private Gson gson;
    RequestQueue queue;
    Bundle bundle;
    Veiculo veiculo;

    ImageView viewLogo;
    ImageView viewLogin;
    ImageView viewRegister;
    ImageView viewAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        bundle = getIntent().getExtras();

        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Carregando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        jsonParser = new JsonParser();
        gson = new Gson();

        queue = Volley.newRequestQueue(this);

        findViewById(R.id.btnGrupKelas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                Intent intent = new Intent(getApplicationContext(), BookingClass.class);
                startActivity(intent);
            }
        });


        viewLogo = (ImageView) findViewById(R.id.imageViewLogo);
        viewLogin = (ImageView) findViewById(R.id.imageViewLogin);
        viewRegister = (ImageView) findViewById(R.id.imageViewregister);
        viewAbout = (ImageView) findViewById(R.id.imageViewAbout);

        scaleImage(viewLogo, 550); // in dp
        scaleImage(viewLogin, 100); // in dp
        scaleImage(viewRegister, 140); // in dp
        scaleImage(viewAbout, 160); // in dp
        setMargins(viewLogo, 50, 50, 50, 50);
        setMargins(viewLogin, 140, 170, 50, 50);
        setMargins(viewRegister, 280, 170, 50, 50);
        setMargins(viewAbout, 460, 170, 50, 50);
        viewLogo.getLayoutParams().width=692;
        viewLogo.requestLayout();

        findViewById(R.id.btneditar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AddVeiculo.class);
                i.putExtra("editar", "editar");
                i.putExtra("marca", veiculo.getMarca());
                i.putExtra("modelo", veiculo.getModelo());
                i.putExtra("cor", veiculo.getCor());
                i.putExtra("ano", veiculo.getAno());
                i.putExtra("preco", veiculo.getPreco());
                i.putExtra("ehnovo", veiculo.getEhnovo());
                i.putExtra("descricao", veiculo.getDescricao());
                i.putExtra("id", veiculo.getId());
                i.putExtra("dt_cadastro", veiculo.getDt_cadastro());
                v.getContext().startActivity(i);


            }
        });
        findViewById(R.id.btnremover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] lista = new String[2];
                lista[0]="Sim";
                lista[1]="Não";

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Tem certeza que deseja remover este veículo?");
                builder.setItems(lista, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (which==0){
                            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                    getString(R.string.webservice)+"deleteVeiculo.php", new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response
                                    // string.
                                    try{

                                        progressDialog.cancel();
                                        Toast.makeText(HomeActivity.this, response, Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(HomeActivity.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);


                                    }catch (Exception e){
                                        Toast.makeText(HomeActivity.this, "Terkendalan dengan service data..1", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                        progressDialog.cancel();

                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.cancel();

                                    Toast.makeText(HomeActivity.this,
                                            "Terkendalan dengan service data..2",
                                            Toast.LENGTH_LONG).show();

                                }

                            }) {
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("PATH", "deleteVeiculo");
                                    params.put("ID", bundle.getString("id"));


                                    return params;
                                };
                            };
                            // Add the request to the RequestQueue.

                            queue.add(stringRequest);

                        }

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();




            }
        });




        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                getString(R.string.webservice)+"getVeiculo.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Display the first 500 characters of the response
                // string.


                try{

                    JsonArray mJson = (JsonArray) jsonParser
                            .parse(response);

                    veiculo = gson.fromJson(mJson.get(0), Veiculo.class);

                    //TextView marca = findViewById(R.id.txtmarca);
                    //TextView modelo = findViewById(R.id.txtmodelo);
                    //TextView cor = findViewById(R.id.txtcor);
                    //TextView ano = findViewById(R.id.txtano);
                    //TextView preco = findViewById(R.id.txtpreco);
                    //TextView descricao = findViewById(R.id.txtdescricao);
                    TextView ehnovo = findViewById(R.id.txtehnovo);
                    //TextView dt_cadastro = findViewById(R.id.txtdt_cadastro);
                    //TextView dt_atualizacao = findViewById(R.id.txtdt_atualizacao);

                    //marca.setText(veiculo.getMarca());
                    //modelo.setText(veiculo.getModelo());
                    //cor.setText(veiculo.getCor());
                    //ano.setText(veiculo.getAno());
                    //preco.setText(veiculo.getPreco());
                    //descricao.setText(veiculo.getDescricao());

                    if(veiculo.getEhnovo().equalsIgnoreCase("1")){
//                        ehnovo.setText("Novo");
                    }else {
 //                       ehnovo.setText("Usado");
                    }

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    SimpleDateFormat formatterbr = new SimpleDateFormat("dd/MM/yyyy 'às' hh:mm");
                    Date result = formatter.parse (veiculo.getDt_cadastro());
                    //dt_cadastro.setText(formatterbr.format(result));

                    try{
                        if (!veiculo.getDt_atualizacao().trim().equalsIgnoreCase("")){
                            result = formatter.parse (veiculo.getDt_atualizacao());
                            //dt_atualizacao.setText(formatterbr.format(result));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        //dt_atualizacao.setText(formatterbr.format(result));
                    }

                    progressDialog.cancel();


                }catch (Exception e){
                    Toast.makeText(HomeActivity.this, "Terkendalan dengan service data..3", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    progressDialog.cancel();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();

                Toast.makeText(HomeActivity.this,
                        "Terkendalan dengan service data..4",
                        Toast.LENGTH_LONG).show();

            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("PATH", "getVeiculoDetalhe");
                params.put("ID", bundle.getString("id"));


                return params;
            };
        };
        // Add the request to the RequestQueue.

        queue.add(stringRequest);


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
