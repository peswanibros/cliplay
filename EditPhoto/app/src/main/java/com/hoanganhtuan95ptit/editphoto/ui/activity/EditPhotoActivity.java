package com.hoanganhtuan95ptit.editphoto.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.hoanganhtuan95ptit.editphoto.R;
import com.hoanganhtuan95ptit.editphoto.ui.fragment.EditPhotoFragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Hoang Anh Tuan on 12/14/2017.
 */
public class EditPhotoActivity extends BaseActivity {
    private static final String INPUT_URL = "inputUrl";

    public static void start(Context context, String inputUrl) {
        Intent starter = new Intent(context, EditPhotoActivity.class);
        starter.putExtra(INPUT_URL, inputUrl);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_photo);
        addFragment(EditPhotoFragment.create(getIntent().getStringExtra(INPUT_URL)));
    }

    private void addFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.rootMain, fragment);
        ft.commit();
    }

    public void addFragmentToStack(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        for (Fragment fragment1 : manager.getFragments()) {
            ft.hide(fragment1);
        }
        ft.add(R.id.rootMain, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
