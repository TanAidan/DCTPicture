import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.text.*;
import java.util.*;
import java.util.List; // resolves problem with java.awt.List and java.util.List

/**
 * A class that represents a picture. This class inherits from SimplePicture and
 * allows the student to add functionality to the Picture class.
 * 
 * @author Barbara Ericson ericson@cc.gatech.edu
 */
public class Picture extends SimplePicture {
	///////////////////// constructors //////////////////////////////////

	/**
	 * Constructor that takes no arguments
	 */
	public Picture() {
		/*
		 * not needed but use it to show students the implicit call to super() child
		 * constructors always call a parent constructor
		 */
		super();
	}

	/**
	 * Constructor that takes a file name and creates the picture
	 * 
	 * @param fileName the name of the file to create the picture from
	 */
	public Picture(String fileName) {
		// let the parent class handle this fileName
		super(fileName);
	}

	/**
	 * Constructor that takes the width and height
	 * 
	 * @param height the height of the desired picture
	 * @param width  the width of the desired picture
	 */
	public Picture(int height, int width) {
		// let the parent class handle this width and height
		super(width, height);
	}

	/**
	 * Constructor that takes a picture and creates a copy of that picture
	 * 
	 * @param copyPicture the picture to copy
	 */
	public Picture(Picture copyPicture) {
		// let the parent class do the copy
		super(copyPicture);
	}

	/**
	 * Constructor that takes a buffered image
	 * 
	 * @param image the buffered image to use
	 */
	public Picture(BufferedImage image) {
		super(image);
	}

	////////////////////// methods ///////////////////////////////////////

	/**
	 * Method to return a string with information about this picture.
	 * 
	 * @return a string with information about the picture such as fileName, height
	 *         and width.
	 */
	public String toString() {
		String output = "Picture, filename " + getFileName() + " height " + getHeight() + " width " + getWidth();
		return output;

	}

	/** Method to set the blue to 0 */
	public void zeroBlue() {
		Pixel[][] pixels = this.getPixels2D();
		for (Pixel[] rowArray : pixels) {
			for (Pixel pixelObj : rowArray) {
				pixelObj.setBlue(0);
			}
		}
	}

