package com.fcn.fanscoin.helper;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;

public final class ImageHelper {

    private static final Integer DEFAULT_THUMBNAIL_HEIGHT = 100;

    private ImageHelper() {
    }

    private static BufferedImage getCompatibleImage(final int w, final int h) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        return gc.createCompatibleImage(w, h);
    }

    public static BufferedImage generateThumbnail(final BufferedImage source) {
        int height = DEFAULT_THUMBNAIL_HEIGHT;
        float ratio = (float) height / source.getHeight();
        int width = Math.round(source.getWidth() * ratio);

        BufferedImage thumbnail = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        thumbnail.createGraphics().drawImage(source.getScaledInstance(width, height, Image.SCALE_SMOOTH),
                                             0,
                                             0,
                                             null);
        return thumbnail;
    }
}
