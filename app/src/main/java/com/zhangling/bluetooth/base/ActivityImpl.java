package com.zhangling.bluetooth.base;

import android.view.View;

public interface ActivityImpl {
//    public void loadData();
    public void setupUI();
    public void setupNavigationView();
    public void updateNavigationView();
    public void leftNavigationViewAction(View view);
    public void rightNavigationViewAction(View view);

}
