package ce326.hw2;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static ce326.hw2.RGBImage.MAX_COLORDEPTH;

public class Histogram {
    private int[] histogramArray;
    private int[] equalizedHistogramArray;
    private int totalImgPixels;
    public static final int MAX_YUV_LUMINOSITY = 235;

    public Histogram(YUVImage img) {
        histogramArray = new int[MAX_COLORDEPTH + 1];
        totalImgPixels = img.getHeight() * img.getWidth();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                short luminosity = img.getPixel(i, j).getY();
                histogramArray[luminosity]++;
            }
        }
    }

    public String toString() {
        StringBuilder luminosityStr = new StringBuilder();

        for (int value : histogramArray) {
            int thousands = value / 1000;
            int mod = value % 1000;
            int hundreds = mod / 100;
            int units = mod % 100;

            luminosityStr.append(value);
            luminosityStr.append(" ");
            for (int j = 0; j < thousands; j++) {
                luminosityStr.append('#');
            }
            for (int j = 0; j < hundreds; j++) {
                luminosityStr.append('$');
            }
            for (int j = 0; j < units; j++) {
                luminosityStr.append('*');
            }
            luminosityStr.append('\n');
        }
        return luminosityStr.toString();
    }

    public void toFile(java.io.File file) {

        try (PrintWriter outHistogram = new PrintWriter(file)) {
            outHistogram.print(this.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void equalize() {
        double[] PMFArray = new double[MAX_COLORDEPTH + 1];
        double[] CDFArray = new double[MAX_COLORDEPTH + 1];
        equalizedHistogramArray = new int[MAX_COLORDEPTH + 1];

        for (int i = 0; i < histogramArray.length; i++) {
            PMFArray[i] = ((double) histogramArray[i] / (double) totalImgPixels);
        }
        CDFArray[0] = PMFArray[0];
        for (int i = 1; i < histogramArray.length; i++) {
            CDFArray[i] = CDFArray[i - 1] + PMFArray[i];
        }

        for (int i = 0; i < histogramArray.length; i++) {
            equalizedHistogramArray[i] = (int) (CDFArray[i] * MAX_YUV_LUMINOSITY);
        }
    }

    public short getEqualizedLuminosity(int luminosity) {
        return (short)equalizedHistogramArray[luminosity];
    }
}


