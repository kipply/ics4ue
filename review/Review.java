
class Being {

}

class Person extends Being {

}

class Superhero extends Being {

}

class Kryptonion extends Superhero { 
  boolean canFly;

  Kryptonion(boolean startCanFly) {
    canFly = startCanFly; 
  }
}

public class Review {
  public static void main(String[] args) {
    Being puppy = new Being(); 
    assert puppy instanceof Being;
    assert !(puppy instanceof Person);

    Person sunnyLan = new Person(); 
    assert sunnyLan instanceof Being;
    assert sunnyLan instanceof Person;

    Superhero mrMangat = new Superhero();
    assert mrMangat instanceof Being; 
    assert mrMangat instanceof Superhero; 
    assert !(mrMangat instanceof Kryptonion);

    Kryptonion mrMangatButAKryptonion = new  Kryptonion(false);
    assert mrMangatButAKryptonion instanceof Being; 
    assert mrMangatButAKryptonion instanceof Superhero;
    assert mrMangatButAKryptonion instanceof Kryptonion;
    assert !mrMangatButAKryptonion.canFly;

    Kryptonion mrMangatButAFlyingKryptonion = new Kryptonion(true);
    assert mrMangatButAFlyingKryptonion instanceof Being; 
    assert mrMangatButAFlyingKryptonion instanceof Superhero;
    assert mrMangatButAFlyingKryptonion instanceof Kryptonion;
    assert mrMangatButAFlyingKryptonion.canFly;
  }
}