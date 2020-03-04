package ce326.hw2;

import java.lang.*;

public class RGBPixel {
    private int pixel; //bytes: msb-red-green-blue(lsb)

    public RGBPixel(short red, short green, short blue) {
        this.pixel = (blue << 16) | (green << 8) | red;

    }

    public short getRed() {
        short redValue;
        //TODO See if you can program it without typecast
        redValue = (short) ((this.pixel >> (8 * (1 - 1))) & 0xFF);
        return redValue;
    }

    public short getGreen() {
        short greenValue;
        greenValue = (short) ((this.pixel >> (8 * (2 - 1))) & 0xFF);
        return greenValue;
    }

    public short getBlue() {
        short blueValue;
        blueValue = (short) ((this.pixel >> (8 * (3 - 1))) & 0xFF);
        return blueValue;
    }

    public void setRed(short red) {
        //TODO See if there is a more optimal way for this
        this.pixel = (this.getBlue() << 16) | (this.getGreen() << 8) | red;
    }

    public void setGreen(short green) {
        this.pixel = (this.getBlue() << 16) | (green << 8) | this.getRed();
    }

    public void setBlue(short blue) {
        this.pixel = (blue << 16) | (this.getGreen() << 8) | this.getRed();
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
