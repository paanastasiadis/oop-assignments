package ce326.hw2;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class PPMImage extends RGBImage {


    public PPMImage(java.io.File file) throws UnsupportedFileFormatException, FileNotFoundException {

        super(0, 0, 0);
        Scanner sc = new Scanner(file);

            if (!isPPMExtension(file)) {
                throw new UnsupportedFileFormatException(file.getName());
            }

            sc.next();

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
    private boolean isPPMExtension(java.io.File file) {
        if (file == null) {
            return false;
        }
        String name = file.getName();
        int i = name.lastIndexOf('.');
        if (i > 0) {
            return name.substring(i + 1).equals("ppm");
        }
        return false;
    }

    public void toFile(java.io.File file) {

        //TODO Search for alternative method of truncation
        if (file.exists() && !file.isDirectory()) {
            file.delete();
        }
        //TODO Search for optimal way of writing to file
        try (FileOutputStream out = new FileOutputStream(file)) {
            ByteBuffer buffer = ByteBuffer.allocate(this.toString().length());
            buffer.put(this.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));
            out.write(buffer.array());

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
