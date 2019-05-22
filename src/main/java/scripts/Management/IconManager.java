package scripts.Management;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import scripts.File.ImageElement;

import java.util.ArrayList;

public class IconManager {

    private final static int ICON_SIZE = 150;

    public static void generate(ScrollPane pane, ArrayList<ImageElement> content) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setMinWidth(ICON_SIZE * 5 + 10);
        anchorPane.setMaxWidth(ICON_SIZE * 5 + 10);
        final int[] posX = {0};
        final int[] posY = { 0 };
        content.forEach(element -> {
            element.setId("id" + posX[0] + "_" + posY[0]);
            connectToAnchorPane(anchorPane, posX, posY, element);
        });
        pane.setContent(anchorPane);
    }

    private static void connectToAnchorPane(AnchorPane anchorPane, int[] posX, int[] posY, StackPane stackPane) {
        AnchorPane.setTopAnchor(stackPane, (double)(posY[0] * ICON_SIZE));
        AnchorPane.setLeftAnchor(stackPane, (double)(posX[0] * ICON_SIZE));
        anchorPane.getChildren().add(stackPane);
        if(posX[0] >= 4) {
            posY[0]++;
            posX[0] = 0;
        } else posX[0]++;

    }
}
