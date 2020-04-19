package ce326.hw3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Objects;


public class EditMenu extends JMenu {
    private File selectedFile;
    private File fileToCopy;
    private File fileToMove;
    private final JPanel mainPanel;
    private ContentsPanelUtilities contentsToUpdate;
    private int selectionType;
    private static final int COPY_OPTION = 1;
    private static final int CUT_OPTION = 2;

    public EditMenu(String MenuName, JPanel appPanel) {
        super(MenuName);

        mainPanel = appPanel;
        setMnemonic(KeyEvent.VK_E);
        setEnabled(false);

        //rename option
        JMenuItem renameItem = new JMenuItem("Rename");
        renameItem.setMnemonic(KeyEvent.VK_R);
        renameItem.setToolTipText("Rename a file or directory");
        renameItem.addActionListener(actionEvent -> renameFile());

        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.setEnabled(false);
        pasteItem.setMnemonic(KeyEvent.VK_V);
        pasteItem.setToolTipText("Paste a file or a directory into another directory");
        pasteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (pasteFile() == 0) {
                    pasteItem.setEnabled(false);
                }
            }
        });

        //copy option
        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                copyFile();
                pasteItem.setEnabled(true);
            }
        });
        copyItem.setMnemonic(KeyEvent.VK_C);
        copyItem.setToolTipText("Copy a file or directory");

        //cut option
        JMenuItem cutItem = new JMenuItem("Cut");
        copyItem.setMnemonic(KeyEvent.VK_X);
        cutItem.setToolTipText("Cut a file or directory");
        cutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cutFile();
                pasteItem.setEnabled(true);
            }
        });


        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.setMnemonic(KeyEvent.VK_D);
        deleteItem.addActionListener(actionEvent -> {
            deleteFile();
        });
        deleteItem.setToolTipText("Delete a file or directory");

        JMenuItem addToFavoritesItem = new JMenuItem("Add to Favorites");
        addToFavoritesItem.setToolTipText("Add a directory to the Favorites panel");

        JMenuItem propertiesItem = new JMenuItem("Properties");
        propertiesItem.setToolTipText("See the properties of a file or a directory");

        add(renameItem);
        add(copyItem);
        add(cutItem);
        add(pasteItem);
        add(deleteItem);
        add(addToFavoritesItem);
        add(propertiesItem);
    }

    public void setSelectedFile(File file, ContentsPanelUtilities contentsPanel) {
        selectedFile = file;
        contentsToUpdate = contentsPanel;
    }

    private int pasteFile() {
        if (selectedFile.isFile()) {
            JOptionPane.showMessageDialog(mainPanel, "Not a directory", "Error", JOptionPane.ERROR_MESSAGE);
            return 1;
        } else {
            int res = -1;
            File existingFile = null;
            for (File f : Objects.requireNonNull(selectedFile.listFiles())) {
                if (selectionType == CUT_OPTION) {
                    if (f.getName().equals(fileToMove.getName()) && f != fileToMove) {
                        res = JOptionPane.showConfirmDialog(mainPanel, "A file or directory with the same name already exists. Do you want to overwrite it?", "Warning", JOptionPane.YES_NO_OPTION);
                        existingFile = f;
                        break;
                    }
                } else if (selectionType == COPY_OPTION) {
                    if (f.getName().equals(fileToCopy.getName()) && f != fileToCopy) {
                        res = JOptionPane.showConfirmDialog(mainPanel, "A file or directory with the same name already exists. Do you want to overwrite it?", "Warning", JOptionPane.YES_NO_OPTION);
                        existingFile = f;
                        break;
                    }
                }
            }
            if (res == JOptionPane.NO_OPTION) {
                return 1;
            } else if (res == JOptionPane.YES_OPTION) {
                try {
                    if (existingFile.isDirectory()) {
                        Files.walk(existingFile.toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                    } else {
                        Files.delete(existingFile.toPath());
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(mainPanel, "Error in moving the specified file or directory", "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    return 1;
                }

            }

            File currentDirectory = new File(selectedFile.getParentFile().getPath());
            if (selectionType == CUT_OPTION) {

                try {
                    Files.move(fileToMove.toPath(), Paths.get(selectedFile.getPath() + File.separator + fileToMove.getName()));
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(mainPanel, "Error in moving the specified file or directory", "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
                contentsToUpdate.updateCurrentDirectory(currentDirectory);
            } else if (selectionType == COPY_OPTION) {
                try {
                    if (fileToCopy.isDirectory()) {
                        Files.walk(fileToCopy.toPath())
                                .forEach(source -> {
                                    try {
                                        Files.copy(source, Paths.get(selectedFile.getPath() + File.separator + fileToCopy.getName()).resolve(fileToCopy.toPath().relativize(source)));
                                    } catch (IOException e) {
                                        JOptionPane.showMessageDialog(mainPanel, "Error in coping the specified file or directory", "Error", JOptionPane.ERROR_MESSAGE);
                                        e.printStackTrace();
                                    }
                                });
                    } else {
                        Files.copy(fileToCopy.toPath(), Paths.get(selectedFile.getPath() + File.separator + fileToCopy.getName()));
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(mainPanel, "Error in coping the specified file or directory", "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
                contentsToUpdate.updateCurrentDirectory(currentDirectory);
            }
        }

        return 0;
    }

    private void copyFile() {
        selectionType = COPY_OPTION;
        fileToCopy = selectedFile;
    }

    private void cutFile() {
        selectionType = CUT_OPTION;
        fileToMove = selectedFile;
    }

    private void deleteFile() {
        int res = JOptionPane.showConfirmDialog(mainPanel, "Are you sure you want to delete this?", "Warning", JOptionPane.YES_NO_OPTION);

        if (res == JOptionPane.YES_OPTION) {

            try {
                if (selectedFile.isDirectory()) {
                    Files.walk(selectedFile.toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                } else {
                    Files.delete(selectedFile.toPath());
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(mainPanel, "Error in deleting the specified file or directory", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
            File currentDirectory = new File(selectedFile.getParentFile().getPath());
            contentsToUpdate.updateCurrentDirectory(currentDirectory);

        }


    }

    private void renameFile() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(mainPanel, "No file or directory is selected", "Sorry", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String renameInput = JOptionPane.showInputDialog(mainPanel, "New Name", selectedFile.getName());

        if (renameInput == null) {
            return;
        } else {
            for (File f : Objects.requireNonNull(selectedFile.getParentFile().listFiles())) {

                if (f.getName().equals(renameInput)) {
                    if (f.isDirectory()) {
                        JOptionPane.showMessageDialog(mainPanel, "A directory with the same name already exists. Try again with a different name.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int res = JOptionPane.showConfirmDialog(mainPanel, "A file or directory with the same name already exists. Do you want to overwrite it?", "Warning", JOptionPane.YES_NO_OPTION);
                    if (res == JOptionPane.NO_OPTION) {
                        return;
                    } else break;
                }
            }
        }
        File renamedFile = new File(selectedFile.getParentFile(), renameInput);


        if (!selectedFile.renameTo(renamedFile)) {
            JOptionPane.showMessageDialog(mainPanel, "Cannot rename file to the specified input text", "Error", JOptionPane.ERROR_MESSAGE);

        }
        contentsToUpdate.updateCurrentDirectory(selectedFile.getParentFile());
    }


}
