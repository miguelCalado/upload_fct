package org.example.snovaisg.myuploadFCT;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Notificacoes extends AppCompatActivity {


    public DataHolder  DH = new DataHolder().getInstance();
    String fileToInternal = DH.fileToInternal();
    String ServerRestauranteFilename = DH.getServerFilename();
    String restaurante = DH.getData();
    JSONObject fullDic = DH.getFullMenu();
    Boolean fileUploaded = false;
    //String JsonNotification = DH.getNotifications();
    JSONObject Dic = DH.getMenuNotifications();

    AmazonS3 s3;
    TransferUtility transferUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacoes);

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted() && !fileUploaded) {
                        Thread.sleep(200); //perguntar ao simao!
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (fileUploaded) {
                                    goBack();
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();

        Button notificacao = (Button) findViewById(R.id.button12);
        notificacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnviarNotificacao();
            }
        });
    }

    void goBack(){
        Toast.makeText(Notificacoes.this,"Notificação Enviada com sucesso",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Notificacoes.this, Atualizado.class);
        startActivity(intent);
    }




    void EnviarNotificacao() {
        try {
            EditText temp = (EditText) findViewById(R.id.editText);
            String notif [] = new String[1];
            notif[0]=temp.getText().toString();
            //JSONArray notifications = fullDic.getJSONObject(restaurante).getJSONArray(JsonNotification);
            String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
            String timeStamp2 = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
            String timeStamp3[] = new String[1];
            timeStamp3[0] = timeStamp2 + "/" + timeStamp;
            //JSONArray newNot = new JSONArray();
            //newNot.put(notif);
            //newNot.put(timeStamp);
            //notifications.put(newNot);
            Dic.put(restaurante, new JSONArray(prepareArray(notif, timeStamp3)));

            fullDic.put("notifications",Dic);

            //Nada
            Writer output = null;
            File file = new File(this.getFilesDir(), fileToInternal);
            output = new BufferedWriter(new FileWriter(file));
            output.write(fullDic.toString());
            output.close();


            credentialsProvider();
            setTransferUtility();
            setFileToUpload(file, ServerRestauranteFilename);

        }catch(Exception e){
            Toast.makeText(Notificacoes.this,"oops,.. Algo correu mal ",Toast.LENGTH_SHORT).show();
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

    public void setFileToUpload(File file, String key){

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
                }
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error","error");
                //PRINT a dizer "LIGUE SE À INTERNET POR FAVOR"
            }
        });
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

}
