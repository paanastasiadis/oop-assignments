package ce326.hw2;

public class UnsupportedFileFormatException extends java.lang.Exception {

    public UnsupportedFileFormatException() {}

    public UnsupportedFileFormatException(String filename) {
        super("File " + filename + " does not have the correct format.");
    }

}
