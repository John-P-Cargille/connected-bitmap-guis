import java.awt.Color;
import java.util.Random;

//Represents an edit to a given BitMap Editor
public class IconEdit extends Subject implements IconEditInterface {

    //The row of the edited pixel
    private int editRow;
    //The column of the edited pixel
    private int editColumn;
    //The color with which to edit the pixel
    private Color editColor;

    //Sets the private data and notifies Observers (BitMapGuis) of the change
    public void setEdit(int row, int column, Color color) {
        editRow = row;
        editColumn = column;
        editColor = color;
        notifyObservers();
    }

    //Getters to return information about the original edit
    @Override
    public int getRow() {
        return editRow;
    }

    @Override
    public int getColumn() {
        return editColumn;
    }

    @Override
    public Color getColor() {
        return editColor;
    }
}
