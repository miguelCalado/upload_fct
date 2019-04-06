package org.example.snovaisg.myuploadFCT.MySpot;

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
import org.example.snovaisg.myuploadFCT.R;
import org.example.snovaisg.myuploadFCT.UploadPageCarne;
import org.example.snovaisg.myuploadFCT.UploadPagePeixe;
import org.example.snovaisg.myuploadFCT.UploadPageSobremesa;
import org.example.snovaisg.myuploadFCT.UploadPageSopa;
import org.example.snovaisg.myuploadFCT.UploadPageVegetariano;
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

public class mySpotPreVisualiza extends AppCompatActivity {
    public static String[] sopa = UploadPageSopa.sopa;
    public static String[] sopaPreco = UploadPageSopa.preco;
    public static String[] carne = UploadPageCarne.carne;
    public static String[] carnePreco = UploadPageCarne.preco;
    public static String[] peixe = UploadPagePeixe.peixe;
    public static String[] peixePreco = UploadPagePeixe.preco;
    public static String[] veg = UploadPageVegetariano.veg;
    public static String[] vegPreco = UploadPageVegetariano.preco;
    public static String[] sobremesa = UploadPageSobremesa.sobremesa;
    public static String[] sobremesaPreco = UploadPageSobremesa.preco;

    String[] sopaV2 = new String[8];
    String[] carneV2 = new String[12];
    String[] peixeV2 = new String[8];
    String[] vegV2 = new String[8];
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
        setContentView(R.layout.myspot);

        run();
        setUp();

        Button voltar = (Button) findViewById(R.id.button7);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mySpotPreVisualiza.this, UploadPageSobremesa.class);
                startActivity(intent);
            }
        });

        Button upload = (Button) findViewById(R.id.button6);
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
            if (veg[i] != null){
                if (veg[i].trim().length() > 0) {
                    vegV2[pos] = veg[i];
                    pos++;
                }
                if(!vegPreco[i].equals("9999") && vegPreco[i].indexOf('€')==-1) {
                    vegPreco[i]=vegPreco[i]+"€";
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
        for(String s : vegV2) {
            if(s != null && s.length() > 0) {
                list.add(s);
            }
        }
        vegV2 = list.toArray(new String[list.size()]);

        list = new ArrayList<String>();
        for(String s : sobremesaV2) {
            if(s != null && s.length() > 0) {
                list.add(s);
            }
        }
        sobremesaV2 = list.toArray(new String[list.size()]);

    }

    public void setUp() {
        TextView menu = (TextView) findViewById(R.id.menu_textView);
        int aa = menu.getCurrentTextColor();

        LinearLayout sopaLayout = (LinearLayout) findViewById(R.id.sopaLayout) ;
        List <String> lista = new ArrayList<String>();
        lista = prepareArray(sopaV2,sopaPreco);
        String [] sopafinal = lista.toArray(new String[lista.size()]);
        alignPrices.getInstance().alignOne(sopafinal, this, aa, sopaLayout);

        LinearLayout pratoDiaLayout = (LinearLayout) findViewById(R.id.pratoDiaLayout);
        List <String> lista1 = new ArrayList<String>();
        lista1 = prepareArray(carneV2,carnePreco);
        String [] carnefinal = lista1.toArray(new String[lista1.size()]);

        List <String> lista2 = new ArrayList<String>();
        lista2 = prepareArray(peixeV2,peixePreco);
        String [] peixefinal = lista2.toArray(new String[lista2.size()]);

        List <String> lista3 = new ArrayList<String>();
        lista3 = prepareArray(vegV2,vegPreco);
        String [] vegetarianofinal = lista3.toArray(new String[lista3.size()]);

        String outra [] = combine (carnefinal, peixefinal);
        String [] pratoDia = combine(outra, vegetarianofinal);
        alignPrices.getInstance().alignOne(pratoDia, this, aa, pratoDiaLayout);

        LinearLayout sobremesaLayout = (LinearLayout) findViewById(R.id.sobremesaLayout);
        List <String> lista4 = new ArrayList<String>();
        lista4 = prepareArray(sobremesaV2,sobremesaPreco);
        String [] sobremesafinal = lista4.toArray(new String[lista4.size()]);
        alignPrices.getInstance().alignOne(sobremesafinal, this, aa, sobremesaLayout);
    }

    public void GoToMain(){
        Intent intent = new Intent(mySpotPreVisualiza.this, Atualizado.class);
        startActivity(intent);
    }

    public void uploadNormally  (){
        try{
            Dic.put("sopa", new JSONArray(prepareArray(sopaV2, sopaPreco)));
            Dic.put("carne",new JSONArray(prepareArray(carneV2,carnePreco)));
            Dic.put("peixe",new JSONArray(prepareArray(peixeV2,peixePreco)));
            Dic.put("vegetariano",new JSONArray(prepareArray(vegV2,vegPreco)));
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
                    Toast.makeText(mySpotPreVisualiza.this, "Upload feito com sucesso!", Toast.LENGTH_SHORT).show();
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
