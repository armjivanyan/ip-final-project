import ij.ImagePlus;
import ij.IJ;
import ij.process.ImageProcessor;
import ij.process.ByteProcessor;
import ij.plugin.filter.PlugInFilter;
import java.awt.Color;

public class Binary_Layers implements PlugInFilter {
	ImagePlus inputImage;

	public double curve(int index, int x) {
		double quad[][] = {
				{-9     , 1.    ,  0.    },
				{-4.0146, 1.1917, -0.0009},
				{ 4.0264, 1.2262, -0.0011},
				{12.067 , 1.2608, -0.0013},
				{14.8   , 1.5713, -0.0026}
		};

		return quad[index][0] + quad[index][1] * x + quad[index][2] * x * x;
	}

	public int setup(String args, ImagePlus im) {
		inputImage = im;
		return DOES_RGB;
	}

	public void run(ImageProcessor inputIP) {
//		String inputTitle = inputImage.getShortTitle();
		String inputTitle = "";
		int width = inputIP.getWidth(), height = inputIP.getHeight();
		int r, g, b;
		double rb;
		Color color;

		ImageProcessor outputIP[] = new ImageProcessor[4];
		for (int i = 0; i < outputIP.length; i++) {
			outputIP[i] = new ByteProcessor(width, height);
			outputIP[i].setValue(255);
			outputIP[i].fill();
		}

		for (int row = 0; row < height; row++)
			for (int col = 0; col < width; col++) {
				color = new Color(inputIP.getPixel(col, row));
				r = color.getRed();
				g = color.getGreen();
				b = color.getBlue();
				rb = (r + b) / 2.;

				if (b < g && g < r)
					if (rb >= curve(0, g) && rb <= curve(1, g))
						outputIP[0].putPixel(col, row, 0);
					else if (rb >= curve(1, g) && rb <= curve(2, g))
						outputIP[1].putPixel(col, row, 0);
					else if (rb >= curve(2, g) && rb <= curve(3, g))
						outputIP[2].putPixel(col, row, 0);
					else if (rb >= curve(3, g) && rb <= curve(4, g))
						outputIP[3].putPixel(col, row, 0);
		}

		for (int i = 0; i < outputIP.length; i++)
			(new ImagePlus("layer_" + i + "_" + inputTitle, outputIP[i])).show(); 
	}
}
