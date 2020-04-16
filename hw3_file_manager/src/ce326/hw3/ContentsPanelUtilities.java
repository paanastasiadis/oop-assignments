package ce326.hw3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

public class ContentsPanelUtilities {
    //    ArrayList<File> stack;
    private JPanel container;
    private JPanel breadCrumb;
    private JFrame frame;

    public ContentsPanelUtilities(JPanel contentsPanel, JPanel breadCrumbPanel, JFrame mainFrame) {
        container = contentsPanel;
        breadCrumb = breadCrumbPanel;
        frame = mainFrame;
    }

    public void browseDirectory(String dir) {
        File f = new File(dir);
        File[] s = f.listFiles();
        ArrayList<JButton> folders = new ArrayList<JButton>();
        ArrayList<JButton> files = new ArrayList<JButton>();
        try {
            for (File s1 : s) {


                JButton btn;

                if (s1.getName().length() > 20) {
                    btn = new JButton(s1.getName().substring(0, 20) + "..");
                } else {
                    btn = new JButton(s1.getName());
                }
                if (s1.isFile()) {
                    files.add(btn);

                } else {
                    folders.add(btn);
                }
                btn.setIcon(getTypeIcon(s1));

                btn.setBorderPainted(false);
                btn.setContentAreaFilled(false);
                btn.setVerticalTextPosition(SwingConstants.BOTTOM);
                btn.setHorizontalTextPosition(SwingConstants.CENTER);

                btn.addMouseListener(new MouseAdapter() {
                                         @Override
                                         public void mouseClicked(MouseEvent e) {
                                             if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                                                 if (s1.isFile()) {
                                                     try {
                                                         Desktop.getDesktop().open(s1);
                                                     } catch (Exception ex) {
                                                         JOptionPane.showMessageDialog(frame.getRootPane(), "Cant open the file!", "Sorry", JOptionPane.ERROR_MESSAGE);
//                                Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
                                                     }
                                                 } else {

                                                     setBreadCrumb(s1);
                                                     container.removeAll();
//                                                     container.revalidate();
                                                     browseDirectory(s1.getAbsolutePath());
                                                     container.revalidate();
                                                     container.repaint();

                                                 }
                                             } else if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
//                                                btn.setBackground(Color.cyan);
                                             }
                                         }
                                     }

                );

            }
            folders.sort(new Comparator<JButton>() {
                @Override
                public int compare(JButton jButton, JButton t1) {

                    return jButton.getText().compareToIgnoreCase(t1.getText());
                }
            });

            files.sort(new Comparator<JButton>() {
                @Override
                public int compare(JButton jButton, JButton t1) {

                    return jButton.getText().compareToIgnoreCase(t1.getText());
                }
            });
            for (JButton folder : folders) {
                JPanel btnContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
                btnContainer.add(folder);
                container.add(btnContainer);
            }
            for (JButton file : files) {
                JPanel btnContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
                btnContainer.add(file);
                container.add(btnContainer);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame.getRootPane(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void setBreadCrumb(File file) {
        breadCrumb.removeAll();
        breadCrumb.revalidate();
        boolean isLastInPath = true;
        while (file != null) {
            JButton btn = new JButton();
//            btn.setContentAreaFilled(false);
//            btn.setBorderPainted(false);
            String btnText;
            if (file.getName().length() > 0) {
                btnText = file.getName();
            } else {
                btnText = file.getPath().substring(0, 1);
            }
            btn.setText(btnText);

            if (isLastInPath) {
                breadCrumb.add(new JLabel(btnText), 0);

                isLastInPath = false;

            } else {
                File finalFile = file;
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        setBreadCrumb(finalFile);
                        container.removeAll();
                        browseDirectory(finalFile.getAbsolutePath());
                        container.revalidate();

                    }
                });
                breadCrumb.add(new JLabel(">"), 0);
                breadCrumb.add(btn, 0);
                breadCrumb.revalidate();
                breadCrumb.repaint();
            }


            file = file.getParentFile();
        }


    }

    private Icon getTypeIcon(File file) {
        if (file.isDirectory()) {
            return new ImageIcon("./icons/folder.png");
        }
        int extensionStartPoint = file.getName().lastIndexOf(".");
        String fileExtension = file.getName().substring(extensionStartPoint + 1);
        switch (fileExtension) {
            case "audio":
                return new ImageIcon("./icons/audio.png");
            case "bmp":
                return new ImageIcon("./icons/bmp.png");
            case "doc":
                return new ImageIcon("./icons/doc.png");
            case "docx":
                return new ImageIcon("./icons/docx.png");
            case "giff":
                return new ImageIcon("./icons/giff.png");
            case "gz":
                return new ImageIcon("./icons/gz.png");
            case "htm":
                return new ImageIcon("./icons/htm.png");
            case "html":
                return new ImageIcon("./icons/html.png");
            case "image":
                return new ImageIcon("./icons/image.png");
            case "jpeg":
                return new ImageIcon("./icons/jpeg.png");
            case "jpg":
                return new ImageIcon("./icons/jpg.png");
            case "mp3":
                return new ImageIcon("./icons/mp3.png");
            case "ods":
                return new ImageIcon("./icons/ods.png");
            case "odt":
                return new ImageIcon("./icons/odt.png");

            case "ogg":
                return new ImageIcon("./icons/ogg.png");
            case "pdf":
                return new ImageIcon("./icons/pdf.png");
            case "png":
                return new ImageIcon("./icons/png.png");
            case "tar":
                return new ImageIcon("./icons/tar.png");
            case "tgz":
                return new ImageIcon("./icons/tgz.png");
            case "txt":
                return new ImageIcon("./icons/txt.png");
            case "video":
                return new ImageIcon("./icons/video.png");
            case "wav":
                return new ImageIcon("./icons/wav.png");
            case "xlsx":
                return new ImageIcon("./icons/xlsx.png");
            case "xlx":
                return new ImageIcon("./icons/xlx.png");
            case "xml":
                return new ImageIcon("./icons/xml.png");
            case "zip":
                return new ImageIcon("./icons/zip.png");
            default:
                return new ImageIcon("./icons/question.png");
        }

    }
}
