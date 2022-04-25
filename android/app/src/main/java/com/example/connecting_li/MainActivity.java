package com.example.connecting_li;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.EventChannel;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.TimeUnit;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


public class MainActivity extends FlutterActivity {

    public static final String TAG = "eventchannelsample";
    public static final String STREAM = "com.yourcompany.eventchannelsample/stream";

    private Disposable timerSubscription;

    private static final boolean SHOW_SPEED_IN_BITS = false;

    private TrafficSpeedMeasurer mTrafficSpeedMeasurer;
    String upStreamS;
    String downStreamS;



    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);

       // ContextCompat.startForegroundService(this, serviceIntent);
        // ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        new EventChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), STREAM).setStreamHandler(
                new EventChannel.StreamHandler() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onListen(Object args, EventChannel.EventSink events) {
                        Log.w(TAG, "adding listener");
                        timerSubscription = Observable
                                .interval(0, 2, TimeUnit.SECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        (Long timer) -> {
//
                                            mTrafficSpeedMeasurer = new TrafficSpeedMeasurer(TrafficSpeedMeasurer.TrafficType.ALL);
                                            mTrafficSpeedMeasurer.startMeasuring();
                                            mTrafficSpeedMeasurer.registerListener(mStreamSpeedListener);
                                            // startService(upStreamS + ", " + downStreamS);

//                                            Intent serviceIntent = new Intent(this,
//                                                    ExampleService.class);

startService(upStreamS);
                                           // ContextCompat.startForegroundService(this, serviceIntent);

                                            events.success(upStreamS + ", " + downStreamS);



//
//                                            ITrafficSpeedListener mStreamSpeedListener = (upStream, downStream) -> runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    String upStreamSpeed = Utils.parseSpeed(upStream, SHOW_SPEED_IN_BITS);
//                                                    String downStreamSpeed = Utils.parseSpeed(downStream, SHOW_SPEED_IN_BITS);
////                mTextView.setText("Up Stream Speed: " + upStreamSpeed + "\n" + "Down Stream Speed: " + downStreamSpeed);
//                                                    Log.w(TAG, "upStreamSpeed " + upStreamSpeed);
//                                                    Log.w(TAG, "downStreamSpeed " + downStreamSpeed);
//
//                                                    events.success(upStreamSpeed + ", " + downStreamSpeed);
//                                                    //return  upStreamSpeed + ", " +downStreamSpeed;
//                                                }
//                                            });
                                        },
                                        (Throwable error) -> {
                                            Log.e(TAG, "error in emitting timer", error);
                                            events.error("STREAM", "Error in processing observable", error.getMessage());
                                        },
                                        () -> Log.w(TAG, "closing the timer observable")
                                );
                    }

                    @Override
                    public void onCancel(Object args) {
                        Log.w(TAG, "cancelling listener");
                        if (timerSubscription != null) {
                            timerSubscription.dispose();
                            timerSubscription = null;
                        }
                    }
                }
        );


    }

    private ITrafficSpeedListener mStreamSpeedListener = new ITrafficSpeedListener() {

        @Override
        public void onTrafficSpeedMeasured(final double upStream, final double downStream) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String upStreamSpeed = Utils.parseSpeed(upStream, SHOW_SPEED_IN_BITS);
                    String downStreamSpeed = Utils.parseSpeed(downStream, SHOW_SPEED_IN_BITS);

                  downStreamS = downStreamSpeed;
                    upStreamS = upStreamSpeed;
//                    Log.e(TAG, "upStreamSpeed", upStreamSpeed);
                    // mTextView.setText("Up Stream Speed: " + upStreamSpeed + "\n" + "Down Stream Speed: " + downStreamSpeed);
                }
            });
        }
    };

    public void startService(String input) {


        Intent serviceIntent = new Intent(this, ExampleService.class);
        serviceIntent.putExtra("inputExtra", input);

        ContextCompat.startForegroundService(this, serviceIntent);
    }

}



