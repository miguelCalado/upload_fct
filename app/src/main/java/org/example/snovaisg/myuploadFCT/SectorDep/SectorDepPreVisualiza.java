package org.example.snovaisg.myuploadFCT.SectorDep;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.example.snovaisg.myuploadFCT.Atualizado;
import org.example.snovaisg.myuploadFCT.DataHolder;
import org.example.snovaisg.myuploadFCT.JsonDic;
import org.example.snovaisg.myuploadFCT.UploadPageCarne;
import org.example.snovaisg.myuploadFCT.UploadPageDieta;
import org.example.snovaisg.myuploadFCT.UploadPageOpcao;
import org.example.snovaisg.myuploadFCT.UploadPagePeixe;
import org.example.snovaisg.myuploadFCT.UploadPageSobremesa;
import org.example.snovaisg.myuploadFCT.UploadPageSopa;
import org.example.snovaisg.myuploadFCT.alignPrices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miguel-PC on 03/03/2018.
 */

public class SectorDepPreVisualiza extends AppCompatActivity {

    public static String[] sopa = UploadPageSopa.sopa;
    public static String[] sopaPreco = UploadPageSopa.preco;
    public static String[] carne = UploadPageCarne.carne;
    public static String[] carnePreco = UploadPageCarne.preco;
    public static String[] peixe = UploadPagePeixe.peixe;
    public static String[] peixePreco = UploadPagePeixe.preco;
    public static String[] opcao = UploadPageOpcao.opcao;
    public static String[] opcaoPreco = UploadPageOpcao.preco;
    public static String[] dieta = UploadPageDieta.dieta;
    public static String[] dietaPreco = UploadPageDieta.preco;
    public static String[] sobremesa = UploadPageSobremesa.sobremesa;
    public static String[] sobremesaPreco = UploadPageSobremesa.preco;

    String[] sopaV2 = new String[8];
    String[] carneV2 = new String[12];
    String[] peixeV2 = new String[8];
    String[] opcaoV2 = new String[8];
    String[] dietaV2 = new String[8];
    String[] sobremesaV2 = new String[8];

