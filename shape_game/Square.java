import java.awt.Rectangle;
import java.lang.Math; 

class Square extends Shape{ 

  double height, width; 

  Square(double startX, double startY, double startHeight, double startWidth) { 
    super(startX, startY);

    height = startHeight; 
    width = startWidth; 
  }

  double getArea() {
    return height * width;
  }

  public void setArea(double area) {
    double length = Math.pow(area, 0.5); 
    setHeight(length); 
    setWidth(length); 
  }

  public void setHeight(double newHeight) {
    height = newHeight;
  } 

  public void setWidth(double newWidth) { 
    width = newWidth;
  }

  public double getHeight() {
    return height;
  } 

  public double getWidth() { 
    return width;
  }
}