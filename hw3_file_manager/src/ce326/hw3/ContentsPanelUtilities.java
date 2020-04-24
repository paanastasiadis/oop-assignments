package ce326.hw3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

public class ContentsPanelUtilities {
    //    ArrayList<File> stack;
    private JPanel container;
    private JPanel favorites;
    private JPanel breadCrumb;
    private JFrame frame;
    private EditMenu editMenu;
    private FavoritesInXML xmlFavorites;
    ButtonGroup bg;

    private File currentDirectory;

    public ContentsPanelUtilities(JPanel contentsPanel, JPanel breadCrumbPanel, EditMenu menuBar, FavoritesInXML xmlFile, JPanel favoritesPanel) {
        container = contentsPanel;
        breadCrumb = breadCrumbPanel;
        favorites = favoritesPanel;
        editMenu = menuBar;
        xmlFavorites = xmlFile;
    }

//    private void setCurrDirEditMenu() {
//        ContentsPanelUtilities contentsToUpdate = this;
//        container.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (e.getButton() == MouseEvent.BUTTON3) {
//                    editMenu.setSelectedFile(currentDirectory, contentsToUpdate);
//                    editMenu.showAsPopUp(container, e.getX(), e.getY());
//                }
//            }
//        });
//    }

    public JList<String> showSearchResults(String keyword, String type) {

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> jlist = new JList(model);

        if (keyword.equals("")) {
            model.addElement("no keyword specified");
            return jlist;
        }

        ArrayList<File> matchedFiles = searchFile(currentDirectory, keyword, type);

        if (matchedFiles.size() != 0) {

            jlist.addListSelectionListener(listSelectionEvent -> {
                String selectedValue = jlist.getSelectedValue();
                String filePath = selectedValue.substring(selectedValue.lastIndexOf(", ") + 2);
                File selectedFile = new File(filePath);
                if (selectedFile.isDirectory()) {
                    setBreadCrumb(selectedFile);
                    container.removeAll();
                    browseDirectory(filePath);
                    container.revalidate();
                } else {
                    try {
                        Desktop.getDesktop().open(selectedFile);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(container, "Cant open the file!", "Sorry", JOptionPane.ERROR_MESSAGE);
//                                Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            });

            for (File matchedFile : matchedFiles) {
                model.addElement(matchedFile.getName() + ", " + matchedFile.getAbsolutePath());

            }
        } else {
            model.addElement("found nothing");
        }

        return jlist;

    }

    private ArrayList<File> searchFile(File startingDirectory, String keyword, String type) {
        ArrayList<File> matchedFiles = new ArrayList<>();
        File[] files = startingDirectory.listFiles();
        if (files != null) {
            for (File f : files) {
                String nameToCompare = f.getName().toLowerCase();
                if (type != null && type.equals("dir")) {
                    if (nameToCompare.contains(keyword) && f.isDirectory()) {
                        matchedFiles.add(f);
                    }
                } else if (type != null) {
                    int extensionStartPoint = f.getName().lastIndexOf(".");
                    String fileExtension = f.getName().substring(extensionStartPoint + 1);
                    if (nameToCompare.contains(keyword) && type.equals(fileExtension)) {
                        matchedFiles.add(f);
                    }
                } else {
                    if (nameToCompare.contains(keyword)) {
                        matchedFiles.add(f);
                    }
                }

                if (f.isDirectory()) {
                    matchedFiles.addAll(0, searchFile(f, keyword, type));
                }
            }
        }
        return matchedFiles;
    }

    public void browseDirectory(String dir) {
        File f = new File(dir);
        File[] s = f.listFiles();
        ArrayList<JToggleButton> folders = new ArrayList<JToggleButton>();
        ArrayList<JToggleButton> files = new ArrayList<JToggleButton>();
        try {
            for (File s1 : s) {
                JToggleButton btn;

                if (s1.getName().length() > 13) {
                    btn = new JToggleButton(s1.getName().substring(0, 13) + "..");
                } else {
                    btn = new JToggleButton(s1.getName());
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
                btn.setFocusable(false);

                ContentsPanelUtilities contentsToUpdate = this;
                btn.addItemListener(itemEvent -> {
                    if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                        editMenu.setEnabled(true);
                        editMenu.setSelectedFile(s1, contentsToUpdate);
                        btn.setContentAreaFilled(true);
                    } else {
                        btn.setContentAreaFilled(false);
                        editMenu.setEnabled(false);
                    }
                });
                btn.addMouseListener(new MouseAdapter() {
                                         @Override
                                         public void mouseClicked(MouseEvent e) {
                                             if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
//                                                 editMenu.setEnabled(false);

                                                 if (s1.isFile()) {
                                                     try {
                                                         Desktop.getDesktop().open(s1);
                                                     } catch (Exception ex) {
                                                         JOptionPane.showMessageDialog(container, "Cant open the file!", "Sorry", JOptionPane.ERROR_MESSAGE);
//                                Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
                                                     }
                                                 } else {

                                                     setBreadCrumb(s1);
                                                     container.removeAll();
                                                     container.revalidate();
                                                     browseDirectory(s1.getAbsolutePath());
                                                     container.revalidate();
                                                     container.repaint();

                                                 }
                                             } else if (e.getButton() == MouseEvent.BUTTON3) {
                                                 btn.setSelected(true);
                                                 editMenu.showAsPopUp(btn, e.getX(), e.getY());
                                             }
                                         }
                                     }
                );


            }

            folders.sort(new Comparator<JToggleButton>() {
                @Override
                public int compare(JToggleButton jButton, JToggleButton t1) {

                    return jButton.getText().compareToIgnoreCase(t1.getText());
                }
            });

            files.sort(new Comparator<JToggleButton>() {
                @Override
                public int compare(JToggleButton jButton, JToggleButton t1) {

                    return jButton.getText().compareToIgnoreCase(t1.getText());
                }
            });


            bg = new ButtonGroup();
            for (JToggleButton folder : folders) {
                JPanel btnContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
                bg.add(folder);
                btnContainer.add(folder);
                btnContainer.setPreferredSize(new Dimension(120, 120));
                btnContainer.setMaximumSize(new Dimension(120, 120));
                btnContainer.setMinimumSize(new Dimension(120, 120));
                container.add(btnContainer);
            }
            for (JToggleButton file : files) {
                JPanel btnContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
                bg.add(file);
                btnContainer.add(file);
                btnContainer.setPreferredSize(new Dimension(120, 120));
                btnContainer.setMaximumSize(new Dimension(120, 120));
                btnContainer.setMinimumSize(new Dimension(120, 120));
                container.add(btnContainer);
            }

            container.revalidate();
            container.repaint();
//            container.add(bg);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame.getRootPane(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void setBreadCrumb(File file) {
        bg.clearSelection();
        breadCrumb.removeAll();
        breadCrumb.revalidate();
        boolean isLastInPath = true;
        while (file != null) {
            JButton btn = new JButton();
            String btnText;
            if (file.getName().length() > 0) {
                btnText = file.getName();
            } else {
                btnText = file.getPath().substring(0, 1);
            }
            btn.setText(btnText);
            btn.setForeground(Color.white);
            btn.setContentAreaFilled(false);
            btn.setFocusable(false);
            if (isLastInPath) {
                currentDirectory = file;
                JLabel currentDirLabel = new JLabel(btnText);
                currentDirLabel.setForeground(Color.WHITE);
                breadCrumb.add(currentDirLabel, 0);

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
                JLabel separator = new JLabel(">");
                separator.setForeground(Color.WHITE);
                breadCrumb.add(separator, 0);
                breadCrumb.add(btn, 0);
                breadCrumb.revalidate();
                breadCrumb.repaint();
            }


            file = file.getParentFile();
        }


    }

    public void initFavorites(File[] existingFiles, String initFilePath) {
        File initFile = new File(initFilePath);
        JButton entry = new JButton(initFile.getName());
        entry.setBackground(Color.GRAY);
        entry.setForeground(Color.WHITE);
        entry.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        entry.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    updateCurrentDirectory(initFile);
                }
            }

        });
        favorites.add(entry);
        favorites.add(Box.createVerticalStrut(20));


        for (File f : existingFiles) {
            setFavorite(f, true);
        }
    }