    String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date());

    DataHolder DH = new DataHolder().getInstance();
    String fileToInternal = DH.fileToInternal();
    String ServerRestauranteFilename = DH.getServerFilename();
    String restaurante = DH.getData();
    String PrecoInvalido = DH.PrecoInvalido();
    String PrecoInvalidoMensagem = DH.PrecoInvalidoMensagem();

    JSONObject Dic = DH.getMenu();
    JSONObject fullDic = DH.getFullMenu();
    boolean fileUploaded = false;

    AmazonS3 s3;
    TransferUtility transferUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.example.snovaisg.myuploadFCT.R.layout.sectordep);

        run();
        setUp();

        Button voltar = (Button) findViewById(org.example.snovaisg.myuploadFCT.R.id.button7);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SectorDepPreVisualiza.this, UploadPageSobremesa.class);
                startActivity(intent);
            }
        });

        Button upload = (Button) findViewById(org.example.snovaisg.myuploadFCT.R.id.button6);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TESTE","AQUIAQUIAQUIAQUI");
                timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date());
                credentialsProvider();
                // Callback method to call the setTransferUtility method
                setTransferUtility();
                // if (restaurante == "Teresa" || restaurante == "Cantina"){
                uploadNormally();
                ////  }
                //   else{
                //   mergeAndUpload();
                //   }
            }
        });
    }

    public void run(){
        int pos = 0;
        int i = 0;
        for(i=0; i<8; i++) {
            if (sopa[i] != null) {
                if (sopa[i].trim().length() >0) {
                    sopaV2[pos] = sopa[i];
                    pos++;
                }
                if(!sopaPreco[i].equals("9999") && sopaPreco[i].indexOf('€')==-1) {
                    sopaPreco[i]=sopaPreco[i]+"€";
                }
            }
        }

        for(i=0, pos=0;i<12; i++) {
            if (carne[i] != null) {
                if (carne[i].trim().length() >0) {
                    carneV2[pos] = carne[i];
                    pos++;
                }
                if(!carnePreco[i].equals("9999") && carnePreco[i].indexOf('€')==-1) {
                    carnePreco[i]=carnePreco[i]+"€";
                }

            }
        }

        for(i=0, pos=0;i<8;i++){
            if (peixe[i] != null){
                if (peixe[i].trim().length() > 0) {
                    peixeV2[pos] = peixe[i];
                    pos++;
                }
                if(!peixePreco[i].equals("9999") && peixePreco[i].indexOf('€')==-1) {
                    peixePreco[i]=peixePreco[i]+"€";
                }
            }
        }

        for(i=0, pos=0;i<8;i++){
            if (opcao[i] != null){
                if (opcao[i].trim().length() > 0) {
                    opcaoV2[pos] = opcao[i];
                    pos++;
                }
                if(!opcaoPreco[i].equals("9999") && opcaoPreco[i].indexOf('€')==-1) {
                    opcaoPreco[i]=opcaoPreco[i]+"€";
                }
            }
        }

        for(i=0, pos=0;i<8;i++){
            if (dieta[i] != null){
                if (dieta[i].trim().length() > 0) {
                    dietaV2[pos] = dieta[i];
                    pos++;
                }
                if(!dietaPreco[i].equals("9999") && dietaPreco[i].indexOf('€')==-1) {
                    dietaPreco[i]=dietaPreco[i]+"€";
                }
            }
        }


        for(i=0; i<8; i++) {
            if (sobremesa[i] != null) {
                if (sobremesa[i].trim().length() >0) {
                    sobremesaV2[pos] = sobremesa[i];
                    pos++;
                }
                if(!sobremesaPreco[i].equals("9999") && sobremesaPreco[i].indexOf('€')==-1) {
                    sobremesaPreco[i]=sobremesaPreco[i]+"€";
                }
            }
        }

        List<String> list = new ArrayList<String>();
        for(String s : sopaV2) {
            if(s != null && s.length() > 0) {
                list.add(s);
            }
        }
        sopaV2 = list.toArray(new String[list.size()]);

        list = new ArrayList<String>();
        for(String s : carneV2) {
            if(s != null && s.length() > 0) {
                list.add(s);
            }
        }
        carneV2 = list.toArray(new String[list.size()]);

        list = new ArrayList<String>();
        for(String s : peixeV2) {
            if(s != null && s.length() > 0) {
                list.add(s);
            }
        }
        peixeV2 = list.toArray(new String[list.size()]);

        list = new ArrayList<String>();
        for(String s : opcaoV2) {
            if(s != null && s.length() > 0) {
                list.add(s);
            }
        }
        opcaoV2 = list.toArray(new String[list.size()]);

        list = new ArrayList<String>();
        for(String s : dietaV2) {
            if(s != null && s.length() > 0) {
                list.add(s);
            }
        }
        dietaV2 = list.toArray(new String[list.size()]);


        list = new ArrayList<String>();
        for(String s : sobremesaV2) {
            if(s != null && s.length() > 0) {
                list.add(s);
            }
        }
        sobremesaV2 = list.toArray(new String[list.size()]);

    }

    public void setUp() {
        TextView menu = (TextView) findViewById(org.example.snovaisg.myuploadFCT.R.id.menu_textView);
        int aa = menu.getCurrentTextColor();

        LinearLayout sopaLayout = (LinearLayout) findViewById(org.example.snovaisg.myuploadFCT.R.id.sopaLayout) ;
        List <String> lista = new ArrayList<String>();
        lista = prepareArray(sopaV2,sopaPreco);
        String [] sopafinal = lista.toArray(new String[lista.size()]);
        alignPrices.getInstance().alignOne(sopafinal, this, aa, sopaLayout);

        LinearLayout carneLayout = (LinearLayout) findViewById(org.example.snovaisg.myuploadFCT.R.id.carneLayout);
        List <String> lista1 = new ArrayList<String>();
        lista1 = prepareArray(carneV2,carnePreco);
        String [] carnefinal = lista1.toArray(new String[lista1.size()]);
        alignPrices.getInstance().alignOne(carnefinal, this, aa, carneLayout);

        LinearLayout peixeLayout = (LinearLayout) findViewById(org.example.snovaisg.myuploadFCT.R.id.peixeLayout);
        List <String> lista2 = new ArrayList<String>();
        lista2 = prepareArray(peixeV2,peixePreco);
        String [] peixefinal = lista2.toArray(new String[lista2.size()]);
        alignPrices.getInstance().alignOne(peixefinal, this, aa, peixeLayout);

        LinearLayout vegetarianoLayout = (LinearLayout) findViewById(org.example.snovaisg.myuploadFCT.R.id.opcaoLayout);
        List <String> lista3 = new ArrayList<String>();
        lista3 = prepareArray(opcaoV2,opcaoPreco);
        String [] vegetarianofinal = lista3.toArray(new String[lista3.size()]);
        alignPrices.getInstance().alignOne(vegetarianofinal, this, aa, vegetarianoLayout);

        LinearLayout dietaLayout = (LinearLayout) findViewById(org.example.snovaisg.myuploadFCT.R.id.dietaLayout);
        List <String> lista5 = new ArrayList<String>();
        lista5 = prepareArray(dietaV2,dietaPreco);
        String [] dietafinal = lista5.toArray(new String[lista5.size()]);
        alignPrices.getInstance().alignOne(dietafinal, this, aa, dietaLayout);

        LinearLayout sobremesaLayout = (LinearLayout) findViewById(org.example.snovaisg.myuploadFCT.R.id.sobremesaLayout);
        List <String> lista4 = new ArrayList<String>();
        lista4 = prepareArray(sobremesaV2,sobremesaPreco);
        String [] sorebemesafinal = lista4.toArray(new String[lista4.size()]);
        alignPrices.getInstance().alignOne(sorebemesafinal, this, aa, sobremesaLayout);

        try {
            JsonDic myJson = new JsonDic(fileToInternal, SectorDepPreVisualiza.this);

            try {
                String bebidas[] = myJson.getStringArray2("Sector + Dep", "Bebidas");
                String bebidaFinal = final_String2( bebidas);
                String bebidaPrice = final_String3(bebidas);
                TextView bebidasText = (TextView) findViewById(org.example.snovaisg.myuploadFCT.R.id.BebidasText);
                bebidasText.setText(bebidaFinal);
                TextView bebidasPrice = (TextView) findViewById(org.example.snovaisg.myuploadFCT.R.id.BebidasPrice);
                bebidasPrice.setText(bebidaPrice);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GoToMain(){
        Intent intent = new Intent(SectorDepPreVisualiza.this, Atualizado.class);
        startActivity(intent);
    }

    public void uploadNormally  (){
        try{
            Dic.put("sopa", new JSONArray(prepareArray(sopaV2, sopaPreco)));
            Dic.put("carne",new JSONArray(prepareArray(carneV2,carnePreco)));
            Dic.put("peixe",new JSONArray(prepareArray(peixeV2,peixePreco)));
            Dic.put("opcao",new JSONArray(prepareArray(opcaoV2,opcaoPreco)));
            Dic.put("dieta",new JSONArray(prepareArray(dietaV2,dietaPreco)));
            Dic.put("sobremesa",new JSONArray(prepareArray(sobremesaV2,sobremesaPreco)));
            Dic.put("weekId",timeStamp);

            fullDic.put(restaurante,Dic);


            Writer output = null;
            File file = new File(this.getFilesDir(), fileToInternal);
            output = new BufferedWriter(new FileWriter(file));
            output.write(fullDic.toString());
            output.close();
            Log.d("IT IS NOW OR NEVER",Dic.toString());
            setFileToUpload(file,ServerRestauranteFilename);
        }
        catch(JSONException e){
            Log.e("ERROR","meh");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void credentialsProvider(){

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "eu-west-1:2fe2b2a9-0d04-4d16-8fb4-51a61d9e92fa", // Identity pool ID
                Regions.EU_WEST_1 // Region
        );

        setAmazonS3Client(credentialsProvider);
    }

    // Create an S3 client

    /**
     *  Create a AmazonS3Client constructor and pass the credentialsProvider.
     * @param credentialsProvider
     */
    public void setAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider){
        // Create an S3 client
        s3 = new AmazonS3Client(credentialsProvider);
        s3.setRegion(Region.getRegion(Regions.EU_WEST_1));
    }

    public void setTransferUtility(){
        transferUtility = new TransferUtility(s3, getApplicationContext());
    }

    public void setFileToUpload(File file,String key){

        TransferObserver transferObserver = transferUtility.upload(
                "myhappymealfctbucket", // The bucket to upload to
                key,  // The key for the uploaded object
                file   // The file where the data to upload exists
        );
        transferObserverListenerUp(transferObserver);
    }


    /**
     * This is listener method of the TransferObserver
     * Within this listener method, we got status of uploading and downloading file,
     * to diaplay percentage of the part of file to be uploaded or downloaded to S3
     * It display error, when there is problem to upload and download file to S3.
     * @param transferObserver
     */


    public void transferObserverListenerUp(TransferObserver transferObserver){

        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("statechange", state+"");
                //DisconnectMaybe(id);
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/bytesTotal * 100);
                Log.e("percentage",percentage +"");
                if (percentage == 100){
                    //DONWLOAD COMPLETED
                    fileUploaded = true;
                    Toast.makeText(SectorDepPreVisualiza.this, "Upload feito com sucesso!", Toast.LENGTH_SHORT).show();
                    GoToMain();
                }
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error","error");
                //PRINT a dizer "LIGUE SE À INTERNET POR FAVOR"
            }
        });
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

    public List prepareArray(String [] pratos, String [] precos){
        List<String> list = new ArrayList<String>();

        int pos = 0;
        for (int i = 0;i < pratos.length;i++){
            list.add(pratos[i]);
            list.add(precos[i]);
        }
        return list;
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

    public String final_String3 (String pratos[]) {
        String preço_final="";
        for (int i=0; i<pratos.length; i=i+2) {
            preço_final = preço_final+pratos[i+1];
            if(i<pratos.length-2) {
                preço_final = preço_final+"\n";
            }
        }
        return preço_final;

    }
}
