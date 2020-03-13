package ce326.hw2;

public class YUVPixel {
    private int pixel; //bytes assigned: MSB-Y-U-(V-LSB)

    public YUVPixel(short Y, short U, short V) {
        this.pixel = (Y << 16) | (U << 8) | V;
    }

    public YUVPixel(YUVPixel pixel) {
        this.pixel = pixel.pixel;
    }

    public YUVPixel(RGBPixel pixel) {
        this.setY((short) (((66 * pixel.getRed() + 129 * pixel.getGreen() + 25 * pixel.getBlue() + 128) >> 8) + 16));
        this.setU((short) (((-38 * pixel.getRed() - 74 * pixel.getGreen() + 112 * pixel.getBlue() + 128) >> 8) + 128));
        this.setV((short) (((112 * pixel.getRed() - 94 * pixel.getGreen() - 18 * pixel.getBlue() + 128) >> 8) + 128));
    }

    public short getY() {
        return ((short) ((this.pixel >> (8 * (3 - 1))) & 0xFF));
    }

    public short getU() {
        return ((short) ((this.pixel >> (8 * (2 - 1))) & 0xFF));
    }

    public short getV() {
        return ((short) ((this.pixel >> (8 * (1 - 1))) & 0xFF));
    }

    public void setY(short Y) {
        this.pixel = (Y << 16) | (this.getU() << 8) | this.getV();
    }

    public void setU(short U) {
        this.pixel = (this.getY() << 16) | (U << 8) | this.getV();
    }

    public void setV(short V) {
        this.pixel = (this.getY() << 16) | (this.getU() << 8) | V;
    }

    @Override
    public String toString() {
        return this.getY() +
                " " +
                this.getU() +
                " " +
                this.getV();
    }
}
