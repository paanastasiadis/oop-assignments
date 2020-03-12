package ce326.hw2;

public interface Image {

    /* converts the image colors to black and white */
    public void grayscale();

    /* doubles the size of the image */
    public void doublesize();

    /* cuts in half the size of the image */
    public void halfsize();

    /* rotate the image 90 degrees clockwise */
    public void rotateClockwise();
}
