package com.itheima.zxingdemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public class Show extends AppCompatActivity {
    private WebView webView;
    private TextView back;
    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        back = (TextView) findViewById(R.id.back);
        webView = (WebView) findViewById(R.id.web_view);

        back.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        webView.loadUrl("http://www.yunliti.com/objviewer/index.php?id=51");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

        audioManager = (AudioManager) getSystemService(Show.this.AUDIO_SERVICE);

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        webView.reload();
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {

        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.onPause();
        }
        //让音乐控件失去焦点来禁止掉声音的播放
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ECLAIR_MR1) {
        } else {
            try {
                audioManager = (AudioManager) getSystemService(Show.this.AUDIO_SERVICE);
                int i = 0;
                do {
                    int result = audioManager.requestAudioFocus(listener
                            , AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                    if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                        Log.d("AudioManager", "I get Audio right: ");
                        break;
                    }
                    i++;
                } while (i < 10);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.resumeTimers();
        webView.destroy();
    }
}
