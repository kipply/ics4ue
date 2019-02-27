import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.net.URL;

class LoadingPanel{

      public static void main(String args[]) {
            JFrame frame = new JFrame();
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());

            URL url = SeatingAlg.class.getResource("/loading.gif");
            ImageIcon loadingIcon = new ImageIcon(url);
            JLabel loadGifLabel = new JLabel(loadingIcon);
            panel.add(loadGifLabel);
            frame.add(panel); 
            frame.setUndecorated(true);
            frame.setLocation(0, 0);
            frame.pack();
            frame.setVisible(true);
      }
}

