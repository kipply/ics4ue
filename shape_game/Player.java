import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Color;
  
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.ArrayList;


class Player extends Circle implements Scalable, Moveable { 

  Player(double startX, double startY, double startRadius) { 
    super(startX, startY, startRadius);
  }

  public void grow() {
    radius += 5; 
  } 

  public void shrink() {
    radius += 5; 
  } 

  public void moveUp() { 
    y += 10; 
  }

  public void moveDown() { 
    y -= 10; 
  }
  public void moveLeft() { 
    x -= 10; 
  }
  public void moveRight() { 
    x += 10; 
  }

}
