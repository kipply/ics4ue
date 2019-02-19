import java.util.Date; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.lang.Math;

/**
* The SeatingAlg class provides methods and tools for generating
* seating arrangements given an ArrayList of Students
*
* @author  Carol Chen
* @version 4.2.0
* @since   2019-02-14
*/
public class SeatingAlg {

  private int numStudents;
  private HashMap<String, Student> studentsById; 
  ArrayList<Table> res; 

  // Settings
  private static int maxRuntime = 1; 
  private static boolean bidirectionalFriendship = true;
  private static boolean communist = false; 

  /**
  * This method is the driving function for table generation. 
  * Any settings (ie. setMaxRuntime()) must be set before this method is 
  * called. 
  * @param students ArrayList of Students to be seated. 
  * @param tableSize  Integer number of students that can bes seating per table. 
  * @return Returns an ArrayList of Table objects. 
  */
  public ArrayList<Table> generateTables(ArrayList<Student> students, int tableSize) {
    studentsById = createStudentsById(students);
    long startTime = System.currentTimeMillis();
    numStudents = students.size();  

    // Process settings 
    if (bidirectionalFriendship) {
      students = makeFriendsBidirectional(students);
    }


    students = sortStudentsByNumFriends(students);
    res = new ArrayList<Table>();
    double score = 0; 
    double standardDeviation = 0; 
    while (((new Date()).getTime() - startTime) < maxRuntime * 1000) {
      students = shuffleStudents(students);
      
      ArrayList<Table> tablesAttempt = new ArrayList<Table>();
      HashMap<String, Integer> studentsPlaced = new HashMap<String, Integer>();  

      for (Student student : students) { 

        // if student not yet placed
        if (!studentsPlaced.containsKey(student.getStudentNumber())) {

          Table currTable = new Table(tableSize); 
          ArrayList<String> shuffledFriends = student.getFriendStudentNumbers();
          Collections.shuffle(shuffledFriends);

          studentsPlaced.put(student.getStudentNumber(), tablesAttempt.size()); 
          currTable.addStudent(student);
          for (String friendID : shuffledFriends) {
            if (currTable.getNumStudents() == tableSize) {
              break;
            }
            if (!studentsPlaced.containsKey(friendID)) {
              studentsPlaced.put(friendID, tablesAttempt.size()); 
              currTable.addStudent(studentsById.get(friendID));
            }
          }
          tablesAttempt.add(currTable);
          currTable = new Table(tableSize); 
        }
      }
      
      double attemptScore = calcSeatingScore(tablesAttempt);
      if (communist) {
        double sd = 0;
        double mean = 0; 
        double[] scores = new double[numStudents]; 
        int curr = 0; 
        for (Table t : tablesAttempt) {
          for (Student s : t.getStudents()) {
            scores[curr] = calcPersonScore(s, getFriendsAtTable(t, s));
            mean += scores[curr];
            curr++; 
          }
        }
        mean /= numStudents; 

        sd = standardDeviation(mean, scores);
        if (((sd - standardDeviation) / ((sd + standardDeviation) / 2.0) < 0.05 && attemptScore > score) || sd < standardDeviation){
          score = attemptScore; 
          res = tablesAttempt; 
          standardDeviation = sd; 
        } 
      } else {
        if (attemptScore > score) {
          score = attemptScore; 
          res = tablesAttempt;
        }
      }
    }

    res = groupTables(res, tableSize);
    return res;
  }

