package com.example.airdash;

import android.content.SharedPreferences;
import android.os.Message;

import java.util.Random;
import java.util.TimerTask;

//====================================================================
//
// Application: Air Dash
// Activity:    Task.java
// Description:
//      Task timer that act as kind of a "clock". Contains all the logic for the calculations.
//
//====================================================================
public class Task extends TimerTask {
    @Override
    public void run() {
        //variables for logic calculations
        int temp;
        Random rand = new Random();


        //Logic for clock and rate.
        Shared.Data.minuteClock = Shared.Data.minuteClock + 1;
        Shared.Data.ordersQueued += Shared.Data.rate;

        if (Shared.Data.minuteClock % Shared.Data.deliveryTime == 0){
            //generating random number for drones available and drones crashed.
            int randomNum = rand.nextInt(100) + 1;

            //Logic for orders queued and orders delivered.
            temp = Shared.Data.ordersQueued - Shared.Data.dronesFlying;
            if (temp < 0) {
                Shared.Data.ordersDelivered += Shared.Data.ordersQueued;
                Shared.Data.ordersQueued = 0;
            }
            else {
                Shared.Data.ordersDelivered += Shared.Data.dronesFlying;
                Shared.Data.ordersQueued = temp;
            }


            //Logic for drones available and drones crashed
            if(Shared.Data.dronesFlying > randomNum){
                Shared.Data.dronesAvailable = randomNum - 1;
                Shared.Data.dronesCrashed = randomNum + 1;
            }

        }

        // Test if message sent
        if (ActMain.timerTaskHandler.sendEmptyMessage(0))
            System.out.println("[Task] Timer task  " +
                    "message sent to main thread.");
        else
            System.out.println("[Task] Error: " +
                    "timer task message NOT sent to main thread.");

    }
}
