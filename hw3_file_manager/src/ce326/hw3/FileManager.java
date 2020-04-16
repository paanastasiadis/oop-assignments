package ce326.hw3;

public class FileManager {

    private static void createAndShowGUI() {
        //Create and set up the window.
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                 new FileManagerGui();
            }
        });
    }
}
