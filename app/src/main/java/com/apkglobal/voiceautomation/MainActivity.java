package com.apkglobal.voiceautomation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private SpeechRecognizer sr;
    private static final int REQUEST_CALL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                sr.startListening(intent);
            }
        });
        intializeTextToSpeech();
        initializeSpeechRecognizer();
    }

    private void initializeSpeechRecognizer() {
        if(SpeechRecognizer.isRecognitionAvailable(this))
        {
            sr=SpeechRecognizer.createSpeechRecognizer(this);
            sr.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    List<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    processResult(result.get(0));

                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }
    private void makePhoneCall() {
        String number = "8527657955";
        if (number.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.CALL_PHONE
                }, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        } else {
            Toast.makeText(MainActivity.this, "ENTER PHONE NUMBER", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_CALL){
            if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            }
        }else {
            Toast.makeText(this,"DENIED",Toast.LENGTH_SHORT).show();
        }
    }
    private void processResult(String command) {
        command=command.toLowerCase();
        if(command.indexOf("what")!=-1) {
            if (command.indexOf("your name") != -1) {
                speak("My name is koko");
            }
            if (command.indexOf("time")!= -1) {
                Date now = new Date();
                String time = DateUtils.formatDateTime(this, now.getTime(), DateUtils.FORMAT_SHOW_TIME);
                speak("The time now is" + time);
            }
            if (command.indexOf("date")!= -1) {
                Date now = new Date();
                String date = DateUtils.formatDateTime(this, now.getDate(), DateUtils.FORMAT_SHOW_DATE);
                speak("The time now is" + date);
            }
        } else if(command.indexOf("open")!=-1){
                if(command.indexOf("browser")!=-1){
                    Intent intent=new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://google.com"));
                    startActivity(intent);
                }
                if(command.indexOf("camera")!=-1)

                {
                    speak("opening camera");

                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(intent);
                }

            }
            else if(command.indexOf("how")!=-1)
        {
            if(command.indexOf("you")!=-1)
            {
                speak("I am fine,Thankyou for asking.And you");
            }
        }
        else if(command.indexOf("I am")!=-1) {
            if (command.indexOf("fine")!=-1) {
                speak("ok good. What can i do for u");
            }

                if (command.indexOf("good")!=-1) {

                    speak("ok good");
                }

        }
        else if(command.indexOf("getting")!=-1)
        {
            if(command.indexOf("bore")!=-1)
            {
                speak("so may i book movie ticket for you");
            }
        }
        else if (command.indexOf("call")!=-1)
        {
            if(command.indexOf("pawan")!=-1)
            {
                speak("calling pawan sir!");
                makePhoneCall();
            }
        }



    }


    private void intializeTextToSpeech() {
        tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(tts.getEngines().size()==0)
                {
                    Toast.makeText(MainActivity.this, "Not Available", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    tts.setLanguage(Locale.US);
                    speak("Hello! Akshay");
                }
            }
        });
    }

    private void speak(String s) {
        if(Build.VERSION.SDK_INT>=21)
        {
            tts.speak(s,TextToSpeech.QUEUE_FLUSH,null,null);

        }
        else{
            tts.speak(s,TextToSpeech.QUEUE_FLUSH,null,null);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        tts.shutdown();
    }
}
