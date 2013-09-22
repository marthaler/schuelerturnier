import javax.swing.*;
import java.awt.*;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;

public class Crop extends JFrame {

    Image image;

    Insets insets;

    public Crop() {
        super();
        ImageIcon icon = new ImageIcon("/res/m1.png");
        image = icon.getImage();
        image = createImage(new FilteredImageSource(image.getSource(),
                new CropImageFilter(0, 0, 200, 200)));
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (insets == null) {
            insets = getInsets();
        }
        g.drawImage(image, insets.left, insets.top, this);
    }

    public static void main(String args[]) {
        JFrame f = new Crop();
        f.setSize(200, 200);
        f.show();
    }
}