package com.gionee.autotest.util;

import com.sun.imageio.plugins.jpeg.JPEGImageWriter;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * @Author Viking Den
 * @Version 1.0
 * @Email dengwj@gionee.com
 * @Time 10:24
 */
public class ImageUtil {

    private static final String TAG = ImageUtil.class.getSimpleName() ;

    private static final String DEFAULT_IMAGE_FORMAT = "PNG" ;

    private static final int DEFAULT_IMAGE_HEIGHT = 800 ;

    private static final int DEFAULT_IMAGE_WIDTH = 500 ;

    private static final String DEFAULT_COMPRESS_PREFIX = "_compress.png" ;

    private static final String DEFAULT_COMPRESS_PREFIX_JPG = "_compress.jpg" ;


    /**
     * 压缩图片target里面的所有文件，压缩后的保存到dest中，压缩后的文件以_copy命名
     * @param target
     * @param dest
     */
    public final static void compressImgDir(String target , String dest){
        File targetDir = new File(target) ;
        File destDir = new File(dest) ;
        if (targetDir.exists() && targetDir.isDirectory()){
            if (!destDir.exists()){
                destDir.mkdirs() ;
            }
            File[] files = targetDir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    if(name.endsWith(".png")|| name.endsWith(".jpg")){
                        return true ;
                    }
                    return false;
                }
            }) ;
            if (files != null && files.length > 0){
                for(File file : files){
                    File disF = new File(destDir , handleFileName(file.getName())) ;
                    compressImg(file , disF);
                }
            }
        }
    }

    /**
     * 处理文件名
     * @param fileName
     * @return
     */
    private final static String handleFileName(String fileName){
        if(fileName == null)  return null ;
        String args = fileName.split(".png")[0] ;
        if (fileName.contains(".jpg")){
            args = fileName.split(".jpg")[0] ;
            return args + DEFAULT_COMPRESS_PREFIX_JPG ;
        }
        return args + DEFAULT_COMPRESS_PREFIX ;
    }

    /**
     * 缩放图像（按高度和宽度缩放），默认的图片格式为PNG，宽度为500，高度为800
     * @param targetFile 源图像文件地址
     * @param destFile 缩放后的图像地址
     */
    public final static void compressImg(File targetFile , File destFile){
        compressImg(targetFile, destFile, DEFAULT_IMAGE_HEIGHT, DEFAULT_IMAGE_WIDTH);
    }

    /**
     * 缩放图像（按高度和宽度缩放），默认的图片格式为PNG
     * @param targetFile 源图像文件地址
     * @param destFile 缩放后的图像地址
     * @param height 缩放后的高度
     * @param width 缩放后的宽度
     */
    public final static void compressImg(File targetFile , File destFile , int height, int width ){
        compressImg(targetFile, destFile, height, width , DEFAULT_IMAGE_FORMAT);
    }

    /**
     * 缩放图像（按高度和宽度缩放）
     * @param targetFile 源图像文件地址
     * @param destFile 缩放后的图像地址
     * @param height 缩放后的高度
     * @param width 缩放后的宽度
     * @param type 图片的格式
     */
    public final static void compressImg(File targetFile , File destFile , int height, int width , String type) {
        try {
            if (!targetFile.exists()) return ;
            if (destFile.exists()){
                destFile.delete() ;
            }
            BufferedImage bufferedImage =  ImageIO.read(targetFile);
            BufferedImage image_to_save = new BufferedImage(width, height, bufferedImage.getType());
            image_to_save.getGraphics().drawImage(
                    bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
            FileOutputStream fos = new FileOutputStream(destFile); // 输出到文件流

            saveAsJPEG(image_to_save, 0.7f, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG , "Compress image has failed . reason : " + e.getMessage());
        }
    }

    /**
     * 以JPEG编码保存图片
     * @param image_to_save
     *            要处理的图像图片
     * @param JPEGcompression
     *            压缩比
     * @param fos
     *            文件输出流
     * @throws IOException
     */
    public static void saveAsJPEG(BufferedImage image_to_save, float JPEGcompression, FileOutputStream fos) throws Exception {

        // Image writer
        JPEGImageWriter imageWriter = (JPEGImageWriter) ImageIO.getImageWritersBySuffix("jpg").next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
        imageWriter.setOutput(ios);
        // and metadata
        IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(image_to_save), null);

        if (JPEGcompression >= 0 && JPEGcompression <= 1f) {
            // new Compression
            JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter
                    .getDefaultWriteParam();
            jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(JPEGcompression);

        }

        // new Write and clean up
        imageWriter.write(imageMetaData, new IIOImage(image_to_save, null, null), null);
        ios.close();
        imageWriter.dispose();

    }

}