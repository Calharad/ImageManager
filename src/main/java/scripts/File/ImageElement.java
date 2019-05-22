package scripts.File;

import Manager.ManagerController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class ImageElement extends StackPane{

    public static final int ICON_SIZE = 150;

    public static boolean isDirectory(ImageElement se) {
        return (se.getClass().getSimpleName().equals(ImageDirectory.class.getSimpleName()));
    }

    public static boolean isImage(ImageElement se) {
        return (se.getClass().getSimpleName().equals(ImageFile.class.getSimpleName()));
    }

    public ImageElement(String name, String iconSrc, Path path) {
        this.name = name;
        defaultIcon = new Image(iconSrc, ICON_SIZE - 20,ICON_SIZE - 30,false, false);
        this.path = path;
        buildPane();
    }

    public ImageElement(String name, String iconSrc, String path) {
        this.name = name;
        defaultIcon = new Image(iconSrc, ICON_SIZE - 20,ICON_SIZE - 30,false, false);
        this.path = Paths.get(path);
        buildPane();
    }

    private String name;
    private Image defaultIcon;
    private Path path;

    private void buildPane() {
        ImageView imageView = new ImageView(defaultIcon);
        Text text;
        if(name.length() > 20) {
            text = new Text(name.substring(0, 19) + "...");
        }
        else {
            text = new Text(name);
        }
        text.setFont(new Font(11));
        StackPane.setAlignment(imageView, Pos.TOP_CENTER);
        StackPane.setMargin(imageView, new Insets(10));
        StackPane.setAlignment(text, Pos.BOTTOM_CENTER);
        getChildren().addAll(imageView, text);
        setOnMouseClicked(ManagerController.getInstance().getMouseHandler());
    }

    public final void click(ManagerController manager, ImageElement ie) {
        ImageElement choosedPane = manager.getChoosedPane();
        if (choosedPane == null) {
            choosedPane = ie;
            choosedPane.setStyle("-fx-background-color: lightblue;");
        } else if (choosedPane != ie) {
            choosedPane.setStyle(null);
            choosedPane.applyCss();
            choosedPane = ie;
            choosedPane.setStyle("-fx-background-color: lightblue;");
        }
        manager.getFilename().setText(ie.getPath().getFileName().toString());
        manager.setChoosedPane(choosedPane);
        clickEvent(manager);
    }

    protected abstract void clickEvent(ManagerController manager);
    public abstract void doubleClick(ManagerController manager);

    public final String getName() {
        return name;
    }

    public final Path getPath() {
        return path;
    }
}
