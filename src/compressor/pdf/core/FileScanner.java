package compressor.pdf.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class FileScanner {

    private final File fileDirectory;

    public FileScanner(File fileDirectory) {
        this.fileDirectory = fileDirectory;
    }

    /**
     * return list with the files of the given extension,
     * inside te given directory of this scanner
     *
     * @param extension file extension [eg: .txt]
     * @return type ArrayList<>()
     */
    public ArrayList<File> getFiles(String extension) {
        File[] files = fileDirectory.listFiles(
                (File file) -> file.getName().toLowerCase().endsWith(extension));
        assert files != null;
        return new ArrayList<>(Arrays.asList(files));
    }

    public File getDirectory() {
        return fileDirectory;
    }
}
