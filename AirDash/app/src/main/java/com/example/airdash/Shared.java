package com.example.airdash;

import android.content.SharedPreferences;

import java.util.Objects;

//--------------------------------------------------------------------
// enum Shared
//
// Description: A public enum that just contains all of the variables do be shared across all paltforms.
//--------------------------------------------------------------------
public enum Shared
{

    // Define enum value
    Data;

    public final int TIMER_TASK_DELAY = 1;  // seconds
    public final int TIMER_TASK_THREAD_PAUSE = 1;  // seconds

    // Declare enum fields
    public int minuteClock = 0;
    public int ordersQueued = 0;
    public int ordersDelivered = 0;
    public int dronesAvailable;
    public int dronesCrashed;
    public int rate;
    public int dronesFlying;
    public int deliveryTime;





}