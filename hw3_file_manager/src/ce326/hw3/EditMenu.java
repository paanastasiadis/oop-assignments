package ce326.hw3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
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

    private JPopupMenu popUpEdit;

    public EditMenu(String MenuName, JPanel appPanel) {
        super(MenuName);

        mainPanel = appPanel;
        setMnemonic(KeyEvent.VK_E);
        setEnabled(false);

        //rename option
        JMenuItem[] items = createEditItems();

        for (JMenuItem i:items) {
            add(i);
        }

        popUpEdit = new JPopupMenu();

        JMenuItem[] popItems = createEditItems();
        for (JMenuItem i:popItems) {
            popUpEdit.add(i);
        }

//        add(renameItem);
//        add(copyItem);
//        add(cutItem);
//        add(pasteItem);
//        add(deleteItem);
//        add(addToFavoritesItem);
//        add(propertiesItem);


    }

    private JMenuItem[] createEditItems() {
        JMenuItem[] editItems = new JMenuItem[7];

        JMenuItem renameItem = new JMenuItem("Rename");
        renameItem.setMnemonic(KeyEvent.VK_R);
        renameItem.setToolTipText("Rename a file or directory");
        renameItem.addActionListener(actionEvent -> renameFile());
        editItems[0] = renameItem;

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
        editItems[1] = pasteItem;

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
        editItems[2] = copyItem;

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
        editItems[3] = cutItem;


        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.setMnemonic(KeyEvent.VK_D);
        deleteItem.addActionListener(actionEvent -> {
            deleteFile();
        });
        deleteItem.setToolTipText("Delete a file or directory");
        editItems[4] = deleteItem;

        JMenuItem addToFavoritesItem = new JMenuItem("Add to Favorites");
        addToFavoritesItem.setToolTipText("Add a directory to the Favorites panel");
        editItems[5] = addToFavoritesItem;

        JMenuItem propertiesItem = new JMenuItem("Properties");
        propertiesItem.addActionListener(actionEvent -> {
            showProperties();
        });
        propertiesItem.setToolTipText("See the properties of a file or a directory");
        editItems[6] = propertiesItem;

        return editItems;

    }

    public void showAsPopUp(JComponent panel, int posX, int posY) {

//        panel.add(popUpEdit);
        popUpEdit.show(panel, posX, posY);
    }

    public void setSelectedFile(File file, ContentsPanelUtilities contentsPanel) {
        selectedFile = file;
        contentsToUpdate = contentsPanel;
    }

    private void showProperties() {
        StringBuilder propertiesMessage = new StringBuilder();
        propertiesMessage.append("Name: ");
        propertiesMessage.append(selectedFile.getName());
        propertiesMessage.append("\n");
        propertiesMessage.append("Path: ");
        propertiesMessage.append(selectedFile.getPath());
        propertiesMessage.append("\n");
        propertiesMessage.append("Size: ");
        long size = 0;
        if (selectedFile.isDirectory()) {
            if (selectedFile.listFiles() != null) {
                for (File f : selectedFile.listFiles()) {
                    size += f.length();
                }
            }
        } else {
            size = selectedFile.length();
        }
        propertiesMessage.append(size);
        propertiesMessage.append(" bytes");
        propertiesMessage.append("\nPermissions");
        JCheckBox readable = new JCheckBox("Read  ");
        JCheckBox writable = new JCheckBox("Write ");
        JCheckBox executable = new JCheckBox("Execute ");

        boolean allowedToWrite;
        boolean initialWritePerm = selectedFile.canWrite();
        if (initialWritePerm) {
            allowedToWrite = selectedFile.setWritable(false);
        } else {
            allowedToWrite = selectedFile.setWritable(true);
        }

        if (!allowedToWrite) {
            writable.setEnabled(false);
            readable.setEnabled(false);
            executable.setEnabled(false);
        } else {
            selectedFile.setWritable(initialWritePerm);

            readable.setSelected(selectedFile.canRead());
            writable.setSelected(selectedFile.canWrite());
            executable.setSelected(selectedFile.canExecute());

            readable.addItemListener(itemEvent -> {
                if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                    selectedFile.setReadable(true);
                } else if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
                    selectedFile.setReadable(false);
                }
            });

            writable.addItemListener(itemEvent -> {
                if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                    selectedFile.setWritable(true);
                } else if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
                    selectedFile.setWritable(false);
                }
            });

            executable.addItemListener(itemEvent -> {
                if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                    selectedFile.setExecutable(true);
                } else if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
                    selectedFile.setExecutable(false);
                }
            });

        }


        Object msg[] = {propertiesMessage, writable, readable, executable};

        JOptionPane.showMessageDialog(mainPanel, msg, "Properties", JOptionPane.INFORMATION_MESSAGE);
    }

    private int pasteFile() {
        selectedFile = selectedFile.getParentFile();
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

            File currentDirectory = new File(selectedFile.getPath());
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
