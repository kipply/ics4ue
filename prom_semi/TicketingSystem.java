import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class TicketingSystem { 
  static int numStudents = 100; 
  static int maxPerTable = 7; 
  static String[] possibleDietaryRestrictions = {"vegetarian", "vegan", "peanut allergy", "lactose intolerant", "halal", "gluten free"};
  
  public static void main(String[] args) {
    ArrayList<Student> mockStudents = new ArrayList<Student>(); 
    Random rand = new Random();

    // Present student numbers
    String[] studentNumbers = new String[numStudents]; 
    for (int i = 0; i < numStudents; i++) { 
      studentNumbers[i] = Integer.toString(rand.nextInt(100000000));
    }

    // Start generating students
    for (int i = 0; i < numStudents; i++) { 

      // Create fake friends list
      int lenMockFriends = rand.nextInt(8); 
      ArrayList<String> mockFriends = new ArrayList<String>();
      for (int j = 0; j < lenMockFriends; j++) { 
        mockFriends.add(studentNumbers[rand.nextInt(studentNumbers.length)]); 
      }

      // Create fake dietary restrictions
      int lenMockDietaryRestrictions = rand.nextInt(3); 
      ArrayList<String> mockDietaryRestrictions = new ArrayList<String>();
      for (int j = 0; j < lenMockDietaryRestrictions; j++) { 
        mockDietaryRestrictions.add(possibleDietaryRestrictions[rand.nextInt(possibleDietaryRestrictions.length)]); 
      }

      // add the student
      mockStudents.add(new Student(
        randName(rand, true), 
        studentNumbers[i], 
        mockDietaryRestrictions,
        mockFriends    
      )); 
    }

    SeatingAlg mockSeatingAlg = new SeatingAlg();
    ArrayList<Table> tables = mockSeatingAlg.generateTables(mockStudents, 7); 

    for (Table table : tables) { 
      ArrayList<Student> students = table.getStudents();
      for (Student student : students) { 
        System.out.println(student.getName() + " " + student.getStudentNumber());
      }
      System.out.println("----");
    }
  }

  private static String randName(Random random, boolean isFirst) {

    String letters = "pyfgcrlaoeuidhtnsqjkxbmwvz"; 
    int nameLength = random.nextInt(6) + 5;
    String res = ""; 
    for (int i = 0; i < nameLength; i++) {
      res += letters.charAt(random.nextInt(26));
    }
    res = res.substring(0, 1).toUpperCase() + res.substring(1);
    if (isFirst){
      return res + " " + randName(random, false);
    } else { 
      return res;
    }
  }
}
