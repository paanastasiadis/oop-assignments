package ce326.hw2;

import java.lang.*;

import static ce326.hw2.RGBImage.MAX_COLORDEPTH;

public class RGBPixel {
    private int pixel; //bytes assigned: MSB-red-green-(blue-LSB)

    public RGBPixel(short red, short green, short blue) {
        this.pixel = (red << 16) | (green << 8) | blue;

    }

    public RGBPixel(RGBPixel pixel) {
        this.pixel = pixel.pixel;
    }

    public RGBPixel(YUVPixel pixel) {
        short C = (short) (pixel.getY() - 16);
        short D = (short) (pixel.getU() - 128);
        short E = (short) (pixel.getV() - 128);

        this.setRed(clip((short) ((298 * C + 409 * E + 128) >> 8)));
        this.setGreen(clip((short) ((298 * C - 100 * D - 208 * E + 128) >> 8)));
        this.setBlue(clip((short) ((298 * C + 516 * D + 128) >> 8)));

    }

    /**
     * Helpful method for YUV2RGB conversions.
     * Sets a value < 0 to 0 and a value > 255 to 255. Values in between stay as are.
     *
     * @param value the value to clip
     * @return returns the new clipped value
     */
    private short clip(short value) {
        if (value < 0) {
            value = 0;
        } else if (value > MAX_COLORDEPTH) {
            value = 255;
        }
        return value;
    }

    public short getRed() {
        return (short) ((this.pixel >> (8 * (3 - 1))) & 0xFF);
    }

    public short getGreen() {
        return (short) ((this.pixel >> (8 * (2 - 1))) & 0xFF);
    }

    public short getBlue() {
        return (short) ((this.pixel >> (8 * (1 - 1))) & 0xFF);
    }

    public void setRed(short red) {
        this.pixel = (red << 16) | (this.getGreen() << 8) | this.getBlue();
    }

    public void setGreen(short green) {
        this.pixel = (this.getRed() << 16) | (green << 8) | this.getBlue();
    }

    public void setBlue(short blue) {
        this.pixel = (this.getRed() << 16) | (this.getGreen() << 8) | blue;
    }

    public int getRGB() {
        return this.pixel;
    }

    public void setRGB(int value) {
        this.pixel = value;
    }

    public final void setRGB(short red, short green, short blue) {
        this.setRed(red);
        this.setGreen(green);
        this.setBlue(blue);
    }

    @Override
    public String toString() {
        return this.getRed() +
                " " +
                this.getGreen() +
                " " +
                this.getBlue();

    }
}
