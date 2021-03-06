package org.example.snovaisg.myuploadFCT.CasaPessoal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.example.snovaisg.myuploadFCT.Atualizado;
import org.example.snovaisg.myuploadFCT.DataHolder;
import org.example.snovaisg.myuploadFCT.UploadPageSopa;
import org.example.snovaisg.myuploadFCT.alignPrices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;

/**
 * Created by Miguel-PC on 04/03/2018.
 */

public class CasaPVisualiza extends AppCompatActivity {

    JSONObject Dic;
    JSONObject fullDic;
    public DataHolder DH = new DataHolder().getInstance();
    String fileToInternal = DH.fileToInternal();
    String restaurante = DataHolder.getInstance().getData();
    String PrecoInvalido = DH.PrecoInvalido();
    String PrecoInvalidoMensagem = DH.PrecoInvalidoMensagem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.example.snovaisg.myuploadFCT.R.layout.casapessoal);

        TextView visualizar = (TextView) findViewById(org.example.snovaisg.myuploadFCT.R.id.textView6);
        visualizar.setText("Visualizar");

        run();

        Button voltar = (Button) findViewById(org.example.snovaisg.myuploadFCT.R.id.button7);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CasaPVisualiza.this, Atualizado.class);
                startActivity(intent);
            }

        });

        Button novo = (Button) findViewById(org.example.snovaisg.myuploadFCT.R.id.button10);
        novo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DH.setEditar(false);
                DH.setDenovo(true);
                Intent intent = new Intent(CasaPVisualiza.this, UploadPageSopa.class);
                startActivity(intent);
            }
        });

        Button editar = (Button) findViewById(org.example.snovaisg.myuploadFCT.R.id.button6);
        editar.setText("Editar");
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DH.setEditar(true);
                Intent intent = new Intent(CasaPVisualiza.this, UploadPageSopa.class);
                startActivity(intent);
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

    public void proceedNormally() throws JSONException {

        TextView menu = (TextView) findViewById(org.example.snovaisg.myuploadFCT.R.id.menu_textView);
        int aa = menu.getCurrentTextColor();

        String[] sopa = toStringArray(Dic.getJSONArray("sopa"));
        LinearLayout sopaLayout = (LinearLayout) findViewById(org.example.snovaisg.myuploadFCT.R.id.sopaLayout) ;
        alignPrices.getInstance().alignOne(sopa, this, aa, sopaLayout);

        LinearLayout pratoDiaLayout = (LinearLayout) findViewById(org.example.snovaisg.myuploadFCT.R.id.pratoDiaLayout);
        String[] carne = toStringArray(Dic.getJSONArray("carne"));
        String[] peixe = toStringArray(Dic.getJSONArray("peixe"));
        String vegetariano[] = toStringArray(Dic.getJSONArray("vegetariano"));
        String outra [] = combine (carne, peixe);
        String [] pratoDia = combine(outra, vegetariano);
        alignPrices.getInstance().alignOne(pratoDia, this, aa, pratoDiaLayout);

        LinearLayout sobremesaLayout = (LinearLayout) findViewById(org.example.snovaisg.myuploadFCT.R.id.sobremesaLayout);
        String[] sobremesa = toStringArray(Dic.getJSONArray("sobremesa"));
        alignPrices.getInstance().alignOne(sobremesa, this, aa, sobremesaLayout);

    }

    public static String[] combine(String[] a, String[] b){
        if(a.length==1) {
            return b;
        }
        if(b.length==1) {
            return a;
        }
        int length = a.length + b.length;
        String [] result = new String[length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

}
