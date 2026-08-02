package com.humas.controller;

import com.humas.model.MediaItem;
import com.humas.util.ThumbnailLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MediaSorterController {

    private static final Set<String> IMAGE_EXTENSIONS = new HashSet<>(Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp", "webp"
    ));

    private static final Set<String> VIDEO_EXTENSIONS = new HashSet<>(Arrays.asList(
            "mp4", "mkv", "avi", "mov", "wmv", "flv"
    ));

    private final ObservableList<MediaItem> mediaItems = FXCollections.observableArrayList();
    private File currentDirectory;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(4, r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

    public ObservableList<MediaItem> getMediaItems() {
        return mediaItems;
    }

    public File getCurrentDirectory() {
        return currentDirectory;
    }

    /**
     * Membuka Dialog DirectoryChooser untuk memilih folder media lokal.
     */
    public void chooseFolder(Stage stage, Runnable onComplete) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Pilih Folder Media");
        if (currentDirectory != null && currentDirectory.exists()) {
            dc.setInitialDirectory(currentDirectory);
        }

        File selectedDir = dc.showDialog(stage);
        if (selectedDir != null && selectedDir.isDirectory()) {
            this.currentDirectory = selectedDir;
            scanAndLoadFolder(selectedDir, onComplete);
        }
    }

    /**
     * Pindai folder dan muat berkas media secara asinkron.
     */
    private void scanAndLoadFolder(File folder, Runnable onComplete) {
        mediaItems.clear();

        File[] files = folder.listFiles();
        if (files == null) {
            if (onComplete != null) onComplete.run();
            return;
        }

        for (File file : files) {
            if (file.isFile() && !file.isHidden()) {
                String ext = getFileExtension(file.getName()).toLowerCase();
                boolean isImage = IMAGE_EXTENSIONS.contains(ext);
                boolean isVideo = VIDEO_EXTENSIONS.contains(ext);

                if (isImage || isVideo) {
                    MediaItem item = new MediaItem(file, isVideo);
                    mediaItems.add(item);

                    if (isImage) {
                        // Muat thumbnail foto di background thread
                        Task<javafx.scene.image.Image> task = ThumbnailLoader.loadThumbnailAsync(file);
                        task.setOnSucceeded(e -> item.setThumbnail(task.getValue()));
                        threadPool.submit(task);
                    }
                }
            }
        }

        if (onComplete != null) {
            onComplete.run();
        }
    }

    /**
     * Memilih / membatalkan semua seleksi item.
     */
    public void setSelectAll(boolean select) {
        for (MediaItem item : mediaItems) {
            item.setSelected(select);
        }
    }

    private String getFileExtension(String name) {
        int lastDot = name.lastIndexOf('.');
        if (lastDot > 0 && lastDot < name.length() - 1) {
            return name.substring(lastDot + 1);
        }
        return "";
    }

    public void shutdown() {
        threadPool.shutdownNow();
    }
}
