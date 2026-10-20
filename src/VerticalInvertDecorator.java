import java.awt.Color;

//Inverts the vertical coordinate of edits made by other editors
public class VerticalInvertDecorator extends IconEditDecorator {

    //The number of rows in the Icon
    int numRows;

    //Public constructor setting the decorated object (Decorator/IconEdit) and numRows
    public VerticalInvertDecorator(IconEditInterface decoratedIconEdit, int numRows) {
        super(decoratedIconEdit);
        this.numRows = numRows;
    }

    //Inverts the vertical coordinate of the edit
    @Override
    public int getRow() {
        //Gets the original vertical coordinate from the centrally-wrapped IconEdit
        int originalRow = super.getRow();
        int invertedRow = numRows - originalRow - 1;
        return invertedRow;
    }

}