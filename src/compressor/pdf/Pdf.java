package compressor.pdf;

import compressor.pdf.UI.Layout.PDFTableLayout;
import compressor.pdf.core.PDFFile;
import compressor.pdf.core.PDFFileCompressor;
import compressor.pdf.settings.Settings;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;

public class Pdf extends JFrame {

    public static Pdf PDFActiveInstance;

    private Thread fileCompressThread;
    private int compressedFilesCount = 0;

    private final ArrayList<PDFFile> selectedPDFFiles = new ArrayList<>();
    private PDFFileCompressor pdfFileCompressorInstance;


//    VARIABLES
    private JButton button1;
    private JButton compressFilesButton;
    private JButton selectedOutputDirectoryButton;

    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;

    private JPanel jPanel1;
    private JPanel jPanel2;

    private JProgressBar compressionProgressBar;
    private JScrollPane jScrollPane;
    private JTable jTableFiles;
    private JTextField outputDirectoryTextField;

    public Pdf() {
        initComponent();

        PDFActiveInstance = this;

        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/assets/app-icon-24x24.png")));

        this.setTitle("PDF Compressor");
        System.out.println("[PDF COMPRESSION VERSION] VERSION-"+Settings.VERSION);
        this.setLocationRelativeTo(null);
        jTableFiles.setModel(new PDFTableLayout(new ArrayList<PDFFile>()));
    }

