package compressor.pdf;

import java.awt.*;

public class PDFCompressorMain {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new Pdf().setVisible(true);
        });
    }
}
