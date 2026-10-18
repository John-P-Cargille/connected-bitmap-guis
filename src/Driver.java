import java.util.ArrayList;
public class Driver {
    public static void main(String[] args) {

        Icon icon1 = new Icon(5,5);
        icon1.setPixelRBGValue(0, 0, 255, 0, 0);
        icon1.setPixelRBGValue(0, 1, 150, 0, 0);
        icon1.setPixelRBGValue(0, 2, 100, 0, 0);
        icon1.setPixelRBGValue(0, 3, 50, 0, 0);
        icon1.setPixelRBGValue(0, 4, 0, 255, 0);
        icon1.setPixelRBGValue(1, 4, 0, 150, 0);
        icon1.setPixelRBGValue(2, 4, 0, 100, 0);
        icon1.setPixelRBGValue(3, 4, 0, 50, 0);
        icon1.setPixelRBGValue(4, 4, 0, 0, 255);
        icon1.setPixelRBGValue(4, 3, 0, 0, 150);
        icon1.setPixelRBGValue(4, 2, 0, 0, 100);
        icon1.setPixelRBGValue(4, 1, 0, 0, 50);
        icon1.setPixelRBGValue(4, 0, 255, 255, 255);
        icon1.setPixelRBGValue(3, 0, 150, 150, 150);
        icon1.setPixelRBGValue(2, 0, 100, 100, 100);
        icon1.setPixelRBGValue(1, 0, 50, 50, 50);
        BitMapCreator bitMapGod = new BitMapCreator(icon1, "/C://Users//saxop//IdeaProjects//OOP//Project1_Bit_Maps/BitMap.BMP");
        System.out.println(icon1.toString());
    }
}