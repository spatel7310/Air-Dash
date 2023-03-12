package com.example.airdash;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Timer;
//====================================================================
//
// Application: Air Dash
// Activity:    ActMain.java
// Description:
//      This applcaition is a silumator for a drone ordering company. This app simulates a model of what it would look like if you
// were to order drones to fly in bulk. The app gives you an an option to choose 3 different inputs: order rate, drones flying, and, delivery time. With these inputs,
// the app will calculate: clock(min), orders queued, orders delivered, drones available, and drones crashed. These are all simulation done by some back end logic.
//
//====================================================================
public class ActMain extends AppCompatActivity {
    //Variables for the recall button.
    public static final String SHARED_PREFS = "SHARED_PREFS";
    public static final String CLOCK = "CLOCK";
    public static final String QUEUED = "QUEUED";
    public static final String DELIVERED = "DELIVERED";
    public static final String AVAILABLE = "AVAILABLE";
    public static final String CRASHED = "CRASHED";
    public static final String RATE = "RATE";
    public static final String FLYING = "FLYING";
    public static final String TIME = "TIME";

    //Variables for the controls
    public Timer timer;
    public Button btnStart, btnStop, btnRecall, btnReset;
    public static EditText clock, queued, delivered, available, crashed;
    public EditText rateText, droneText, timeText;
    public SeekBar rateSeek, droneSeek, timeSeek;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.laymain);

        //Shared prefs for the recall button
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        //Linking the control to this activity.
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnRecall = findViewById(R.id.btnRecall);
        btnReset = findViewById(R.id.btnReset);
        clock = findViewById(R.id.clock);
        queued = findViewById(R.id.queued);
        delivered = findViewById(R.id.delivered);
        available = findViewById(R.id.available);
        crashed = findViewById(R.id.crashes);
        rateSeek = findViewById(R.id.rateSeek);
        droneSeek = findViewById(R.id.droneSeek);
        timeSeek = findViewById(R.id.timeSeek);
        rateText = findViewById(R.id.rateText);
        droneText = findViewById(R.id.droneText);
        timeText = findViewById(R.id.timeText);

        // Set buttons
        btnStart.setEnabled(false);
        btnStop.setEnabled(false);

        //Settings the title and icon of the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_foreground);

        //order rate seekbar.
        rateSeek.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar, int progress, boolean fromUser)
                    {
                        rateText.setText(String.valueOf(progress));
                        //records progress.
                        Shared.Data.rate = progress;
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar)
                    {}
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar)
                    {
                    }
                });

        //drones flying selector.
        droneSeek.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        //records progress.
                        droneText.setText(String.valueOf(progress));
                        Shared.Data.dronesFlying = progress;

                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );

        //delivery time selector
        timeSeek.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        //records progress. and allows user to now press start. This is a sort of input validation.

                        btnStart.setEnabled(true);
                        timeText.setText(String.valueOf(progress));
                        Shared.Data.deliveryTime = progress;

                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );

        //checks for if the user has pressed the stop button.
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Once the user clicks stop, the values will be saved into the shared prefs.
                editor.putString(CLOCK, clock.getText().toString());
                editor.putString(QUEUED, queued.getText().toString());
                editor.putString(DELIVERED, delivered.getText().toString());
                editor.putString(AVAILABLE, available.getText().toString());
                editor.putString(CRASHED, crashed.getText().toString());
                editor.putString(RATE, rateText.getText().toString());
                editor.putString(FLYING, droneText.getText().toString());
                editor.putString(TIME, timeText.getText().toString());

                // Cancel timer if  exists
                if (timer != null) timer.cancel();
                timer = null;

                // Set buttons
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);

                // Show message
                Toast.makeText(getApplicationContext(),
                        "Timer task stopped.", Toast.LENGTH_LONG)
                        .show();

            }
        });

        //checks for if the user has pressed the recall button.
        btnRecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Alert Dialog
                AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to recall the last saved instance?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Loads the values into the edittexts, sets the progress of the seekbars.
                                clock.setText(sharedPreferences.getString(CLOCK, ""));
                                queued.setText(sharedPreferences.getString(QUEUED, ""));
                                delivered.setText(sharedPreferences.getString(DELIVERED, ""));
                                available.setText(sharedPreferences.getString(AVAILABLE, ""));
                                crashed.setText(sharedPreferences.getString(CRASHED, ""));
                                rateSeek.setProgress(sharedPreferences.getInt(RATE, 0));
                                rateText.setText(sharedPreferences.getString(RATE, ""));
                                droneSeek.setProgress(sharedPreferences.getInt(FLYING, 0));
                                droneText.setText(sharedPreferences.getString(FLYING, ""));
                                timeSeek.setProgress(sharedPreferences.getInt(TIME, 0));
                                timeText.setText(sharedPreferences.getString(TIME, ""));
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();


            }
        });
    }
    //function for if the user pressed the start button.
    public void startTimerTask(View v)
    {

        // Cancel timer if  exists
        if (timer != null) timer.cancel();

        // Create and start timer
        timer = new Timer();
        timer.schedule(
                new Task(),
                Shared.Data.TIMER_TASK_DELAY * 1000,
                Shared.Data.TIMER_TASK_THREAD_PAUSE * 1000);

        // Set buttons
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);

        // Show message
        Toast.makeText(getApplicationContext(),
                "Timer task started.", Toast.LENGTH_LONG)
                .show();

    }

    //function for if the reset button is pressed. Just sets everything to zero.
    public void reset(View v){

        //Alert Dialog
        AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to reset?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Shared.Data.minuteClock = 0;
                        Shared.Data.ordersQueued = 0;
                        Shared.Data.ordersDelivered = 0;
                        Shared.Data.dronesAvailable = 0;
                        Shared.Data.dronesCrashed = 0;

                        rateSeek.setProgress(0);
                        Shared.Data.rate = 0;
                        droneSeek.setProgress(0);
                        Shared.Data.dronesFlying = 0;
                        timeSeek.setProgress(0);
                        Shared.Data.deliveryTime = 0;

                        clock.setText(String.valueOf(0));
                        queued.setText(String.valueOf(0));
                        delivered.setText(String.valueOf(0));
                        available.setText(String.valueOf(0));
                        crashed.setText(String.valueOf(0));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    //handler to send the information back and forth.
    public static Handler timerTaskHandler =
            new Handler(Looper.getMainLooper())
            {
                //------------------------------------------------------------
                // handleMessage
                //------------------------------------------------------------
                @Override
                public void handleMessage(Message msg)
                {
                    clock.setText(String.valueOf(Shared.Data.minuteClock));
                    queued.setText(String.valueOf(Shared.Data.ordersQueued));
                    delivered.setText(String.valueOf(Shared.Data.ordersDelivered));
                    available.setText(String.valueOf(Shared.Data.dronesAvailable));
                    crashed.setText(String.valueOf(Shared.Data.dronesCrashed));

                }
            };

}