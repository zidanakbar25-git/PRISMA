package com.humas.model;

import javafx.beans.property.*;
import javafx.scene.image.Image;

import java.io.File;

public class MediaItem {

    private final File file;
    private final String fileName;
    private final boolean isVideo;

    private final IntegerProperty flagProperty = new SimpleIntegerProperty(0);
    private final BooleanProperty selectedProperty = new SimpleBooleanProperty(false);
    private final ObjectProperty<Image> thumbnailProperty = new SimpleObjectProperty<>(null);

    public MediaItem(File file, boolean isVideo) {
        this.file = file;
        this.fileName = file.getName();
        this.isVideo = isVideo;
    }

    public File getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isVideo() {
        return isVideo;
    }

    // Flag Property (0 = unrated, 1-5 = stars)
    public int getFlag() {
        return flagProperty.get();
    }

    public void setFlag(int flag) {
        this.flagProperty.set(flag);
    }

    public IntegerProperty flagProperty() {
        return flagProperty;
    }

    // Selected Property
    public boolean isSelected() {
        return selectedProperty.get();
    }

    public void setSelected(boolean selected) {
        this.selectedProperty.set(selected);
    }

    public BooleanProperty selectedProperty() {
        return selectedProperty;
    }

    // Thumbnail Property
    public Image getThumbnail() {
        return thumbnailProperty.get();
    }

    public void setThumbnail(Image thumbnail) {
        this.thumbnailProperty.set(thumbnail);
    }

    public ObjectProperty<Image> thumbnailProperty() {
        return thumbnailProperty;
    }
}
