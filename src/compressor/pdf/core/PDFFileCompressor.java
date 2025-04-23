package compressor.pdf.core;

import compressor.pdf.Pdf;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class PDFFileCompressor {

    private final ArrayList<PDFFile> pdfFiles;
    private final String fileOutputDirectoryPath;
//    private String ghostPathScript = "C:\\Program Files\\gs\\gs10.05.0\\bin\\gswin64c.exe";
    private String ghostPathScript = "E:\\File Compressor\\Prerequisites\\gs\\gs10.05.0\\bin\\gswin64c.exe";

    private boolean fileCompressionRunningKey = false;


    public PDFFileCompressor(ArrayList<PDFFile> pdfFiles, String fileOutputDirectoryPath, Pdf PDFActiveInstance) {
        this.pdfFiles = pdfFiles;
        this.fileOutputDirectoryPath = fileOutputDirectoryPath;

        try {
//            this.ghostPathScript = "C:\\Program Files\\gs\\gs10.05.0\\bin\\gswin64c.exe";
//            this.ghostPathScript = "E:\\File Compressor\\Prerequisites\\gs\\gs10.05.0\\bin\\gswin64c.exe";
            this.ghostPathScript = new File("Prerequisites\\gs\\gs10.05.0\\bin\\gswin64c.exe").getCanonicalPath();
        } catch (Exception exception) {
            System.out.println("\n[PDFFileCompressor] Error encountered while PDF File Compression operation...");
            exception.printStackTrace();
        }
    }

    public void compressFiles() {
        fileCompressionRunningKey = true;
        boolean flag = false;
        for (PDFFile pdfFile : pdfFiles) {
            if (fileCompressionRunningKey) {
                System.out.println("\n[PDFFileCompressor] Files Compressing...");

                pdfFile.setMessage("Compressing....");
                if (Pdf.PDFActiveInstance != null) {
                    Pdf.PDFActiveInstance.UpdateJTableFiles();
                }

                String outputPath = fileOutputDirectoryPath + "\\" + pdfFile.getFileName();

//                If you want more ROBUST AND AGGRESSIVE compression, if any
//                StringBuilder command = getCommand(pdfFile, outputPath);

//                Optimal Compressor
                StringBuilder command = new StringBuilder("\"" + ghostPathScript + "\"");
                command.append(" -sDEVICE=pdfwrite -dCompatibilityLevel=1.4 -dPDFSETTINGS=/ebook -dNOPAUSE -dQUIET -dBATCH");
                command.append(" -sOutputFile=\"").append(outputPath).append("\" ");
                command.append("\"").append(pdfFile.getPhysicalFile().getAbsolutePath()).append("\"");
                System.out.println(command.toString());

                try {
                    Process process = Runtime.getRuntime().exec(command.toString());
                    int exitValue = process.waitFor();

//                    CHECK FOR ERROR,
//                    IF ANY
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println("[PDFFileCompressor] Error : " + line);
                        }
                    }

                    File compressedFile = new File(outputPath);
//                    If some error caused while compression and compressed file don't exist
//                    then copy the ORIGINAL FILE to the o/p path
                    if (!compressedFile.exists()) {
                        CopyFile(pdfFile.getPhysicalFile().getAbsolutePath(), outputPath);
                    }

//                    If the compressed file size is greater than the Original then use the original one
                    if (compressedFile.length() > pdfFile.getPhysicalFile().length()) {
                        CopyFile(pdfFile.getPhysicalFile().getAbsolutePath(), outputPath);
                        pdfFile.setFileCompressedSize(pdfFile.getPhysicalFile().length());
                    } else {
                        pdfFile.setFileCompressedSize(compressedFile.length());
                    }
                    System.out.println("[PDFFileCompressor] Original Size : " + pdfFile.getPhysicalFile().length());
                    System.out.println("[PDFFileCompressor] Compressed Size : " + compressedFile.length());


//                    NOW PDF FILE HAS BEEN COMPRESSED
//                    INFORM THE [PDFMain()] PAGE
//                    AND DISPLAY THE COMPRESSED SIZE OF THE FILE
//                    ON THE TABLE / UI PART
                    Pdf.PDFActiveInstance.FileCompressedFinished(pdfFile);

                    process.destroy();
                    process = null; // SAFETY
                    flag = true;
                } catch (Exception exception) {
                    System.out.println("[PDFFileCompressor] Ghostscript Can't be loaded...");
                    exception.printStackTrace();
                }

                if (flag) {
                    System.out.println("[PDFFileCompressor] Files Compression Successful...");
                }
            }
        }
    }


    private @NotNull
    StringBuilder getCommand(@NotNull PDFFile pdfFile, String outputPath) {
        StringBuilder command = new StringBuilder("\"" + ghostPathScript + "\"");
        command.append(" -sDEVICE=pdfwrite -dCompatibilityLevel=1.4 -dPDFSETTINGS=/screen");
        command.append(" -dNOPAUSE -dQUIET -dBATCH");
        command.append(" -dColorImageDownsampleType=/Bicubic -dColorImageResolution=72");
        command.append(" -dMonoImageDownsampleType=/Subsample -dMonoImageResolution=72");
        command.append(" -dCompressFonts=true");
        command.append(" -dEmbedAllFonts=true");
        command.append(" -dAutoFilterColorImages=false");
        command.append(" -dAutoFilterGrayImages=false");
        command.append(" -dDownsampleColorImages=true");
        command.append(" -dDownsampleGrayImages=true");
        command.append(" -dDownsampleMonoImages=true");
        command.append(" -sOutputFile=\"").append(outputPath).append("\" ");
        command.append(" \"").append(pdfFile.getPhysicalFile().getAbsolutePath()).append("\"");
        System.out.println(command.toString());
        return command;
    }


    private void CopyFile(String from, String to) {
        try {
            Files.copy(Path.of(from), Path.of(to), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception exception) {
            System.out.println("[PDFFileCompressor] Error encountered while Copying file...");
        }
    }

    public void cancelFileCompressionRunning() {
        this.fileCompressionRunningKey = false;
    }
}
