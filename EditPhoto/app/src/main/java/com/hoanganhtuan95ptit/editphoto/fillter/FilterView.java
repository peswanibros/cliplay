package com.hoanganhtuan95ptit.editphoto.fillter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.hoanganhtuan95ptit.editphoto.fillter.library.filter.FilterManager;
import com.hoanganhtuan95ptit.editphoto.fillter.library.image.ImageEglSurface;
import com.hoanganhtuan95ptit.editphoto.fillter.library.image.ImageRenderer;

import androidx.appcompat.widget.AppCompatImageView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
;

/**
 * Created by Hoang Anh Tuan on 12/7/2017.
 */

public class FilterView extends AppCompatImageView {

    private FilterManager.FilterType type;
    private ImageRenderer imageRenderer;

    public FilterView(Context context) {
        super(context);
        initView(context);
    }

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        imageRenderer = new ImageRenderer(context, FilterManager.FilterType.Normal);
    }

    public ImageRenderer getImageRenderer() {
        return imageRenderer;
    }

    public FilterManager.FilterType getType() {
        return type;
    }

    public void setType(String url, FilterManager.FilterType type, final Observer<String> observer) {
        if (this.type == type) return;
        this.type = type;

        Observable.just(url)
                .map(url1 -> filterBitmap(url1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        observer.onSubscribe(d);
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    private Bitmap filterBitmap(String url) {
        Bitmap bitmap = Utils.getBitmapSdcard(url);
        bitmap = Utils.scaleDown(bitmap);

        ImageEglSurface imageEglSurface = new ImageEglSurface(bitmap.getWidth(), bitmap.getHeight());
        imageEglSurface.setRenderer(imageRenderer);
        imageRenderer.changeFilter(type);
        imageRenderer.setImageBitmap(bitmap);
        imageEglSurface.drawFrame();
        bitmap = imageEglSurface.getBitmap();
        imageEglSurface.release();
        imageRenderer.destroy();

        return bitmap;
    }

}
