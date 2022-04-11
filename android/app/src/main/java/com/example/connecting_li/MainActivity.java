package com.example.connecting_li;

import androidx.annotation.NonNull;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.EventChannel;

import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.util.Log;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


public class MainActivity extends FlutterActivity {

//    private static final String CHANNEL = "myChannel";
//
//
//    @Override
//    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
//        super.configureFlutterEngine(flutterEngine);
//        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
//                .setMethodCallHandler(
//                        (call, result) -> {
//                            if (call.method.equals("open")) {
//                                openCamera();
//
//                            }
//                        }
//                );
//    }
//
//    public void openCamera() {
//
//
//        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
//        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
//        int downSpeed = nc.getLinkDownstreamBandwidthKbps() / 1000;
//        int upSpeed = nc.getLinkUpstreamBandwidthKbps() / 1000;
//        Log.d("downSpeed", String.valueOf(downSpeed));
//
//        Log.d("upSpeed", String.valueOf(upSpeed));
//
//        //return String.valueOf(downSpeed) +' '+ String.valueOf(downSpeed);
//    }

    public static final String TAG = "eventchannelsample";
    public static final String STREAM = "com.yourcompany.eventchannelsample/stream";

    private Disposable timerSubscription;

    //    ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
//    NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
//    int downSpeed = nc.getLinkDownstreamBandwidthKbps() / 1000;
//    int upSpeed = nc.getLinkUpstreamBandwidthKbps() / 1000;
    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        new EventChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), STREAM).setStreamHandler(
                new EventChannel.StreamHandler() {


                    @Override
                    public void onListen(Object args, EventChannel.EventSink events) {
//                        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
//                        int downSpeed = nc.getLinkDownstreamBandwidthKbps() / 1024;
//                        int upSpeed = nc.getLinkUpstreamBandwidthKbps() / 1024;

                        Log.w(TAG, "adding listener");
                        timerSubscription = Observable
                                .interval(0, 4, TimeUnit.SECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        (Long timer) -> {
                                            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
                                            double downSpeed =(nc.getLinkDownstreamBandwidthKbps())/1000;
                                            double upSpeed = nc.getLinkUpstreamBandwidthKbps()/100;
                                            Log.d("downSpeed", String.valueOf(downSpeed));

                                            Log.d("upSpeed", String.valueOf(upSpeed));
                                            Log.w(TAG, "emitting timer event " + timer);
//                                            JSONObject obj = new JSONObject();
//                                            obj.put("upSpeed", downSpeed);
//                                            obj.put("downSpeed", upSpeed);
//                                            main.downSpeed = downSpeed;
//                                            main.upSpeed = upSpeed;
//                                            MainClass mainClass = new MainClass();
//                                            mainClass.setDownSpeed(downSpeed);
//                                            mainClass.setUpSpeed(upSpeed);

events.success(upSpeed + ","+ downSpeed);
//                                            events.success(obj.getClass());
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

    public static class MainClass {
        public int upSpeed;

        public int getUpSpeed() {
            return upSpeed;
        }

        public void setUpSpeed(int upSpeed) {
            this.upSpeed = upSpeed;
        }

        public int getDownSpeed() {
            return downSpeed;
        }

        public void setDownSpeed(int downSpeed) {
            this.downSpeed = downSpeed;
        }

        public int downSpeed;

    }


}



