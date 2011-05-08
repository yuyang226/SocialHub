package com.ebay.sdk.sample.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ViewSwitcher;
import android.widget.ImageView.ScaleType;

import com.ebay.sdk.sample.imageloader.ImageLoader;
import com.ebay.sdk.sample.imageloader.ImageLoaderHandler;

/**
 * An image view that fetches its image off the web using the supplied URL.
 * While the image is being downloaded, a progress indicator will be shown.
 * 
 * @author Will
 */
public class WebImageView extends ViewSwitcher {

    private String imageUrl;

    private boolean isLoaded;

    private ProgressBar loadingSpinner;

    private ImageView imageView;

    private ScaleType scaleType = ScaleType.CENTER_CROP;

    private Drawable progressDrawable;

    /**
     * @param context
     *        the view's current context
     * @param imageUrl
     *        the URL of the image to download and show
     * @param autoLoad
     *        Whether the download should start immediately after creating the
     *        view. If set to false, use {@link #loadImage()} to manually
     *        trigger the image download.
     */
    public WebImageView(Context context, String imageUrl, boolean autoLoad) {
        super(context);
        initialize(context, imageUrl, autoLoad);
    }

    public WebImageView(Context context) {
        super(context);
        initialize(context, null, true);
        // styles.recycle();
    }
    
    public WebImageView(Context context, AttributeSet attrs) {
    	super(context, attrs);
    	initialize(context,null, true);
    }

    private void initialize(Context context, String imageUrl,
            boolean autoLoad) {
        this.imageUrl = imageUrl;

        ImageLoader.initialize(context);

        // ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
        // 125.0f, preferredItemHeight / 2.0f);
        // anim.setDuration(500L);

        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500L);
        setInAnimation(anim);

        addLoadingSpinnerView(context);
        addImageView(context);

        if (autoLoad && imageUrl != null) {
            loadImage();
        }
    }

    private void addLoadingSpinnerView(Context context) {
        loadingSpinner = new ProgressBar(context);
        loadingSpinner.setIndeterminate(true);
        this.progressDrawable = loadingSpinner.getIndeterminateDrawable();

        LayoutParams lp = new LayoutParams(progressDrawable.getIntrinsicWidth(),
                progressDrawable.getIntrinsicHeight());
        lp.gravity = Gravity.CENTER;

        addView(loadingSpinner, 0, lp);
    }

    private void addImageView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(scaleType);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        addView(imageView, 1, lp);
    }

    /**
     * Use this method to trigger the image download if you had previously set
     * autoLoad to false.
     */
    public void loadImage() {
        if (imageUrl == null) {
            throw new IllegalStateException(
                    "image URL is null; did you forget to set it for this view?");
        }
        ImageLoader.start(imageUrl, new DefaultImageLoaderHandler());
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Often you have resources which usually have an image, but some don't. For
     * these cases, use this method to supply a placeholder drawable which will
     * be loaded instead of a web image.
     * 
     * @param imageResourceId
     *        the resource of the placeholder image drawable
     */
    public void setNoImageDrawable(int imageResourceId) {
        imageView.setImageDrawable(getContext().getResources().getDrawable(imageResourceId));
        setDisplayedChild(1);
    }

    @Override
    public void reset() {
        super.reset();

        this.setDisplayedChild(0);
    }

    private class DefaultImageLoaderHandler extends ImageLoaderHandler {

        public DefaultImageLoaderHandler() {
            super(imageView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isLoaded = true;

            setDisplayedChild(1);
        }
    }
}
