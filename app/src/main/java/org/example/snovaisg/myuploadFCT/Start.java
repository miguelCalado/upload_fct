package org.example.snovaisg.myuploadFCT;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.github.javiersantos.appupdater.AppUpdater;
//import com.github.javiersantos.appupdater.AppUpdater;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Start extends AppCompatActivity {

    Animation aniFade;
    AmazonS3 s3;
    TransferUtility transferUtility;

    public DataHolder  DH = new DataHolder().getInstance();
    String ServerLoginInfo = DH.getServerLoginInfo(); //para sacar
    String LocalLoginInfo = DH.getLocalLoginInfo(); //para guardar

    public boolean downloadReady = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        AppUpdater appUpdater = new AppUpdater(this);
        appUpdater.start();

        aniFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        TextView bemVindo = (TextView) findViewById(R.id.textView7);
        bemVindo.setAnimation(aniFade);

        //Button botao = (Button) findViewById(R.id.button5);
        //botao.setAnimation(aniFade);

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
                                        //show begin button
                                        handle();
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
        downloadJson();
    }

    public void handle(){

        JSONObject Login = readJsonFromFile(LocalLoginInfo);
        DH.setLogin(Login);

        final ProgressBar loading = (ProgressBar) findViewById(R.id.progressBar3);
        loading.setVisibility(View.GONE);
        final Button entrar = (Button) findViewById(R.id.button5);
        entrar.setVisibility(View.VISIBLE);
        entrar.setAnimation(aniFade);
    }
    public void begin(View view){
        Intent intent = new Intent(Start.this, Login.class);
        startActivity(intent);
    }

    public boolean downloadJson(){
        credentialsProvider();
        setTransferUtility();
        try {
            File file = createFile(LocalLoginInfo);
            setFileToDownload(file,ServerLoginInfo);
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


    public JSONObject readJsonFromFile(String filename){
        String JsonData ="";
        JSONObject myJson;
        getCacheDir();
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
}
