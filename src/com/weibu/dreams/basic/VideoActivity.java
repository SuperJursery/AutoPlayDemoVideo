package com.weibu.dreams.basic;

import java.io.File;
import java.util.TimerTask;
import java.util.Timer;

import android.app.Activity; 
import android.app.KeyguardManager;
import android.service.dreams.DreamService;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import android.os.Environment;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent; 
import android.app.StatusBarManager;
import android.content.Context;
import android.provider.Settings;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.media.MediaPlayer; 
import android.os.Build;
import android.view.View;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.os.BatteryManager;
import android.content.Intent;
import android.content.IntentFilter; 
import android.text.TextUtils;

import android.app.ActivityManager;
import static android.provider.Settings.System.SCREEN_OFF_TIMEOUT;

import android.widget.Toast;

public class VideoActivity extends Activity {

   private Context context;
   private  VideoView videoView;
   private Uri uri;
   private  MediaController mController;
   private  long currentTimeout;
   private PowerManager.WakeLock wakeLock;
   private Timer timer = null;
   private MyTimerTask mTimerTask;

   private  KeyguardManager km= null;
   private  KeyguardManager.KeyguardLock kl =null;
   private  PowerManager pm=null;
   private  PowerManager.WakeLock wl =null;
 

   private final String moviePath="/system/media/demo.avi";
   private final String CAMERA_IMAGE_DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/";

  class  MyTimerTask  extends TimerTask {

 @Override
  public void run() {
       Message msg = new Message();
       msg.what = 1;
       handler.sendMessage(msg);
     } 
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     setContentView(R.layout.main_dream);
     context=this;

     IntentFilter commandFilter = new IntentFilter();
     commandFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
     commandFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
     registerReceiver(PowerConnectionReceiver, commandFilter);

     registerReceiver(mHomeKeyEventReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)); 