    @SuppressWarnings("unchecked")
    private void initComponent() {
        renderComponent();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("PDF COMPRESSOR");
        setResizable(false);

        jPanel1.setBorder(BorderFactory.createTitledBorder(null, "[SELECT OUTPUT DIRECTORY]", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", 1, 14), new Color(0, 102, 204)));
        jPanel1.setInheritsPopupMenu(true);

        jLabel1.setText("Directory");

        outputDirectoryTextField.setEditable(false);

        selectedOutputDirectoryButton.setIcon(new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/addDest.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        selectedOutputDirectoryButton.setText(" Dest.");
        selectedOutputDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedOutputDirectoryButtonActionPerformed(e);
            }
        });

        compressionProgressBar.setStringPainted(true);
        jLabel3.setText("[PROGRESS] : ");

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(outputDirectoryTextField, GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(selectedOutputDirectoryButton))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(compressionProgressBar, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(outputDirectoryTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(selectedOutputDirectoryButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(compressionProgressBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(1, 1, 1)))
                .addContainerGap(14, Short.MAX_VALUE)));

        jPanel2.setBorder(BorderFactory.createTitledBorder(null, "[CHOOSE PDF DIRECTORY]", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", 1, 14), new Color(0, 102, 204)));

        jTableFiles.setAutoCreateRowSorter(true);
        jTableFiles.setModel(new DefaultTableModel(
                new Object[][] {
                        {null},
                        {null},
                        {null},
                        {null}
                }, new String[] {
                        "Column 1"
                }
        ));
        jScrollPane.setViewportView(jTableFiles);

        button1.setText("Load PDF Files");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button1ActionPerformed(e);
            }
        });

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(button1)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button1)
                        .addContainerGap()));

        jLabel4.setFont(new Font("Segoe UI", 0, 11));
        jLabel4.setText("This Software is Completely free, Free for personal and commercial use...");

        jLabel5.setFont(new Font("Segoe UI", 0, 11));
        jLabel5.setText("MadeBy@ShreeGovindJee ");

        jLabel6.setFont(new Font("Segoe UI", 0, 11));
        jLabel6.setForeground(new Color(0, 51, 204));
        jLabel6.setText("https://imgovindjee.github.io/site/");
        jLabel6.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jLabel6MouseClicked(e);
            }
        });

        compressFilesButton.setFont(new Font("Segoe UI", 1, 12));
        compressFilesButton.setForeground(new Color(0, 102, 204));
        compressFilesButton.setText("Compress Files");
        compressFilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compressFilesButtonActionPerformed(e);
            }
        });


        GroupLayout groupLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(groupLayout);
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(GroupLayout.Alignment.TRAILING, groupLayout.createSequentialGroup()
                                                .addComponent(compressFilesButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel4)
                                                        .addGroup(GroupLayout.Alignment.TRAILING, groupLayout.createSequentialGroup()
                                                                .addComponent(jLabel5)
                                                                .addGap(0, 0, 0)
                                                                .addComponent(jLabel6)))))
                                .addContainerGap()));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(jLabel4)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel5)
                                                        .addComponent(jLabel6)))
                                        .addComponent(compressFilesButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void compressFilesButtonActionPerformed(ActionEvent e) {
        if (compressFilesButton.getText().equals("Compress Files")) {
            if (outputDirectoryTextField.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Please Select the output Directory", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            fileCompressThread = new Thread(() -> {
                pdfFileCompressorInstance = new PDFFileCompressor(selectedPDFFiles, outputDirectoryTextField.getText(), Pdf.PDFActiveInstance);
                pdfFileCompressorInstance.compressFiles();
            });

            compressedFilesCount = 0;
            compressionProgressBar.setMaximum(selectedPDFFiles.size());
            compressionProgressBar.setValue(compressedFilesCount);
            compressFilesButton.setText("Click to Stop");
//            RUN THE THREAD
            fileCompressThread.start();
        } else {
            if (pdfFileCompressorInstance != null) {
                pdfFileCompressorInstance.cancelFileCompressionRunning();
                pdfFileCompressorInstance = null;
            }
            compressFilesButton.setText("Compress Files");
        }
    }

    private void jLabel6MouseClicked(MouseEvent e) {
        try {
            Desktop.getDesktop().browse(new URI("https://imgovindjee.github.io/site/"));
        } catch (Exception exception) {
            System.out.println("[PDF] Error encountered while clicked on URL(URI)-Link...");
            exception.printStackTrace();
        }
    }

    private void button1ActionPerformed(ActionEvent e) {
        System.out.println("[PDF] Selecting the Files from the Directory...");

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setDialogTitle("Select Directory");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new FileNameExtensionFilter(".PDF Files", "pdf"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File[] PDFSelectedFiles = fileChooser.getSelectedFiles();
            for (File pdfFile : PDFSelectedFiles) {
                boolean flag = false;
                for (PDFFile selectedPDFFile : selectedPDFFiles) {
                    if (selectedPDFFile.getPhysicalFile().getAbsolutePath().equals(pdfFile.getAbsolutePath())) {
                        flag = true;
                        break;
                    }
                }

                if (!flag) {
                    selectedPDFFiles.add(new PDFFile(pdfFile));
                }
            }

            jTableFiles.setModel(new PDFTableLayout(new ArrayList<>(selectedPDFFiles)));
        } else {
            System.out.println("[] DO NOTHING");
        }
    }

    private void selectedOutputDirectoryButtonActionPerformed(ActionEvent e) {
        System.out.println("[PDF] Selecting the Output File Directory...");

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setDialogTitle("Select Directory");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            outputDirectoryTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            System.out.println("[PDF] Output File Directory Selected Successfully...");
        } else {
            System.err.println("[PDF] Output File Directory Not Selected Selected...");
        }
    }

    private void renderComponent() {
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();

        outputDirectoryTextField = new JTextField();
        selectedOutputDirectoryButton = new JButton();
        compressionProgressBar = new JProgressBar();

        jLabel2 = new JLabel();
        jPanel2 = new JPanel();

        jScrollPane = new JScrollPane();
        jTableFiles = new JTable();
        button1 = new JButton();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();

        compressFilesButton = new JButton();
    }


    public void UpdateJTableFiles() {
        jTableFiles.repaint();
    }

    public void FileCompressedFinished(PDFFile pdfFile) {
        try {
            jTableFiles.repaint();

            compressedFilesCount += 1;
            compressionProgressBar.setValue(compressedFilesCount);
            compressionProgressBar.invalidate();
            compressionProgressBar.repaint();
        } catch(Exception exception) {
            System.out.println("\n[PDF] Error encountered while file Compression...");
        }
    }

    public void compressionFinished() {
        compressFilesButton.setText("Compress Files");
        JOptionPane.showMessageDialog(this, String.valueOf(compressedFilesCount) + " files Compressed...", "Compression Finished", JOptionPane.INFORMATION_MESSAGE);
    }
}
