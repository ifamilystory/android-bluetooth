package com.zhangling.bluetooth.util;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;

import com.zhangling.bluetooth.custom.ZLColor;

import java.text.SimpleDateFormat;

/**
 * Created by ysec on 2018/3/19.
 */

public class ZLUtil {

    public static SimpleDateFormat HHmmssFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            if (i != src.length -1){
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }

    public static String byteToHexString(byte src){
        StringBuilder stringBuilder = new StringBuilder("");
        int v = src & 0xFF;
        String hv = Integer.toHexString(v);
        if (hv.length() < 2) {
            stringBuilder.append(0);
        }
        stringBuilder.append(hv);
        return stringBuilder.toString();
    }


    public static int byte2ToUnsignedShort(byte src) {
        return (src & 0xFF);
    }
    public static int twoBytes2ToUnsignedShort(byte[] src) {
        int high = src[0];
        int low = src[1];
        return (high << 8 & 0xFF00) | (low & 0xFF);
    }

    public static int fourBytes2Int(byte[] bytes) {
        int b0 = bytes[0] & 0xFF;
        int b1 = bytes[1] & 0xFF;
        int b2 = bytes[2] & 0xFF;
        int b3 = bytes[3] & 0xFF;
        return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
    }

    /*
     * 字符转换为字节
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /*
     * 16进制字符串转字节数组
     */
    public static byte[] hexString2Bytes(String hex) {

        if ((hex == null) || (hex.equals(""))){
            return null;
        }
        else if (hex.length()%2 != 0){
            return null;
        }
        else{
            hex = hex.toUpperCase();
            int len = hex.length()/2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i=0; i<len; i++){
                int p=2*i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p+1]));
            }
            return b;
        }

    }

    public static void tinkButtonColor(ImageButton button,int colorValue){
        Drawable src = button.getDrawable();
        button.setImageDrawable(ZLColor.tintDrawable(src, ColorStateList.valueOf(colorValue)));
    }
}
