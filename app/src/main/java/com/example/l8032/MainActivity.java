package com.example.l8032;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button pois;
    Button osta;
    Button lisaa;
    TextView teksti;
    TextView viesti;
    EditText valinta;
    SeekBar rahan_muutos;
    int seekbar_rahanmaara;
    double raha = 0;
    int ticket = 1;
    BottleDispencer bd = BottleDispencer.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pois = (Button) findViewById(R.id.nosto);
        osta = (Button) findViewById(R.id.osta);
        lisaa = (Button) findViewById(R.id.lisaa);
        teksti = (TextView) findViewById(R.id.ui);
        viesti = (TextView) findViewById(R.id.tulostus);
        valinta = (EditText) findViewById(R.id.valinta);
        rahan_muutos = (SeekBar) findViewById(R.id.seekBar);
        seekbar_rahanmaara =  rahan_muutos.getProgress();
        rahan_muutos.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int maara, boolean b) {
                seekbar_rahanmaara = maara;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                   ilmotus("You are setting "+seekbar_rahanmaara+"€ to your balance");
            }
        });
        if (ticket == 1){
            bd.lisaaPullot();
            ticket = 0;}
        viesti.setText("You have "+String.format("%.2f", bd.raha)+"€ to spend");;


        int n = 0;

        for (Bottle i : bd.getList()) {
            teksti.append((n + 1) + ". Name: " + i.getName()+"\n"+"	Size: " + i.getSize() + "	Price: " + i.getPrice()+"\n");
            n++;
        }
    }


    public void addMoney(View v){
        //double lisays = 1;
        bd.addMoney(seekbar_rahanmaara);
        viesti.setText("You have "+String.format("%.2f", bd.raha)+"€ to spend");
        ilmotus("Added "+seekbar_rahanmaara+"€ to balance");
        rahan_muutos.setProgress(0);


    }
    public void returnMoney(View v){
        ilmotus("Klink klink. Money came out! You got "+String.format("%.2f", bd.raha)+"€ back");
        raha = bd.returnMoney();

        viesti.setText("You have "+String.format("%.2f", bd.raha)+"€ to spend");

    }



    public void buyBottle(View v) {
        try {
            int valinta1 = Integer.parseInt(valinta.getText().toString());
            int kokoalussa = bd.getList().size();
            if (valinta1 > bd.getList().size()) {
                ilmotus("Please select one of the sodas shown");
            } else {
                bd.buyBottle(valinta1);
                int kokolopussa = bd.getList().size();
                if (kokoalussa != kokolopussa) {
                    ilmotus("You got soda " + valinta1 + ". Hope you enjoy it!!");
                } else if (bd.raha < bd.getList().get(valinta1 - 1).getPrice()) {
                    ilmotus("Not enough money. Add money first");
                }
                int n = 0;
                teksti.setText("");
                for (Bottle i : bd.getList()) {
                    teksti.append((n + 1) + ". Name: " + i.getName() + "\n" + "	Size: " + i.getSize() + "   Price: " + i.getPrice() + "\n");
                    n++;
                }
                valinta.setText("");
            }
            viesti.setText("You have " + String.format("%.2f", bd.raha) + "€ to spend");
        }catch(NumberFormatException nfe){
            ilmotus("Please select one of the sodas shown");
        }


    }
    public void ilmotus(String s){
        Toast.makeText(MainActivity.this, s,Toast.LENGTH_SHORT).show();
    }
}
