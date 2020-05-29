package ce326.hw3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

public class BrowserUtilities {
    private final JPanel container;
    private final JPanel favorites;
    private final JPanel breadCrumb;
    private final EditMenu editMenu;
    private FavoritesInXML xmlFavorites;
    ButtonGroup bg;

    private File currentDirectory;

    public BrowserUtilities(String startingPath, JPanel contentsPanel, JPanel breadCrumbPanel, EditMenu menuBar, JPanel favoritesPanel) {
        container = contentsPanel;
        breadCrumb = breadCrumbPanel;
        favorites = favoritesPanel;
        editMenu = menuBar;
        currentDirectory = new File(startingPath);

    }

    //initialize files view, favorites panel and breadcrumb
    public void initNavigation() {

        createFilesView(currentDirectory.getAbsolutePath());
        setBreadCrumb(currentDirectory);

        xmlFavorites = new FavoritesInXML();
        File[] existingFiles = xmlFavorites.readAllXMLEntries();
        this.initFavorites(existingFiles, currentDirectory.getAbsolutePath());
    }


    //create the contents of the files view
    public void createFilesView(String dir) {
        File f = new File(dir);
        File[] s = f.listFiles();
        ArrayList<JToggleButton> folders = new ArrayList<JToggleButton>();
        ArrayList<JToggleButton> files = new ArrayList<JToggleButton>();
        try {
            for (File s1 : s) {
                JToggleButton btn;

                //shorten the name with dots in the end if it is too big
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

                //configure the characteristics of the button
                btn.setIcon(getTypeIcon(s1));
                btn.setBorderPainted(false);
                btn.setContentAreaFilled(false);
                btn.setVerticalTextPosition(SwingConstants.BOTTOM);
                btn.setHorizontalTextPosition(SwingConstants.CENTER);
                btn.setFocusable(false);

                //mark the button if it is selected and enable Edit Menu options
                BrowserUtilities contentsToUpdate = this;
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

                //configure actions for the left and right mouse clicks on the button
                btn.addMouseListener(new MouseAdapter() {
                                         @Override
                                         public void mouseClicked(MouseEvent e) {
                                             //action happens on double left click
                                             // nothing happens with a single click
                                             if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {

                                                 //if a file, try to open it with default system app
                                                 if (s1.isFile()) {
                                                     try {
                                                         Desktop.getDesktop().open(s1);
                                                     } catch (Exception ex) {
                                                         JOptionPane.showMessageDialog(container, "Cant open the file!", "Sorry", JOptionPane.ERROR_MESSAGE);
                                                     }
                                                 } else {

                                                     //if a folder, access it by updating the current files view
                                                     //update also the breadcrumb
                                                     setBreadCrumb(s1);
                                                     container.removeAll();
                                                     container.revalidate();
                                                     createFilesView(s1.getAbsolutePath());
                                                     container.revalidate();
                                                     container.repaint();

                                                 }
                                             } else if (e.getButton() == MouseEvent.BUTTON3) {
                                                 //for right click, show the edit menu as pop up
                                                 btn.setSelected(true);
                                                 editMenu.showAsPopUp(btn, e.getX(), e.getY());
                                             }
                                         }
                                     }
                );


            }
            //sort the buttons by alphabetic order
            folders.sort((jButton, t1) -> jButton.getText().compareToIgnoreCase(t1.getText()));
            files.sort((jButton, t1) -> jButton.getText().compareToIgnoreCase(t1.getText()));

            //add the buttons on the button group to disable multiple selection of a files
            bg = new ButtonGroup();
            createFileButtons(folders);
            createFileButtons(files);

            container.revalidate();
            container.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(container.getRootPane(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //add a container around every button for better orientation between them
    private void createFileButtons(ArrayList<JToggleButton> buttonsList) {
        for (JToggleButton btn : buttonsList) {
            JPanel btnContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
            bg.add(btn);
            btnContainer.add(btn);
            btnContainer.setPreferredSize(new Dimension(120, 120));
            btnContainer.setMaximumSize(new Dimension(120, 120));
            btnContainer.setMinimumSize(new Dimension(120, 120));
            container.add(btnContainer);
        }
    }

    //update files view and breadcrumb with the newly specified directory
    public void updateCurrentDirectory(File currentDirectory) {
        setBreadCrumb(currentDirectory);
        container.removeAll();
        createFilesView(currentDirectory.getAbsolutePath());
        container.revalidate();
        container.repaint();

    }

    //set the breadcrumb for the currently displayed directory
    private void setBreadCrumb(File file) {
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

            //assign traits for every button in the breadcrumb
            btn.setText(btnText);
            btn.setForeground(Color.white);
            btn.setContentAreaFilled(false);
            btn.setFocusable(false);

            if (isLastInPath) {
                //for the last point in the breadcrumb we assign a label instead of a button
                //A button would be meaningless because the last point is the current directory
                currentDirectory = file;
                JLabel currentDirLabel = new JLabel(btnText);
                currentDirLabel.setForeground(Color.WHITE);
                breadCrumb.add(currentDirLabel, 0);

                isLastInPath = false;

            } else {
                File finalFile = file;
                btn.addActionListener(actionEvent -> {
                    setBreadCrumb(finalFile);
                    container.removeAll();
                    createFilesView(finalFile.getAbsolutePath());
                    container.revalidate();
                });
                //assign in between the buttons a separation ">" label
                JLabel separator = new JLabel(">");
                separator.setForeground(Color.WHITE);
                breadCrumb.add(separator, 0);
                breadCrumb.add(btn, 0);
                breadCrumb.revalidate();
                breadCrumb.repaint();
            }
            //make a button for every point in the path
            file = file.getParentFile();
        }
    }

    private void initFavorites(File[] existingFiles, String initFilePath) {

        //assign a default irremovable entry in the favorites panel, the link for the home directory
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

        //search the favorites xml file to add favorite links for the stored entries
        for (File f : existingFiles) {
            setFavorite(f, true);
        }
    }

    //remove an entry from both the xml file and the favorites panel
    private void removeFavorite(String filePath, JButton buttonToRemove) {
        xmlFavorites.removeEntry(filePath);

        int pos = favorites.getComponentZOrder(buttonToRemove);
        favorites.remove(pos + 1); //remove vertical gap element
        favorites.remove(buttonToRemove);

        favorites.revalidate();
        favorites.repaint();
    }

    //add the specified directory to the favorites panel and store it to the xml file
    public void setFavorite(File dir, boolean existsInXML) {

        JButton entry = new JButton(dir.getName());
        entry.setForeground(Color.WHITE);
        entry.setBackground(Color.GRAY);
        JPopupMenu removePopUp = new JPopupMenu();
        JMenuItem removeItem = new JMenuItem("Remove from Favorites");
        removeItem.addActionListener(actionEvent -> removeFavorite(dir.getAbsolutePath(), entry));
        removePopUp.add(removeItem);

        //store it to the xml if it is not already stored
        if (!existsInXML) {
            xmlFavorites.addToXML(dir.getName(), dir.getPath());
        }
        //add remove option for a right click action
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

    //show the search results for the specified keyword and type
    public JList<String> showSearchResults(String keyword, String type) {

        DefaultListModel<String> model = new DefaultListModel<String>();
        JList<String> jlist = new JList<String>(model);

        //if keyword is nothing display a list with a relevant message
        if (keyword.equals("")) {
            model.addElement("no keyword specified");
            return jlist;
        }

        //search recursively for matched files starting from the current directory
        ArrayList<File> matchedFiles = searchFile(currentDirectory, keyword, type);

        if (matchedFiles.size() != 0) {

            //make the search results in the list clickable

            jlist.addListSelectionListener(listSelectionEvent -> {
                String selectedValue = jlist.getSelectedValue();
                String filePath = selectedValue.substring(selectedValue.lastIndexOf(", ") + 2);
                File selectedFile = new File(filePath);

                if (selectedFile.isDirectory()) {
                    setBreadCrumb(selectedFile);
                    container.removeAll();
                    createFilesView(filePath);
                    container.revalidate();
                } else {
                    try {
                        Desktop.getDesktop().open(selectedFile);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(container, "Cant open the file!", "Sorry", JOptionPane.ERROR_MESSAGE);
                    }
                }

            });
            //display the name and the path for every matched file or directory
            for (File matchedFile : matchedFiles) {
                model.addElement(matchedFile.getName() + ", " + matchedFile.getAbsolutePath());

            }
        } else {
            model.addElement("found nothing");
        }

        return jlist;

    }

    //search recursively for matched files starting from the current directory
    private ArrayList<File> searchFile(File startingDirectory, String keyword, String type) {
        ArrayList<File> matchedFiles = new ArrayList<>();
        File[] files = startingDirectory.listFiles();

        if (files != null) {
            for (File f : files) {
                String nameToCompare = f.getName().toLowerCase();

                //for type = dir search only for directories
                if (type != null && type.equals("dir")) {
                    if (nameToCompare.contains(keyword) && f.isDirectory()) {
                        matchedFiles.add(f);
                    }
                } else if (type != null) { //for other type options search the files with the matching extensions
                    int extensionStartPoint = f.getName().lastIndexOf(".");
                    String fileExtension = f.getName().substring(extensionStartPoint + 1);
                    if (nameToCompare.contains(keyword) && type.equals(fileExtension)) {
                        matchedFiles.add(f);
                    }
                } else { //for no type search everything
                    if (nameToCompare.contains(keyword)) {
                        matchedFiles.add(f);
                    }
                }

                //search all the folders recursively
                if (f.isDirectory()) {
                    matchedFiles.addAll(0, searchFile(f, keyword, type));
                }
            }
        }
        return matchedFiles;
    }

    //assign the right icon for different file extensions
    private static Icon getTypeIcon(File file) {
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

