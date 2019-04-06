package org.example.snovaisg.myuploadFCT;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.example.snovaisg.myuploadFCT.BarCampus.BarCampusPreVisualiza;
import org.example.snovaisg.myuploadFCT.Cantina.CantinaPreVisualiza;
import org.example.snovaisg.myuploadFCT.CasaPessoal.CasaPPreVisualiza;
import org.example.snovaisg.myuploadFCT.Come.ComePreVisualiza;
import org.example.snovaisg.myuploadFCT.Girassol.GirassolPreVisualiza;
import org.example.snovaisg.myuploadFCT.Lidia.BarLidiaPreVisualiza;
import org.example.snovaisg.myuploadFCT.MySpot.mySpotPreVisualiza;
import org.example.snovaisg.myuploadFCT.Sector7.Sector7PreVisualiza;
import org.example.snovaisg.myuploadFCT.SectorDep.SectorDepPreVisualiza;
import org.example.snovaisg.myuploadFCT.Teresa.TeresaPreVisualiza;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;

/**
 * Created by Miguel-PC on 03/03/2018.
 */

public class UploadPageSobremesa extends AppCompatActivity {

    public DataHolder DH = new DataHolder().getInstance();
    String fileToInternal = DH.fileToInternal();
    String PrecoInvalido = DH.PrecoInvalido();
    String restaurante = DH.getData();

    public static String[] sobremesa = new String[8];
    public static String[] preco = new String[8];
    int[] ids;
    int[] ids_preco;

    // para fazer auto fill quando se volta à página
    public String[] backupPrato = new String[8];
    public String[] backupPreco = new String[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_page_sobremesa);
        ids = new int[]{R.id.Sobremesa1, R.id.Sobremesa2, R.id.Sobremesa3, R.id.Sobremesa4, R.id.Sobremesa5, R.id.Sobremesa6, R.id.Sobremesa7, R.id.Sobremesa8};
        ids_preco = new int[]{R.id.precoSobremesa1, R.id.precoSobremesa2, R.id.precoSobremesa3, R.id.precoSobremesa4, R.id.precoSobremesa5, R.id.precoSobremesa6, R.id.precoSobremesa7, R.id.precoSobremesa8};
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
                        Intent intent = new Intent(UploadPageSobremesa.this, Atualizado.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(UploadPageSobremesa.this, PorAtualizar.class);
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
                Intent intent;
                if(restaurante.equals("Cantina")) {
                    intent = new Intent(UploadPageSobremesa.this, CantinaPreVisualiza.class);
                } else {
                    if(restaurante.equals("Teresa")) {
                        intent = new Intent(UploadPageSobremesa.this, TeresaPreVisualiza.class);
                    } else {
                        if(restaurante.equals("My Spot")) {
                            intent = new Intent(UploadPageSobremesa.this, mySpotPreVisualiza.class);
                        } else {
                            if(restaurante.equals("Bar Campus")) {
                                intent = new Intent(UploadPageSobremesa.this, BarCampusPreVisualiza.class);
                            } else {
                                if(restaurante.equals("Casa do P.")) {
                                    intent = new Intent(UploadPageSobremesa.this, CasaPPreVisualiza.class);
                                } else {
                                    if(restaurante.equals("C@m. Come")) {
                                        intent = new Intent(UploadPageSobremesa.this, ComePreVisualiza.class);
                                    } else {
                                        if(restaurante.equals("Sector + Dep")) {
                                            intent = new Intent(UploadPageSobremesa.this, SectorDepPreVisualiza.class);
                                        } else {
                                            if(restaurante.equals("Sector + Ed.7")) {
                                                intent = new Intent(UploadPageSobremesa.this, Sector7PreVisualiza.class);
                                            } else {
                                                if(restaurante.equals("Girassol")) {
                                                    intent = new Intent(UploadPageSobremesa.this, GirassolPreVisualiza.class);
                                                } else {
                                                    intent = new Intent(UploadPageSobremesa.this, BarLidiaPreVisualiza.class);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                startActivity(intent);
            }
        });

        Button anterior = (Button) findViewById(R.id.button3);
        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordString();
                if(restaurante.equals("C@m. Come")) {
                    Intent intent = new Intent(UploadPageSobremesa.this, UploadPageMenu.class);
                    startActivity(intent);
                } else {
                    if(restaurante.equals("Sector + Dep")) {
                        Intent intent = new Intent(UploadPageSobremesa.this, UploadPageOpcao.class);
                        startActivity(intent);
                    } else{
                        goToVegetariano();
                    }
                }

            }
        });
    }

    void recordString(){
        getAll();
    }


    public void goToVegetariano()
    {
        Intent intent = new Intent(UploadPageSobremesa.this, UploadPageVegetariano.class);
        startActivity(intent);
    }

    public void getAll(){
        String prato;
        String temp_preco;
        String [] ans = new String[8];
        int pos = 0;
        for (int i = 0; i<= 7;i++){
            EditText tempPrato = (EditText) findViewById(ids[i]);
            prato = tempPrato.getText().toString();
            Log.d("PRATO","<"+prato+">");
            if (!prato.isEmpty() && prato.trim().length() > 0) {
                Log.d("Prato","im IN");
                sobremesa[pos] = prato;
                EditText tempPreco = (EditText) findViewById(ids_preco[i]);
                temp_preco = tempPreco.getText().toString();
                if (!temp_preco.isEmpty() && temp_preco.trim().length() > 0)
                    preco[pos] = temp_preco;
                else
                    preco[pos] = PrecoInvalido;
            }
            else{
                Log.d("Prato","NOT IN");
                sobremesa[pos]=null;
                preco[pos]=null;
            }
            pos++;
        }
    }

    public void Preencher() {
        if (!DH.getDenovo()) {
            if (sobremesa != null) {
                int e = sobremesa.length;
                for (int i = 0; i < e; i++) {
                    if (sobremesa[i] != null) {
                        //prato
                        EditText temp = (EditText) findViewById(ids[i]);
                        temp.setText(sobremesa[i]);
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
            String [] carneV1 = toStringArray(menu.getJSONArray("sobremesa"));
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        int pos = 0;
        if (sobremesa != null) {
            for (int i = 0; i < sobremesa.length; i++) {
                if (sobremesa[i] != null){
                    if (sobremesa[i].trim().length() >0){
                        savedInstanceState.putString(backupPrato[pos], sobremesa[i]);
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
                sobremesa[i] = backupPrato[i];
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
