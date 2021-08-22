package com.fcn.fanscoin.helper;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class VideoHelper {
    private VideoHelper() {
    }

    public static byte[] generateVideoThumbnail(final String mediaPath)
            throws IOException {
        byte[] imageBytes;
        FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(mediaPath);
        ff.start();
        Frame f;
        f = ff.grabImage();
        imageBytes = convertFrameToByteArray(f, "png");
        ff.stop();

        return imageBytes;
    }

    private static byte[] convertFrameToByteArray(final Frame frame, final String format)
            throws IOException {
        if (null == frame || null == frame.image) {
            return null;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bi = converter.getBufferedImage(frame);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bi, format, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
