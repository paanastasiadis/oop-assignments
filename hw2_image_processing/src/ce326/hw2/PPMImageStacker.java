package ce326.hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PPMImageStacker {

    private List<PPMImage> stackerImagesList;
    private PPMImage finalImage;


    public PPMImageStacker(java.io.File dir) throws FileNotFoundException, UnsupportedFileFormatException  {

        if (!dir.exists()) {
            System.err.println("[ERROR] Directory " + dir.getName() + " does not exist!");
            throw new FileNotFoundException("[ERROR] Directory " + dir.getName() + " does not exist!");
        } else if (dir.exists() && (!dir.isDirectory())) {
            System.err.println("[ERROR] " + dir.getName() + " is not a directory!");
            throw new FileNotFoundException("[ERROR] " + dir.getName() + " is not a directory!");
        }

        stackerImagesList = new ArrayList<>();
        File[] files = dir.listFiles();

        for (File f : files) {
            if( f.isFile() && f.getName().substring(f.getName().length()-4).equals(".ppm")) {
                PPMImage fileImage = new PPMImage(f);
                stackerImagesList.add(fileImage);
            }
        }
        if (stackerImagesList.isEmpty()) {
            throw new FileNotFoundException("No compatible .ppm files found inside the directory!");
        }
    }

    public void stack() {

        finalImage = new PPMImage(stackerImagesList.get(0));

        for (int i = 0; i < finalImage.getHeight(); i++) {
            for (int j = 0; j < finalImage.getWidth(); j++) {

                short totalRed = finalImage.getPixel(i, j).getRed();
                short totalGreen = finalImage.getPixel(i, j).getGreen();
                short totalBlue = finalImage.getPixel(i, j).getBlue();

                for (int k = 1; k < stackerImagesList.size(); k++) {
                    PPMImage currentImage = stackerImagesList.get(k);
                    short currentRed = currentImage.getPixel(i, j).getRed();
                    totalRed += currentRed;

                    short currentGreen = currentImage.getPixel(i, j).getGreen();
                    totalGreen += currentGreen;

                    short currentBlue = currentImage.getPixel(i, j).getBlue();
                    totalBlue += currentBlue;
                }

                finalImage.getPixel(i, j).setRed((short) (totalRed / stackerImagesList.size()));
                finalImage.getPixel(i, j).setGreen((short) (totalGreen / stackerImagesList.size()));
                finalImage.getPixel(i, j).setBlue((short) (totalBlue / stackerImagesList.size()));

            }
        }
    }

    //TODO See if it is needed to throw exception for null occasion
    public PPMImage getStackedImage() {
        return finalImage;
    }
}
