import ij.ImagePlus;
import ij.IJ;
import ij.process.ImageProcessor;
import ij.process.ByteProcessor;
import ij.plugin.filter.PlugInFilter;
import java.awt.Color;

public class All_Layers implements PlugInFilter {
	
	public double curve(int index, int x) {
		double quad[][] = {
				{-9.    , 1.    ,  0.    },
				{-4.0146, 1.1917, -0.0009},
				{ 4.0264, 1.2262, -0.0011},
				{12.067 , 1.2608, -0.0013},
				{14.8   , 1.5713, -0.0026}
		};

		return quad[index][0] + quad[index][1] * x + quad[index][2] * x * x;
	}

	public int setup(String args, ImagePlus im) {
		return DOES_RGB + DOES_STACKS;
	}

	public void run(ImageProcessor inputIP) {
		int width = inputIP.getWidth(), height = inputIP.getHeight();
		int r, g, b;
		double rb;
		Color color;

		for (int row = 0; row < height; row++)
			for (int col = 0; col < width; col++) {
				color = new Color(inputIP.getPixel(col, row));
				r = color.getRed();
				g = color.getGreen();
				b = color.getBlue();
				rb = (r + b) / 2.;

				if (b < g && g < r)
					if (rb >= curve(0, g) && rb <= curve(1, g))
						inputIP.putPixel(col, row, 65280); // GREEN
					else if (rb >= curve(1, g) && rb <= curve(2, g))
						inputIP.putPixel(col, row, 16776960); // YELLOW
					else if (rb >= curve(2, g) && rb <= curve(3, g))
						inputIP.putPixel(col, row, 16711680); // RED
					else if (rb >= curve(3, g) && rb <= curve(4, g))
						inputIP.putPixel(col, row, 16711935); // MAGENTA
		}
	}
}
