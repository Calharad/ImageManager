package scripts.File;

import Manager.ManagerController;
import Viewer.Viewer;
import javafx.scene.image.Image;
import scripts.Position;

import java.nio.file.Path;
import java.util.ArrayList;

public class ImageFile extends ImageElement {


    public ImageFile(Path path) {
        super(path.getFileName().toString(), "assets/img/file.png", path);
    }

    public ImageFile(String name, String path) {
        super(name, "assets/img/file.png", path);
    }

    private Image preview;

    @Override
    protected void clickEvent(ManagerController manager) {
        manager.getPreview().setImage(((ImageFile)manager.getChoosedPane()).loadPreview());
    }

    @Override
    public void doubleClick(ManagerController manager) {

        final ArrayList<ImageElement> dc = manager.getChildren();

        Viewer view = new Viewer();
        ArrayList<ImageElement> f = new ArrayList<>();
        for(int i=0;i<dc.size();i++) {
            if(ImageElement.isImage(dc.get(i))) {
                f.addAll(dc.subList(i, dc.size()));
                break;
            }
        }
        view.setParams(f, Position.extract(this.getId()).getCalcValue(5) - dc.size() + f.size());
        new Thread(view).run();
    }

    public Image loadPreview() {
        if(preview == null) {
            preview = new Image(getPath().toUri().toString(), 200, 150, true, true);
        }
        return preview;
    }
}
