package com.zhangling.bluetooth.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.zhangling.bluetooth.view.NavigationView;

/**
 * Created by ysec on 2017/12/12.
 */
public class BaseActivity extends AppCompatActivity implements ActivityImpl {
    public NavigationView.Model mNavigationViewModel;
    public NavigationView mNavigationView;
    @Override
    public void setupUI() {

    }
    @Override
    public void setupNavigationView() {
        if (mNavigationViewModel == null) {
            mNavigationViewModel = new NavigationView.Model();
        }
        if (mNavigationView == null) {
            NavigationView navView = new NavigationView(this);
            mNavigationView = navView;
            getWindow().addContentView(navView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        updateNavigationView();

    }

    @Override
    public void updateNavigationView() {
        mNavigationView.setTitleText(mNavigationViewModel.getTitle());
        if (mNavigationViewModel.getRightIconName() != null || mNavigationViewModel.getRightIconNameId() >0) {

            if (mNavigationViewModel.getRightIconNameId() >0) {
                mNavigationView.setRightImageBtn(mNavigationViewModel.getRightIconNameId());
            }else {

            }

            mNavigationView.setRightImageButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rightNavigationViewAction(view);
                }
            });
        }else {

        }

        if (mNavigationViewModel.getLeftIconName() != null || mNavigationViewModel.getLeftIconNameId() >0) {

            if (mNavigationViewModel.getLeftIconNameId() >0) {
                mNavigationView.setLeftImageBtn(mNavigationViewModel.getLeftIconNameId());
            }else {

            }

            mNavigationView.setLeftButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    leftNavigationViewAction(view);
                }
            });
        }else {

        }

        if (mNavigationViewModel.getBackgroundColorId()>0) {
            mNavigationView.setBackgroundColor(mNavigationViewModel.getBackgroundColorId());
        }
    }

    @Override
    public void leftNavigationViewAction(View view) {

    }

    @Override
    public void rightNavigationViewAction(View view) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setupNavigationView();
        setupUI();

    }
}



