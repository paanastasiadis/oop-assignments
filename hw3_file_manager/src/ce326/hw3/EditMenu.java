package ce326.hw3;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.File;


public class EditMenu extends JMenu {

    private final JPanel mainPanel;
    private final JPopupMenu popUpEdit;

    private BrowserUtilities servicesToUpdate;
    private File selectedFile;
    private final FileOperations operations;

    public EditMenu(String MenuName, JPanel appPanel) {
        super(MenuName);

        mainPanel = appPanel;

        setMnemonic(KeyEvent.VK_E);
        setEnabled(false);

        //items for top bar edit menu
        JMenuItem[] items = createEditItems();

        for (JMenuItem i : items) {
            add(i);
        }


        popUpEdit = new JPopupMenu();

        //items for the pop up edit menu
        JMenuItem[] popItems = createEditItems();
        for (JMenuItem i : popItems) {
            popUpEdit.add(i);
        }

        //initialize file operations
        operations = new FileOperations(mainPanel);
    }

    public void setSelectedFile(File file, BrowserUtilities services) {
        operations.setTargetServices(services);
        selectedFile = file;
        servicesToUpdate = services;
    }

    private JMenuItem[] createEditItems() {
        JMenuItem[] editItems = new JMenuItem[7];

        JMenuItem renameItem = new JMenuItem("Rename");
        renameItem.setMnemonic(KeyEvent.VK_R);
        renameItem.setToolTipText("Rename a file or directory");
        renameItem.addActionListener(actionEvent -> operations.renameFile(selectedFile));
        editItems[0] = renameItem;

        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.setEnabled(false);
        pasteItem.setMnemonic(KeyEvent.VK_V);
        pasteItem.setToolTipText("Paste a file or a directory into another directory");
        pasteItem.addActionListener(actionEvent -> {
            if (operations.pasteFile(selectedFile) == 0) {
                pasteItem.setEnabled(false);
            }
        });
        editItems[1] = pasteItem;

        //copy option
        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.addActionListener(actionEvent -> {
            operations.copyFile(selectedFile);
            pasteItem.setEnabled(true);
        });
        copyItem.setMnemonic(KeyEvent.VK_C);
        copyItem.setToolTipText("Copy a file or directory");
        editItems[2] = copyItem;

        //cut option
        JMenuItem cutItem = new JMenuItem("Cut");
        copyItem.setMnemonic(KeyEvent.VK_X);
        cutItem.setToolTipText("Cut a file or directory");
        cutItem.addActionListener(actionEvent -> {
            operations.cutFile(selectedFile);
            pasteItem.setEnabled(true);
        });
        editItems[3] = cutItem;

        //init delete option
        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.setMnemonic(KeyEvent.VK_D);
        deleteItem.addActionListener(actionEvent -> operations.deleteFile(selectedFile));
        deleteItem.setToolTipText("Delete a file or directory");
        editItems[4] = deleteItem;

        //init add to favorites option
        JMenuItem addToFavoritesItem = new JMenuItem("Add to Favorites");
        addToFavoritesItem.setToolTipText("Add a directory to the Favorites panel");
        addToFavoritesItem.addActionListener(actionEvent -> {
            if (!selectedFile.isDirectory()) {
                JOptionPane.showMessageDialog(mainPanel, "Not a directory", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                servicesToUpdate.setFavorite(selectedFile, false);
            }
        });
        editItems[5] = addToFavoritesItem;

        //init properties item
        JMenuItem propertiesItem = new JMenuItem("Properties");
        propertiesItem.addActionListener(actionEvent -> operations.showProperties(selectedFile));
        propertiesItem.setToolTipText("See the properties of a file or a directory");
        editItems[6] = propertiesItem;

        return editItems;

    }

    public void showAsPopUp(JComponent panel, int posX, int posY) {

        popUpEdit.show(panel, posX, posY);
    }


}