	/**
	 * Method that mirrors the picture around a vertical mirror in the center of the
	 * picture from left to right
	 */
	public void mirrorVertical() {
		Pixel[][] pixels = this.getPixels2D();
		Pixel leftPixel = null;
		Pixel rightPixel = null;
		int width = pixels[0].length;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < width / 2; col++) {
				leftPixel = pixels[row][col];
				rightPixel = pixels[row][width - 1 - col];
				rightPixel.setColor(leftPixel.getColor());
			}
		}
	}

	/** Mirror just part of a picture of a temple */
	public void mirrorTemple() {
		int mirrorPoint = 276;
		Pixel leftPixel = null;
		Pixel rightPixel = null;
		int count = 0;
		Pixel[][] pixels = this.getPixels2D();

		// loop through the rows
		for (int row = 27; row < 97; row++) {
			// loop from 13 to just before the mirror point
			for (int col = 13; col < mirrorPoint; col++) {

				leftPixel = pixels[row][col];
				rightPixel = pixels[row][mirrorPoint - col + mirrorPoint];
				rightPixel.setColor(leftPixel.getColor());
			}
		}
	}

	/**
	 * copy from the passed fromPic to the specified startRow and startCol in the
	 * current picture
	 * 
	 * @param fromPic  the picture to copy from
	 * @param startRow the start row to copy to
	 * @param startCol the start col to copy to
	 */
	public void copy(Picture fromPic, int startRow, int startCol) {
		Pixel fromPixel = null;
		Pixel toPixel = null;
		Pixel[][] toPixels = this.getPixels2D();
		Pixel[][] fromPixels = fromPic.getPixels2D();
		for (int fromRow = 0, toRow = startRow; fromRow < fromPixels.length
				&& toRow < toPixels.length; fromRow++, toRow++) {
			for (int fromCol = 0, toCol = startCol; fromCol < fromPixels[0].length
					&& toCol < toPixels[0].length; fromCol++, toCol++) {
				fromPixel = fromPixels[fromRow][fromCol];
				toPixel = toPixels[toRow][toCol];
				toPixel.setColor(fromPixel.getColor());
			}
		}
	}

	/** Method to create a collage of several pictures */
	public void createCollage() {
		Picture flower1 = new Picture("flower1.jpg");
		Picture flower2 = new Picture("flower2.jpg");
		this.copy(flower1, 0, 0);
		this.copy(flower2, 100, 0);
		this.copy(flower1, 200, 0);
		Picture flowerNoBlue = new Picture(flower2);
		flowerNoBlue.zeroBlue();
		this.copy(flowerNoBlue, 300, 0);
		this.copy(flower1, 400, 0);
		this.copy(flower2, 500, 0);
		this.mirrorVertical();
		this.write("collage.jpg");
	}

	/**
	 * Method to show large changes in color
	 * 
	 * @param edgeDist the distance for finding edges
	 */
	public void edgeDetection(int edgeDist) {
		Pixel leftPixel = null;
		Pixel rightPixel = null;
		Pixel[][] pixels = this.getPixels2D();
		Color rightColor = null;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length - 1; col++) {
				leftPixel = pixels[row][col];
				rightPixel = pixels[row][col + 1];
				rightColor = rightPixel.getColor();
				if (leftPixel.colorDistance(rightColor) > edgeDist)
					leftPixel.setColor(Color.BLACK);
				else
					leftPixel.setColor(Color.WHITE);
			}
		}
	}
