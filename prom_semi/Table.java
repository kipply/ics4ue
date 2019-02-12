import java.util.ArrayList;

class Table { 

  private int size;
  private ArrayList<Student> students;

  Table(int initSize) { 
    size = initSize;
  }

  public ArrayList<Student> getStudents() { 
    return students; 
  }

  public void setStudents(ArrayList<Student> newStudents) { 
    students = newStudents;
  }
}
