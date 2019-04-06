package org.example.snovaisg.myuploadFCT.Cantina;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.example.snovaisg.myuploadFCT.Atualizado;
import org.example.snovaisg.myuploadFCT.DataHolder;
import org.example.snovaisg.myuploadFCT.R;
import org.example.snovaisg.myuploadFCT.UploadPageSopa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;

/**
 * Created by Miguel-PC on 04/03/2018.
 */

public class CantinaVisualiza extends AppCompatActivity {

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
        setContentView(R.layout.cantina);

        TextView visualizar = (TextView) findViewById(R.id.textView6);
        visualizar.setText("Visualizar");

        run();

        Button voltar = (Button) findViewById(R.id.button7);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CantinaVisualiza.this, Atualizado.class);
                startActivity(intent);
            }

        });

        Button novo = (Button) findViewById(R.id.button10);
        novo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DH.setEditar(false);
                DH.setDenovo(true);
                Intent intent = new Intent(CantinaVisualiza.this, UploadPageSopa.class);
                startActivity(intent);
                ;
            }
        });

        Button editar = (Button) findViewById(R.id.button6);
        editar.setText("Editar");
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DH.setEditar(true);
                Intent intent = new Intent(CantinaVisualiza.this, UploadPageSopa.class);
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

        String[] sopa = toStringArray(Dic.getJSONArray("sopa"));

        String sopa_final = final_String2(sopa);
        TextView sopas = (TextView) findViewById(R.id.sopaText);
        sopas.setText(sopa_final);

        String[] carne = toStringArray(Dic.getJSONArray("carne"));
        String carne_final = final_String2(carne);
        final TextView carnes = (TextView) findViewById(R.id.carneText);
        carnes.setText(carne_final);

        String[] peixe = toStringArray(Dic.getJSONArray("peixe"));
        String peixe_final = final_String2(peixe);
        TextView peixes = (TextView) findViewById(R.id.peixeText);
        peixes.setText(peixe_final);

        String[] sobremesa = toStringArray(Dic.getJSONArray("sobremesa"));
        String sobremesa_final = final_String2(sobremesa);
        TextView sobremesas = (TextView) findViewById(R.id.sobremesaText);
        sobremesas.setText(sobremesa_final);
    }

    public String final_String2 (String pratos[]) {
        String prato_final="";
        for (int i=0; i<pratos.length; i=i+2) {
            prato_final = prato_final+pratos[i];
            if(i<pratos.length-2) {
                prato_final = prato_final+"\n";
            }
        }
        return prato_final;

    }

}
