import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.text.*;
import java.util.*;
import java.util.List; // resolves problem with java.awt.List and java.util.List

/**
 * A class that represents a picture.  This class inherits from 
 * SimplePicture and allows the student to add functionality to
 * the Picture class.  
 * 
 * @author Barbara Ericson ericson@cc.gatech.edu
 */
public class Picture extends SimplePicture 
{
  ///////////////////// constructors //////////////////////////////////
  
  /**
   * Constructor that takes no arguments 
   */
  public Picture ()
  {
    /* not needed but use it to show students the implicit call to super()
     * child constructors always call a parent constructor 
     */
    super();  
  }
  
  /**
   * Constructor that takes a file name and creates the picture 
   * @param fileName the name of the file to create the picture from
   */
  public Picture(String fileName)
  {
    // let the parent class handle this fileName
    super(fileName);
  }
  
  /**
   * Constructor that takes the width and height
   * @param height the height of the desired picture
   * @param width the width of the desired picture
   */
  public Picture(int height, int width)
  {
    // let the parent class handle this width and height
    super(width,height);
  }
  
  /**
   * Constructor that takes a picture and creates a 
   * copy of that picture
   * @param copyPicture the picture to copy
   */
  public Picture(Picture copyPicture)
  {
    // let the parent class do the copy
    super(copyPicture);
  }
  
  /**
   * Constructor that takes a buffered image
   * @param image the buffered image to use
   */
  public Picture(BufferedImage image)
  {
    super(image);
  }
  
  ////////////////////// methods ///////////////////////////////////////
  
  /**
   * Method to return a string with information about this picture.
   * @return a string with information about the picture such as fileName,
   * height and width.
   */
  public String toString()
  {
    String output = "Picture, filename " + getFileName() + 
      " height " + getHeight() 
      + " width " + getWidth();
    return output;
    
  }
  
  /** Method to set the blue to 0 */
  public void zeroBlue()
  {
    Pixel[][] pixels = this.getPixels2D();
    for (Pixel[] rowArray : pixels)
    {
      for (Pixel pixelObj : rowArray)
      {
        pixelObj.setBlue(0);
      }
    }
  }
  
  /** Method that mirrors the picture around a 
    * vertical mirror in the center of the picture
    * from left to right */
  public void mirrorVertical()
  {
    Pixel[][] pixels = this.getPixels2D();
    Pixel leftPixel = null;
    Pixel rightPixel = null;
    int width = pixels[0].length;
    for (int row = 0; row < pixels.length; row++)
    {
      for (int col = 0; col < width / 2; col++)
      {
        leftPixel = pixels[row][col];
        rightPixel = pixels[row][width - 1 - col];
        rightPixel.setColor(leftPixel.getColor());
      }
    } 
  }
  
  /** Mirror just part of a picture of a temple */
  public void mirrorTemple()
  {
    int mirrorPoint = 276;
    Pixel leftPixel = null;
    Pixel rightPixel = null;
    int count = 0;
    Pixel[][] pixels = this.getPixels2D();
    
    // loop through the rows
    for (int row = 27; row < 97; row++)
    {
      // loop from 13 to just before the mirror point
      for (int col = 13; col < mirrorPoint; col++)
      {
        
        leftPixel = pixels[row][col];      
        rightPixel = pixels[row]                       
                         [mirrorPoint - col + mirrorPoint];
        rightPixel.setColor(leftPixel.getColor());
      }
    }
  }
  
  /** copy from the passed fromPic to the
    * specified startRow and startCol in the
    * current picture
    * @param fromPic the picture to copy from
    * @param startRow the start row to copy to
    * @param startCol the start col to copy to
    */
  public void copy(Picture fromPic, 
                 int startRow, int startCol)
  {
    Pixel fromPixel = null;
    Pixel toPixel = null;
    Pixel[][] toPixels = this.getPixels2D();
    Pixel[][] fromPixels = fromPic.getPixels2D();
    for (int fromRow = 0, toRow = startRow; 
         fromRow < fromPixels.length &&
         toRow < toPixels.length; 
         fromRow++, toRow++)
    {
      for (int fromCol = 0, toCol = startCol; 
           fromCol < fromPixels[0].length &&
           toCol < toPixels[0].length;  
           fromCol++, toCol++)
      {
        fromPixel = fromPixels[fromRow][fromCol];
        toPixel = toPixels[toRow][toCol];
        toPixel.setColor(fromPixel.getColor());
      }
    }   
  }