  /**
  * This method will print some statistics into the console. Must be called after 
  * generateTables(), and uses the result of the last generation. <br>
  * Full details on output: <br>
  * Percentage of friendships satisfied: Number of friendships seated together / total number of friendships<br>
  * Mean percent satisfaction per person: Average friendsAtTable / total friends per person. People with no friends are assumed to be fully happy, as they were satisfied to the full ability of this software. <br>
  * Adjusted mean percent satisfaction per person: Uses the adjusted scores that are actually used to pick the optimal seating arrangement. The calculation is (-0.75 / (2 * friends at table + 1) + 0.75) + (-1/4.0) * (friendsAtTable / (totalFriends * 1.0) - 1)^2 + 0.25<br>
  * Median percent satisfaction per person: Median percent satisfactions (friends at table / total friends)<br>
  * Adjusted median percent satisfaction per person: Adjusted median percent satisfaction (uses same calculations described in adjusted mean percent satisfaction)<br>
  */
  public void outputStatistics() {
    // standard deviation of percent satisfaction per person
    // adjusted standard deviation of satisfaction per person
    // top ten unhappy people 
    int totalFriendships = 0; 
    int friendshipsSatisfied = 0; 
    double meanPercentSatisfaction = 0; 
    double[] medianPercentSatisfaction = new double[numStudents]; 
    double meanAdjustedPercentSatisfaction = 0; 
    double[] medianAdjustedPercentSatisfaction = new double[numStudents]; 

    int iter = 0; 
    for (Table t : res) {
      int studentsInTable = t.getNumStudents();
      for (Student s : t.getStudents()) {
        int friendsAtTable = getFriendsAtTable(t, s); 
        friendshipsSatisfied += friendsAtTable; 
        totalFriendships += s.getFriendStudentNumbers().size();
        
        if (s.getFriendStudentNumbers().size() > 0) {
          meanPercentSatisfaction += (friendsAtTable * 1.0) /  s.getFriendStudentNumbers().size(); 
          meanAdjustedPercentSatisfaction += calcPersonScore(s, friendsAtTable); 
          medianPercentSatisfaction[iter] = (friendsAtTable * 1.0) /  s.getFriendStudentNumbers().size(); 
          medianAdjustedPercentSatisfaction[iter] = calcPersonScore(s, friendsAtTable); 
        } else {
          meanPercentSatisfaction += 1;
          meanAdjustedPercentSatisfaction += 1;
          medianPercentSatisfaction[iter] = 1; 
          medianAdjustedPercentSatisfaction[iter] = 1; 
        }

        Map<String, Double> studentScoreAdjusted = new HashMap<String, Double>();
        studentScoreAdjusted.put(s.getStudentNumber(), calcPersonScore(s, friendsAtTable));

        iter++;
      }
    }
    meanPercentSatisfaction /= numStudents; 
    System.out.println("\033[1mPercentage of friendships satisfied\033[0m: " + Math.round((friendshipsSatisfied * 1000.0) / totalFriendships) / 10.0);
    System.out.println("\033[1mMean percent satisfaction per person\033[0m: " + Math.round(meanPercentSatisfaction * 1000) / 10.0);
    System.out.println("\033[1mAdjusted mean percent satisfaction per person\033[0m: " + Math.round(meanAdjustedPercentSatisfaction * 1000) / 100.0);
    System.out.println("\033[1mMedian percent satisfaction per person\033[0m: " + Math.round(medianPercentSatisfaction[numStudents / 2] * 1000) / 10.0);
    System.out.println("\033[1mAdjusted median percent satisfaction per person\033[0m: " + Math.round(medianAdjustedPercentSatisfaction[numStudents / 2] * 1000) / 10.0);
    System.out.println(""); 
    System.out.println("\033[1mStandard deviation of percent satisfaction\033[0m: " + Math.round(standardDeviation(meanPercentSatisfaction, medianPercentSatisfaction) * 1000.0) / 1000.0);
    System.out.println("\033[1mAdjusted standard deviation of percent satisfaction\033[0m: " + Math.round(standardDeviation(meanAdjustedPercentSatisfaction, medianAdjustedPercentSatisfaction) * 1000.0) / 1000.0);
    System.out.println(""); 
  }

  /**
  * This method is a builder that sets preferences for your algorithm. If True, then friendships will be assumed to go both ways (if Person A is Person B's friend, then Person B is also Person A's friend)
   * @param setting should be true if you want bidirectional friendships, otherwise false. Default is true.
  */
  public void bidirectionalFriendships(boolean setting) {
    bidirectionalFriendship = setting; 
  }

  /**
  * This method is a builder that sets preferences for the algorithm. <br>
  * If True, then the algorithm will optimize for lower standard deviation rather than greater happiness.
  * This algorithm will attempt to pick higher scores if the standard deviation is within 5%.
  * @param setting should be true if you want to implement communism, otherwise false. Default is false.
  */
  public void communist(boolean setting) {
    communist = setting; 
  }

