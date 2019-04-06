package org.example.snovaisg.myuploadFCT;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PorAtualizar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_por_atualizar2);
        initMenu();
    }


    public void initMenu() {
        String[] semana = {"Domingo", "Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado"};
        Date a = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(a);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        TextView date1 = (TextView) findViewById(R.id.date1);
        date1.setText(semana[dayOfWeek - 1]);

        String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        TextView date2 = (TextView) findViewById(R.id.date2);
        date2.setText(timeStamp);

        TextView tv = (TextView) findViewById(R.id.tv10);
        tv.setText(DataHolder.getInstance().getData());

        Button atualizar = (Button) (findViewById(R.id.button));
        atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PorAtualizar.this, UploadPageSopa.class);
                startActivity(intent);
            }
        });
    }
}
