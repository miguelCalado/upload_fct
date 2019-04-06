package org.example.snovaisg.myuploadFCT;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import android.util.Log;

import android.widget.TextView;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;


import org.json.JSONException;
import org.json.JSONObject;


import java.io.FileInputStream;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    public DataHolder  DH = new DataHolder().getInstance();
    String ServerFilename = DH.ServerFilename();
    String fileToInternal = DH.fileToInternal();
    String restaurante = DH.getData();
    AmazonS3 s3;
    TransferUtility transferUtility;
    public static JSONObject Dic;
    public static JsonDic Doc;
    public static boolean estado;

    boolean downloadReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMenu();
        downloadJson();

        //Perguntar amannha!
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted() && !downloadReady) {
                        Thread.sleep(200); //perguntar ao simao!
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (downloadReady) {
                                    try {
                                        Dic = readJsonFromFile(fileToInternal);
                                        DH.setFullMenu(Dic);
                                        DH.setMenu(Dic.getJSONObject(restaurante));
                                        DH.setMenuNotifications(Dic.getJSONObject("notifications"));
                                        Log.d("RESTAURANTE",restaurante);
                                        Log.d("RESTAURANTE",DH.getMenu().toString());
                                        estado = getEstado();
                                        VerifyJson();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
    get("teste1,teste2,teste 3");
    }

    public boolean getEstado() throws  JSONException{
        String weekId = DH.getMenu().getString("weekId");
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date());
        Log.d("TESTE",weekId);
        Log.d("TESTE",timeStamp);

        return weekId.equals(timeStamp);

    }
    public void VerifyJson() throws JSONException {


        if (!estado){
            //Ir para a página de "Falta atualizar
            goToPorAtualizar();
        }
        else{
            //Ir para a página de "Atualizado"
            goToAtualizado();
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
            int a = 1 + 2;
            return myJson;
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d("MEH","Something went wrong");
            return null;
        }
    }

    public void goToAtualizado(){
        Intent intent = new Intent(MainActivity.this, Atualizado.class);
        startActivity(intent);
    }

    public void goToPorAtualizar()
    {
        Intent intent = new Intent(MainActivity.this, PorAtualizar.class);
        startActivity(intent);
    }


    public static String valueOf(Object obj) {
        return (obj == null) ? "null" : obj.toString();
    }

    public void initMenu(){
        String[] semana = { "Domingo", "Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado"};
        Date a = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(a);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        TextView date1 = (TextView) findViewById(R.id.date1);
        date1.setText(semana[dayOfWeek-1]);

        String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        TextView date2 = (TextView) findViewById(R.id.date2);
        date2.setText(timeStamp);

        TextView tv = (TextView) findViewById(R.id.tv10);
        tv.setText(DataHolder.getInstance().getData());

    }

    public boolean downloadJson(){
        credentialsProvider();
        setTransferUtility();
        try {
            File file = createFile(fileToInternal);
            setFileToDownload(file,ServerFilename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Método que pede para ligar à internet
            return false;
        }
        return true;

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

    /**
     *  This method is used to Download the file to S3 by using transferUtility class
     * @param //view
     **/
    public void setFileToDownload(File file,String ServerFilename) throws FileNotFoundException {


        TransferObserver transferObserver = transferUtility.download(
                "myhappymealfctbucket",     /* The bucket to download from */
                ServerFilename,    /* The key for the object to download */
                file        /* The file to download the object to */
        );


        //função para ler o estado da transferencia
        transferObserverListener(transferObserver);

    }


    /**
     * This is listener method of the TransferObserver
     * Within this listener method, we got status of uploading and downloading file,
     * to diaplay percentage of the part of file to be uploaded or downloaded to S3
     * It display error, when there is problem to upload and download file to S3.
     * @param transferObserver
     */

    public void transferObserverListener(TransferObserver transferObserver){

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
                    downloadReady = true;
                    //ver


                }
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error","error");
                //PRINT a dizer "LIGUE SE À INTERNET POR FAVOR"
            }
        });
    }

    public File createFile(String stringFile){
        File fileToDownload = new File(this.getFilesDir(),stringFile);
        Log.d("Testing createFile","this.getFilesDir()");
        return fileToDownload;
    }


    public void get(String a){
        String test = "";
        String[] res = new String[100];
        int pos = 0;
        int start = 0;
        int end = 1;
        for (;end < a.length();end++){
            if (a.substring(end,end+1).equals(",")){
                res[pos] = a.substring(start,end);
                pos++;
                start = end;
                break;
            }
        }
        Log.d("Here Goes Nothin",res[0] + " " + res[1] + " " + res[2]);

    }

    @Override
    protected void onResume() {
        super.onResume();
        downloadReady=false;
    }


}
