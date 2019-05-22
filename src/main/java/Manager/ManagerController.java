package Manager;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import scripts.*;
import scripts.File.ImageDirectory;
import scripts.File.ImageElement;
import scripts.File.ImageFile;
import scripts.Management.DirectoryManager;
import scripts.Management.IconManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ManagerController {

    private static ManagerController instance;

    public static ManagerController getInstance() {
        return instance;
    }

    private DirectoryManager dm;

    private ArrayList<ImageElement> dc;

    private ImageElement choosedPane = null;

    @FXML private ScrollPane directory_pane;

    @FXML private TextField directory_field;

    @FXML private ImageView preview;

    @FXML private Text filename;

    @FXML private AnchorPane root;

    private VBox configTab = null;

    private boolean configOpened = false;

    public Text getFilename() {
        return filename;
    }

    public ImageView getPreview() {
        return preview;
    }

    public ArrayList<ImageElement> getChildren() {
        return dc;
    }

    public ImageElement getChoosedPane() {
        return choosedPane;
    }

    public void setChoosedPane(ImageElement choosedPane) {
        this.choosedPane = choosedPane;
    }

    @FXML
    public void initialize() {
        if(instance == null) instance = this;
        dm = new DirectoryManager();
        fillWholeView();
    }

    public EventHandler<MouseEvent> getMouseHandler() {
        return mouseHandler;
    }

    public void setContent(Path path) {
        dm.setNewDirectory(path);
    }

    @FXML
    public void onBackButtonClicked(MouseEvent event) {
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            previousDirectory();
        }
    }

    @FXML
    public void onButtonClicked(KeyEvent event) {
        event.consume();
        KeyCode code = event.getCode();
        if(code == KeyCode.BACK_SPACE) {
            previousDirectory();
        }
        if(code == KeyCode.ENTER && choosedPane != null) {
            choosedPane.doubleClick(this);
        }
        if(code == KeyCode.UP && choosedPane != null) {
            Position id = Position.extract(choosedPane.getId());
            if(id.getY() > 0) {
                changeActiveElement(id.getCalcValue(5) - 5);
            }
        }
        if(code == KeyCode.DOWN) {
            if(choosedPane != null) {
                Position id = Position.extract(choosedPane.getId());
                if(id.getY() < dc.size()/5 - 1) {
                    try {
                        changeActiveElement(id.getCalcValue(5) + 5);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Warning, out of bounds");
                    }
                }
            }
            else {
                try {
                    choosedPane = dc.get(0);
                    changeActiveElement(0);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Warning, out of bounds");
                }

            }
        }
        if(code == KeyCode.LEFT) {
            if(choosedPane != null) {
                Position id = Position.extract(choosedPane.getId());
                if(id.getX() > 0)
                    changeActiveElement(id.getCalcValue(5) - 1);
            }
        }
        if(code == KeyCode.RIGHT) {
            if(choosedPane != null) {
                Position id = Position.extract(choosedPane.getId());
                if(id.getX() < 4) {
                    try {
                        changeActiveElement(id.getCalcValue(5) + 1);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Warning, out of bounds");
                    }
                }
            } else {
                try {
                    choosedPane = dc.get(0);
                    changeActiveElement(0);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Warning, out of bounds");
                }

            }
        }

    }

    @FXML
    public void onLinkEnterClicked(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)) {
            String text = ((TextField)event.getSource()).getText();
            if(Files.exists(Paths.get(text))) {
                dm.setNewDirectory(Paths.get(text));
                fillWholeView();
            }
            else {
                new Alert(Alert.AlertType.ERROR, "Wrong path").show();
                directory_field.setText(dm.getPathString());
            }
        }
    }

    @FXML
    public void onHomeButtonClicked(MouseEvent event) {
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            dm.setHomeDirectory();
            fillWholeView();
        }
    }

    public void openDirectory(ImageDirectory dir) {
        //dc = dm.onDirectoryOpen(Position.extract(choosedPane.getId()).getCalcValue(5));
        dm.setNewDirectory(dir.getPath());
        directory_field.setText(dm.getPathString());
        IconManager.generate(directory_pane, dc);
        choosedPane = null;
        preview.setImage(null);
    }

    private void changeActiveElement(int id) throws ArrayIndexOutOfBoundsException{
        if(choosedPane != null) {
            choosedPane.setStyle(null);
            choosedPane.applyCss();
        }
        choosedPane = dc.get(id);
        choosedPane.setStyle("-fx-background-color: lightblue");
        filename.setText(choosedPane.getPath().getFileName().toString());
        if(!ImageElement.isDirectory(choosedPane)) {
            preview.setImage(((ImageFile)choosedPane).loadPreview());
        } else preview.setImage(null);
    }

    private void fillWholeView() {
        dc = dm.getChildren();
        directory_field.setText(dm.getPathString());
        IconManager.generate(directory_pane, dc);
    }

    private void previousDirectory() {
        dm.setPreviousDirectory();
        fillWholeView();
        preview.setImage(null);
        filename.setText("<nie wybrano>");
    }

    private final EventHandler<MouseEvent> mouseHandler = event -> {
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            if (event.getClickCount() >= 2) {
                ImageElement pane = (ImageElement) event.getSource();
                pane.doubleClick(ManagerController.getInstance());
            }
            else {
                ImageElement sp = (ImageElement) event.getSource();
                sp.click(ManagerController.getInstance(), sp);
            }
        }
    };

    @FXML
    public void openConfig()
    {
        if(configOpened)
        {
            root.getChildren().remove(configTab);
            configOpened = false;
        }
        else {
            if(configTab == null)
            {
                configTab = new VBox();
                configTab.getStyleClass().add("config_menu");

                EventHandler<ActionEvent> handler = event -> {
                    if(event.getSource() instanceof CheckBox) {
                        CheckBox x = (CheckBox)event.getSource();
                        dm.setFilter(x.getText(), x.isSelected());
                        fillWholeView();
                        if(dc.indexOf(choosedPane) == -1) {
                            if (dc.size() > 0) changeActiveElement(0);
                            else choosedPane.setStyle("-fx-background-color: lightblue");
                        } else choosedPane = null;

                    }
                };

                AnchorPane.setRightAnchor(configTab, 10.0);
                AnchorPane.setTopAnchor(configTab, 60.0);
                Text text = new Text("Filter format:");
                CheckBox jpg = new CheckBox("jpg|jpeg");
                CheckBox png = new CheckBox("png");
                CheckBox bmp = new CheckBox("bmp");
                CheckBox gif = new CheckBox("gif");
                jpg.setOnAction(handler);
                png.setOnAction(handler);
                gif.setOnAction(handler);
                bmp.setOnAction(handler);
                jpg.setSelected(true);  bmp.setSelected(true);
                gif.setSelected(true);  png.setSelected(true);
                configTab.getChildren().addAll(text, jpg, gif, png, bmp);
                configTab.getChildren().forEach((node -> VBox.setMargin(node, new Insets(10, 10, 10, 10))));
            }
            root.getChildren().add(configTab);
            configOpened = true;
        }

    }
}
