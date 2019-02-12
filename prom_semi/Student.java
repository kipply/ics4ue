import java.util.ArrayList;

class Student { 

  private String name;
  private String studentNumber; 
  private ArrayList<String> dietaryRestrictions; 
  private ArrayList<String> friendStudentNumbers; 

  Student(String initName, 
          String initStudentNumber, 
          ArrayList<String> initDietaryRestrictions, 
          ArrayList<String> initFriendStudentNumbers) { 
    name = initName; 
    studentNumber = initStudentNumber; 
    dietaryRestrictions = initDietaryRestrictions;
    friendStudentNumbers = initFriendStudentNumbers;
  }

  public String getName() { 
    return name;
  }

  public void setName(String newName) { 
    name = newName;
  }

  public String getStudentNumber() { 
    return studentNumber; 
  }

  public void studentNumber(String newStudentNumber) { 
    studentNumber = newStudentNumber; 
  }

  public ArrayList<String> getDietaryRestrictions() {
    return dietaryRestrictions;
  }

  public void setDietaryRestrictions(ArrayList<String> newDietaryRestrictions) {
    dietaryRestrictions = newDietaryRestrictions;
  }

  public ArrayList<String> getFriendStudentNumbers() {
    return friendStudentNumbers;
  }

  public void setFriendStudentNumbers(ArrayList<String> newFriendStudentNumbers) {
    friendStudentNumbers = newFriendStudentNumbers;
  }
}