    private void removeFavorite(String filePath, JButton buttonToRemove) {
        xmlFavorites.removeEntry(filePath);
        int pos = favorites.getComponentZOrder(buttonToRemove);
        favorites.remove(pos + 1); //remove vertical gap element
        favorites.remove(buttonToRemove);

        favorites.revalidate();
        favorites.repaint();
    }

    public void setFavorite(File dir, boolean existsInXML) {
        JButton entry = new JButton(dir.getName());
        entry.setForeground(Color.WHITE);
        entry.setBackground(Color.GRAY);
        JPopupMenu removePopUp = new JPopupMenu();
        JMenuItem removeItem = new JMenuItem("Remove from Favorites");
        removeItem.addActionListener(actionEvent -> removeFavorite(dir.getAbsolutePath(), entry));
        removePopUp.add(removeItem);

        if (!existsInXML) {
            xmlFavorites.addToXML(dir.getName(), dir.getPath());
        }
        entry.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    updateCurrentDirectory(dir);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    removePopUp.show(entry, e.getX(), e.getY());

                }
            }

        });

        favorites.add(entry);
        favorites.add(Box.createVerticalStrut(20));

        favorites.revalidate();
        favorites.repaint();

    }

    public void updateCurrentDirectory(File currentDirectory) {
        setBreadCrumb(currentDirectory);
        container.removeAll();
        browseDirectory(currentDirectory.getAbsolutePath());
        container.revalidate();
        container.repaint();

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

