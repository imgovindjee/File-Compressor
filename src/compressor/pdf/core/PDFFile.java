package compressor.pdf.core;

import java.io.File;

public class PDFFile {

    private final File physicalFile;
    private long fileCompressedSize = 0;
    private String message = null;

    public PDFFile(File physicalFile) {
        this.physicalFile = physicalFile;
        this.fileCompressedSize = 0;
        this.message = null;
    }

    public File getPhysicalFile() {
        return physicalFile;
    }

    public String getFileName() {
        return physicalFile.getName();
    }

    public double getSizeInKB() {
        return (double) physicalFile.length() / 1024;
    }

    public void setFileCompressedSize(long fileCompressedSize) {
        this.fileCompressedSize = fileCompressedSize;
    }

    public double getFileCompressedSize() {
        return (double) fileCompressedSize / 1024;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
