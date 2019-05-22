package Viewer;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import scripts.File.ImageElement;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class Viewer implements Runnable {
    //pokaz slajdow
    private int index;

    private ArrayList<ImageElement> list;

    private ImageView image;

    @Override
    public void run() {
        StackPane parent;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("viewer.fxml"));
            parent = fxmlLoader.load();
            image = new ImageView(list.get(index).getPath().toUri().toString());
            Stage primaryStage = new Stage();
            primaryStage.setTitle(list.get(index).getName());

            image.fitWidthProperty().bind(primaryStage.widthProperty());
            image.fitHeightProperty().bind(primaryStage.heightProperty().subtract(40));
            image.setSmooth(true);
            image.setPreserveRatio(true);
            image.setId("image");
            /*if(image.getImage().isError())
            {
                Text text = new Text("Invalid image");
                text.setFont(new Font(15));
                text.toFront();
                StackPane.setAlignment(text, Pos.CENTER);
                parent.getChildren().add(text);
            }*/
                parent.getChildren().add(image);

            parent.lookup("#previous_button").toFront();
            parent.lookup("#next_button").toFront();

            ViewerController controller = fxmlLoader.<ViewerController>getController();
            controller.setParams(list, index, image);

            primaryStage.setMinHeight(400);
            primaryStage.setMinWidth(400);
            primaryStage.setScene(new Scene(parent));
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setParams(ArrayList<ImageElement> list, int index) {
        this.list = list;
        this.index = index;
    }

}
