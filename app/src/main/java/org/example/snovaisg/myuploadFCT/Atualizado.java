package org.example.snovaisg.myuploadFCT;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.example.snovaisg.myuploadFCT.BarCampus.BarCampusVisualiza;
import org.example.snovaisg.myuploadFCT.Cantina.CantinaVisualiza;
import org.example.snovaisg.myuploadFCT.CasaPessoal.CasaPVisualiza;
import org.example.snovaisg.myuploadFCT.Come.ComeVisualiza;
import org.example.snovaisg.myuploadFCT.Girassol.GirassolVisualiza;
import org.example.snovaisg.myuploadFCT.Lidia.BarLidiaVisualiza;
import org.example.snovaisg.myuploadFCT.MySpot.mySpotVisualiza;
import org.example.snovaisg.myuploadFCT.Sector7.Sector7Visualiza;
import org.example.snovaisg.myuploadFCT.SectorDep.SectorDepVisualiza;
import org.example.snovaisg.myuploadFCT.Teresa.TeresaVisualiza;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Atualizado extends AppCompatActivity {

    Button notificar;

    DataHolder DH = new DataHolder().getInstance();
    String fileToInternal = DH.fileToInternal();
    String ServerRestauranteFilename = DH.getServerFilename();
    String restaurante = DH.getData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizado);
        initMenu();

        notificar = (Button) findViewById(R.id.button11);

        Button visualizar = (Button) findViewById(R.id.button);
        visualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if(restaurante.equals("Cantina")) {
                    intent = new Intent(Atualizado.this, CantinaVisualiza.class);
                } else {
                    if(restaurante.equals("Teresa")) {
                        intent = new Intent(Atualizado.this, TeresaVisualiza.class);
                    } else {
                        if(restaurante.equals("My Spot")) {
                            intent = new Intent(Atualizado.this, mySpotVisualiza.class);
                        } else {
                            if(restaurante.equals("Bar Campus")) {
                                intent = new Intent(Atualizado.this, BarCampusVisualiza.class);
                            } else {
                                if(restaurante.equals("Casa do P.")) {
                                    intent = new Intent(Atualizado.this, CasaPVisualiza.class);
                                } else {
                                    if(restaurante.equals("C@m. Come")) {
                                        intent = new Intent(Atualizado.this, ComeVisualiza.class);
                                    } else {
                                        if(restaurante.equals("Sector + Dep")) {
                                            intent = new Intent(Atualizado.this, SectorDepVisualiza.class);
                                        } else {
                                            if(restaurante.equals("Sector + Ed.7")) {
                                                intent = new Intent(Atualizado.this, Sector7Visualiza.class);
                                            } else {
                                                if(restaurante.equals("Girassol")) {
                                                    intent = new Intent(Atualizado.this, GirassolVisualiza.class);
                                                } else {
                                                    intent = new Intent(Atualizado.this, BarLidiaVisualiza.class);
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

        notificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Atualizado.this, Notificacoes.class);
                startActivity(intent);
            }
        });
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
}
