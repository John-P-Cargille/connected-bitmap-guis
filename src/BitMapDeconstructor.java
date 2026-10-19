import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
public class BitMapDeconstructor {

    //The icon to write bytes to
    private Icon iconToWriteTo;
    //The FileStream which will read from the file
    private FileInputStream fstream;
    //The number of rows in the imported BitMap
    private int numRows;
    //The number of columns in the imported BitMap
    private int numColumns;

    public BitMapDeconstructor(String filePath) {
        //Catches FileNotFound and IO exceptions
        try {
            fstream = new FileInputStream(filePath);
            byte[] infoBytes = new byte[54];
            //Reads bytes concerning the set-up of the BMP file from the File and Info Header
            fstream.read(infoBytes);
            //Gets the row and column number from these spots in the BitMap Info Header
            numColumns = fourBytesToInteger(infoBytes, 17);
            numRows = fourBytesToInteger(infoBytes, 22);
            //Fills the icon
            fillIcon();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found!");
        } catch (IOException ex) {
            System.out.println("Encounterd IO error");
        }
    }

    //Converts four bytes into an integer
    private int fourBytesToInteger(byte[] bytes, int indexToStartAt) {
        int retVal;

        //Adds the next four bytes to a byte array
        byte byte1 = bytes[indexToStartAt];
        byte byte2 = bytes[indexToStartAt + 1];
        byte byte3 = bytes[indexToStartAt + 2];
        byte byte4 = bytes[indexToStartAt + 3];
        byte[] bytesToConvert = new byte[4];

        //Converts those bytes to an int via a ByteBuffer
        retVal = ByteBuffer.wrap(bytesToConvert).getInt();

        return retVal;
    }

    //Fills the imported Icon
    private void fillIcon() throws IOException {
        //Initializes the imported Icon
        iconToWriteTo = new Icon(numRows, numColumns);
        //Reads in left to right, bottom to top
        for (int i = (numRows - 1); i >= 0; i--) {
            int bytesReadInRow = 0;
            for (int j = 0; j < numColumns; j++) {
                //Read values for the next pixel in order from fstream
                int redVal = fstream.read();
                int greenVal = fstream.read();
                int blueVal = fstream.read();
                //Sets the appropriate pixel to have those RGB values
                iconToWriteTo.setPixelRBGValue(i, j, redVal, greenVal, blueVal);
                bytesReadInRow += 3;
            }
            //Accounts for padding bytes at the end of rows
            while((bytesReadInRow % 4) != 0) {
                fstream.read();
                bytesReadInRow++;
            }
        }
    }

    //Returns the imported Icon
    public Icon getIcon() {
        return iconToWriteTo;
    }

}