import java.awt.Color;

//Abstract Decorator for the IconEdit
public class IconEditDecorator implements IconEditInterface {

    //The decorated object (either a concrete decorator or an IconEdit)
    private IconEditInterface decoratedIconEdit;

    //Public constructor setting what Component this Decorator references
    public IconEditDecorator(IconEditInterface decoratedIconEdit) {
        this.decoratedIconEdit = decoratedIconEdit;
    }

    //These getters call the appropriate getter of the next decorated object,
    //effectively skipping it if it has no implementation for a given method
    @Override
    public int getRow() {
        return decoratedIconEdit.getRow();
    }

    @Override
    public int getColumn() {
        return decoratedIconEdit.getColumn();
    }

    @Override
    public Color getColor() {
        return decoratedIconEdit.getColor();
    }
}