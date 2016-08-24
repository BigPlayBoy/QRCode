package com.imooc.qrcode;

import java.awt.image.BufferedImage;

/**
 * Created by CUI on 2016/8/24.
 * Student
 */
public class MYQRCodeImage implements jp.sourceforge.qrcode.data.QRCodeImage{
    BufferedImage bufferedImage;
    @Override
    public int getWidth() {
        return bufferedImage.getWidth();
    }

    @Override
    public int getHeight() {
        return bufferedImage.getHeight();
    }

    @Override
    public int getPixel(int i, int i1) {
        return bufferedImage.getRGB(i,i1);
    }

    public MYQRCodeImage(BufferedImage bufferedImage) {
        this.bufferedImage=bufferedImage;
    }
}
