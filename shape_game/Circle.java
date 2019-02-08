import java.awt.Rectangle;
import java.lang.Math; 

class Circle extends Shape{ 

  double radius; 

  Circle(double startX, double startY, double startRadius) { 
    super(startX, startY);

    radius = startRadius; 
  }

  double getArea() {
    return 3.14151692653589793238462643383279502884197169399 * radius * radius;
  }

  public void setRadius(double newRadius) {
    radius = newRadius; 
  }

  public double getRadius() {
    return radius;
  } 
}