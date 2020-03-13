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
        setImage(copyImg.getWidth(), copyImg.getHeight(), copyImg.getColorDepth(), copyImg.getImage());
    }

    public RGBImage(YUVImage img) {
        RGBPixel[][] YUV2RGBArray = new RGBPixel[img.getHeight()][img.getWidth()];
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                YUV2RGBArray[i][j] = new RGBPixel(img.getPixel(i, j));
            }
        }
        setImage(img.getWidth(), img.getHeight(), MAX_COLORDEPTH, YUV2RGBArray);
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

    public RGBPixel[][] getImage() {
        return image;
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
        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                double grayValueFloating = this.getPixel(i, j).getRed() * (0.3) +
                        this.getPixel(i, j).getGreen() * (0.59) +
                        this.getPixel(i, j).getBlue() * (0.11);
                int grayValue = (int) grayValueFloating;
                this.getPixel(i, j).setRGB((short) grayValue, (short) grayValue, (short) grayValue);
            }

        }
    }

    @Override
    public void doublesize() {

        RGBImage doubledImage = new RGBImage(2 * this.getWidth(), 2 * this.getHeight(),
                this.getColorDepth());

        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                doubledImage.setPixel(2 * i, 2 * j, this.getPixel(i, j));
                doubledImage.setPixel(2 * i + 1, 2 * j, this.getPixel(i, j));
                doubledImage.setPixel(2 * i, 2 * j + 1, this.getPixel(i, j));
                doubledImage.setPixel(2 * i + 1, 2 * j + 1, this.getPixel(i, j));
            }
        }
        this.setImage(doubledImage.getWidth(), doubledImage.getHeight(),
                doubledImage.getColorDepth(), doubledImage.getImage());
    }

    @Override
    public void halfsize() {
        RGBImage halfImage = new RGBImage(this.getWidth() / 2, this.getHeight() / 2,
                this.getColorDepth());

        for (int i = 0; i < halfImage.getHeight(); i++) {
            for (int j = 0; j < halfImage.getWidth(); j++) {
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
        this.setImage(halfImage.getWidth(), halfImage.getHeight(),
                halfImage.getColorDepth(), halfImage.getImage());
    }

    @Override
    public void rotateClockwise() {
        RGBImage rotatedImage = new RGBImage(this.getHeight(), this.getWidth(), this.getColorDepth());

        for (int i = 0; i < this.getWidth(); i++) {
            for (int j = 0; j < this.getHeight(); j++) {
                rotatedImage.setPixel(i, j, this.getPixel(this.getHeight() - j - 1, i));

            }

        }
        this.setImage(rotatedImage.getWidth(), rotatedImage.getHeight(),
                rotatedImage.getColorDepth(), rotatedImage.getImage());
    }
}
