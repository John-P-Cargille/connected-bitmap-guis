import java.awt.Color;
import java.util.Random;

//Randomizes the color of edits made by other editors
public class RandomColorDecorator extends IconEditDecorator {

    //Public constructor setting the decorated object (Decorator/IconEdit)
    public RandomColorDecorator(IconEditInterface decoratedIconEdit) {
        super(decoratedIconEdit);
    }

    //Returns a random color for the edit
    @Override
    public Color getColor() {
        //Creates a new Random and then a new color with three random color values
        Random rng = new Random();
        int redVal = rng.nextInt(256);
        int greenVal = rng.nextInt(256);
        int blueVal = rng.nextInt(256);
        Color randomColor = new Color(redVal, greenVal, blueVal);
        return randomColor;
    }

}
