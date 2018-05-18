package com.zhangling.bluetooth.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhangling.bluetooth.R;
import com.zhangling.bluetooth.custom.ZLColor;
import com.zhangling.bluetooth.util.ZLUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ysec on 2017/11/29.
 */

public class NavigationView extends RelativeLayout {

    public static class Model {
        private String title;
        private int titleId;
        private String leftTitle;
        private int leftTitleId;
        private String rightTitle;
        private int rightTitleId;
        private String leftIconName;
        private int leftIconNameId;
        private String rightIconName;
        private int rightIconNameId;
        private Boolean isHiddenLeft = true;
        private int backgroundColorId;

        public int getBackgroundColorId() {
            return backgroundColorId;
        }

        public void setBackgroundColorId(int backgroundColorId) {
            this.backgroundColorId = backgroundColorId;
        }

        public int getTitleId() {
            return titleId;
        }

        public void setTitleId(int titleId) {
            this.titleId = titleId;
        }

        public int getLeftTitleId() {
            return leftTitleId;
        }

        public void setLeftTitleId(int leftTitleId) {
            this.leftTitleId = leftTitleId;
        }

        public int getRightTitleId() {
            return rightTitleId;
        }

        public void setRightTitleId(int rightTitleId) {
            this.rightTitleId = rightTitleId;
        }

        public int getLeftIconNameId() {
            return leftIconNameId;
        }

        public void setLeftIconNameId(int leftIconNameId) {
            this.leftIconNameId = leftIconNameId;
        }

        public int getRightIconNameId() {
            return rightIconNameId;
        }

        public void setRightIconNameId(int rightIconNameId) {
            this.rightIconNameId = rightIconNameId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLeftTitle() {
            return leftTitle;
        }

        public void setLeftTitle(String leftTitle) {
            this.leftTitle = leftTitle;
        }

        public String getRightTitle() {
            return rightTitle;
        }

        public void setRightTitle(String rightTitle) {
            this.rightTitle = rightTitle;
        }

        public String getLeftIconName() {
            return leftIconName;
        }

        public void setLeftIconName(String leftIconName) {
            this.leftIconName = leftIconName;
        }

        public String getRightIconName() {
            return rightIconName;
        }

        public void setRightIconName(String rightIconName) {
            this.rightIconName = rightIconName;
        }

        public Boolean getHiddenLeft() {
            return isHiddenLeft;
        }

        public void setHiddenLeft(Boolean hiddenLeft) {
            isHiddenLeft = hiddenLeft;
        }
    }

    @BindView(R.id.leftImageButton)
    public ImageButton mLeftImageBtn;
    @BindView(R.id.rightImageButton)
    public ImageButton mRightImageBtn;
    @BindView(R.id.leftButton)
    public Button mLeftBtn;
    @BindView(R.id.rightButton)
    public Button mRightBtn;
    @BindView(R.id.titleTextView)
    public TextView mTitleTextView;

    public NavigationView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.navigation_bar, this);
        ButterKnife.bind(this);
    }
    // 设置标题的方法
    public void setTitleText(String title) {
        mTitleTextView.setText(title);
    }

    // 设置标题的方法
    public void setTitleText(Context content,int id) {
        mTitleTextView.setText(content.getString(id));
    }

    // 设置标题的方法
    public void setRightBtnText(String title) {
        mRightImageBtn.setClickable(false);
        mRightBtn.setText(title);
    }

    // 设置标题的方法
    public void setLeftBtnText(String title) {
        mLeftImageBtn.setClickable(false);
        mLeftBtn.setText(title);
    }

    // 设置标题的方法
    public void setRightBtnText(Context content, int id) {
        mRightImageBtn.setClickable(false);
        mRightBtn.setText(content.getString(id));
    }

    // 设置标题的方法
    public void setLeftBtnText(Context content,int id) {
        mLeftImageBtn.setClickable(false);
        mLeftBtn.setText(content.getString(id));
    }

    // 设置标题的方法
    public void setRightImageBtn(int id) {
        mRightBtn.setClickable(false);
        mRightImageBtn.setImageResource(id);
        ZLUtil.tinkButtonColor(mRightImageBtn,Color.WHITE);
    }

    // 设置标题的方法
    public void setLeftImageBtn(int id) {
        mLeftBtn.setClickable(false);
        mLeftImageBtn.setImageResource(id);
    }

    // 为左侧返回按钮添加自定义点击事件
    public void setLeftButtonListener(OnClickListener listener) {
        mLeftBtn.setOnClickListener(listener);
    }

    // 为左侧返回按钮添加自定义点击事件
    public void setRightButtonListener(OnClickListener listener) {
        mRightBtn.setOnClickListener(listener);
    }

    // 为左侧返回按钮添加自定义点击事件
    public void setLeftImageButtonListener(OnClickListener listener) {
        mLeftImageBtn.setOnClickListener(listener);
    }

    // 为左侧返回按钮添加自定义点击事件
    public void setRightImageButtonListener(OnClickListener listener) {
        mRightImageBtn.setOnClickListener(listener);
    }
}
