package com.humas.view;

import atlantafx.base.theme.Styles;
import com.humas.controller.MediaSorterController;
import com.humas.model.MediaItem;
import com.humas.util.UserSession;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MediaSorterView {

    private final MediaSorterController controller = new MediaSorterController();

    public void start(Stage stage) {
        // --- TOP TOOLBAR ---
        Label lblModuleTitle = new Label("Sortir Media Lokal");
        lblModuleTitle.getStyleClass().add(Styles.TITLE_3);

        Button btnOpenFolder = new Button("Buka Folder");
        btnOpenFolder.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.ACCENT);

        Label lblFolderPath = new Label("Belum ada folder terpilih");
        lblFolderPath.getStyleClass().add(Styles.TEXT_MUTED);

        ComboBox<String> cbFilterFlag = new ComboBox<>();
        cbFilterFlag.getItems().addAll("Semua Bintang", "Bintang 1", "Bintang 2", "Bintang 3", "Bintang 4", "Bintang 5");
        cbFilterFlag.setValue("Semua Bintang");
        cbFilterFlag.setDisable(true);
        Tooltip.install(cbFilterFlag, new Tooltip("Filter Flag tersedia setelah Mode Fullscreen diimplementasikan (Step 2)"));

        CheckBox chkSelectAll = new CheckBox("Pilih Semua");
        chkSelectAll.setOnAction(e -> controller.setSelectAll(chkSelectAll.isSelected()));

        Label lblStatus = new Label("Total: 0 berkas");
        lblStatus.getStyleClass().add(Styles.TEXT_MUTED);

        Button btnBack = new Button("Kembali ke Dashboard");
        btnBack.setOnAction(e -> {
            controller.shutdown();
            String role = UserSession.getInstance().getUser() != null ? UserSession.getInstance().getUser().getRole().toLowerCase() : "";
            if ("staff".equals(role)) {
                new StaffDashboardView().start(stage);
            } else if ("intern".equals(role)) {
                new InternDashboardView().start(stage);
            } else {
                new LoginView().start(stage);
            }
        });

        HBox topLeft = new HBox(15, btnBack, lblModuleTitle, btnOpenFolder, lblFolderPath);
        topLeft.setAlignment(Pos.CENTER_LEFT);

        HBox topRight = new HBox(15, chkSelectAll, cbFilterFlag, lblStatus);
        topRight.setAlignment(Pos.CENTER_RIGHT);

        BorderPane toolbar = new BorderPane();
        toolbar.setLeft(topLeft);
        toolbar.setRight(topRight);
        toolbar.setPadding(new Insets(15, 20, 15, 20));
        toolbar.getStyleClass().add(Styles.ELEVATED_1);

        // --- GRID CONTENT AREA (FlowPane Responsif) ---
        FlowPane gridFlowPane = new FlowPane();
        gridFlowPane.setHgap(16);
        gridFlowPane.setVgap(16);
        gridFlowPane.setPadding(new Insets(20));
        gridFlowPane.setAlignment(Pos.TOP_LEFT);

        ScrollPane scrollPane = new ScrollPane(gridFlowPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Empty state label
        Label lblEmpty = new Label("Klik 'Buka Folder' untuk memilih lokasi foto dan video");
        lblEmpty.getStyleClass().add(Styles.TEXT_MUTED);
        VBox emptyStateBox = new VBox(lblEmpty);
        emptyStateBox.setAlignment(Pos.CENTER);
        emptyStateBox.setPadding(new Insets(100));

        gridFlowPane.getChildren().add(emptyStateBox);

        // Listen perubahan data media items
        controller.getMediaItems().addListener((ListChangeListener<MediaItem>) change -> {
            gridFlowPane.getChildren().clear();
            if (controller.getMediaItems().isEmpty()) {
                gridFlowPane.getChildren().add(emptyStateBox);
                lblStatus.setText("Total: 0 berkas");
            } else {
                long photoCount = controller.getMediaItems().stream().filter(i -> !i.isVideo()).count();
                long videoCount = controller.getMediaItems().stream().filter(MediaItem::isVideo).count();
                lblStatus.setText(String.format("Total: %d berkas (%d foto, %d video)",
                        controller.getMediaItems().size(), photoCount, videoCount));

                for (MediaItem item : controller.getMediaItems()) {
                    gridFlowPane.getChildren().add(createMediaCard(item));
                }
            }
        });

        // Handler aksi Buka Folder
        btnOpenFolder.setOnAction(e -> {
            controller.chooseFolder(stage, () -> {
                if (controller.getCurrentDirectory() != null) {
                    lblFolderPath.setText("📍 " + controller.getCurrentDirectory().getAbsolutePath());
                }
            });
        });

        // Layout Utama
        BorderPane root = new BorderPane();
        root.setTop(toolbar);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 1100, 700);
        stage.setTitle("PRISMA - Modul Sortir Media");
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> controller.shutdown());
        stage.show();
    }

    /**
     * Membuat Komponen Card Thumbnail AtlantaFX untuk setiap MediaItem
     */
    private VBox createMediaCard(MediaItem item) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(10));
        card.getStyleClass().add(Styles.ELEVATED_1);
        card.setPrefSize(180, 210);
        card.setMaxSize(180, 210);
        card.setAlignment(Pos.CENTER);

        // Container Thumbnail dengan kliping pembatas
        StackPane imgPane = new StackPane();
        imgPane.setPrefSize(160, 140);
        imgPane.setMaxSize(160, 140);
        imgPane.setStyle("-fx-background-color: -color-bg-subtle; -fx-background-radius: 6px;");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(160);
        imageView.setFitHeight(140);
        imageView.setPreserveRatio(true);

        Rectangle clip = new Rectangle(160, 140);
        clip.setArcWidth(12);
        clip.setArcHeight(12);
        imgPane.setClip(clip);

        if (item.isVideo()) {
            Label lblVideoBadge = new Label("🎥 VIDEO");
            lblVideoBadge.getStyleClass().addAll("badge", Styles.ACCENT);
            imgPane.getChildren().add(lblVideoBadge);
        } else {
            Label lblLoading = new Label("Memuat...");
            lblLoading.getStyleClass().add(Styles.TEXT_MUTED);
            imgPane.getChildren().add(lblLoading);

            item.thumbnailProperty().addListener((obs, oldImg, newImg) -> {
                if (newImg != null) {
                    imageView.setImage(newImg);
                    imgPane.getChildren().setAll(imageView);
                }
            });

            if (item.getThumbnail() != null) {
                imageView.setImage(item.getThumbnail());
                imgPane.getChildren().setAll(imageView);
            }
        }

        // Checkbox & Nama File
        CheckBox chkSelect = new CheckBox();
        chkSelect.selectedProperty().bindBidirectional(item.selectedProperty());

        Label lblName = new Label(item.getFileName());
        lblName.getStyleClass().add(Styles.TEXT_SMALL);
        lblName.setTooltip(new Tooltip(item.getFileName()));
        lblName.setMaxWidth(130);

        HBox cardFooter = new HBox(8, chkSelect, lblName);
        cardFooter.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(imgPane, cardFooter);
        return card;
    }
}
