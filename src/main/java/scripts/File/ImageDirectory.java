package scripts.File;

import Manager.ManagerController;

import java.nio.file.Path;

public class ImageDirectory extends ImageElement {

    public ImageDirectory(Path path) {
        super(path.getNameCount() > 1? path.getFileName().toString(): "", "assets/img/directory.png", path );
    }

    public ImageDirectory(String name, String path) {
        super(name, "assets/img/directory.png", path );
    }

    @Override
    protected void clickEvent(ManagerController manager) {
        manager.getPreview().setImage(null);
    }

    @Override
    public void doubleClick(ManagerController manager) {
        manager.openDirectory(this);
    }
}
