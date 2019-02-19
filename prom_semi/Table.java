import java.util.ArrayList;

/**
* The Table class provides methods and tools for storing tables
*
* @author  Carol Chen
* @version 4.2.0
* @since   2019-02-14
*/
public class Table { 

  private int size;
  private ArrayList<Student> students;

  /**
  * Constructor for Table object creation. 
  * @param initSize Integer that represents the size of the table. 
  */
  Table(int initSize) { 
    size = initSize;
    students = new ArrayList<Student>();
  }

  /**
  * @return ArrayList of students seated at this table. 
  */
  public ArrayList<Student> getStudents() { 
    return students; 
  }

  /**
  * @return Integer value of the number of students seated at this table.  
  */
  public int getNumStudents() { 
    return students.size(); 
  }

  /**
  * @param newStudents ArrayList of Students to place at this table. Overrides any existing students.
  */
  public void setStudents(ArrayList<Student> newStudents) { 
    students = newStudents;
  }

  /**
  * @param student Student object to add to this table. 
  */
  public void addStudent(Student student) { 
    students.add(student);
  }

  /**
  * @param addedStudents ArrayList of students to add to this table.
  */
  public void addStudents(ArrayList<Student> addedStudents) { 
    students.addAll(addedStudents);
  }
}
