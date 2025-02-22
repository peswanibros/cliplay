package com.hoanganhtuan95ptit.editphoto.crop;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoanganhtuan95ptit.editphoto.R;
import com.wang.avi.AVLoadingIndicatorView;
import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.TransformImageView;
import com.yalantis.ucrop.view.UCropView;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Hoang Anh Tuan on 11/15/2017.
 */
public class CropFragment extends Fragment implements TransformImageView.TransformImageListener,
        CropAdapter.OnItemCropClickedListener,
        View.OnClickListener {
    private static final String INPUT_URL = "inputUrl";
    private UCropView ivCrop;
    private AVLoadingIndicatorView ivLoading;
    private RecyclerView list;

    public static CropFragment create(String inputUrl, OnCropListener onCropListener) {
        CropFragment fragment = new CropFragment();
        fragment.setOnCropListener(onCropListener);
        Bundle bundle = new Bundle();
        bundle.putString(INPUT_URL, inputUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    private GestureCropImageView mGestureCropImageView;
    private OverlayView mOverlayView;
    private OnCropListener onCropListener;

    private void setOnCropListener(OnCropListener onCropListener) {
        this.onCropListener = onCropListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crop, container, false);
        mappingView(view);
        return view;
    }

    private void mappingView(View view) {
        ivCrop = view.findViewById(R.id.ivCrop);
        ivLoading = view.findViewById(R.id.ivLoading);
        list = view.findViewById(R.id.list);
        ImageView ivCancel = view.findViewById(R.id.ivCancel);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        ImageView ivCheck = view.findViewById(R.id.ivCheck);
        RelativeLayout rootCrop = view.findViewById(R.id.rootCrop);
        ivCancel.setOnClickListener(this);
        ivCheck.setOnClickListener(this);
        rootCrop.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        CropAdapter cropAdapter = new CropAdapter(getActivity());
        cropAdapter.setOnItemCropClickedListener(this);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .HORIZONTAL, false));
        list.setAdapter(cropAdapter);
        new StartSnapHelper().attachToRecyclerView(list);
        cropAdapter.add(new CropModel(R.drawable.pg_sdk_edit_crop_crop11_selected, "1:1",
                CropModel.Type.TYPE11));
        cropAdapter.add(new CropModel(R.drawable.pg_sdk_edit_crop_crop23_selected, "2:3",
                CropModel.Type.TYPE23));
        cropAdapter.add(new CropModel(R.drawable.pg_sdk_edit_crop_crop32_selected, "3:2",
                CropModel.Type.TYPE32));
        cropAdapter.add(new CropModel(R.drawable.pg_sdk_edit_crop_crop43_selected, "4:3",
                CropModel.Type.TYPE43));
        cropAdapter.add(new CropModel(R.drawable.pg_sdk_edit_crop_crop34_selected, "3:4",
                CropModel.Type.TYPE34));
        cropAdapter.add(new CropModel(R.drawable.pg_sdk_edit_crop_crop169_selected, "16:9",
                CropModel.Type.TYPE169));
        cropAdapter.add(new CropModel(R.drawable.pg_sdk_edit_crop_crop916_selected, "9:16",
                CropModel.Type.TYPE916));
        mGestureCropImageView = ivCrop.getCropImageView();
        mOverlayView = ivCrop.getOverlayView();
        mGestureCropImageView.setScaleEnabled(true);
        mGestureCropImageView.setRotateEnabled(true);
        mGestureCropImageView.setImageToWrapCropBounds(true);
        mGestureCropImageView.setTransformImageListener(this);
        try {
            if (getArguments() != null) {
                showLoading();
                String inputUrl = getArguments().getString(INPUT_URL);
                assert inputUrl != null;
                mGestureCropImageView.setImageUri(Uri.fromFile(new File(inputUrl)), Uri.fromFile
                        (new File(inputUrl)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveImage() {
        showLoading();
        mGestureCropImageView.cropAndSaveImage(Bitmap.CompressFormat.PNG, 100, new
                BitmapCropCallback() {
            @Override
            public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int
                    imageWidth, int imageHeight) {
                if (onCropListener != null)
                    onCropListener.onCropPhotoCompleted(resultUri.getPath());
                back();
            }

            @Override
            public void onCropFailure(@NonNull Throwable t) {
                back();
            }
        });
    }

    private void back() {
        hideLoading();
        getActivity().onBackPressed();
    }

    @Override
    public void onLoadComplete() {
        hideLoading();
        changeCropType(1f / 1f);
    }

    @Override
    public void onLoadFailure(@NonNull Exception e) {
    }

    @Override
    public void onRotate(float currentAngle) {
    }

    @Override
    public void onScale(float currentScale) {
    }

    @Override
    public void onItemCropClicked(CropModel.Type type) {
        switch (type) {
            case TYPE11:
                changeCropType(1f / 1f);
                break;
            case TYPE23:
                changeCropType(2f / 3f);
                break;
            case TYPE32:
                changeCropType(3f / 2f);
                break;
            case TYPE43:
                changeCropType(4f / 3f);
                break;
            case TYPE34:
                changeCropType(3f / 4f);
                break;
            case TYPE169:
                changeCropType(16f / 9f);
                break;
            case TYPE916:
                changeCropType(9f / 16f);
                break;
        }
    }

    private void hideLoading() {
        if (ivLoading != null)
            ivLoading.smoothToHide();
    }

    private void showLoading() {
        if (ivLoading != null)
            ivLoading.smoothToShow();
    }

    private void changeCropType(float crop) {
        mOverlayView.setTargetAspectRatio(crop);
        mGestureCropImageView.setTargetAspectRatio(crop);
    }

    @Override
    public void onClick(View view) {
        if (ivLoading.isShown()) return;
        if (view.getId() == R.id.ivCancel) {
            back();
        } else if (view.getId() == R.id.ivCheck) {
            saveImage();
        }
    }
}
