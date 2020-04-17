package ce326.hw3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.nio.file.Files;


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
                pasteFile();
                pasteItem.setEnabled(false);
            }
        });

        //copy option
        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                pasteItem.setEnabled(true);
                copyFile();
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
                pasteItem.setEnabled(true);
                cutFile(); }
        });



        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.setMnemonic(KeyEvent.VK_D);
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

    private void pasteFile() {
        if (selectedFile.isFile()) {
            JOptionPane.showMessageDialog(mainPanel, "Not a directory", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        else {
            if (selectionType == CUT_OPTION) {
                for (File f: Objects.requireNonNull(selectedFile.listFiles())) {
                    if (f.getName().equals(selectedFile.getName()) && f != selectedFile) {
                        int res = JOptionPane.showConfirmDialog(mainPanel, "A file or directory with the same name already exists. Do you want to overwrite it?","Warning" ,JOptionPane.YES_NO_OPTION);
                        if (res  == JOptionPane.NO_OPTION) {
                            return;
                        }
                        else break;
                    }
                }
                try {
                    System.out.println(selectedFile.toPath().toString());
//                    File pasteDir= new File(selectedFile.getPath());
                   Files.copy(fileToMove.toPath(), selectedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(mainPanel, "Error in moving the specified file or directory", "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
//                if (!fileToMove.moveTo(selectedFile)) {
//
//                }
                contentsToUpdate.updateCurrentDirectory(selectedFile.getParentFile());
            }
        }
    }

    private void copyFile() {
        selectionType = COPY_OPTION;
        fileToCopy = selectedFile;
    }
    private void cutFile() {
        selectionType = CUT_OPTION;
        fileToMove = selectedFile;
    }

    private void renameFile() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(mainPanel, "No file or directory is selected", "Sorry", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String renameInput = JOptionPane.showInputDialog(mainPanel, "New Name", selectedFile.getName());

        if (renameInput == null) {
            return;
        }
        else  {
            for (File f: Objects.requireNonNull(selectedFile.getParentFile().listFiles())) {
                if (f.getName().equals(selectedFile.getName()) && f != selectedFile) {
                   int res = JOptionPane.showConfirmDialog(mainPanel, "A file or directory with the same name already exists. Do you want to overwrite it?","Warning" ,JOptionPane.YES_NO_OPTION);
                   if (res  == JOptionPane.NO_OPTION) {
                       return;
                   }
                   else break;
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
