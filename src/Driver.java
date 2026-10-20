import javax.swing.JOptionPane;
public class Driver {
    public static void main(String[] args) {

        //Creates the single instance of IconEdit the GUIs will reference
        IconEdit iconEdit = new IconEdit();

        //Prompts user for set-up info
        int numGuis = Integer.parseInt(JOptionPane.showInputDialog("Enter the desired number of editors:"));
        int numRows = Integer.parseInt(JOptionPane.showInputDialog("Enter the desired number of rows in each editor:"));
        int numColumns = Integer.parseInt(JOptionPane.showInputDialog("Enter the desired number of columns in each editor:"));

        //Creates the requested number of new BitMapEditors, attaching them to the IconEdit by default
        for(int i = 0; i < numGuis; i++) {
            iconEdit.attach(new BitMapGui("BitMap Editor " + (i + 1), iconEdit, numRows, numColumns));
        }
    }
}
