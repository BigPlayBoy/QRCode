package com.imooc.qrcode;

/**
 * Created by CUI on 2016/8/24.
 * Student
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.print.DocFlavor;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.exception.DecodingFailedException;
import com.swetake.util.Qrcode;

public class javaQRCode {
    char QRCODEERRORCORRECT;//设置二维码的容错率
    char QRCODEENCODEMODE;//设置二维码编码的格式 B支持中文
    int QRCODEVERSION;//设置二维码尺寸
    String IMAGETYPE;//设置图片格式

    public String getIMAGETYPE() {
        return IMAGETYPE;
    }

    public void setIMAGETYPE(String IMAGETYPE) {
        this.IMAGETYPE = IMAGETYPE;
    }

    public int getQRCODEVERSION() {

        return QRCODEVERSION;
    }

    public void setQRCODEVERSION(int QRCODEVERSION) {
        this.QRCODEVERSION = QRCODEVERSION;
    }

    public char getQrcodeencodemode() {

        return QRCODEENCODEMODE;
    }

    public void setQrcodeencodemode(char qrcodeencodemode) {
        this.QRCODEENCODEMODE = qrcodeencodemode;
    }

    public char getQRCODEERRORCORRECT() {
        return QRCODEERRORCORRECT;
    }

    public void setQRCODEERRORCORRECT(char QRCODEERRORCORRECT) {
        this.QRCODEERRORCORRECT = QRCODEERRORCORRECT;
    }

    public javaQRCode() {
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param imgPath 图片路径
     */
    public void encoderQRCode(String content, String imgPath) {
        this.encoderQRCode(content, imgPath, "png", 7);
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param output  输出流
     */
    public void encoderQRCode(String content, OutputStream output) {
        this.encoderQRCode(content, output, "png", 7);
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param imgPath 图片路径
     * @param imgType 图片类型
     */
    private void encoderQRCode(String content, String imgPath, String imgType) {
        this.encoderQRCode(content, imgPath, imgType, 7);
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param output  输出流
     * @param imgType 图片类型
     */
    public void encoderQRCode(String content, OutputStream output, String imgType) {
        this.encoderQRCode(content, output, imgType, 7);
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param imgPath 图片路径
     * @param imgType 图片类型
     * @param size    二维码尺寸
     */
    private void encoderQRCode(String content, String imgPath, String imgType, int size) {
        try {
            BufferedImage bufImg = this.qRCodeCommon(content, size);

            File imgFile = new File(imgPath);
            // 生成二维码QRCode图片
            ImageIO.write(bufImg, imgType, imgFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param output  输出流
     * @param imgType 图片类型
     * @param size    二维码尺寸
     */
    private void encoderQRCode(String content, OutputStream output, String imgType, int size) {
        try {
            BufferedImage bufImg = this.qRCodeCommon(content, size);
            // 生成二维码QRCode图片
            ImageIO.write(bufImg, imgType, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码(QRCode)图片的公共方法
     *
     * @param content 存储内容
     * @param size    二维码尺寸
     */

    private BufferedImage qRCodeCommon(String content, int size) {
        BufferedImage bufImg = null;
        try {
            Qrcode qrcodeHandler = new Qrcode();
            // 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
            qrcodeHandler.setQrcodeErrorCorrect(QRCODEERRORCORRECT);
            qrcodeHandler.setQrcodeEncodeMode(QRCODEENCODEMODE);
            // 设置设置二维码尺寸，取值范围1-40，值越大尺寸越大，可存储的信息越大
            qrcodeHandler.setQrcodeVersion(QRCODEVERSION);
            // 获得内容的字节数组，设置编码格式
            byte[] contentBytes = content.getBytes("utf-8");
            // 图片尺寸
            int imgSize = 67 + 30 * (QRCODEVERSION - 1);
            bufImg = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
            Graphics2D gs = bufImg.createGraphics();
            // 设置背景颜色
            gs.setBackground(Color.WHITE);
            gs.clearRect(0, 0, imgSize, imgSize);

            // 设定图像颜色> BLACK
            gs.setColor(Color.BLACK);
            // 设置偏移量，不设置可能导致解析出错
            int pixoff = 10;
            // 输出内容> 二维码
            if (contentBytes.length > 0 && contentBytes.length < 8000) {
                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            gs.fillRect(j * 5 + pixoff, i * 5 + pixoff, 5, 5);//起始坐标 填充宽度/高度
                        }
                    }
                }
            } else {
                throw new Exception("QRCode content bytes length = " + contentBytes.length + " not in [0, 800].");
            }
            gs.dispose();
            bufImg.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufImg;
    }

    /**
     * 解析二维码（QRCode）
     *
     * @param imgPath 图片路径
     */
    private String decoderQRCode(String imgPath) {
        // QRCode 二维码图片的文件
        File imageFile = new File(imgPath);
        BufferedImage bufImg = null;
        String content = null;
        try {
            bufImg = ImageIO.read(imageFile);
            QRCodeDecoder decoder = new QRCodeDecoder();
            content = new String(decoder.decode(new MYQRCodeImage(bufImg)), "utf-8");
        } catch (IOException | DecodingFailedException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return content;
    }

    /**
     * 解析二维码（QRCode）
     *
     * @param input 输入流
     */
    public String decoderQRCode(InputStream input) {
        BufferedImage bufImg = null;
        String content = null;
        try {
            bufImg = ImageIO.read(input);
            QRCodeDecoder decoder = new QRCodeDecoder();
            content = new String(decoder.decode(new MYQRCodeImage(bufImg)), "utf-8");
        } catch (IOException | DecodingFailedException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return content;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("欢迎使用此功能：\n");
        System.out.println("这里可以快速的将文本生成二维码\n");
        System.out.print("请输入要导出的路径：");
//        String imagePath = scanner.nextLine();
                String imagePath = "e://1.png";

        System.out.println("请在下面输入你要说的话：");
//        String context = scanner.nextLine();
        String context = "找我聊天";

        javaQRCode handler = new javaQRCode();
        handler.setIMAGETYPE("png");
        handler.setQrcodeencodemode('B');
        handler.setQRCODEERRORCORRECT('M');
        handler.setQRCODEVERSION(7);
        handler.encoderQRCode(context, imagePath, "png");
        System.out.println("========encoder success");
        String decoderContent = handler.decoderQRCode(imagePath);
        System.out.println("解析结果如下：");
        System.out.println(decoderContent);
        System.out.println("========decoder success!!!");
    }
}