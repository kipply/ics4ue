import java.awt.Rectangle;

abstract class Shape { 

  double x, y; 
  Rectangle boundingBox; 

  Shape(double startX, double startY) { 
    x = startX; 
    y = startY; 
  }

  abstract double getArea(); 
  
  public Rectangle getBoundingBox() {
    return boundingBox;
  } 

  public void setX(double newX) {
    x = newX;
  } 

  public void setY(double newY) { 
    y = newY;
  }

  public double getX() {
    return x;
  } 

  public double getY() { 
    return y;
  }

}