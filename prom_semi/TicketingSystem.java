import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class TicketingSystem { 

  public static void main(String[] args) {
    ArrayList<Student> mockStudents = new ArrayList<Student>(); 
    Random rand = new Random();
    for (int i = 0; i < 100; i++) { // generate 100 students
      mockStudents.add(new Student(
        "My Name", 
        Integer.toString(rand.nextInt(100000000)), 
        new ArrayList<>(Arrays.asList("No Meat", "No dairy")),
        new ArrayList<>(Arrays.asList(Integer.toString(rand.nextInt(100000000)), Integer.toString(rand.nextInt(100000000))))       
      )); 
    }

    SeatingAlg mockSeatingAlg = new SeatingAlg();
    ArrayList<Table> tables = mockSeatingAlg.generateTables(mockStudents, 7); 

    for (Table table : tables) { 
      ArrayList<Student> students = table.getStudents();
      for (Student student : students) { 
        System.out.println(student.getStudentNumber());
      }
      System.out.println("----");
    }
  }
}
