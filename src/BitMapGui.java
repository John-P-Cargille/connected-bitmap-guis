import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

//The Graphical User Interface for a BitMap Editor
public class BitMapGui extends JFrame implements ColorChanger, ChangeListener, ActionListener {

    //The underlying icon to the grid
    private Icon guiIcon;
    //A BitMapCreator to export the finished BitMap
    private BitMapCreator guiBitMapCreator;
    //A BitMapDeconstructor to import BitMaps
    private BitMapDeconstructor guiBitMapDeconstructor;
    //A collection of PixelButtons
    private JPanel grid;
    //The last five colors used by the user
    private LastFiveColors lastFiveColorsWidget;
    //A 2d Arraylist of PixelButtons mirroring the grid
    private ArrayList < ArrayList < PixelButton > > pixelButtons;
    //The current color selected by the user
    private Color currentColor;
    //A state for whether or not the advanced CheckBox is checked
    private boolean advancedCbIsChecked;
    //The slider for the red value
    private JSlider redSlider;
    //The textbox for the red value
    private JTextField redTB;
    //The label for the red value widgets
    private JLabel redLabel;
    //The slider for the green value
    private JSlider greenSlider;
    //The textbox for the green value
    private JTextField greenTB;
    //The label for the green value widgets
    private JLabel greenLabel;
    //The slider for the blue value
    private JSlider blueSlider;
    //The textbox for the blue value
    private JTextField blueTB;
    //The label for the blue value widgets
    private JLabel blueLabel;
    //A preview of the currentColor
    private JLabel previewColor;
    //The advanced options checkbox
    private JCheckBox advancedCB;
    //A dialog for the advanced options
    private JDialog advancedDialog;
    //The row number of the PixelButton clicked which opens the advancedDialog
    private JTextField advancedRowNumTB;
    //The column number of the PixelButton clicked which opens the advancedDialog
    private JTextField advancedColumnNumTB;
    //The button which fills in the advanced dialog
    private JButton advancedFillButton;
    //The button which selects a file to import
    private JButton advancedImportSelectButton;
    //The label which the preview of the file inhabits
    private JLabel advancedImportPreviewImageLabel;
    //The preview of the file selected
    private ImageIcon advancedImportPreviewImage;
    //The file path of the selected file
    private String advancedImportFilePath;
    //The button which confirms the importing of the selected file
    private JButton advancedImportConfirmButton;
    //The row of the PixelButton which initiated the advancedDialog
    private int lastPixelRowNum;
    //The column of the PixelButton which initiated the advancedDialog
    private int lastPixelColumnNum;
    //The button for exporting the final BitMap
    private JButton bitMapButton;

