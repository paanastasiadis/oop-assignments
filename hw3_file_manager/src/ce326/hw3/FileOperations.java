package ce326.hw3;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Objects;

public class FileOperations {
    private final JPanel targetPanel;
    private BrowserUtilities targetServices;

    private File fileToCopy;
    private File fileToMove;
    private int selectionType;
    private static final int COPY_OPTION = 1;
    private static final int CUT_OPTION = 2;

    public FileOperations(JPanel panel) {
        targetPanel = panel;
    }

    //set the target services that need update, when a file operation is done
    public void setTargetServices(BrowserUtilities services) {
        this.targetServices = services;
    }

    //mark and store in memory the file that was copied for when the paste command will be executed
    public void copyFile(File targetFile) {
        selectionType = COPY_OPTION;
        fileToCopy = targetFile;
    }

    //mark and store in memory the file that was cut for when the paste command will be executed
    public void cutFile(File targetFile) {
        selectionType = CUT_OPTION;
        fileToMove = targetFile;
    }

    //paste operation
    public int pasteFile(File targetFile) {

        //paste the file on the current viewed or parent directory of the selected file
        targetFile = targetFile.getParentFile();

        if (targetFile.isFile()) {
            JOptionPane.showMessageDialog(targetPanel, "Not a directory", "Error", JOptionPane.ERROR_MESSAGE);
            return 1;
        } else {
            int res = -1;
            File existingFile = null;

            //check for potential existence of a file with same name and ask for overwrite
            for (File f : Objects.requireNonNull(targetFile.listFiles())) {
                if (selectionType == CUT_OPTION) {
                    if (f.getName().equals(fileToMove.getName()) && f != fileToMove) {
                        res = JOptionPane.showConfirmDialog(targetPanel, "A file or directory with the same name already exists. Do you want to overwrite it?", "Warning", JOptionPane.YES_NO_OPTION);
                        existingFile = f;
                        break;
                    }
                } else if (selectionType == COPY_OPTION) {
                    if (f.getName().equals(fileToCopy.getName()) && f != fileToCopy) {
                        res = JOptionPane.showConfirmDialog(targetPanel, "A file or directory with the same name already exists. Do you want to overwrite it?", "Warning", JOptionPane.YES_NO_OPTION);
                        existingFile = f;
                        break;
                    }
                }
            }

            //proceed or cancel according to the user's preference
            if (res == JOptionPane.NO_OPTION) {
                return 1;
            } else if (res == JOptionPane.YES_OPTION) {
                try {
                    //overwrite the file or folder by deleting the previously existing one
                    if (existingFile.isDirectory()) {
                        Files.walk(existingFile.toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                    } else {
                        Files.delete(existingFile.toPath());
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(targetPanel, "Error in moving the specified file or directory", "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    return 1;
                }

            }

            //complete paste command by moving or coping the file according to the user
            File currentDirectory = new File(targetFile.getPath());
            if (selectionType == CUT_OPTION) {
                try {
                    Files.move(fileToMove.toPath(), Paths.get(targetFile.getPath() + File.separator + fileToMove.getName()));
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(targetPanel, "Error in moving the specified file or directory", "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
                //update the panel to display the new content
                targetServices.updateCurrentDirectory(currentDirectory);
            } else if (selectionType == COPY_OPTION) {
                try {
                    if (fileToCopy.isDirectory()) {
                        File finalTargetFile = targetFile;
                        Files.walk(fileToCopy.toPath())
                                .forEach(source -> {
                                    try {
                                        Files.copy(source, Paths.get(finalTargetFile.getPath() + File.separator + fileToCopy.getName()).resolve(fileToCopy.toPath().relativize(source)));
                                    } catch (IOException e) {
                                        JOptionPane.showMessageDialog(targetPanel, "Error in coping the specified file or directory", "Error", JOptionPane.ERROR_MESSAGE);
                                        e.printStackTrace();
                                    }
                                });
                    } else {
                        Files.copy(fileToCopy.toPath(), Paths.get(targetFile.getPath() + File.separator + fileToCopy.getName()));
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(targetPanel, "Error in coping the specified file or directory", "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
                //update the panel to display the new content
                targetServices.updateCurrentDirectory(currentDirectory);
            }
        }

        return 0;
    }

    public void deleteFile(File targetFile) {
        int res = JOptionPane.showConfirmDialog(targetPanel, "Are you sure you want to delete this?", "Warning", JOptionPane.YES_NO_OPTION);

        //ask for permission to delete the file
        if (res == JOptionPane.YES_OPTION) {

            try {
                if (targetFile.isDirectory()) {
                    Files.walk(targetFile.toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                } else {
                    Files.delete(targetFile.toPath());
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(targetPanel, "Error in deleting the specified file or directory", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
            File currentDirectory = new File(targetFile.getParentFile().getPath());
            //update the panel to display the new content
            targetServices.updateCurrentDirectory(currentDirectory);

        }


    }

    public void renameFile(File targetFile) {
        if (targetFile == null) {
            JOptionPane.showMessageDialog(targetPanel, "No file or directory is selected", "Sorry", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String renameInput = JOptionPane.showInputDialog(targetPanel, "New Name", targetFile.getName());

        //check for files with the same name as the name specified and ask for overwrite
        if (renameInput == null) {
            return;
        } else {
            for (File f : Objects.requireNonNull(targetFile.getParentFile().listFiles())) {

                if (f.getName().equals(renameInput)) {
                    if (f.isDirectory()) {
                        JOptionPane.showMessageDialog(targetPanel, "A directory with the same name already exists. Try again with a different name.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int res = JOptionPane.showConfirmDialog(targetPanel, "A file or directory with the same name already exists. Do you want to overwrite it?", "Warning", JOptionPane.YES_NO_OPTION);
                    if (res == JOptionPane.NO_OPTION) {
                        return;
                    } else break;
                }
            }
        }
        File renamedFile = new File(targetFile.getParentFile(), renameInput);


        if (!targetFile.renameTo(renamedFile)) {
            JOptionPane.showMessageDialog(targetPanel, "Cannot rename file to the specified input text", "Error", JOptionPane.ERROR_MESSAGE);

        }
        targetServices.updateCurrentDirectory(targetFile.getParentFile());
    }

    public void showProperties(File targetFile) {
        StringBuilder propertiesMessage = new StringBuilder();
        propertiesMessage.append("Name: ");
        propertiesMessage.append(targetFile.getName());
        propertiesMessage.append("\n");
        propertiesMessage.append("Path: ");
        propertiesMessage.append(targetFile.getPath());
        propertiesMessage.append("\n");
        propertiesMessage.append("Size: ");
        long size;
        if (targetFile.isDirectory()) {
            size = getDirSize(targetFile);
        } else {
            size = targetFile.length();
        }
        propertiesMessage.append(size);
        propertiesMessage.append(" bytes");
        propertiesMessage.append("\nPermissions");
        JCheckBox readable = new JCheckBox("Read  ");
        JCheckBox writable = new JCheckBox("Write ");
        JCheckBox executable = new JCheckBox("Execute ");

        boolean allowedToWrite;
        boolean initialWritePerm = targetFile.canWrite();
        //attempt to make the file writable to see if you have the permission to do it
        if (initialWritePerm) {
            allowedToWrite = targetFile.setWritable(false);
        } else {
            allowedToWrite = targetFile.setWritable(true);
        }

        //if you do not have the permission, disable the options from the properties
        if (!allowedToWrite) {
            writable.setEnabled(false);
            readable.setEnabled(false);
            executable.setEnabled(false);
        } else {
            targetFile.setWritable(initialWritePerm);

            readable.setSelected(targetFile.canRead());
            writable.setSelected(targetFile.canWrite());
            executable.setSelected(targetFile.canExecute());

            readable.addItemListener(itemEvent -> {
                if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                    targetFile.setReadable(true);
                } else if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
                    targetFile.setReadable(false);
                }
            });

            writable.addItemListener(itemEvent -> {
                if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                    targetFile.setWritable(true);
                } else if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
                    targetFile.setWritable(false);
                }
            });

            executable.addItemListener(itemEvent -> {
                if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                    targetFile.setExecutable(true);
                } else if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
                    targetFile.setExecutable(false);
                }
            });

        }
        //show the dialog with the file info
        Object msg[] = {propertiesMessage, writable, readable, executable};
        JOptionPane.showMessageDialog(targetPanel, msg, "Properties", JOptionPane.INFORMATION_MESSAGE);
    }

    //recursive way to find the size of all contents in a folder
    private long getDirSize(File file) {
        long size = 0;
        if (file.isDirectory()) {
            if (file.listFiles() != null) {
                for (File f : file.listFiles()) {
                    if (f.isDirectory()) {
                        size += getDirSize(f);
                    }
                    else {
                        size += f.length();
                    }
                }
            }
        }
        else {
            size += file.length();
        }
        return size;
    }

}
