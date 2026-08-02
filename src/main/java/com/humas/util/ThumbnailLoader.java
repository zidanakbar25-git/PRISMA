package com.humas.util;

import javafx.concurrent.Task;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;

public class ThumbnailLoader {

    public static final int THUMB_WIDTH = 180;
    public static final int THUMB_HEIGHT = 180;

    /**
     * Memuat thumbnail gambar secara asinkron (background thread) dengan membatasi resolusi
     * ke 180x180 pixel demi efisiensi penggunaan RAM.
     */
    public static Task<Image> loadThumbnailAsync(File imageFile) {
        return new Task<>() {
            @Override
            protected Image call() throws Exception {
                if (imageFile == null || !imageFile.exists()) {
                    return null;
                }
                try (FileInputStream fis = new FileInputStream(imageFile)) {
                    // JavaFX Image constructor mendukung requestedWidth, requestedHeight, preserveRatio, smooth
                    return new Image(fis, THUMB_WIDTH, THUMB_HEIGHT, true, true);
                }
            }
        };
    }
}