    //Takes in a title for the frame
    public BitMapGui(String title) {
        //Sets the title of the frame and defaults other values
        super(title);
        currentColor = Color.BLACK;
        advancedCbIsChecked = false;
        //Initializes the 2d ArrayList of PixelButtons
        pixelButtons = new ArrayList < ArrayList< PixelButton > >();
        //JOptionPanes prompt the user for the size of the BitMap
        int numRows = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of rows in the BitMap"));
        int numColumns = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of columns in the BitMap"));
        //Sets up the grid and icon
        grid = new JPanel();
        grid.setLayout(new GridLayout(numRows, numColumns));
        guiIcon = new Icon(numRows, numColumns);
        //Fills the grid and 2d ArrayList with PixelButtons
        for (int i = 0; i < numRows; i++) {
            ArrayList < PixelButton > pixelButtonRow =  new ArrayList< PixelButton >();
            for (int j = 0; j < numColumns; j++) {
                PixelButton pixelButton = new PixelButton(i, j);
                pixelButton.setBackground(Color.BLACK);
                pixelButtonRow.add(pixelButton);
                //Anonymous inner class listener for the PixelButtons
                pixelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(advancedCbIsChecked == false) {
                            //if it's not advanced, simply set the color to the current color
                            pixelButton.setBackground(currentColor);

                            //Mirrors this setting in the icon
                            guiIcon.setPixelRBGValue(pixelButton.getRow(),
                                    pixelButton.getColumn(),
                                    currentColor.getRed(),
                                    currentColor.getGreen(),
                                    currentColor.getBlue());
                            lastFiveColorsWidget.addColor(currentColor);
                        } else {
                            //If it is advanced, get the source's row and column
                            PixelButton lastPixel = (PixelButton) e.getSource();
                            lastPixelRowNum = lastPixel.getRow();
                            lastPixelColumnNum = lastPixel.getColumn();

                            //Open a modal dialogue
                            advancedDialog = new JDialog(BitMapGui.this,
                                                                   "Advanced Color Options",
                                                                 true);
                            advancedDialog.setLayout(new GridLayout(1,1 ));

                            //Initialize widgets
                            JLabel advancedRowNumLabel = new JLabel("Enter the number of rows to fill, starting at row " + Integer.toString(lastPixelRowNum + 1) + ":");

                            advancedRowNumTB = new JTextField("0");
                            advancedRowNumTB.addActionListener(BitMapGui.this);

                            JLabel advancedColumnNumLabel = new JLabel("Enter the number of columns to fill, starting at column " + Integer.toString(lastPixelColumnNum + 1) + ":");

                            advancedColumnNumTB = new JTextField("0");
                            advancedColumnNumTB.addActionListener(BitMapGui.this);

                            advancedFillButton = new JButton("Fill");
                            advancedFillButton.addActionListener(BitMapGui.this);

                            //Panel with the fill widgets
                            JPanel advancedFillPanel = new JPanel();
                            advancedFillPanel.setLayout(new GridLayout(5,1));
                            advancedFillPanel.add(advancedRowNumLabel);
                            advancedFillPanel.add(advancedRowNumTB);
                            advancedFillPanel.add(advancedColumnNumLabel);
                            advancedFillPanel.add(advancedColumnNumTB);
                            advancedFillPanel.add(advancedFillButton);

                            //Initialize more widgets
                            JLabel advancedImportLabel = new JLabel("Select a .BMP file to import into the BitMap Editor, starting at row " + Integer.toString(lastPixelRowNum + 1) + " and column " + Integer.toString(lastPixelColumnNum + 1) + ".");

                            advancedImportSelectButton = new JButton("Select File");
                            advancedImportSelectButton.addActionListener(BitMapGui.this);

                            //Needs a minimum size to be visible in the BorderLayout
                            advancedImportPreviewImageLabel = new JLabel();
                            advancedImportPreviewImageLabel.setMinimumSize(new Dimension(100,100));
                            advancedImportPreviewImageLabel.setPreferredSize(new Dimension(100,100));
                            advancedImportPreviewImageLabel.setOpaque(true);
                            advancedImportPreviewImageLabel.setBackground(Color.WHITE);

                            advancedImportConfirmButton = new JButton("Confirm and Import");
                            advancedImportConfirmButton.addActionListener(BitMapGui.this);

                            //Panel for the select button and its label
                            JPanel advancedImportSelectPanel = new JPanel();
                            advancedImportSelectPanel.setLayout(new GridLayout(2,1));
                            advancedImportSelectPanel.add(advancedImportLabel);
                            advancedImportSelectPanel.add(advancedImportSelectButton);

                            //Panel for the import widgets
                            JPanel advancedImportPanel = new JPanel();
                            advancedImportPanel.setLayout(new BorderLayout());
                            advancedImportPanel.add(advancedImportSelectPanel, BorderLayout.NORTH);
                            advancedImportPanel.add(advancedImportPreviewImageLabel, BorderLayout.CENTER);
                            advancedImportPanel.add(advancedImportConfirmButton, BorderLayout.SOUTH);

                            //Creates tabs within the dialog for fill and import
                            JTabbedPane advancedTabs = new JTabbedPane();
                            advancedTabs.add("Fill", advancedFillPanel);
                            advancedTabs.add("Import BitMap", advancedImportPanel);

                            advancedDialog.add(advancedTabs);

                            //Sets details of dialog
                            advancedDialog.setSize(500, 400);
                            advancedDialog.setLocation(425, 200);
                            advancedDialog.setVisible(true);
                            advancedDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

                        }
                    }
                });
                grid.add(pixelButton);
            }
            pixelButtons.add(pixelButtonRow);
        }

        //Initialzes the values and ticks for sliders, and initializes other widgets
        redSlider = new JSlider(0, 255);
        redSlider.setValue(0);
        redSlider.setMajorTickSpacing(20);
        redSlider.setMinorTickSpacing(4);
        redSlider.setPaintTicks(true);
        redSlider.addChangeListener(this);

        redTB = new JTextField("0");
        redTB.addActionListener(this);

        redLabel = new JLabel("Red Value:");

        greenSlider = new JSlider(0, 255);
        greenSlider.setValue(0);
        greenSlider.setMajorTickSpacing(20);
        greenSlider.setMinorTickSpacing(4);
        greenSlider.setPaintTicks(true);
        greenSlider.addChangeListener(this);

        greenTB = new JTextField("0");
        greenTB.addActionListener(this);

        greenLabel = new JLabel("Green Value:");

        blueSlider = new JSlider(0, 255);
        blueSlider.setValue(0);
        blueSlider.setMajorTickSpacing(20);
        blueSlider.setMinorTickSpacing(4);
        blueSlider.setPaintTicks(true);
        blueSlider.addChangeListener(this);

        blueTB = new JTextField("0");
        blueTB.addActionListener(this);

        blueLabel = new JLabel("Blue Value:");

        //Creates the previewColor box, setting a minimum size so it is visible in the border layout
        previewColor = new JLabel();
        previewColor.setOpaque(true);
        previewColor.setBackground(Color.BLACK);
        previewColor.setMinimumSize(new Dimension(100, 100));
        previewColor.setPreferredSize(new Dimension(100, 100));

        lastFiveColorsWidget = new LastFiveColors(this);

        bitMapButton = new JButton("Export BitMap");
        bitMapButton.addActionListener(this);

        //Adds a tool tip so the user knows when the advancedCB will change things
        advancedCB = new JCheckBox("Advanced Color Options");
        advancedCB.setToolTipText("Allows the user to fill swaths of pixels or import a bitMap into a section of the editor when selecting a pixel");
        advancedCB.addActionListener(this);

        //Adds widgets of different colors to different panels
        JPanel redPanel = new JPanel();
        redPanel.setLayout(new GridLayout(1, 3));
        redPanel.add(redLabel);
        redPanel.add(redTB);
        redPanel.add(redSlider);

        JPanel greenPanel = new JPanel();
        greenPanel.setLayout(new GridLayout(1, 2));
        greenPanel.add(greenLabel);
        greenPanel.add(greenTB);
        greenPanel.add(greenSlider);

        JPanel bluePanel = new JPanel();
        bluePanel.setLayout(new GridLayout(1, 2));
        bluePanel.add(blueLabel);
        bluePanel.add(blueTB);
        bluePanel.add(blueSlider);

        //Combines the color panels into a larger panel
        JPanel colorSlidersPanel = new JPanel();
        colorSlidersPanel.setLayout(new GridLayout(3,1));
        colorSlidersPanel.add(redPanel);
        colorSlidersPanel.add(greenPanel);
        colorSlidersPanel.add(bluePanel);

        //Adds the previewColor to the left of these color widgets
        JPanel colorDisplayPanel = new JPanel();
        colorDisplayPanel.setLayout(new BorderLayout());
        colorDisplayPanel.add(previewColor, BorderLayout.WEST);
        colorDisplayPanel.add(colorSlidersPanel, BorderLayout.CENTER);

        //Adds checkbox and export button to a panel
        JPanel auxiliaryActions = new JPanel();
        auxiliaryActions.setLayout(new GridLayout(1, 2));
        auxiliaryActions.add(advancedCB);
        auxiliaryActions.add(bitMapButton);

        //Adds various widgets to the window
        setLayout(new GridLayout(4,1));
        add(grid);
        add(colorDisplayPanel);
        add(lastFiveColorsWidget);
        add(auxiliaryActions);

        //Sets info for window
        setSize(550, 650);
        setLocation(400, 25);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    //When the LastFiveColorsWidget is pressed, change currentColor to match that
    @Override
    public void changeColor(Color color) {
        currentColor = color;
        previewColor.setBackground(color);
    }

    //When the sliders change, update the currentColor based on the slider positions
    @Override
    public void stateChanged(ChangeEvent e) {
        Color updatedColor = new Color(redSlider.getValue(), greenSlider.getValue(),
                                       blueSlider.getValue());
        currentColor = updatedColor;
        previewColor.setBackground(currentColor);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == redTB) {
            //Sets red value based on text entry
            redSlider.setValue(Integer.parseInt(redTB.getText()));
            if(Integer.parseInt(redTB.getText()) > 255) {
                redTB.setText("255");
            }
        } else if (e.getSource() == greenTB) {
            //Sets green value based on text entry
            greenSlider.setValue(Integer.parseInt(greenTB.getText()));
            if(Integer.parseInt(greenTB.getText()) > 255) {
                greenTB.setText("255");
            }
        } else if(e.getSource() == blueTB) {
            //Sets blue value based on text entry
            blueSlider.setValue(Integer.parseInt(blueTB.getText()));
            if(Integer.parseInt(blueTB.getText()) > 255) {
                blueTB.setText("255");
            }
        } else if (e.getSource() == advancedCB) {
            //changes state of checkbox
            advancedCbIsChecked = !advancedCbIsChecked;
        } else if (e.getSource() == advancedFillButton) {
            //Gets appropriate number of rows and columns to fill based on input textboxes
            //Either the amount entered or to the edge of the grid
            int userNumRowsToFill = Integer.parseInt(advancedRowNumTB.getText());
            int userNumColumnsToFill = Integer.parseInt(advancedColumnNumTB.getText());
            int numRowsToGoThrough = 0;
            int numColumnsToGoThrough = 0;
            if (lastPixelRowNum + userNumRowsToFill <= pixelButtons.size()) {
                numRowsToGoThrough = userNumRowsToFill;
            } else {
                numRowsToGoThrough = pixelButtons.size() - lastPixelRowNum;
            }
            if(lastPixelColumnNum + userNumColumnsToFill <= pixelButtons.get(0).size()) {
                numColumnsToGoThrough = userNumColumnsToFill;
            }else {
                numColumnsToGoThrough = pixelButtons.get(0).size() - lastPixelColumnNum;
            }
            //Fills using the correct number of rows and columns
            for (int i = lastPixelRowNum; i < (lastPixelRowNum + numRowsToGoThrough); i++) {
                for(int j = lastPixelColumnNum; j < (lastPixelColumnNum + numColumnsToGoThrough); j++) {
                    PixelButton nextPixel = pixelButtons.get(i).get(j);
                    nextPixel.setBackground(currentColor);
                    guiIcon.setPixelRBGValue(nextPixel.getRow(),
                            nextPixel.getColumn(),
                            currentColor.getRed(),
                            currentColor.getGreen(),
                            currentColor.getBlue());
                }
            }
            //adds the color filled with
            lastFiveColorsWidget.addColor(currentColor);
        } else if(e.getSource() == advancedImportSelectButton) {
            //Open a file chooser
            JFileChooser fileExplorerImport = new JFileChooser(new File("C://"));
            //Assuming it's good
            if(fileExplorerImport.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                //Gets the path of a selected file, and creates an ImageIcon from the file which then
                //was supposed to be set to the label
                File selectedFile = fileExplorerImport.getSelectedFile();
                Path filePath = selectedFile.toPath();
                advancedImportFilePath = filePath.toString();
                advancedImportPreviewImageLabel.setIcon(new ImageIcon(advancedImportFilePath));
//                advancedImportPreviewImageLabel = new JLabel(advancedImportPreviewImage);
//                advancedImportPreviewImageLabel.update(BitMapGui.this.getGraphics());
            }
        } else if (e.getSource() == advancedImportConfirmButton) {
            //Creates a deconstructor based on the file passed in and returns its constructed icon
            guiBitMapDeconstructor = new BitMapDeconstructor(advancedImportFilePath);
            Icon iconToImport = guiBitMapDeconstructor.getIcon();
            //Gets number of rows and columns to go through based on the size of the icon
            //Either the dimensions of the Icon or to the edges of the grid
            int numRowsToGoThrough = 0;
            int numColumnsToGoThrough = 0;
            if (lastPixelRowNum + iconToImport.numRows <= pixelButtons.size()) {
                numRowsToGoThrough = iconToImport.numRows;
            } else {
                numRowsToGoThrough = pixelButtons.size() - lastPixelRowNum;
            }
            if(lastPixelColumnNum + iconToImport.numColumns <= pixelButtons.get(0).size()) {
                numColumnsToGoThrough = iconToImport.numColumns;
            }else {
                numColumnsToGoThrough = pixelButtons.get(0).size() - lastPixelColumnNum;
            }
            //Creates a color using the next pixel in the icon, which is then used to recolor the next pixel in the grid
            for (int i = lastPixelRowNum; i < (lastPixelRowNum + numRowsToGoThrough); i++) {
                for(int j = lastPixelColumnNum; j < (lastPixelColumnNum + numColumnsToGoThrough); j++) {
                    PixelButton nextPixelToSet = pixelButtons.get(i).get(j);
                    Pixel nextImportedPixel = iconToImport.getPixel((i - lastPixelRowNum), (j - lastPixelColumnNum));
                    Color nextColor = new Color(nextImportedPixel.getRed(), nextImportedPixel.getGreen(), nextImportedPixel.getBlue());
                    nextPixelToSet.setBackground(nextColor);
                    guiIcon.setPixelRBGValue(nextPixelToSet.getRow(),
                            nextPixelToSet.getColumn(),
                            currentColor.getRed(),
                            currentColor.getGreen(),
                            currentColor.getBlue());
                }
            }
        } else if (e.getSource() == bitMapButton) {
            //Opens a file chooser
            JFileChooser fileExplorerExport = new JFileChooser(new File("C://"));
            //Assuming they gave a good path, aka didn't cancel
            if(fileExplorerExport.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                //Creates a file with the given filePath, and passes that in as an argument for the BitMapCreator
                File selectedFile = fileExplorerExport.getSelectedFile();
                Path filePath = selectedFile.toPath();
                String pathString = filePath.toString();
                guiBitMapCreator = new BitMapCreator(guiIcon, pathString);
            }
        }
    }

}