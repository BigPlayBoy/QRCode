package com.imooc.qrcode;

import com.swetake.util.Qrcode;
import jp.sourceforge.qrcode.QRCodeDecoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by CUI on 2016/8/24.
 * Student
 */
public class CreateQRCode {
    public static void main(String[] args) throws IOException {
        String content="崔明辉";
        File file=new File("e:/cuiminghui.png");
        int version=7;
        char mode='B';//N 代表数字 A代表a-Z  B代表其他字符
        char correct='M';
        createQRCode(content,file,version,mode,correct);
        readQRCode(file);
    }

    private static void readQRCode(File file)  {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        QRCodeDecoder codeDecoder = new QRCodeDecoder();
        String result = null;
        try {
            result = new String(codeDecoder.decode(new MYQRCodeImage(bufferedImage)), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }

    private static void createQRCode(String content,File file,int version,char mode,char correct) throws IOException {
        int imageSize = 67 + 12 * (version - 1);
        Qrcode qrCode = new Qrcode();
        qrCode.setQrcodeErrorCorrect(correct);
        qrCode.setQrcodeEncodeMode(mode);
        qrCode.setQrcodeVersion(version);

        BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setBackground(Color.white);
        graphics2D.setColor(Color.black);
        graphics2D.clearRect(0, 0, imageSize, imageSize);

        byte[] bytes = content.getBytes("utf-8");
        int pix0ff = 2;
        if (bytes.length > 0 && bytes.length < 100) {
            boolean[][] s = qrCode.calQrcode(bytes);
            for (int i = 0; i < s.length; i++) {
                for (int j = 0; j < s.length; j++) {
                    if (s[j][i]) {
                        graphics2D.fillRect(j * 3 + pix0ff, i * 3 + pix0ff, 3, 3);
                    }
                }
            }
        }
        graphics2D.dispose();
        bufferedImage.flush();
        ImageIO.write(bufferedImage, "png", file);
    }
}
