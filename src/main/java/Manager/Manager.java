package Manager;

import Viewer.Viewer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scripts.File.ImageElement;
import scripts.File.ImageFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Manager extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainManager.fxml"));
        Parent root = fxmlLoader.load();
        List<String> args = getParameters().getUnnamed();
        if(args.size() > 0)
        {
            Path path = Paths.get(args.get(0));
            ((ManagerController)fxmlLoader.getController()).setContent(path.getParent());
            Viewer x = new Viewer();
            ArrayList<ImageElement> a = new ArrayList<>();
            a.add(new ImageFile(path));
            x.setParams(a, 0);
            new Thread(x).run();
        }
        else {
            primaryStage.setTitle("Image Manager");
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}
