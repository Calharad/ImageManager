package scripts.Management;

import scripts.File.ImageDirectory;
import scripts.File.ImageElement;
import scripts.File.ImageFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirectoryManager {

    private static final Path DEFAULT_DIR = Paths.get(System.getProperty("user.home"));

    private ImageDirectory directory = new ImageDirectory(DEFAULT_DIR);

    private ArrayList<ImageElement> elements;

    public String getPathString() {
        return directory.getPath().toString();
    }

    public ArrayList<ImageElement> getChildren() {
        return elements;
    }

    private String regex;

    private HashMap<String, Boolean> filters = new HashMap<>();

    public DirectoryManager() {
        elements = new ArrayList<>();

        filters.put("jpg|jpeg", true);
        filters.put("png", true);
        filters.put("bmp", true);
        filters.put("gif", true);

        updateRegex();

        prepareDirectoryContent();
    }

    private void prepareDirectoryContent() {
        try (Stream<Path> stream = Files.list(directory.getPath())) {
            elements.clear();
            ArrayList<ImageDirectory> directories = new ArrayList<>();
            ArrayList<ImageFile> files = new ArrayList<>();
            for(Path path: stream.collect(Collectors.toList())) {
                if(!Files.isHidden(path)) {
                    if (Files.isDirectory(path)) directories.add(new ImageDirectory(path));
                    String[] splitArray = path.toString().split("[.]");
                    if (splitArray.length > 1) {
                        if (splitArray[splitArray.length - 1].toLowerCase().matches(regex))
                            files.add(new ImageFile(path));
                    }
                }
            }
            directories.sort(Comparator.comparing(ImageDirectory::getName));
            files.sort(Comparator.comparing(ImageFile::getName));
            elements.addAll(directories);
            elements.addAll(files);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void setFilter(String format, boolean val) {
        if(filters.containsKey(format)) {
            filters.put(format, val);
            updateRegex();
            prepareDirectoryContent();
        }
    }

    private void updateRegex() {
        StringBuilder sb = new StringBuilder();
        regex = "";
        filters.forEach((key, value) -> {
            if(value) {
                sb.append(key).append("|");
            }
        });
        if(regex.length() > 0) {
            sb.deleteCharAt(regex.length() - 1);
        }
        regex = sb.toString();

    }



    public void setPreviousDirectory() {
        directory = new ImageDirectory(directory.getPath().getParent());
        prepareDirectoryContent();
    }

    public void setHomeDirectory() {
        directory = new ImageDirectory(DEFAULT_DIR);
        prepareDirectoryContent();
    }

    public void setNewDirectory(Path path) {
        directory = new ImageDirectory(path);
        prepareDirectoryContent();
    }
}
