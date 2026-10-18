// Represents a single pixel with red, green, and blue color values
public class Pixel {

    //An integer whose (left to right) first eight bits are empty, next eight bits
    //are the blue value, next eight are the green, next eight are the red
    private int color;

    //Default constructor setting each value to 0, making the pixel black
    Pixel() {
        setRed(0);
        setGreen(0);
        setBlue(0);
    }

    //Constructor with color values specified by the user
    Pixel(int redValue, int greenValue, int blueValue) {
        setRed(redValue);
        setGreen(greenValue);
        setBlue(blueValue);
    }

    //Returns the isolated red value
    public int getRed() {
        //all bits not associated with the red value become zero via bitwise and
        int redMask = 0b00000000000000000000000011111111;
        int isolatedRedBits = color & redMask;
        //Moves the isolated red bits over 0 places, in order to undergo the same
        //operation as the other colors
        int redValue = isolatedRedBits >> 0;
        return redValue;
    }

    //Sets the red value if the passed-in value is inclusively between 0 and 255
    public void setRed(int r) {
        if ((r >= 0) && (r <= 255)) {
            //moves the bits which will become the red bits over 0 bits, in order
            //to undergo the same operation as the other colors
            int adjustedRedBitValue = r << 0;
            //Green and blue bits stay the same (bitwise and with 1s), while
            //zeroing out the red bits
            int greenAndBlueMask = 0b00000000111111111111111100000000;
            int greenAndBlueValues = color & greenAndBlueMask;
            //the red bits change to match the 1s and 0s of the red value passed in
            //via bitwise or; the other values remain the same
            color = greenAndBlueValues | adjustedRedBitValue;
        }
    }

    //Returns the isolated green value
    public int getGreen() {
        //all bits not associated with the green value become zero via bitwise and
        int greenMask = 0b00000000000000001111111100000000;
        int isolatedGreenBits = color & greenMask;
        //Moves the isolated green bits over 8 places, in order to have those bits
        //be in the least-significant digits of the integer returned
        int greenValue = isolatedGreenBits >> 8;
        return greenValue;
    }

    //Sets the green value if the passed-in value is inclusively between 0 and 255
    public void setGreen(int g) {
        if ((g >= 0) && (g <= 255)) {
            //moves the bits which will become the green bits over 8 bits, in order
            //to put those bits in the same position as the green bits in "color"
            int adjustedGreenBitValue = g << 8;
            //Red and blue bits stay the same (bitwise and with 1s), while
            //zeroing out the green bits
            int redAndBlueMask = 0b00000000111111110000000011111111;
            int redAndBlueValues = color & redAndBlueMask;
            //the green bits change to match the 1s and 0s of the green value passed in
            //via bitwise or; the other values remain the same
            color = redAndBlueValues | adjustedGreenBitValue;
        }
    }

    //Returns the isolated blue value
    public int getBlue() {
        //all bits not associated with the blue value become zero via bitwise and
        int blueMask = 0b00000000111111110000000000000000;
        int isolatedBlueBits = color & blueMask;
        //Moves the isolated blue bits over 16 places, in order to have those bits
        //be in the least-significant digits of the integer returned
        int blueValue = isolatedBlueBits >> 16;
        return blueValue;
    }

    //Sets the blue value if the passed-in value is inclusively between 0 and 255
    public void setBlue(int b) {
        if ((b >= 0) && (b <= 255)) {
            //moves the bits which will become the blue bits over 16 bits, in order
            //to put those bits in the same position as the blue bits in "color"
            int adjustedBlueBitValue = b << 16;
            //Red and green bits stay the same (bitwise and with 1s), while
            //zeroing out the blue bits
            int redAndGreenMask = 0b00000000000000001111111111111111;
            int redAndGreenValues = color & redAndGreenMask;
            //the blue bits change to match the 1s and 0s of the blue value passed in
            //via bitwise or; the other values remain the same
            color = redAndGreenValues | adjustedBlueBitValue;
        }
    }

    //returns a string representing the color of the pixel as a hex code
    public String toStringHex() {

        String pixelColorString = "#";

        //adds the red, green, and blue values as hexidigits into the return string
        //Adds a placeholder 0 if the value is only one hexidigit
        if (getRed() < 16) {
            pixelColorString += "0";
        }
        pixelColorString += Integer.toHexString(getRed());
        if (getGreen() < 16) {
            pixelColorString += "0";
        }
        pixelColorString += Integer.toHexString(getGreen());
        if (getBlue() < 16) {
            pixelColorString += "0";
        }
        pixelColorString += Integer.toHexString(getBlue());

        //Changes the case of the letter hexidigits to uppercase
        pixelColorString = pixelColorString.toUpperCase();

        return pixelColorString;
    }

}
