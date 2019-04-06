package org.example.snovaisg.myuploadFCT;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static android.view.View.VISIBLE;

public class Login extends AppCompatActivity {


    public DataHolder  DH = new DataHolder().getInstance();
    JSONObject Login = DH.getLogins();
    //Cuidado com o JsonRest
    String ChosenOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        final Button entrar = (Button) findViewById(R.id.button9);
        final Button avancar = (Button) findViewById(R.id.button8);
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et = (EditText) findViewById(R.id.et1);
                String id = et.getText().toString();
                if (id.isEmpty()==true)
                    return;
                if (Login.has(id)) {
                    TextView tv9 = (TextView) findViewById(R.id.tv9);
                    try {
                        tv9.setText(Login.getJSONArray(id).getString(0));
                        DH.getInstance().setData(Login.getJSONArray(id).getString(1));
                        //DH.getInstance().setServerFilename(DH.getInstance().getData()+".json"); deprecated
                        entrar.setVisibility(View.INVISIBLE);
                        avancar.setVisibility(VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                    Toast.makeText(Login.this,"Código não existe. Tente novamente por favor",Toast.LENGTH_SHORT).show();
            }

        });

        avancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });
}

    /*

    public void run(View view){
        EditText et = (EditText) findViewById(R.id.et1);
        String id = et.getText().toString();
        if (id.isEmpty()==true)
            return;
        int res = -1;
        for( int e = 0;e<10;e++){
            if (id.equals(pass[e]) == true)
                res = e;
        }
        if (res != -1){
            TextView  tv9= (TextView) findViewById(R.id.tv9);
            tv9.setText(rest[res]);
            ChosenOne = JsonRest[res];
            DataHolder.getInstance().setData(ChosenOne);
            Button a = (Button) findViewById(R.id.button8);
            a.setVisibility(VISIBLE);

        }
        else{
            Toast.makeText(Login.this,"Código não existe. Tente novamente por favor",Toast.LENGTH_SHORT).show();

        }
    }
    */
    /*
    void nextPage(View view){
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }*/
}
