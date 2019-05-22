package Viewer;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import scripts.File.ImageElement;

import java.util.ArrayList;

public class ViewerController {

    private int index;

    private ArrayList<ImageElement> list;

    private ImageView image;

    void setParams(ArrayList<ImageElement> f, int i, ImageView image) {
        index = i;
        list = f;
        this.image = image;
    }

    @FXML
    public void handleButtons(KeyEvent event) {
        KeyCode code = event.getCode();
        switch (code) {
            case RIGHT:
                nextImage();
                break;
            case LEFT:
                previousImage();
                break;
        }
    }

    @FXML
    public void onNextButtonClicked(MouseEvent event) {
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            nextImage();
        }
    }

    @FXML
    public void onPreviousButtonClicked(MouseEvent event) {
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            previousImage();
        }
    }

    private void nextImage() {
        index++;
        if(index >= list.size()) index=0;
        image.setImage(new Image(list.get(index).getPath().toUri().toString()));
    }

    private void previousImage() {
        index--;
        if(index < 0) index = list.size() - 1;
        image.setImage(new Image(list.get(index).getPath().toUri().toString()));
    }
}
