import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

//Represents the creation process of the BitMaps
public class BitMapCreator {

    // Number of bytes in the Bit Map File Header
    public static final int NUM_BYTES_FILE_HEADER = 14;
    //Number of bytes in the Bit Map Info Header
    public static final int NUM_BYTES_INFO_HEADER = 40;

    //The icon to turn into a bit map
    private final Icon icon;
    //The file path to where you should create the bit map file
    private final String filePath;
    //The bytes needed for the whole Bit Map
    private int bytesNeededForWholeBitMap;
    //The bytes needed for just the pixel data of the Bit Map
    private int bytesNeededForPixels;
    //A collection of the bytes to later be passed in to the filestream which will
    //create the Bit Map file
    private ArrayList< Byte > bytes;

    //Constructor asking for the icon and filePath to use for the Bit Map
    BitMapCreator(Icon icon, String filePath) {
        //Sets private data members, creates a byte ArrayList with the appropriate size
        this.icon = icon;
        int numBytesRequired = getSize();
        this.filePath = filePath;
        bytes = new ArrayList<Byte>(numBytesRequired);
        //Fills each piece of the byte ArrayList and writes it to the filestream
        fillFileHeader();
        fillInfoHeader();
        filLPixelData();
        writeToFile();
    }

    //Returns the size needed in bytes for the array
    public int getSize() {
        //The size of the headers by default
        bytesNeededForWholeBitMap = NUM_BYTES_FILE_HEADER + NUM_BYTES_INFO_HEADER;
        //Adds three bytes to the numBytesInPixelRow for each pixel in a given row
        int numPixelsInRow = icon.numColumns;
        int numBytesInPixelRow = numPixelsInRow * 3;
        //Adds padding until the row is divisible by 4
        while ((numBytesInPixelRow % 4) != 0) {
            numBytesInPixelRow++;
        }
        //Multiplies the bytes in each row times the number of rows overall
        bytesNeededForPixels = icon.numRows * numBytesInPixelRow;
        //Adds that total to the size of the headers
        bytesNeededForWholeBitMap += bytesNeededForPixels;
        return bytesNeededForWholeBitMap;
    }

    //Fills the bytes for the file header
    private void fillFileHeader() {
        int B = 66;
        addSingleByte((byte) B);
        int M = 77;
        addSingleByte((byte) M);
        int sizeOfBmpFile = bytesNeededForWholeBitMap;
        addIntegerAsFourBytes(sizeOfBmpFile);
        int applicationDependentBytes = 0;
        addIntegerAsFourBytes(applicationDependentBytes);
        int offset = NUM_BYTES_FILE_HEADER + NUM_BYTES_INFO_HEADER;
        addIntegerAsFourBytes(offset);
    }

    ////Fills the bytes for the info header
    private void fillInfoHeader() {
        //requires the number of bytes in info header as first four bytes
        addIntegerAsFourBytes(NUM_BYTES_INFO_HEADER);
        int widthInPixels = icon.numColumns;
        addIntegerAsFourBytes(widthInPixels);
        int heightInPixels = icon.numRows;
        addIntegerAsFourBytes(heightInPixels);
        int numColorPanes = 1;
        addIntegerAsTwoBytes(numColorPanes);
        int numBitsPerPixel = 24;
        addIntegerAsTwoBytes(numBitsPerPixel);
        int compressionMethod = 0;
        addIntegerAsFourBytes(compressionMethod);
        //the amount of bytes needed for the pixel section
        addIntegerAsFourBytes(bytesNeededForPixels);
        int horizontalResolution = 0;
        addIntegerAsFourBytes(horizontalResolution);
        int verticalResolution = 0;
        addIntegerAsFourBytes(verticalResolution);
        int numColorsInColorPalette = 0;
        addIntegerAsFourBytes(numColorsInColorPalette);
        int numImportantColorsUsed = 0;
        addIntegerAsFourBytes(numImportantColorsUsed);
    }

    //Fills the bytes for the pixel data
    private void filLPixelData() {
        //Moves left to right, bottom to top through the collection,
        //Passing in bytes for the blue then green then red values for a given pixel
        for (int i = icon.numRows - 1; i >= 0; i--) {
            int bytesAddedInRow = 0;
            for (int j = 0; j < icon.numColumns; j++) {
                int nextPixelR = icon.getPixel(i, j).getBlue();
                addSingleByte((byte) nextPixelR);
                int nextPixelG = icon.getPixel(i, j).getGreen();
                addSingleByte((byte) nextPixelG);
                int nextPixelB = icon.getPixel(i, j).getRed();
                addSingleByte((byte) nextPixelB);
                bytesAddedInRow += 3;
            }
            //Adds padding bytes at the end of rows
            while((bytesAddedInRow % 4) != 0) {
                addSingleByte((byte) 0);
                bytesAddedInRow++;
            }
        }
    }

    //Adds a single byte to the bytes ArrayList
    private void addSingleByte(byte byteToAdd) {
        bytes.add(byteToAdd);
    }

    //Adds an integer to the bytes ArrayList
    private void addIntegerAsFourBytes(int intToAdd) {
        //Casts each part of the integer passed in as a byte, starting with LSB
        //and moving to the right (using Big Endian)
        addSingleByte((byte) intToAdd);
        addSingleByte((byte) (intToAdd >> 8));
        addSingleByte((byte) (intToAdd >> 16));
        addSingleByte((byte) (intToAdd >> 24));
    }

    //Adds the first two bytes of an integer to the bytes ArrayList
    private void addIntegerAsTwoBytes(int intToAdd) {
        addSingleByte((byte) intToAdd);
        addSingleByte((byte) (intToAdd >> 8));
    }

    //Writes the bytes ArrayList to the file
    private void writeToFile() {
        //Catches File's FileNotFound and IO exceptions
        try {
            //Opens a file with the given file path
            File fileToWriteTo = new File(filePath);
            //Turns the ArrayList into an array
            byte[] bytesArray = new byte[bytes.size()];
            for (int i = 0; i < bytes.size(); i++) {
                bytesArray[i] = bytes.get(i);
            }
            //Opens a file stream to the relevant file
            FileOutputStream fstream = new FileOutputStream(fileToWriteTo);
            //Writes the bytes to the file
            fstream.write(bytesArray);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found!");
        } catch (IOException ex) {
            System.out.println("Encounterd IO error");
        }
    }

}