package com.signalteller.trafficteller;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Locale;

public class AutomodeActivity extends AppCompatActivity {

    TextView textView,signalColor;
    Firebase mRef;
    int result;
    String sigCol;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automode);
        signalColor=(TextView)findViewById(R.id.signalCol);
        textView= (TextView) findViewById(R.id.textView2);
        tts=new TextToSpeech(AutomodeActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    result= tts.setLanguage(Locale.UK);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Feature not supported!!",Toast.LENGTH_LONG).show();
                }
            }
        });
        mRef =new Firebase("https://trafficteller-556aa.firebaseio.com/color1");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sigCol= dataSnapshot.getValue(String.class);
                textView.setText("Connected..!");
                signalColor.setText(sigCol);
                if(sigCol.equals("red")){

                    tts.speak("now the Signal is red so You can cross the intersection now", TextToSpeech.QUEUE_FLUSH, null);
                }
                else{
                    tts.speak("now the Signal is green so You should wait for 60 seconds", TextToSpeech.QUEUE_FLUSH, null);

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(60000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(sigCol.equals("red")){
                                mRef.setValue("green");
                                }
                                else {
                                    mRef.setValue("red");
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();



    }
}
