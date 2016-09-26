/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.weibu.dreams.basic;

import android.service.dreams.DreamService;
import android.util.Log;
import android.view.TextureView;
import android.os.Handler;
import android.os.HandlerThread;
import  android.content.Intent;
import android.os.PowerManager;
import android.content.Context;

/**
 * Plays a delightful show of colors.
 * <p>
 * This dream performs its rendering using OpenGL on a separate rendering thread.
 * </p>
 */
public class VideoPlayService extends DreamService  {

    // The handler thread and handler on which the GL renderer is running.
    private HandlerThread mRendererHandlerThread;
    private Handler mRendererHandler;
    private  PowerManager pm;
    private PowerManager.WakeLock wakeLock;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("fei8100","-------onCreate-----in VideoPlayService.java");
        setInteractive(false);


    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setInteractive(false);
      //setLowProfile(true);
        setFullscreen(true);
    //  wakeLock.setReferenceCounted(false);
     //finish(); 
    }
  @Override
  public void onDreamingStarted(){
      Log.d("fei8100","----------onDreamingStarted()---in  VideoPlayService.java");
       Intent toIntent = new Intent(getApplicationContext(), VideoActivity.class);   
       toIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
       getApplication().startActivity(toIntent); 
  // finish(); 


}


/*  @Override
  public void onDetachedFromWindow(){
    Log.d("fei8100","-----onDetachedFromWindow---in VideoPlayService.java");
   // wakeLock.release();
  }
*/

@Override
 public void onDestroy(){
   super.onDestroy();   
  Log.d("fei8100","--------onDestroy---in VideoPlayService.java");
   }
}
