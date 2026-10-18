import java.util.ArrayList;

//Represents an icon composed of pixels
public class Icon {

    //A collection of all of the pixels in the icon
    private ArrayList <ArrayList<Pixel>> allPixels;
    //The number of pixel rows in the icon
    public final int numRows;
    //The number of pixel columns in the icon
    public final int numColumns;

    //Default constructor which sets the Pixel collection to 40x40
    Icon() {
        //Sets size of collection
        this.numRows = 40;
        this.numColumns = 40;
        //Instantiates the outer ArrayList
        allPixels = new ArrayList<ArrayList<Pixel>>(numRows);
        //Instantiates the inner ArrayLists and adds them to allPixels
        for (int i = 0; i < numRows; i++) {
            ArrayList <Pixel> rowOfPixels = new ArrayList<Pixel>(numColumns);
            allPixels.add(rowOfPixels);
            //Sets the pixels to black by default and adds them to the inner ArrayLists
            for (int j = 0; j < numColumns; j++) {
                Pixel nextPixel = new Pixel(0, 0, 0);
                rowOfPixels.add(nextPixel);
            }
        }
    }

    //Constructor which sets the Pixel collection to the user's passed in values
    Icon(int numRows, int numColumns) {
        //Sets size of collection
        this.numRows = numRows;
        this.numColumns = numColumns;
        //Instantiates the outer ArrayList
        allPixels = new ArrayList<ArrayList<Pixel>>(numRows);
        //Instantiates the inner ArrayLists and adds them to allPixels
        for (int i = 0; i < numRows; i++) {
            ArrayList <Pixel> rowOfPixels = new ArrayList<Pixel>(numColumns);
            allPixels.add(rowOfPixels);
            //Sets the pixels to black by default and adds them to the inner ArrayLists
            for (int j = 0; j < numColumns; j++) {
                Pixel nextPixel = new Pixel(0, 0, 0);
                rowOfPixels.add(nextPixel);
            }
        }
    }

    //Returns a Pixel at a particular (y, x) within the collection
    public Pixel getPixel(int row, int column) {
        Pixel chosenPixel = null;
        //Return null and print an error if the indices are out of bounds
        if ((row < 0) || (row >= numRows) || (column < 0) || (column >= numColumns)) {
            System.out.println("Tried to get a pixel at an out-of-bound index");
        } else {
            //otherwise return the pixel
            chosenPixel = allPixels.get(row).get(column);
        }
        return chosenPixel;
    }


    //Returns, as a hex string, the RGB values of a given pixel
    public String getPixelRGBValue(int row, int column) {
        String retString;
        //if it's out of bounds, set the return string to say that
        if ((row < 0) || (row >= numRows) || (column < 0) || (column >= numColumns)) {
            retString = "Tried to get the RGB value with out-of-bound indices";
        }  else {
            //otherwise return the pixel's RGB values
            String pixelRGBValue = allPixels.get(row).get(column).toStringHex();
            retString = pixelRGBValue;
        }
        return retString;
    }

    //Sets the RGB values for a given pixel
    public void setPixelRBGValue(int row, int column,
                                 int redValue, int greenValue, int blueValue) {
        boolean doSet = true;
        //If the coordinates passed in are out of bounds of the collection, do nothing
        if ((row < 0) || (row >= numRows) || (column < 0) || (column >= numColumns)) {
            doSet = false;
        }
        //Otherwise, given that doSet has not been falsified, set the pixel's RGB values
        if (doSet == true) {
            allPixels.get(row).get(column).setRed(redValue);
            allPixels.get(row).get(column).setGreen(greenValue);
            allPixels.get(row).get(column).setBlue(blueValue);
        }
    }

    //Returns a grid made up of the hex values of the pixels within the collection
    public String toString() {

        String iconString = "";

        for (int i = 0; i < allPixels.size(); i++) {
            for (int j = 0; j < allPixels.get(i).size(); j++) {
                iconString += allPixels.get(i).get(j).toStringHex() + " ";
            }
            iconString += "\n";
        }

        return iconString;
    }

}