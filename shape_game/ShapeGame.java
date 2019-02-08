import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Color;
  
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

class ShapeGame extends JFrame { 

  static JFrame window;
  private static int windowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
  private static int windowHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
  JPanel gamePanel;
  Player player; 
  ArrayList<Enemy> enemies; 
   
  public static void main(String[] args) {
    window = new ShapeGame(); 
  }

  ShapeGame() { 
    super("My Game");  

    // create enemies and player
    player = new Player(0, 0, 50);

    //spawn 5 eneimies
    enemies = new ArrayList<Enemy>(); 
    for (int i = 0; i < 5; i++) {
      Random rand = new Random();
      int x = rand.nextInt(windowWidth - 50);
      int y = rand.nextInt(windowHeight - 50);
      enemies.add(new Enemy(x, y, 50, 50));
    }

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    this.setUndecorated(true);

    gamePanel = new GameAreaPanel();
    this.add(new GameAreaPanel());
      
    MyKeyListener keyListener = new MyKeyListener();
    this.addKeyListener(keyListener);

    this.requestFocusInWindow(); 
    this.setVisible(true);
  } 

  private class GameAreaPanel extends JPanel {
    public void paintComponent(Graphics g) {   
      super.paintComponent(g);
      setDoubleBuffered(true); 

      //move enemies
      for (Enemy enemy : enemies) {
        Random rand = new Random(); 
        int direction = rand.nextInt(4); 
        switch (direction) {
          case 0: 
            if (enemy.getX() > 0) {
              enemy.moveLeft();
            } 
            break; 
          case 1: 
            if (enemy.getY() < windowHeight - 50){
              enemy.moveUp();
            } 
            break;
          case 2: 
            if (enemy.getX() < windowWidth - 50){
              enemy.moveRight();
            }
            break; 
          case 3:
            if (enemy.getY() > 0){
              enemy.moveDown();
            }
            break;
        }
      }

      //check for collision
      for (int i = 0; i < enemies.size(); i++) {
        if (Math.abs(enemies.get(i).getX() - player.x) < 10 && Math.abs(enemies.get(i).getY() - player.y) < 10) {
          enemies.remove(i);
        }
      }

      //draw all squares
      for (Enemy enemy : enemies) {
        g.setColor(Color.CYAN);
        g.fillRect((int) enemy.getX(), (int) enemy.getY(), (int) enemy.getHeight(), (int) enemy.getWidth());
      }

      //draw player circle
      g.setColor(Color.PINK);
      g.fillOval((int) player.getX(), (int) player.getY(), (int) player.getRadius(), (int) player.getRadius()); 

      //repaint
      repaint();
    }
  }
  
  private class MyKeyListener implements KeyListener {

    public void keyPressed(KeyEvent e) {

      if (KeyEvent.getKeyText(e.getKeyCode()).equals("A")) {  //If 'D' is pressed
        if (player.x > 0) {
          player.moveLeft();
        } 
      } else if (KeyEvent.getKeyText(e.getKeyCode()).equals("O")) { 
        if (player.y < windowHeight - 50){
          player.moveUp();
        } 
      } else if (KeyEvent.getKeyText(e.getKeyCode()).equals("E")) { 
        if (player.x < windowWidth - 50){
          player.moveRight();
        }
      } else if (KeyEvent.getKeyText(e.getKeyCode()).equals("W")) { 
        if (player.y > 0){
          player.moveDown();
        }
      } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { 
        System.out.println("Quitting!"); 
        window.dispose();
      } 
    }   
    
    public void keyReleased(KeyEvent e) { }
    public void keyTyped(KeyEvent e) { }

  } 
}