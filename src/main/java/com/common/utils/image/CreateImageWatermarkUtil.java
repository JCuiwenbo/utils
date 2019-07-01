package com.common.utils.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Title: CreateImage
 * @Description: 图片生成水印
 * @Author Cui
 * @Date 2019/7/1 10:54
 */
public class CreateImageWatermarkUtil {

    public static void main(String[] args) {
        paintWaterMarkPhoto("D:1.png", "D:4.png", "用户信息");
    }


    public static void paintWaterMarkPhoto(String srcImagePath, String targetImagePath, String mark) {
//        String srcImagePath = "D:1.png";
//        String targetImagePath = "D:3.png";
        Integer degree = -15;
        OutputStream os = null;
        try {
            Image srcImage = ImageIO.read(new File(srcImagePath));
            BufferedImage bufImage = new BufferedImage(srcImage.getWidth(null), srcImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
            // 得到画布对象
            Graphics2D graphics2D = bufImage.createGraphics();
            // 设置对线段的锯齿状边缘处理
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.drawImage(srcImage.getScaledInstance(srcImage.getWidth(null), srcImage.getHeight(null), Image.SCALE_SMOOTH),
                    0, 0, null);
            if (null != degree) {
                // 设置水印旋转角度及坐标
                graphics2D.rotate(Math.toRadians(degree), (double) bufImage.getWidth() / 2, (double) bufImage.getHeight() / 2);
            }
            // 透明度
            float alpha = 0.25f;
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            // 设置颜色和画笔粗细
            graphics2D.setColor(Color.gray);
            graphics2D.setStroke(new BasicStroke(10));
            graphics2D.setFont(new Font("SimSun", Font.ITALIC, 18));
            // 绘制图案或文字
//            String cont = "无名氏";
            String dateStr = new SimpleDateFormat("YYYY-MM-dd").format(new Date());
            int charWidth1 = 8;
            int charWidth2 = 8;
            int halfGap = 12;
            graphics2D.drawString(mark, (srcImage.getWidth(null) - mark.length() * charWidth1) / 2,
                    (srcImage.getHeight(null) - (charWidth1 + halfGap)) / 2);
            graphics2D.drawString(dateStr, (srcImage.getWidth(null) - dateStr.length() * charWidth2) / 2,
                    (srcImage.getHeight(null) + (charWidth2 + halfGap)) / 2);

            graphics2D.dispose();

            os = new FileOutputStream(targetImagePath);
            // 生成图片 (可设置 jpg或者png格式)
            ImageIO.write(bufImage, "png", os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
