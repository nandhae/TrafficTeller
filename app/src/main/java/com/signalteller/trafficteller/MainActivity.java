package com.signalteller.trafficteller;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {


    private ImageButton micButton;
    private TextView resultText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        micButton=(ImageButton)findViewById(R.id.micButton);
        resultText= (TextView)findViewById(R.id.resultText);


        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

    }

    public void promptSpeechInput(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say something...");
        try {
            startActivityForResult(i, 100);
        }
        catch (ActivityNotFoundException e){
            Toast.makeText(MainActivity.this, "Speech functionality not found!!!",Toast.LENGTH_SHORT).show();
        }

    }


    public void onActivityResult(int request_code, int result_code, Intent i){
        super.onActivityResult(request_code, result_code, i);
        switch(request_code){
            case 100:
                if (result_code == RESULT_OK && i != null){
                    ArrayList<String> result= i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    resultText.setText(result.get(0));
                    String str=resultText.getText().toString();


                   if(str.equals("manual")) {
                       Intent intent = new Intent(this, OpencvActivity.class);
                       startActivity(intent);
                   }
                    else if (str.equals("auto")){
                       Intent intent = new Intent(this, AutomodeActivity.class);
                       startActivity(intent);

                   }
                }
            break;
            default:
                break;
        }
    }
}
