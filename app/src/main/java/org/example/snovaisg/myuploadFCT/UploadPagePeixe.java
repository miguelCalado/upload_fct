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

public class UploadPagePeixe extends AppCompatActivity {
    public String[] backupPrato = new String[8];
    public String[] backupPreco = new String[8];

    public DataHolder  DH = new DataHolder().getInstance();
    String ServerFilename = DH.ServerFilename();
    String fileToInternal = DH.fileToInternal();
    String PrecoInvalido = DH.PrecoInvalido();
    String restaurante = DH.getData();

    public static String[] peixe = new String[8];
    public static String[] preco = new String[8];
    int[] ids;
    int [] ids_preco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_peixe);
        ids = new int[]{R.id.peixe1,R.id.peixe2,R.id.peixe3,R.id.peixe4,R.id.peixe5,R.id.peixe6,R.id.peixe7,R.id.peixe8};
        ids_preco = new int[]{R.id.precoPeixe1,R.id.precoPeixe2,R.id.precoPeixe3,R.id.precoPeixe4,R.id.precoPeixe5,R.id.precoPeixe6,R.id.precoPeixe7,R.id.precoPeixe8};
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
                    DH.setDenovo(false);
                    if (getEstado()) {
                        Intent intent = new Intent(UploadPagePeixe.this, Atualizado.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(UploadPagePeixe.this, PorAtualizar.class);
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
                if(restaurante.equals("Sector + Dep")) {
                    Intent intent = new Intent(UploadPagePeixe.this, UploadPageDieta.class);
                    startActivity(intent);

                }else {
                    goToVegetariano();
                }

            }

        });

        Button anterior = (Button) findViewById(R.id.button3);
        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordString();
                goToCarne();
            }

        });

    }

    void recordString(){
       getAll();
    }

    public void goToVegetariano()
    {
        Intent intent = new Intent(UploadPagePeixe.this, UploadPageVegetariano.class);
        startActivity(intent);
    }

    public void goToCarne(){
        Intent intent = new Intent(UploadPagePeixe.this, UploadPageCarne.class);
        startActivity(intent);
    }

    public void Editar() throws JSONException {
        if (DH.getEditar()){
            Log.d("Editar","1");

            JSONObject fullmenu = readJsonFromFile(fileToInternal);
            JSONObject menu = fullmenu.getJSONObject(restaurante);
            String [] peixeV1 = toStringArray(menu.getJSONArray("peixe"));
            Log.d("Editar","2");
            int pos = 0;
            for (int i = 0; i < peixeV1.length; i = i + 2) {
                Log.d("Editar","3");
                if (peixeV1[i] != null && peixeV1[i+1] != null) {
                    Log.d("Editar","4");
                    //prato
                    EditText temp = (EditText) findViewById(ids[pos]);
                    temp.setText(peixeV1[i]);
                    //preco
                    EditText temp2 = (EditText) findViewById(ids_preco[pos]);
                    if (!peixeV1[i+1].equals(PrecoInvalido) && !peixeV1[i+1].isEmpty())
                        temp2.setText(peixeV1[i+1]);
                    pos++;
                }
            }
        }

    }

    public void getAll(){
        String prato;
        String temp_preco;
        String [] ans = new String[8];
        int pos = 0;
        for (int i = 0; i<= 7;i++){
            EditText temp = (EditText) findViewById(ids[i]);
            prato = temp.getText().toString();
            if (!prato.isEmpty() && prato.trim().length() > 0) {
                peixe[pos] = prato;
                EditText tempPreco = (EditText) findViewById(ids_preco[i]);
                temp_preco = tempPreco.getText().toString();
                if (!temp_preco.isEmpty() || temp_preco.trim().length() > 0)
                    preco[pos] = temp_preco;
                else
                    preco[pos] = PrecoInvalido;
            }
            else{
                Log.d("Prato","NOT IN");
                peixe[pos]=null;
                preco[pos]=null;
            }
            pos++;
        }
    }

    public void Preencher() {
        if (!DH.getDenovo()) {
            if (peixe != null) {
                int e = peixe.length;
                for (int i = 0; i < e; i++) {
                    if (peixe[i] != null) {
                        EditText temp = (EditText) findViewById(ids[i]);
                        temp.setText(peixe[i]);

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
        if (peixe != null) {
            for (int i = 0; i < peixe.length; i++) {
                if (peixe[i] != null){
                    if (peixe[i].length() >3){
                        savedInstanceState.putString(backupPrato[pos], peixe[i]);

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
                peixe[i] = backupPrato[i];
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
