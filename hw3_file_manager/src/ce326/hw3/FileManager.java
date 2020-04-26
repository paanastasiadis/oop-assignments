package ce326.hw3;

//The main class or the way to run the FileBrowser app
public class FileManager {

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
