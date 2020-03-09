package ce326.hw2;

public class RGBImage implements Image {
    private RGBPixel[][] image;
    private int imageWidth;
    private int imageHeight;
    private int imageColorDepth;
    public static final int MAX_COLORDEPTH = 255;


    public RGBImage(int width, int height, int colordepth) {
        setImage(width, height, colordepth);
    }

    public RGBImage(RGBImage copyImg) {
        setImage(copyImg.imageWidth, copyImg.imageHeight, copyImg.imageColorDepth, copyImg.image);
    }

    public void setImage(int width, int height, int colordepth) {
        this.imageHeight = height;
        this.imageWidth = width;
        this.image = new RGBPixel[height][width];
        this.imageColorDepth = colordepth;
    }

    public void setImage(int width, int height, int colordepth, RGBPixel[][] pixelArray) {
        this.imageHeight = height;
        this.imageWidth = width;
        this.imageColorDepth = colordepth;
        this.image = pixelArray;
    }

    public int getHeight() {
        return this.imageHeight;
    }

    public int getWidth() {
        return this.imageWidth;
    }

    public int getColorDepth() {
        return this.imageColorDepth;
    }

    public RGBPixel getPixel(int row, int col) {
        return this.image[row][col];
    }

    public void setPixel(int row, int col, RGBPixel pixelRGBPixel) {
        this.image[row][col] = pixelRGBPixel;
    }

    @Override
    public void grayscale() {
        double grayValue;
        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                grayValue = this.getPixel(i, j).getRed() * (0.3) +
                        this.getPixel(i, j).getGreen() * (0.59) +
                        this.getPixel(i, j).getBlue() * (0.11);

                this.getPixel(i, j).setRGB((short) grayValue, (short) grayValue, (short) grayValue);
            }

        }
    }

    @Override
    public void doublesize() {
        int doubleWidth = 2 * this.getWidth();
        int doubleHeight = 2 * this.getHeight();
        RGBImage doubledImage = new RGBImage(doubleWidth, doubleHeight, MAX_COLORDEPTH);


        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                doubledImage.setPixel(2 * i, 2 * j, this.getPixel(i, j));
                doubledImage.setPixel(2 * i + 1, 2 * j, this.getPixel(i, j));
                doubledImage.setPixel(2 * i, 2 * j + 1, this.getPixel(i, j));
                doubledImage.setPixel(2 * i + 1, 2 * j + 1, this.getPixel(i, j));
            }
        }
        this.setImage(doubleWidth, doubleHeight, MAX_COLORDEPTH, doubledImage.image);
    }

    @Override
    public void halfsize() {
        int halfWidth = this.getWidth() / 2;
        int halfHeight = this.getHeight() / 2;
        RGBImage halfImage = new RGBImage(halfWidth, halfHeight, MAX_COLORDEPTH);

        for (int i = 0; i < halfHeight; i++) {
            for (int j = 0; j < halfWidth; j++) {
                short red = (short) ((this.getPixel(2 * i, 2 * j).getRed()
                        + this.getPixel(2 * i + 1, 2 * j).getRed()
                        + this.getPixel(2 * i, 2 * j + 1).getRed()
                        + this.getPixel(2 * i + 1, 2 * j + 1).getRed()) / 4);

                short green = (short) ((this.getPixel(2 * i, 2 * j).getGreen()
                        + this.getPixel(2 * i + 1, 2 * j).getGreen()
                        + this.getPixel(2 * i, 2 * j + 1).getGreen()
                        + this.getPixel(2 * i + 1, 2 * j + 1).getGreen()) / 4);

                short blue = (short) ((this.getPixel(2 * i, 2 * j).getBlue()
                        + this.getPixel(2 * i + 1, 2 * j).getBlue()
                        + this.getPixel(2 * i, 2 * j + 1).getBlue()
                        + this.getPixel(2 * i + 1, 2 * j + 1).getBlue()) / 4);

                halfImage.setPixel(i, j, new RGBPixel(red, green, blue));

            }

        }
        this.setImage(halfWidth, halfHeight, MAX_COLORDEPTH, halfImage.image);
    }

    @Override
    public void rotateClockwise() {
        int rotatedHeight = this.getWidth();
        int rotatedWidth = this.getHeight();
        RGBImage rotatedImage = new RGBImage(rotatedWidth, rotatedHeight, MAX_COLORDEPTH);

        for (int i = 0; i < this.getWidth(); i++) {
            for (int j = 0; j < this.getHeight(); j++) {
                rotatedImage.setPixel(i, j, this.getPixel(this.getHeight() - j - 1, i));

            }

        }
        this.setImage(rotatedWidth, rotatedHeight, MAX_COLORDEPTH, rotatedImage.image);
    }
}
