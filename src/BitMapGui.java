import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.io.File;

//The Graphical User Interface for a BitMap Editor
public class BitMapGui extends JFrame implements ColorChanger, ChangeListener, ActionListener, MouseListener, Observer {

    //The underlying icon to the grid
    private Icon guiIcon;
    //The IconEdit which the GUI observes
    private IconEdit iconEdit;
    //A collection of PixelButtons
    private JPanel grid;
    //The last five colors used by the user
    private LastFiveColors lastFiveColorsWidget;
    //A 2d Arraylist of PixelButtons mirroring the grid
    private ArrayList < ArrayList < PixelButton > > pixelButtons;
    //A state for if this GUI is the source of a given color change
    private boolean isSourceOfEdit;
    //The current color selected by the user
    private Color currentColor;
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
    //A state for whether this editor is connected to other editors
    private boolean isConnectedEditor;
    //A checkbox to connect and disconnect the editor from the other editors
    private JCheckBox connectedCB;
    //A state for whether to invert edits vertically
    private boolean isVerticallyInverted;
    //A checkbox to invert edits vertically
    private JCheckBox verticalInvertCB;
    //A state for whether to invert edits horizontally
    private boolean isHorizontallyInverted;
    //A checkbox to invert edits horizontally
    private JCheckBox horizontalInvertCB;
    //A state for whether to randomize the color of edits
    private boolean colorIsRandomized;
    //A checkbox to randomize the color of edits
    private JCheckBox randomColorCB;
    //A button to export the BitMap
    private JButton bitMapButton;


