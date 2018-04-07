package com.example.admin.concurrencycontrol;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {

    ProgressBar prgBar;
    TextView txtPhanTram;
    EditText txtNhap;
    Button btnStart;

    int progressStep = 1;
    int MAX_PROGRESS = 0;
    int globalVar = 0;
    int accum = 0;
    long startingMills = System.currentTimeMillis();
    boolean isRunning = false;
    Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStart.setEnabled(false);
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        accum = 0;
        MAX_PROGRESS= Integer.parseInt(txtNhap.getText().toString());
        prgBar.setMax(MAX_PROGRESS);
        prgBar.setProgress(0);
        prgBar.setVisibility(View.VISIBLE);

        // create-start background thread were the busy work will be done
        Thread myBackgroundThread = new Thread( backgroundTask, "backAlias1" );
        myBackgroundThread.start();
    }

    private void addControls() {
        prgBar=findViewById(R.id.prgBar);
        txtPhanTram=findViewById(R.id.txtPhanTram);
        txtNhap=findViewById(R.id.txtNhap);
        btnStart=findViewById(R.id.btnStart);

    }

    private Runnable foregroundRunnable = new Runnable() {
        @Override
        public void run() {
            prgBar.incrementProgressBy(progressStep);
            accum += progressStep;

            int pt=accum*100/MAX_PROGRESS;
            txtPhanTram.setText(pt+"%");

            if (accum >= prgBar.getMax()) {
                prgBar.setVisibility(View.VISIBLE);
                btnStart.setEnabled(true);
            }

        }
    };

    private Runnable backgroundTask = new Runnable() {
        @Override
        public void run() {
            try {
                for (int n = 0; n < MAX_PROGRESS; n++) {
                    Thread.sleep(0);
                    globalVar++;
                    myHandler.post(foregroundRunnable);
                }
            } catch (InterruptedException e) {
                Log.e("<<foregroundTask>>", e.getMessage());
            }

        }
    };
}
