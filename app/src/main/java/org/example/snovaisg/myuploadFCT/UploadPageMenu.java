package org.example.snovaisg.myuploadFCT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;

/**
 * Created by snovaisg on 1/18/18.
 */

public class UploadPageMenu extends AppCompatActivity {
    public static String[] menu = new String[8];
    public static String[] preco = new String[8];
    public String[] backupPrato = new String[8];
    public String[] backupPreco = new String[8];
    int[] ids;
    int[] ids_preco;
    public DataHolder  DH = new DataHolder().getInstance();
    String ServerFilename = DH.ServerFilename();
    String fileToInternal = DH.fileToInternal();
    String PrecoInvalido = DH.PrecoInvalido();
    String restaurante = DH.getData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_page_menu);
        ids = new int[]{R.id.Menu1,R.id.Menu2,R.id.Menu3,R.id.Menu4,R.id.Menu5,R.id.Menu6,R.id.Menu7,R.id.Menu8};
        ids_preco = new int[]{R.id.precoMenu1,R.id.precoMenu2,R.id.precoMenu3,R.id.precoMenu4,R.id.precoMenu5,R.id.precoMenu6,R.id.precoMenu7,R.id.precoMenu8};
        Preencher();
        try {
            Editar();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Button cancelar = (Button) findViewById(R.id.button4);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DH.setEditar(false);
                    DH.setDenovo(false);
                    if (getEstado()) {
                        Intent intent = new Intent(UploadPageMenu.this, Atualizado.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(UploadPageMenu.this, PorAtualizar.class);
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
                Intent intent = new Intent(UploadPageMenu.this, UploadPageSobremesa.class);
                startActivity(intent);
            }

        });

        Button anterior = (Button) findViewById(R.id.button3);
        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordString();
                goToVegetariano();
            }
        });

    }

    void recordString(){
        getAll();
    }


    public void goToVegetariano()
    {
        Intent intent = new Intent(UploadPageMenu.this, UploadPageVegetariano.class);
        startActivity(intent);
    }

    void goToSobremesa(){
        Intent intent = new Intent(UploadPageMenu.this, UploadPageSobremesa.class);
        startActivity(intent);
    }
    public void getAll(){
        String prato;
        String temp_preco;
        int pos = 0;
        for (int i = 0; i<= 7;i++){
            EditText temp = (EditText) findViewById(ids[i]);
            prato = temp.getText().toString();
            if (!prato.isEmpty() && prato.trim().length() > 0){
                menu[pos] = prato;
                EditText tempPreco = (EditText) findViewById(ids_preco[i]);
                temp_preco = tempPreco.getText().toString();
                if (!temp_preco.isEmpty() || temp_preco.trim().length() > 0)
                    preco[pos] = temp_preco;
                else
                    preco[pos] = PrecoInvalido;
            }
            else{
                Log.d("Prato","NOT IN");
                menu[pos]=null;
                preco[pos]=null;
            }
            pos++;
        }
    }

    public void Preencher() {
        if (!DH.getDenovo()) {
            if (menu != null) {
                int e = menu.length;
                for (int i = 0; i < e; i++) {
                    if (menu[i] != null) {
                        //prato
                        EditText temp = (EditText) findViewById(ids[i]);
                        temp.setText(menu[i]);

                        //preco
                        EditText temp2 = (EditText) findViewById(ids_preco[i]);
                        if (preco[i] != PrecoInvalido || preco[i].isEmpty())
                            temp2.setText(preco[i]);
                    }
                }
            }
        }
    }

    public void Editar() throws JSONException {
        if (DH.getEditar()){
            Log.d("Editar","1");

            JSONObject fullmenu = readJsonFromFile(fileToInternal);
            JSONObject menu = fullmenu.getJSONObject(restaurante);
            String [] vegV1 = toStringArray(menu.getJSONArray("menu"));
            Log.d("Editar","2");
            int pos = 0;
            for (int i = 0; i < vegV1.length; i = i + 2) {
                Log.d("Editar","3");
                if (vegV1[i] != null && vegV1[i+1] != null) {
                    Log.d("Editar","4");
                    //prato
                    EditText temp = (EditText) findViewById(ids[pos]);
                    temp.setText(vegV1[i]);
                    //preco
                    EditText temp2 = (EditText) findViewById(ids_preco[pos]);
                    if (!vegV1[i+1].equals(PrecoInvalido) && !vegV1[i+1].isEmpty())
                        temp2.setText(vegV1[i+1]);
                    pos++;
                }
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        int pos = 0;
        if (menu != null) {
            for (int i = 0; i < menu.length; i++) {
                if (menu[i] != null){
                    if (menu[i].length() >3){
                        savedInstanceState.putString(backupPrato[pos], menu[i]);
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
                menu[i] = backupPrato[i];
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

