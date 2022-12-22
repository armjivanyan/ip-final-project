import ij.plugin.filter.PlugInFilter;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import ij.process.ColorProcessor;
import ij.process.ByteProcessor;
import ij.IJ;
import java.awt.Color;

public class LowRes implements PlugInFilter {
	String title;
	int width;
	int height;
	public int setup(String args, ImagePlus img) {
		title = img.getShortTitle();
        width = img.getWidth();
		height = img.getHeight();
		return DOES_ALL;
	}
	public void run(ImageProcessor inputIP) {
        int newHeight = (int) Math.ceil((float) height / 5);
        int newWidth = (int) Math.ceil((float) width / 5);
        ImageProcessor outputIP = new ColorProcessor(newWidth , newHeight);
        for (int i = 0; i < newWidth; i++){
            for (int j = 0; j < newHeight; j++) {
                int r = 0, g = 0, b = 0;
                for (int innerI = 5 * i; innerI < 5 * i + 5; innerI++) {
                    for (int innerJ = 5 * j; innerJ < 5 * j + 5; innerJ++) {
                        if (innerI < width && innerJ < height) {
                            Color pixelColor = new Color(inputIP.getPixel(innerI, innerJ));
                            r += pixelColor.getRed();
                            b += pixelColor.getBlue();
                            g += pixelColor.getGreen();
                        } 
                    }
                }
                outputIP.putPixel(i, j, new int[]{r / 25, g / 25, b / 25});
            }
        }
	    (new ImagePlus("low_res_" + title, outputIP)).show();
	}
}