//-----------------------------------------------------------------------------------------------------------
// Dct performs the discrete cosine theorem on 8x8 blocks of pixels on the picture and returns the values in a 3-d RGB array
	public double[][][] dct() {
		Pixel[][] currPixels = this.getPixels2D();
		int i, j, k, l;
		int m2 = currPixels.length;
		int n2 = currPixels[0].length;
		int n = 8, m = 8;
		System.out.println("width: " + m);
		System.out.println("height: " + n);

		// dct will store the discrete cosine transform
		double m1 = Math.sqrt(8);
		double n1 = Math.sqrt(8);
		double var2 = Math.sqrt(2);
		double ci, cj, dctR, dctG, dctB, sumR, sumB, sumG;
		double[][][] colorArray = new double[3][m2][n2];
		/*
		 * for (i = 0; i < m2; i++) { for (j = 0; j < n2; j++) System.out.printf("%f\t",
		 * currPixels[i][j].getAverage()); System.out.println(); }
		 */
		for (int r = 0; r < m2; r += 8) { // for loops breaking up the image into 8x8 blocks
			for (int c = 0; c < n2; c += 8) {

				for (i = 0; i < 8; i++) {
					for (j = 0; j < 8; j++) {

						// ci and cj depends on frequency as well as
						// number of row and columns of specified matrix
						if (i == 0)
							ci = 1 / m1;
						else
							ci = var2 / m1;

						if (j == 0)
							cj = 1 / n1;
						else
							cj = var2 / n1;

						// sum will temporarily store the sum of
						// cosine signals
						sumR = 0;
						sumB = 0;
						sumG = 0;
						for (k = 0; k < 8; k++) {
							for (l = 0; l < 8; l++) {
								// dct for red pixels
								dctR = currPixels[k + r][l + c].getRed() * Math.cos((2 * k + 1) * i * Math.PI / (2 * m))
										* Math.cos((2 * l + 1) * j * Math.PI / (2 * n));
								sumR = sumR + dctR;
								// dct for green pixels;
								dctG = currPixels[k + r][c + l].getGreen()
										* Math.cos((2 * k + 1) * i * Math.PI / (2 * m))
										* Math.cos((2 * l + 1) * j * Math.PI / (2 * n));
								sumG = sumG + dctG;
								// dct for blue pixels;
								dctB = currPixels[k + r][l + c].getBlue()
										* Math.cos((2 * k + 1) * i * Math.PI / (2 * m))
										* Math.cos((2 * l + 1) * j * Math.PI / (2 * n));
								sumB = sumB + dctB;

							}
						}
						colorArray[0][i + r][j + c] = ((ci * cj * sumR));
						colorArray[1][i + r][j + c] = ((ci * cj * sumG));
						colorArray[2][i + r][j + c] = ((ci * cj * sumB));

						System.out.println("coordinates: x: " + (i + r) + " y: " + (j + c));

					}

				}
			}
		}

		// --------------------------------------------------------------------------------------------------------------
		// takes an array and seperates it into 8x8 arrays - returns a 2-d array

		/*
		 * int RCounter = 0, CCounter = 0; int newArrayCounter = 0; for(int i = 0 ;
		 * i<currPixels.length; i++) { for(int j=0; j<currPixels[0].length; j++) {
		 * if(CCounter%8==0) { break; } else {
		 * 
		 * newPixels[newArrayCounter] } }
		 * 
		 * }
		 */
		return colorArray;
	}

	// -------------------------------------------------------------------------------------------------------------
	// inverse dct

	public void idct(double[][][] colorArray) {

		int i, j, k, l;
		// dct will store the discrete cosine transform

		Pixel[][] matrix = this.getPixels2D();
		int m1 = matrix.length;
		int n1 = matrix[0].length;

		int m = 8, n = 8;
		double ck, cl, dctR, dctG, dctB, sumR, sumB, sumG;
		for (int r = 0; r < m1; r += 8) { // for loops breaking up the image into 8x8 blocks
			for (int c = 0; c < n1; c += 8) {

				for (i = 0; i < 8; i++) {
					for (j = 0; j < 8; j++) {
						// ci and cj depends on frequency as well as
						// number of row and columns of specified matrix

						// sum will temporarily store the sum of
						// cosine signals

						sumR = 0;
						sumG = 0;
						sumB = 0;

						for (k = 0; k < 8; k++) {
							for (l = 0; l < 8; l++) {

								if (k == 0)
									ck = 1 / Math.sqrt(8);
								else
									ck = Math.sqrt(2) / Math.sqrt(8);

								if (l == 0)
									cl = 1 / Math.sqrt(8);
								else
									cl = Math.sqrt(2) / Math.sqrt(8);

								dctR = colorArray[0][k + r][l + c] * Math.cos((2 * i + 1) * k * Math.PI / (2 * m))
										* Math.cos((2 * j + 1) * l * Math.PI / (2 * n));
								sumR = sumR + ck * cl * dctR;

								dctG = colorArray[1][k + r][l + c] * Math.cos((2 * i + 1) * k * Math.PI / (2 * m))
										* Math.cos((2 * j + 1) * l * Math.PI / (2 * n));
								sumG = sumG + ck * cl * dctG;

								dctB = colorArray[2][k + r][l + c] * Math.cos((2 * i + 1) * k * Math.PI / (2 * m))
										* Math.cos((2 * j + 1) * l * Math.PI / (2 * n));
								sumB = sumB + ck * cl * dctB;
							}
						}
						if (sumR >= 255) {
							sumR = 255;
						}
						if (sumG >= 255) {
							sumG = 255;
						}
						if (sumB >= 255) {
							sumB = 255;
						}

						// System.out.println("running inverse dct");
						matrix[i + r][j + c].setRed((int) sumR);
						matrix[i + r][j + c].setGreen((int) sumG);
						matrix[i + r][j + c].setBlue((int) sumB);
					}
				}
			}
		}
		for (i = 0; i < m1; i++) {
			for (j = 0; j < n1; j++)
				System.out.printf("%f\t", matrix[i][j].getAverage());
			System.out.println();
		}
	}

	// --------------------------------------------------------------------------------------------------------------------------
	//inverse idct method that shows the picture, but uses 3 seperate 8x8 array to store the colors and zero part of the array out...
	// to blur it
	public void idctBlur(double[][][] colorArray) {

		int i, j, k, l, q, w;
		// dct will store the discrete cosine transform

		Pixel[][] matrix = this.getPixels2D();
		int m1 = matrix.length;
		int n1 = matrix[0].length;

		int m = 8, n = 8;
		double ck, cl, dctR, dctG, dctB, sumR, sumB, sumG;
		for (int r = 0; r < m1; r += 8) { // for loops breaking up the image into 8x8 blocks
			for (int c = 0; c < n1; c += 8) {

				double[][] blurredGreenArray = new double[8][8];
				double[][] blurredRedArray = new double[8][8];
				double[][] blurredBlueArray = new double[8][8];
				for (q = 0; q < 8; q++) {
					for (w = 0; w < 8; w++) {
						if(q<=3 && w<=3) {
						blurredGreenArray[q][w] = colorArray[1][q+r][w+c];
						blurredRedArray[q][w] = colorArray[0][q+r][w+c];
						blurredBlueArray[q][w] = colorArray[2][q+r][w+c];

						}
						else {
							blurredGreenArray[q][w] = 0;
							blurredRedArray[q][w] = 0;
							blurredBlueArray[q][w] = 0;
						}
					}
				}

				for (i = 0; i < 8; i++) {
					for (j = 0; j < 8; j++) {
						// ci and cj depends on frequency as well as
						// number of row and columns of specified matrix

						// sum will temporarily store the sum of
						// cosine signals

						sumR = 0;
						sumG = 0;
						sumB = 0;

						for (k = 0; k < 8; k++) {
							for (l = 0; l < 8; l++) {

								if (k == 0)
									ck = 1 / Math.sqrt(8);
								else
									ck = Math.sqrt(2) / Math.sqrt(8);

								if (l == 0)
									cl = 1 / Math.sqrt(8);
								else
									cl = Math.sqrt(2) / Math.sqrt(8);

								dctR = blurredRedArray[k][l] * Math.cos((2 * i + 1) * k * Math.PI / (2 * m))
										* Math.cos((2 * j + 1) * l * Math.PI / (2 * n));
								sumR = sumR + ck * cl * dctR;

								dctG = blurredGreenArray[k][l] * Math.cos((2 * i + 1) * k * Math.PI / (2 * m))
										* Math.cos((2 * j + 1) * l * Math.PI / (2 * n));
								sumG = sumG + ck * cl * dctG;

								dctB = blurredBlueArray[k][l] * Math.cos((2 * i + 1) * k * Math.PI / (2 * m))
										* Math.cos((2 * j + 1) * l * Math.PI / (2 * n));
								sumB = sumB + ck * cl * dctB;
							}
						}
						if (sumR >= 255) {
							sumR = 255;
						}
						if (sumG >= 255) {
							sumG = 255;
						}
						if (sumB >= 255) {
							sumB = 255;
						}

						// System.out.println("running inverse dct");
						matrix[i + r][j + c].setRed((int) sumR);
						matrix[i + r][j + c].setGreen((int) sumG);
						matrix[i + r][j + c].setBlue((int) sumB);
					}
				}
			}
		}
		for (i = 0; i < m1; i++) {
			for (j = 0; j < n1; j++)
				System.out.printf("%f\t", matrix[i][j].getAverage());
			System.out.println();
		}
	}

	/*
	 * Main method for testing - each class in Java can have a main method
	 */
	public static void main(String[] args) {
		Picture beach = new Picture("beach.jpg");
		beach.explore();
		beach.zeroBlue();
		beach.explore();
	}

} // this } is the end of class Picture, put all new methods before this