    //Takes in a title for the frame
    public BitMapGui(String title, IconEdit iconEdit, int numRows, int numColumns) {
        //Sets the title of the frame and defaults other values
        super(title);
        currentColor = Color.BLACK;
        isConnectedEditor = true;
        isVerticallyInverted = false;
        isHorizontallyInverted = false;
        colorIsRandomized = false;
        isSourceOfEdit = false;
        //Initializes the 2d ArrayList of PixelButtons
        pixelButtons = new ArrayList < ArrayList< PixelButton > >();
        //gets a reference to the one IconEdit instance
        this.iconEdit = iconEdit;
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
                        //Sets this GUI to be the source of a given edit
                        isSourceOfEdit = true;
                        //Gets the row and column of the edited PixelButton
                        int row = pixelButton.getRow();
                        int column = pixelButton.getColumn();
                        //Sets the edit if this editor is connected to other editors
                        if(isConnectedEditor) {
                            iconEdit.setEdit(row, column, currentColor);
                        }

                        //Sets the color of the PixelButton to the currentColor
                        pixelButtons.get(row).get(column).setBackground(currentColor);

                        //Mirrors this setting in the icon
                        guiIcon.setPixelRBGValue(row,
                                column,
                                currentColor.getRed(),
                                currentColor.getGreen(),
                                currentColor.getBlue());
                        lastFiveColorsWidget.addColor(currentColor);
                    }
                });

                //Adds this BitMapGui to be a MouseListener for each button
                pixelButton.addMouseListener(this);
                //Adds the button to the GUI collection of PixelButtons
                grid.add(pixelButton);
            }
            //Adds a row of PixelButtons to the code collection of them
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

        //Sets the connected checkbox to be checked by default
        connectedCB = new JCheckBox("Connected Editor");
        connectedCB.setToolTipText("Connects edits made in this editor to other connected editors");
        connectedCB.setSelected(true);
        connectedCB.addActionListener(this);

        //Sets up the other checkboxes
        verticalInvertCB = new JCheckBox("Invert Vertically");
        verticalInvertCB.setToolTipText("Vertically inverts edits made by connected editors");
        verticalInvertCB.addActionListener(this);

        horizontalInvertCB = new JCheckBox("Invert Horizontally");
        horizontalInvertCB.setToolTipText("horizontally inverts edits made by connected editors");
        horizontalInvertCB.addActionListener(this);

        randomColorCB = new JCheckBox("Randomize Color");
        randomColorCB.setToolTipText("Randomizes the color of edits made by connected editors");
        randomColorCB.addActionListener(this);

        //Sets up the export button
        bitMapButton = new JButton("Export BitMap");
        bitMapButton.addActionListener(this);

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

        //Adds checkboxes to a panel
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new GridLayout(2, 2));
        checkboxPanel.add(connectedCB);
        checkboxPanel.add(randomColorCB);
        checkboxPanel.add(verticalInvertCB);
        checkboxPanel.add(horizontalInvertCB);

        //Adds the checkboxPanel and export button to a panel
        JPanel auxiliaryActions = new JPanel();
        auxiliaryActions.setLayout(new GridLayout(1, 2));
        auxiliaryActions.add(checkboxPanel);
        auxiliaryActions.add(bitMapButton);

        //Adds various widgets to the window
        setLayout(new GridLayout(4,1));
        add(grid);
        add(colorDisplayPanel);
        add(lastFiveColorsWidget);
        add(auxiliaryActions);

        //Sets info for window
        setSize(350, 400);
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
        Color updatedColor = new Color(redSlider.getValue(),
                                       greenSlider.getValue(),
                                       blueSlider.getValue());
        //Sets the textboxes for each color to reflect the slider value
        redTB.setText(String.valueOf(redSlider.getValue()));
        greenTB.setText(String.valueOf(greenSlider.getValue()));
        blueTB.setText(String.valueOf(blueSlider.getValue()));
        currentColor = updatedColor;
        previewColor.setBackground(currentColor);
    }

    //Responds to various ActionEvents
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
        } else if (e.getSource() == connectedCB) {
            //changes state of checkbox
            isConnectedEditor = !isConnectedEditor;
            //Attaches/detaches the editor
            if (isConnectedEditor == false) {
                iconEdit.detach(BitMapGui.this);
            } else {
                iconEdit.attach(BitMapGui.this);
            }
        } else if (e.getSource() == verticalInvertCB) {
            //changes state of checkbox
            isVerticallyInverted = !isVerticallyInverted;
        } else if (e.getSource() == horizontalInvertCB) {
            //changes state of checkbox
            isHorizontallyInverted = !isHorizontallyInverted;
        } else if (e.getSource() == randomColorCB) {
            //changes state of checkbox
            colorIsRandomized = !colorIsRandomized;
        } else if (e.getSource() == bitMapButton) {
            //Opens a file chooser
            JFileChooser fileExplorerExport = new JFileChooser(new File("C://"));
            //Assuming they gave a good path, aka didn't cancel
            if(fileExplorerExport.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                //Creates a file with the given filePath, and passes that in as an argument for the BitMapCreator
                File selectedFile = fileExplorerExport.getSelectedFile();
                Path filePath = selectedFile.toPath();
                String pathString = filePath.toString();
                BitMapCreator guiBitMapCreator = new BitMapCreator(guiIcon, pathString);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //do nothing
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //do nothing
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //do nothing
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //If shift is down when a button is entered
        if(e.isShiftDown()) {
            //Set the background of the entered button
            PixelButton enteredButton = (PixelButton) e.getComponent();
            enteredButton.doClick();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //do nothing
    }

    //Update method for each connected GUI as an Observer
    @Override
    public void update() {

        //Only updates if it did not already set its own information
        //as the source of the edit
        if(!isSourceOfEdit) {
            //Creates a temporary IconEditInterface to wrap with 0+ decorators
            IconEditInterface decoratedIconEdit = iconEdit;
            //Wraps the temp in the appropriate decorators
            if (isVerticallyInverted) {
                int numRows = pixelButtons.size();
                decoratedIconEdit = new VerticalInvertDecorator(decoratedIconEdit, numRows);
            }
            if (isHorizontallyInverted) {
                int numColumns = pixelButtons.get(0).size();
                decoratedIconEdit = new HorizontalInvertDecorator(decoratedIconEdit, numColumns);
            }
            if (colorIsRandomized) {
                decoratedIconEdit = new RandomColorDecorator(decoratedIconEdit);
            }

            //Gets the appropriate row, column, and color to edit with
            //from the wrapped IconEdit
            int rowNum = decoratedIconEdit.getRow();
            int columnNum = decoratedIconEdit.getColumn();
            Color editColor = decoratedIconEdit.getColor();

            //Sets the color of the PixelButton at the appropriate coordinates
            //to the appropriate color
            pixelButtons.get(rowNum).get(columnNum).setBackground(editColor);

            //Mirrors this setting in the icon
            guiIcon.setPixelRBGValue(rowNum,
                    columnNum,
                    editColor.getRed(),
                    editColor.getGreen(),
                    editColor.getBlue());
        } else {
            //If it was the source, reset its source state to false
            isSourceOfEdit = false;
        }
    }
}
