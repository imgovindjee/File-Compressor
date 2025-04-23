package compressor.pdf.UI.Layout;

import compressor.pdf.core.PDFFile;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class PDFTableLayout extends AbstractTableModel {

    private final ArrayList<PDFFile> pdfFiles;
    private final int columnFilesCount = 3;

    public PDFTableLayout() {
        this.pdfFiles = new ArrayList<>();
    }

    public PDFTableLayout(ArrayList<PDFFile> pdfFiles) {
        this.pdfFiles = pdfFiles;
    }

    public PDFFile getPDFFileFromRow(int row) {
        return pdfFiles.get(row);
    }

    public void add(PDFFile pdfFile) {
        pdfFiles.add(pdfFile);

        final int row = pdfFiles.indexOf(pdfFile);
        fireTableRowsInserted(row, row);
    }

    public void remove(PDFFile pdfFile) {
        if (pdfFiles.contains(pdfFile)) {
            final int row = pdfFiles.indexOf(pdfFile);

            pdfFiles.remove(row);
            fireTableRowsDeleted(row, row);
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Object.class;
    }

    @Override
    public int getRowCount() {
        return pdfFiles.size();
    }

    @Override
    public int getColumnCount() {
        return columnFilesCount;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0){
            return "FileName";
        } else if (column == 1) {
            return "Size(KB)";
        } else if (column == 2) {
            return "Status";
        }
        return null;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PDFFile pdfFile = pdfFiles.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return pdfFile.getFileName();
            case 1:
                return String.valueOf(pdfFile.getSizeInKB());
            case 2:
                if (pdfFile.getFileCompressedSize() > 0) {
                    return "Result : " + String.valueOf(pdfFile.getFileCompressedSize()) + "KB";
                } else {
                    if (pdfFile.getMessage() != null) {
                        return pdfFile.getMessage();
                    } else {
                        System.out.println("[] DO NOTHING...");
                    }
                }
        }
        return null;
    }
}
