import javax.swing.JButton;

public class PixelButton extends JButton {
    private int row;
    private int column;

    public PixelButton(int row, int column) {
        setRow(row);
        setColumn(column);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
