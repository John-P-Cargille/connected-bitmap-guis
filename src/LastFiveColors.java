import javax.swing.JPanel;
import java.util.ArrayList;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
public class LastFiveColors extends JPanel {
    //An array of JButtons which all have colors
    private JButton[] buttonsWithColors;
    //Who should be notified when a button is pressed
    private ColorChanger whoToNotify;

    public LastFiveColors(ColorChanger colorChanger) {
        //Sets the colorChanger to notify
        whoToNotify = colorChanger;
        //Creates the button array
        buttonsWithColors = new JButton[5];
        //Creates a GridLayout for the buttons
        setLayout(new GridLayout(1, 5));

        for (int i = 0; i < 5; i++) {
            //Gives each button a default color and an anonymous inner class listener
            JButton colorButton = new JButton();
            colorButton.setBackground(Color.BLACK);
            colorButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    //When the button is pressed, tell the ColorChanger what color it was
                    JButton colorButton = (JButton) e.getSource();
                    whoToNotify.changeColor(colorButton.getBackground());
                }

            });
            //adds the button to the array and the frame
            buttonsWithColors[i] = colorButton;
            add(colorButton);
        }
    }

    public void addColor(Color color) {
        //Do nothing if most recent was used again
        if(buttonsWithColors[0].getBackground() != color) {
            //Change aka move all the colors by default
            int numColorsToChange = 5;
            for (int i = 1; i < 5; i++) {
                //If the color was found, only change colors up to that point
                if (color == buttonsWithColors[i].getBackground()) {
                    numColorsToChange = i + 1;
                    break;
                }
            }
            //Move colors over one, starting with where the color was found or at the end
            for (int i = (numColorsToChange - 1); i > 0; i--) {
                buttonsWithColors[i].setBackground(buttonsWithColors[i - 1].getBackground());
            }
            buttonsWithColors[0].setBackground(color);
        }
    }
}
