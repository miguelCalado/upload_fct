package org.example.snovaisg.myuploadFCT;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;

public class UploadPageCarne extends AppCompatActivity {
    public DataHolder  DH = new DataHolder().getInstance();
    String fileToInternal = DH.fileToInternal();
    String PrecoInvalido = DH.PrecoInvalido();
    String restaurante = DH.getData();

    public static String[] carne = new String[12];
    public static String[] preco = new String[12];
    int[] ids;
    int [] ids_preco;

    // para fazer auto fill quando se volta à página
    public String[] backupPrato = new String[12];
    public String[] backupPreco = new String[12];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_page_carne);
        ids = new int[]{R.id.carne1,R.id.carne2,R.id.carne3,R.id.carne4,R.id.carne5,R.id.carne6,R.id.carne7,R.id.carne8, R.id.carne9,R.id.carne10, R.id.carne11, R.id.carne12};
        ids_preco = new int[]{R.id.precoCarne1,R.id.precoCarne2,R.id.precoCarne3,R.id.precoCarne4,R.id.precoCarne5,R.id.precoCarne6,R.id.precoCarne7,R.id.precoCarne8,R.id.precoCarne9, R.id.precoCarne10, R.id.precoCarne11, R.id.precoCarne12};
        Preencher();
        try {
            Editar();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Button cancelar = (Button) findViewById(R.id.button14);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DH.setEditar(false);
                    if (getEstado()) {
                        Intent intent = new Intent(UploadPageCarne.this, Atualizado.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(UploadPageCarne.this, PorAtualizar.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Button seguinte = (Button) findViewById(R.id.button2);
        seguinte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordString();
                goToPeixe();
            }

        });


        Button anterior = (Button) findViewById(R.id.button3);
        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordString();
                goToSopa();
            }

        });

    }

    void recordString(){
        getAll();
    }

    void goToSopa() {
        Intent intent = new Intent(UploadPageCarne.this, UploadPageSopa.class);
        startActivity(intent);
    }

    void goToPeixe() {
        Intent intent = new Intent(UploadPageCarne.this, UploadPagePeixe.class);
        startActivity(intent);
    }


    public void Editar() throws JSONException {
        if (DH.getEditar()){
            Log.d("Editar","1");

            JSONObject fullmenu = readJsonFromFile(fileToInternal);
            JSONObject menu = fullmenu.getJSONObject(restaurante);
            String [] carneV1 = toStringArray(menu.getJSONArray("carne"));
            Log.d("Editar","2");
            int pos = 0;
                for (int i = 0; i < carneV1.length; i = i + 2) {
                    Log.d("Editar","3");
                    if (carneV1[i] != null && carneV1[i+1] != null) {
                        Log.d("Editar","4");
                        //prato
                        EditText temp = (EditText) findViewById(ids[pos]);
                        temp.setText(carneV1[i]);
                        //preco
                        EditText temp2 = (EditText) findViewById(ids_preco[pos]);
                        if (!carneV1[i+1].equals(PrecoInvalido) && !carneV1[i+1].isEmpty())
                            temp2.setText(carneV1[i+1]);
                        pos++;
                    }
                }
            }

        }


    public void getAll(){
        String prato;
        String temp_preco;
        String [] ans = new String[12];
        int pos = 0;
        for (int i = 0; i<= 11;i++){
            EditText tempPrato = (EditText) findViewById(ids[i]);
            prato = tempPrato.getText().toString();
            Log.d("PRATO","<"+prato+">");
            if (!prato.isEmpty() && prato.trim().length() > 0) {
                Log.d("Prato","im IN");
                carne[pos] = prato;
                EditText tempPreco = (EditText) findViewById(ids_preco[i]);
                temp_preco = tempPreco.getText().toString();
                if (!temp_preco.isEmpty() && temp_preco.trim().length() > 0)
                    preco[pos] = temp_preco;
                else
                    preco[pos] = PrecoInvalido;
            }
            else{
                Log.d("Prato","NOT IN");
                carne[pos]=null;
                preco[pos]=null;
            }
            pos++;
        }
    }
    public void Preencher() {
        if (!DH.getDenovo()) {
            if (carne != null) {
                int e = carne.length;
                for (int i = 0; i < e; i++) {
                    if (carne[i] != null) {
                        //prato
                        EditText temp = (EditText) findViewById(ids[i]);
                        temp.setText(carne[i]);
                        //preco
                        EditText temp2 = (EditText) findViewById(ids_preco[i]);
                        if (preco[i] != PrecoInvalido || preco[i].isEmpty())
                            temp2.setText(preco[i]);
                    }
                }
            }
        }
    }



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        int pos = 0;
        if (carne != null) {
            for (int i = 0; i < carne.length; i++) {
                if (carne[i] != null){
                    if (carne[i].trim().length() >0){
                        savedInstanceState.putString(backupPrato[pos], carne[i]);
                        savedInstanceState.putString(backupPreco[pos],preco[i]);
                        pos++;
                    }
                }
            }
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int pos = 0;
        if (backupPrato != null){
            for (int i = 0;i < backupPrato.length;i++){
                carne[i] = backupPrato[i];
                preco[i] = backupPreco[i];
            }
        }
    }

    public boolean getEstado() throws JSONException {
        JSONObject fullDic = readJsonFromFile(fileToInternal);
        String weekId = fullDic.getJSONObject(restaurante).getString("weekId");
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date());
        Log.d("TESTE",weekId);
        Log.d("TESTE",timeStamp);

        return weekId.equals(timeStamp);

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
            int a = 1 + 2;
            return myJson;
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d("MEH","Something went wrong");
            return null;
        }
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

}
