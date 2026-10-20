import java.awt.Color;

//Inverts the horizontal coordinate of edits made by other editors
public class HorizontalInvertDecorator extends IconEditDecorator {

    //The number of columns in the Icon
    int numColumns;

    //Public constructor setting the decorated object (Decorator/IconEdit) and numColumns
    public HorizontalInvertDecorator(IconEditInterface decoratedIconEdit, int numColumns) {
        super(decoratedIconEdit);
        this.numColumns = numColumns;
    }

    //Returns the inverted horizontal coordinate of the edit
    @Override
    public int getColumn() {
        //Gets the original horizontal coordinate from the centrally-wrapped IconEdit
        int originalColumn = super.getColumn();
        int invertedColumn = numColumns - originalColumn - 1;
        return invertedColumn;
    }

}