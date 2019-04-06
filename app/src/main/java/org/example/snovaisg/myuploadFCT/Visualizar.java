package org.example.snovaisg.myuploadFCT;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;

public class Visualizar extends AppCompatActivity {

    LinearLayout preVisLL;
    JSONObject Dic;
    JSONObject fullDic;
    public DataHolder  DH = new DataHolder().getInstance();
    String fileToInternal = DH.fileToInternal();
    String restaurante = DataHolder.getInstance().getData();
    String PrecoInvalido = DH.PrecoInvalido();
    String PrecoInvalidoMensagem = DH.PrecoInvalidoMensagem();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar);
        preVisLL = (LinearLayout) findViewById(R.id.previsLL);
        TextView tv = (TextView) findViewById(R.id.tv10);
        tv.setText(DataHolder.getInstance().getData());

        run();

        Button voltar = (Button) findViewById(R.id.button7);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Visualizar.this, Atualizado.class);
                startActivity(intent);
            }

        });

        Button novo = (Button) findViewById(R.id.button10);
        novo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DH.setEditar(false);
                DH.setDenovo(true);
                Intent intent = new Intent(Visualizar.this, UploadPageSopa.class);
                startActivity(intent);;
            }
        });

        Button editar = (Button) findViewById(R.id.button6);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DH.setEditar(true);
                Intent intent = new Intent(Visualizar.this, UploadPageSopa.class);
                startActivity(intent);;
            }
        });

    }


    public static String[] toStringArray(JSONArray array) {
        if (array == null)
            return null;

        String[] arr = new String[array.length()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = array.optString(i);
        }
        return arr;
    }

    public void proceedNormally() throws JSONException {

        TextView TV0 = new TextView(this);
        TV0.setText("carne");
        TV0.setPadding(0, toDP(24), 0, 0);
        TV0.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        TV0.setTypeface(null, Typeface.BOLD);
        preVisLL.addView(TV0);

        String[] carne = toStringArray(Dic.getJSONArray("carne"));
        String[] peixe = toStringArray(Dic.getJSONArray("peixe"));
        String[] veg = toStringArray(Dic.getJSONArray("vegetariano"));




        for (int e = 0; e < carne.length; e=e+2) {
            TextView TVcarne = new TextView(this);
            if (carne[e+1].equals(PrecoInvalido))
                TVcarne.setText(carne[e] + "\t" + PrecoInvalidoMensagem);
            else
                TVcarne.setText(carne[e] + "\t" + carne[e+1] + "€");
            TVcarne.setPadding(0, toDP(8), 0, 0);
            preVisLL.addView(TVcarne);
        }

        TextView TV1 = new TextView(this);
        TV1.setText("peixe");
        TV1.setPadding(0, toDP(16), 0, 0);
        TV1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        preVisLL.addView(TV1);
        TV1.setTypeface(null, Typeface.BOLD);



        for (int e = 0; e < peixe.length; e=e+2) {
            TextView TVcarne = new TextView(this);
            if (peixe[e+1].equals(PrecoInvalido))
                TVcarne.setText(peixe[e] + "\t" + PrecoInvalidoMensagem);
            else
                TVcarne.setText(peixe[e] + "\t" + peixe[e+1] + "€");
            TVcarne.setPadding(0, toDP(8), 0, 0);
            preVisLL.addView(TVcarne);
        }

        TextView TV2 = new TextView(this);
        TV2.setText("vegetariano");
        TV2.setPadding(0, toDP(16), 0, 0);
        TV2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        TV2.setTypeface(null, Typeface.BOLD);
        preVisLL.addView(TV2);



        for (int e = 0; e < veg.length; e=e+2) {
            TextView TVcarne = new TextView(this);
            if (veg[e+1].equals(PrecoInvalido))
                TVcarne.setText(veg[e] + "\t" + PrecoInvalidoMensagem);
            else
                TVcarne.setText(veg[e] + "\t" + veg[e+1] + "€");
            TVcarne.setPadding(0, toDP(8), 0, 0);
            preVisLL.addView(TVcarne);
        }

    }


    void run() {
        try {
            fullDic = readJsonFromFile(fileToInternal);
            Dic = fullDic.getJSONObject(restaurante);
            DH.setMenu(Dic);
            DH.setFullMenu(fullDic);

            //if (restaurante.equals("Teresa") == true || restaurante.equals("Cantina") == true) {
            try {
                proceedNormally();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            // }
            // else{
            //     mergeAndProceed();
            //  }


        } catch (Exception e) {

        }
    }

    //Não está atualizado (porque provavelmente já não vamos precisar desta função)
    public void mergeAndProceed() throws JSONException {
        TextView TV1 = new TextView(this);
        TV1.setText("Pratos do Dia");
        TV1.setPadding(0, toDP(16), 0, 0);
        TV1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        preVisLL.addView(TV1);
        TV1.setTypeface(null, Typeface.BOLD);

        String[] pratoDoDia = toStringArray(Dic.getJSONObject(restaurante).getJSONArray("pratoDoDia"));


        for (int i = 0;i < pratoDoDia.length;i++){
            TextView TVcarne = new TextView(this);
            TVcarne.setText(pratoDoDia[i]);
            TVcarne.setPadding(0, toDP(8), 0, 0);
            preVisLL.addView(TVcarne);
        }


    }

    public int toDP(int dp){
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (dp*scale + 0.5f);
        return dpAsPixels;
    }


    public void goToNotifications(View view){
        Intent intent = new Intent(Visualizar.this, Notificacoes.class);
        startActivity(intent);
    }

    public JSONObject readJsonFromFile(String filename){
        String JsonData ="";
        JSONObject myJson;
        try {
            FileInputStream fis = this.openFileInput(filename);
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();
            JsonData = new String(buffer);
            Log.d("Hallo",JsonData);
            myJson = new JSONObject(JsonData);
            Log.d("HALLO",myJson.getJSONObject(restaurante).toString());
            return myJson;
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d("MEH","Something went wrong");
            return null;
        }
    }

}
