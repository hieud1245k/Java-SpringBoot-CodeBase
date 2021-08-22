package com.fcn.fanscoin.helper;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public final class FileHelper {

    private FileHelper() {
    }

    public static String getFormatFile(final MultipartFile file) {
        return Objects.requireNonNull(file.getContentType()).split("/")[1];
    }

    public static byte[] getThumbnailBytes(final MultipartFile file)
            throws IOException {
        BufferedImage thumbnailBufferedImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
        BufferedImage optimizedThumbnailBufferedImage = ImageHelper.generateThumbnail(thumbnailBufferedImage);
        ByteArrayOutputStream thumbnailByteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(optimizedThumbnailBufferedImage, getFormatFile(file), thumbnailByteArrayOutputStream);
        return thumbnailByteArrayOutputStream.toByteArray();
    }
}