         km= (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE); 
         kl = km.newKeyguardLock("unLock");
         pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
         wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"bright"); 
    


    timer = new Timer(true);
    currentTimeout = Settings.System.getLong(getContentResolver(), SCREEN_OFF_TIMEOUT,15000);
    wakeUpAndUnlock(context,true);
    //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
    videoView = (VideoView) findViewById(R.id.video);
    
       //     mController = new MediaController(this);
       //     videoView.setMediaController(mController);
           uri =Uri.fromFile(new File(moviePath));
           videoView.setVideoURI(uri);
           videoView.start(); 
           showSystemUi(false);
           videoView.requestFocus();
   Log.d("fei8100","----------onCreate------in  VideoActivity.java");
    }

   public void wakeUpAndUnlock(Context content,boolean isEnable){
         //  KeyguardManager km= (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE); 
          // KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
          // PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
           //PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"bright"); 
          if(isEnable){
         	 kl.disableKeyguard(); 
                 wl.acquire();  
                 wl.release();
             }
           else
           	kl.reenableKeyguard();
   }




   private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {  
        String SYSTEM_REASON = "reason";  
        String SYSTEM_HOME_KEY = "homekey";  
        String SYSTEM_HOME_KEY_LONG = "recentapps";  
           
        @Override  
        public void onReceive(Context context, Intent intent) {  
            String action = intent.getAction();  
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {  
                String reason = intent.getStringExtra(SYSTEM_REASON);  
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {  
                     Log.d("fei8100","------------SYSTEM_HOME_KEY-----");
                       finish();
                }
            }   
        }  

   };


  @Override
   public void  onResume(){
      super.onResume();
     Log.v("fei8100", "onResume----1----in VideoActivity.java");  
    if(kl!=null);
     wakeUpAndUnlock(context,false);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {  
   
                     @Override  
                     public void onCompletion(MediaPlayer mp) {  
         //                myVideoView.setVideoPath(videopath);  
                         videoView.setVideoURI(uri);  
                         videoView.start(); 
                         showSystemUi(false);
                         videoView.requestFocus();
                         Log.v("fei8100", "-----repeat play----in VideoActivity.java");   
                     }  
                 }); 
                       initAppUserData("com.android.chrome");
                       initAppUserData("com.android.camera2");
		       clearCameraPhotoAndVideo(CAMERA_IMAGE_DIRECTORY);
   }

   private void clearCameraPhotoAndVideo(String camera_path) {
              File file = new File(camera_path);
              if (file.exists()) {
                                          if (file.isDirectory()) {
                                                           File[] childFiles = file.listFiles();
                                                           if (childFiles == null || childFiles.length == 0) {
                                                                               file.delete();
                                                                            return;
                                                           }
                                                           for (int i = 0; i < childFiles.length; i++) {
                                                                               childFiles[i].delete();
                                                                               Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                                                                               Uri uri = Uri.fromFile(childFiles[i]);
                                                                                               intent.setData(uri);
                                                                                               context.sendBroadcast(intent);
                                                           }
                                                           file.delete();
                                                }
                          }
    }

   private void initAppUserData(String pkgname) {
                       ActivityManager am = (ActivityManager)
                       context.getSystemService(Context.ACTIVITY_SERVICE);
                       am.clearApplicationUserData(pkgname, null);
    }

    @Override
 public void onRestart() {
        super.onRestart();
        Log.v("fei8100", "--------------onRestart");
    }
 
   @Override
 public void onPause() {
        if(videoView.isPlaying()){
            Log.v("fei8100", "--------is palying------onPause");
           finish();
           }
        super.onPause();
        Log.v("fei8100", "--------------onPause");
    }

    @Override
       public void onStart() {
           super.onStart();
           Log.v("fei8100", "----------------onStart");
       }
    

   @Override
    public void onStop() {
        super.onStop();
        Log.v("fei8100", "--------------onStop");
    }


     @SuppressWarnings("deprecation")
     @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
     private void showSystemUi(boolean visible) {                                                                                                                        
         int flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                 |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
         if (!visible) {
             // We used the deprecated "STATUS_BAR_HIDDEN" for unbundling
             flag |= View.STATUS_BAR_HIDDEN | View.SYSTEM_UI_FLAG_FULLSCREEN
                     | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
         }
         videoView.setSystemUiVisibility(flag);
     }
 

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (super.onTouchEvent(event)) {
            return true;
        }

        switch (event.getAction()) {                                                                                                                                    
            case MotionEvent.ACTION_DOWN:
				if(event.getY() >= 80 && event.getY() <= 1000){
                if(videoView.isPlaying()){
                     videoView.pause();
                     StartDelayPlay(); 
                   }
                else 
                     videoView.start();
                     showSystemUi(false);
					}
                break;
            case MotionEvent.ACTION_UP:
                Log.d("fei810","---------MotionEvent.ACTION_UP--1---");
                break;
        }
        return true;

    }

  public void StartDelayPlay(){
        if (timer !=null) {
            if(mTimerTask !=null)
               mTimerTask.cancel();
           
           mTimerTask = new MyTimerTask();
           timer.schedule(mTimerTask, currentTimeout-800);
          }
       }


   private Handler handler  = new Handler(){

    public void handleMessage(Message msg) {

        super.handleMessage(msg);

        if(msg.what == 1){
          //todo something....
              videoView.start();
              showSystemUi(false);

          }

       }
 
   };
 private BroadcastReceiver  PowerConnectionReceiver =new  BroadcastReceiver() {
     @Override
     public void onReceive(Context context, Intent intent) { 
         int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
         boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                             status == BatteryManager.BATTERY_STATUS_FULL;
  
         int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -2);
         boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
         boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;


     if(videoView.isPlaying()&&!isCharging){
          videoView.pause();
            finish();
   }
     if(!isCharging)
           finish();
        Log.d("fei8100","PowerConnectionReceiver-----isCharging:"+isCharging+"  chargePlug:"+chargePlug);
     }
 };

 

	@Override
	public void onDestroy() {
                Log.d("fei8100","onDestroy()----in  VideoActivity.java");
                 wakeUpAndUnlock(context,false);
                unregisterReceiver(PowerConnectionReceiver); 
                unregisterReceiver(mHomeKeyEventReceiver); 
               //   wakeLock.release();
		super.onDestroy();
	}
}