  private ArrayList<Student> makeFriendsBidirectional(ArrayList<Student> students) {
    for (Student s : students) { 
      ArrayList<String> friendIDs = s.getFriendStudentNumbers(); 
      for (String fID : friendIDs) {
        for (int i = 0; i < students.size(); i++) {
          if (students.get(i).getStudentNumber() == fID) {
            if (!students.get(i).getFriendStudentNumbers().contains(s.getStudentNumber())){
              ArrayList<String> updatedFriends = students.get(i).getFriendStudentNumbers(); 
              updatedFriends.add(s.getStudentNumber()); 
              students.get(i).setFriendStudentNumbers(updatedFriends);
            }
          }
        }
      }
    }
    return students; 
  }

  // shuffle students without losing sorting integrity
  private ArrayList<Student> shuffleStudents(ArrayList<Student> students) { 
    int currNumFriends = students.get(0).getFriendStudentNumbers().size(); 
    int prev = 0; // stores index of the last change in number of friends
    ArrayList<Student> res = new ArrayList<Student>(); 
    ArrayList<Student> extension = new ArrayList<Student>(); // temporary variable used to add to res
    
    for (int i = 0;  i < numStudents; i++) {
      if (students.get(i).getFriendStudentNumbers().size() != currNumFriends) {
        extension = new ArrayList<>(students.subList(prev, i));
        prev = i; 
        currNumFriends = students.get(i).getFriendStudentNumbers().size();
        Collections.shuffle(extension); 
        res.addAll(extension); 
      }
    }

    extension = new ArrayList<>(students.subList(prev, numStudents));
    Collections.shuffle(extension); 
    res.addAll(extension); 

    return res;
  }

  // bubble through students to sort them in increasing number of friends
  private ArrayList<Student> sortStudentsByNumFriends(ArrayList<Student> students) {
    boolean swap;
    Student temp;

    for (int i = 0; i < numStudents - 1; i++) {
      swap = false;
      for (int j = 0; j < numStudents - i - 1; j++) { 
        if (students.get(j).getFriendStudentNumbers().size() > students.get(j + 1).getFriendStudentNumbers().size()) {
          temp = students.get(j); 
          students.set(j, students.get(j + 1)); 
          students.set(j + 1, temp); 
          swap = true; 
        }
      }
      if (!swap) { 
        break;
      }
    }
    return students; 
  }

  private HashMap<String, Student> createStudentsById(ArrayList<Student> students) {
    HashMap<String, Student> res = new HashMap(); 
    for (Student student : students) {
      res.put(student.getStudentNumber(), student);
    }
    return res;
  }

  private ArrayList<Table> groupTables(ArrayList<Table> tables, int tableSize) {
    ArrayList<Table> res = new ArrayList<Table>();
    Table temp = new Table(tableSize);
    int tempSum = 0;

    for (Table t : tables) { 
      if (tempSum + t.getNumStudents() <= tableSize) {
        temp.addStudents(t.getStudents()); 
        tempSum += t.getNumStudents();
      } else {
        res.add(temp); 
        temp = new Table(tableSize);
        temp.addStudents(t.getStudents());
        tempSum = t.getNumStudents();
      }
    }
    if (temp.getNumStudents() > 0) {
        res.add(temp);
    }
    return res;
  }

  private double calcSeatingScore(ArrayList<Table> tables) {
    double score = 0; 
    for (Table t :  tables) { 
      for (Student s : t.getStudents()) {
        score += calcPersonScore(s, getFriendsAtTable(t, s));
      }
    }
    return score; 
  }

  private int getFriendsAtTable(Table table, Student student) {
    int friendsAtTable = 0; 
    for (String f : student.getFriendStudentNumbers()) { 
      if (table.getStudents().contains(studentsById.get(f))) {
        friendsAtTable++; 
      }
    }
    return friendsAtTable;
  }

  private double calcPersonScore(Student student, int friendsAtTable) {
    int totalFriends = student.getFriendStudentNumbers().size(); 
    double happinessByNumFriends = -0.75 / (2 * friendsAtTable + 1) + 0.75;
    double adjustmentByPercentFriends = 0; 
    if (totalFriends > 0) { 
      adjustmentByPercentFriends = -1/4.0 * (friendsAtTable / (totalFriends * 1.0) - 1) * (friendsAtTable / (totalFriends * 1.0) - 1) + 0.25;
    } else {
      adjustmentByPercentFriends = 1;
    }
    return happinessByNumFriends + adjustmentByPercentFriends;
  }

  private double standardDeviation(double mean, double[] values) {
    double sd = 0; 
    for (int i = 0; i < numStudents; i++) {
      sd = sd + Math.pow(values[i] - mean, 2);
    }
    return sd;
  }
}
