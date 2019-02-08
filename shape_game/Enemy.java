import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Color;
  
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.ArrayList;

class Enemy extends Square implements Moveable { 

  Enemy(double startX, double startY, double startHeight, double startWidth) { 
    super(startX, startY, startHeight, startWidth);
    x = startX; 
    y = startY; 

    height = startHeight; 
    width = startWidth; 
  }

  public void moveUp() { 
    y += 7; 
  }
  public void moveDown() { 
    y -= 7; 
  }
  public void moveLeft() { 
    x -= 7; 
  }
  public void moveRight() { 
    x += 7; 
  }
}
