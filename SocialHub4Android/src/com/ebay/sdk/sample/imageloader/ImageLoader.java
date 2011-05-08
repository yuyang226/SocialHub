package com.ebay.sdk.sample.imageloader;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

/**
* Realizes an background image loader ,a thread from a thread pool will be used to download
* the image in the background and set the image on the view as soon as it
* completes.
* 
* @author Will
*/
public class ImageLoader implements Runnable {

   private static ThreadPoolExecutor executor;

   private static final int DEFAULT_POOL_SIZE = 6;

   static final int HANDLER_MESSAGE_ID = 0;

   static final String BITMAP_EXTRA = "sdk:extra_bitmap";

   private static int numAttempts = 3;

   /**
    * @param numThreads
    *        the maximum number of threads that will be started to download
    *        images in parallel
    */
   public static void setThreadPoolSize(int numThreads) {
       executor.setMaximumPoolSize(numThreads);
   }

   /**
    * @param numAttempts
    *        how often the image loader should retry the image download if
    *        network connection fails
    */
   public static void setMaxDownloadAttempts(int numAttempts) {
       ImageLoader.numAttempts = numAttempts;
   }

   /**
    * This method must be called before any other method is invoked on this
    * class. This method is idempotent. You may call it multiple times without any
    * side effects.
    * 
    * @param context
    *        the current context
    */
   public static synchronized void initialize(Context context) {
       if (executor == null) {
           executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);
       }
   }

   private String imageUrl;

   private Handler handler;

   private ImageLoader(String imageUrl, ImageView imageView) {
       this.imageUrl = imageUrl;
       this.handler = new ImageLoaderHandler(imageView);
   }

   private ImageLoader(String imageUrl, ImageLoaderHandler handler) {
       this.imageUrl = imageUrl;
       this.handler = handler;
   }

   /**
    * Triggers the image loader for the given image and view. The image loading
    * will be performed concurrently to the UI main thread, using a fixed size
    * thread pool. The loaded image will be posted back to the given ImageView
    * upon completion.
    * 
    * @param imageUrl
    *        the URL of the image to download
    * @param imageView
    *        the ImageView which should be updated with the new image
    */
   public static void start(String imageUrl, ImageView imageView) {
	   ImageLoader loader = new ImageLoader(imageUrl, imageView);
       // fetch the image in the background
       executor.execute(loader);
   }

   /**
    * Triggers the image loader for the given image and handler. The image
    * loading will be performed concurrently to the UI main thread, using a
    * fixed size thread pool. The loaded image will not be automatically posted
    * to an ImageView; instead, you can pass a custom
    * {@link ImageLoaderHandler} and handle the loaded image yourself (e.g.
    * cache it for later use).
    * 
    * @param imageUrl
    *        the URL of the image to download
    * @param handler
    *        the handler which is used to handle the downloaded image
    */
   public static void start(String imageUrl, ImageLoaderHandler handler) {
       ImageLoader loader = new ImageLoader(imageUrl, handler);
       executor.execute(loader);
   }

   public void run() {
       Bitmap bitmap = null;
       int timesTried = 1;

       while (timesTried <= numAttempts) {
           try {
               URL url = new URL(imageUrl);
   			   InputStream is = url.openStream();
   			   BufferedInputStream bis = new BufferedInputStream(is);
   			   bitmap = BitmapFactory.decodeStream(bis);
               bis.close();
   			   is.close();
               break;
           } catch (Throwable e) {
               Log.w(ImageLoader.class.getSimpleName(), "download for " + imageUrl
                       + " failed (attempt " + timesTried + ")");
               try {
                   Thread.sleep(2000);
               } catch (InterruptedException e1) {
               }

               timesTried++;
           }
       }

       if (bitmap != null) {
           notifyImageLoaded(bitmap);
       }
   }

   public void notifyImageLoaded(Bitmap bitmap) {
       Message message = new Message();
       message.what = HANDLER_MESSAGE_ID;
       Bundle data = new Bundle();
       data.putParcelable(BITMAP_EXTRA, bitmap);
       message.setData(data);

       handler.sendMessage(message);
   }
}