  /** Method to create a collage of several pictures */
  public void createCollage()
  {
    Picture flower1 = new Picture("flower1.jpg");
    Picture flower2 = new Picture("flower2.jpg");
    this.copy(flower1,0,0);
    this.copy(flower2,100,0);
    this.copy(flower1,200,0);
    Picture flowerNoBlue = new Picture(flower2);
    flowerNoBlue.zeroBlue();
    this.copy(flowerNoBlue,300,0);
    this.copy(flower1,400,0);
    this.copy(flower2,500,0);
    this.mirrorVertical();
    this.write("collage.jpg");
  }
  
  
  /** Method to show large changes in color 
    * @param edgeDist the distance for finding edges
    */
  public void edgeDetection(int edgeDist)
  {
    Pixel leftPixel = null;
    Pixel rightPixel = null;
    Pixel[][] pixels = this.getPixels2D();
    Color rightColor = null;
    for (int row = 0; row < pixels.length; row++)
    {
      for (int col = 0; 
           col < pixels[0].length-1; col++)
      {
        leftPixel = pixels[row][col];
        rightPixel = pixels[row][col+1];
        rightColor = rightPixel.getColor();
        if (leftPixel.colorDistance(rightColor) > 
            edgeDist)
          leftPixel.setColor(Color.BLACK);
        else
          leftPixel.setColor(Color.WHITE);
      }
    }
  }
  public void dct() {
		Pixel[][] currPixels = this.getPixels2D();
		 int i, j, k, l; 
		  int m = currPixels.length;
		  int  n = currPixels[0].length;
	        // dct will store the discrete cosine transform 
	       double m1  = Math.sqrt(m);
	       double n1 = Math.sqrt(n);
	       double var2 = Math.sqrt(2);
	        double ci, cj, dctR, dctG, dctB, sumR,sumB, sumG; 
	   
	        for (i = 0; i < m; i++)  
	        { 
	            for (j = 0; j < n; j++)  
	            { 
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
	                for (k = 0; k < m; k++)  
	                { 
	                    for (l = 0; l < n; l++)  
	                    { 
	                    	//dct for red pixels
	                        dctR = currPixels[k][l].getRed() *  
	                               Math.cos((2 * k + 1) * i * Math.PI / (2 * m)) *  
	                               Math.cos((2 * l + 1) * j * Math.PI / (2 * n)); 
	                        sumR = sumR + dctR; 
	                        // dct for green pixels;
	                        dctG = currPixels[k][l].getGreen() *  
		                               Math.cos((2 * k + 1) * i * Math.PI / (2 * m)) *  
		                               Math.cos((2 * l + 1) * j * Math.PI / (2 * n)); 
		                        sumG = sumG + dctG; 
		                    // dct for blue pixels;   
		                        dctB = currPixels[k][l].getBlue() *  
			                               Math.cos((2 * k + 1) * i * Math.PI / (2 * m)) *  
			                               Math.cos((2 * l + 1) * j * Math.PI / (2 * n)); 
			                    sumB = sumB + dctB; 
	                    } 
	                } 
	                currPixels[i][j].setRed((int)(ci * cj * sumR));  
	                currPixels[i][j].setGreen((int)(ci * cj * sumG));  
	                currPixels[i][j].setBlue((int)(ci * cj * sumB));  
					System.out.println("running");

	                
	                
	            } 
	            
	        } 
	        
	        
	   
	       
	}
  
  //-------------------------------------------------------------------------------------------------------------
  //inverse dct
  
public void idct() 
	{ 
	  int m = 640;
	  int n = 480;
		int i, j, k, l; 
		// dct will store the discrete cosine transform 
		double[][] dct = new double[m][n]; 
		Pixel[][] matrix = this.getPixels2D();
		double ck, cl, dctR,dctG, dctB, sumR, sumB, sumG; 
		for (i = 0; i < m; i++) 
		{ 
			for (j = 0; j < n; j++) 
			{ 
				// ci and cj depends on frequency as well as 
				// number of row and columns of specified matrix 
			
				// sum will temporarily store the sum of 
				// cosine signals 
				sumR = 0; 
				sumG = 0;
				sumB = 0;
				for (k = 0; k < m; k++) 
				{ 
					for (l = 0; l < n; l++) 
					{ 
					 	if (k == 0) 
					ck = 1 / Math.sqrt(m); 
				else
					ck = Math.sqrt(2) / Math.sqrt(m); 
					
				if (l == 0) 
					cl = 1 / Math.sqrt(n); 
				else
					cl = Math.sqrt(2) / Math.sqrt(n); 
					    
					    
						dctR = matrix[k][l].getRed() * 
							Math.cos((2 * i + 1) *  k* Math.PI / (2 * m)) * 
							Math.cos((2 * j + 1) * l * Math.PI / (2 * n)); 
						sumR = sumR +ck*cl*dctR; 
						
						dctG = matrix[k][l].getGreen() * 
							Math.cos((2 * i + 1) *  k* Math.PI / (2 * m)) * 
							Math.cos((2 * j + 1) * l * Math.PI / (2 * n)); 
						sumG = sumG +ck*cl*dctG; 
							
						dctB = matrix[k][l].getBlue() * 
							Math.cos((2 * i + 1) *  k* Math.PI / (2 * m)) * 
							Math.cos((2 * j + 1) * l * Math.PI / (2 * n)); 
						sumB = sumB +ck*cl*dctB; 
					} 
				} 
				matrix[i][j].setRed((int) sumR);
				matrix[i][j].setGreen((int) sumG);

				matrix[i][j].setBlue((int) sumB);
				System.out.println("running inverse dct");
			} 
		} 
		for (i = 0; i < m; i++) 
		{ 
			for (j = 0; j < n; j++) 
				System.out.printf("%f\t", dct[i][j]); 
			System.out.println(); 
		} 
	} 
  
  /* Main method for testing - each class in Java can have a main 
   * method 
   */
  public static void main(String[] args) 
  {
    Picture beach = new Picture("beach.jpg");
    beach.explore();
    beach.zeroBlue();
    beach.explore();
  }
  
} // this } is the end of class Picture, put all new methods before this
