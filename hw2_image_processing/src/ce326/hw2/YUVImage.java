package ce326.hw2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class YUVImage {
    private YUVPixel[][] image;
    private int imageWidth;
    private int imageHeight;

    public YUVImage(int width, int height) {
        setDefaultImage(width, height);
    }

    public YUVImage(YUVImage copyImg) {
        setImage(copyImg.getHeight(), copyImg.getWidth(), copyImg.getImage());
    }

    public YUVImage(RGBImage RGBImg) {
        setImage(RGBImg.getWidth(), RGBImg.getHeight());
        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                setPixel(i, j, new YUVPixel(RGBImg.getPixel(i, j)));
            }
        }
    }

    public YUVImage(java.io.File file) throws FileNotFoundException, UnsupportedFileFormatException {

        Scanner sc = new Scanner(file);

        //Check if the given file has a .yuv extension
        YUVFileFilter fileExtension = new YUVFileFilter();
        if (file.isDirectory() || !fileExtension.accept(file)) {
            throw new UnsupportedFileFormatException(fileExtension.getDescription());
        }

        sc.next(); //to get ahead of the string "YUV3" on .yuv format files

        int yuvWidth = sc.nextInt();
        int yuvHeight = sc.nextInt();

        this.setImage(yuvWidth, yuvHeight);

        int i = 0;
        int j = 0;
        while (sc.hasNext()) {
            short Y = sc.nextShort();
            short U = sc.nextShort();
            short V = sc.nextShort();
            this.setPixel(i, j, new YUVPixel(Y, U, V));

            if (j == this.getWidth() - 1) {
                j = 0;
                i++;
            } else {
                j++;
            }
        }
    }

    /**
     * Sets the default YUV image with every pixel having YUV = {16,128,128}.
     */
    public void setDefaultImage(int width, int height) {

        setImage(width, height);
        YUVPixel defaultPixel = new YUVPixel((short) 16, (short) 128, (short) 128);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                setPixel(i, j, defaultPixel);
            }
        }
    }

    /**
     * Initialize an empty YUV image by setting its parameters.
     * Allocates space for YUVPixel array field "image".
     */
    public void setImage(int width, int height) {
        this.imageHeight = height;
        this.imageWidth = width;
        this.image = new YUVPixel[height][width];

    }

    /**
     * Set an image with parameters from another existing image.
     */
    public void setImage(int width, int height, YUVPixel[][] pixelArray) {
        this.imageHeight = height;
        this.imageWidth = width;
        this.image = pixelArray;
    }

    public int getHeight() {
        return this.imageHeight;
    }

    public int getWidth() {
        return this.imageWidth;
    }

    public YUVPixel[][] getImage() {
        return image;
    }

    public void setPixel(int row, int col, YUVPixel pixelYUVPixel) {
        this.image[row][col] = pixelYUVPixel;
    }

    public YUVPixel getPixel(int row, int col) {
        return this.image[row][col];
    }

    public String toString() {
        StringBuilder YUVStr = new StringBuilder();
        YUVStr.append("YUV3\n");
        YUVStr.append(this.getWidth());
        YUVStr.append(" ");
        YUVStr.append(this.getHeight());
        YUVStr.append("\n");

        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                YUVStr.append(this.getPixel(i, j).toString());
                YUVStr.append("\n");
            }
        }
        return YUVStr.toString();
    }

    public void toFile(java.io.File file) {

        try (PrintWriter outYUVfile = new PrintWriter(file)) {
            outYUVfile.print(this.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Equalization of the YUV Image
     */
    public void equalize() {

        Histogram imgHistogram = new Histogram(this);
        imgHistogram.equalize();

        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                short oldYValue = this.getPixel(i, j).getY();
                this.getPixel(i, j).setY(imgHistogram.getEqualizedLuminosity(oldYValue));
            }
        }
    }
}
