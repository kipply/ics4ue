import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class TicketingSystem { 

  public static void main(String[] args) {
    ArrayList<Student> mockStudents = new ArrayList<Student>(); 
    Random rand = new Random();
    String[] studentNumbers = new String[100]; 

    for (int i = 0; i < studentNumbers.length; i++) { // generate 100 students
      studentNumbers[i] = Integer.toString(rand.nextInt(100000000));
    }
    for (int i = 0; i < studentNumbers.length; i++) { // generate 100 students
      mockStudents.add(new Student(
        randName(rand), 
        studentNumbers[i], 
        new ArrayList<>(Arrays.asList("No Meat", "No dairy")),
        new ArrayList<>(Arrays.asList(
          studentNumbers[rand.nextInt(studentNumbers.length)], 
          studentNumbers[rand.nextInt(studentNumbers.length)]
        ))       
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

  private static String randName(Random random) {
    String letters = "pyfgcrlaoeuidhtnsqjkxbmwvz"; 
    int nameLength = random.nextInt(6) + 5;
    String res = ""; 
    for (int i = 0; i < nameLength; i++) {
      res += letters.charAt(random.nextInt(26));
    }
    return res;
  }


}
