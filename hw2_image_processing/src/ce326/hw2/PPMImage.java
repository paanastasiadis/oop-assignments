package ce326.hw2;

import java.io.*;
import java.util.Scanner;

public class PPMImage extends RGBImage {

    public PPMImage(java.io.File file) throws UnsupportedFileFormatException, FileNotFoundException {

        //initialize the parent class with zero values before reading from file
        super(0, 0, 0);
        Scanner sc = new Scanner(file);

        //Check if the given file has a .ppm extension
        PPMFileFilter fileExtension = new PPMFileFilter();
        if (file.isDirectory() || !fileExtension.accept(file)) {
            throw new UnsupportedFileFormatException(fileExtension.getDescription());
        }

        sc.next(); //to get ahead of the string "P3" on PPM format files

        int ppmWidth = sc.nextInt();
        int ppmHeight = sc.nextInt();
        int ppmColorDepth = sc.nextInt();

        super.setImage(ppmWidth, ppmHeight, ppmColorDepth);

        int i = 0;
        int j = 0;
        while (sc.hasNext()) {
            short red = sc.nextShort();
            short green = sc.nextShort();
            short blue = sc.nextShort();
            super.setPixel(i, j, new RGBPixel(red, green, blue));

            if (j == super.getWidth() - 1) {
                j = 0;
                i++;
            } else {
                j++;
            }
        }
    }

    public PPMImage(RGBImage img) {
        super(img);
    }

    public PPMImage(YUVImage img) {
        super(img);
    }

    public void toFile(java.io.File file) {

        try (PrintWriter outPPMFile = new PrintWriter(file)) {
            outPPMFile.print(this.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder PPMStr = new StringBuilder();
        PPMStr.append("P3\n");
        PPMStr.append(super.getWidth());
        PPMStr.append(" ");
        PPMStr.append(super.getHeight());
        PPMStr.append("\n");
        PPMStr.append(super.getColorDepth());
        PPMStr.append("\n");

        for (int i = 0; i < super.getHeight(); i++) {
            for (int j = 0; j < super.getWidth(); j++) {
                PPMStr.append(super.getPixel(i, j).toString());
                PPMStr.append("\n");
            }
        }
        return PPMStr.toString();
    }
}
