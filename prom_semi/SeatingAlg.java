import java.util.Date; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Map;
import java.lang.Math;

import javax.swing.JOptionPane;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.GridBagLayout;
import java.net.URL;
import java.net.MalformedURLException;

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
  private static boolean showUISettings = true; 
  private static int maxRuntime = 5; 
  private static boolean bidirectionalFriendships = true;
  private static boolean communist = false; 
  private static boolean special = true; 

  /**
  * This method is the driving function for table generation. 
  * Any settings (ie. maxRuntime()) must be set before this method is 
  * called. 
  * @param students ArrayList of Students to be seated. 
  * @param tableSize  Integer number of students that can bes seating per table. 
  * @return Returns an ArrayList of Table objects. 
  */
  public ArrayList<Table> generateTables(ArrayList<Student> students, int tableSize) {
    if (showUISettings) { // start getting user input

      String timeInput = null; 
      while (timeInput == null) {
        timeInput = JOptionPane.showInputDialog(null, 
          "Please enter the amount of time you want the algorithm to run(seconds).", 
          "Seating Algorithm Settings",
          JOptionPane.INFORMATION_MESSAGE
        );
      }
      maxRuntime = Integer.parseInt(timeInput);

      Object[] friendshipOptions = {"Leave friendships", "Bidirectional"};
      int n = JOptionPane.showOptionDialog(null,
                  "Please select whether or not you want to assume friendships are bidirectional. \nAssuming bidirectional is favourable when there's little drama and/or people are lazy to fill in their friends.",
                  "Seating Algorithm Settings",
                  JOptionPane.YES_NO_CANCEL_OPTION,
                  JOptionPane.DEFAULT_OPTION,
                  null,
                  friendshipOptions,
                  friendshipOptions[1]);  
      bidirectionalFriendships(n == 0);

      Object[] communistOptions = {"Communism", "Capitalism"};
      n = JOptionPane.showOptionDialog(null,
                  "Please select which political theory you'd like to enforce to govern happiness over these people. \nSetting to 'communism' will cause the algorithm to optimize for a lower standard deviation (while taking higher scores with very similar standard deviations), \nwhile captalism will optimize for sum of happiness.",
                  "Seating Algorithm Settings",
                  JOptionPane.YES_NO_CANCEL_OPTION,
                  JOptionPane.DEFAULT_OPTION,
                  null,
                  communistOptions,
                  communistOptions[1]);  
      communist(n == 0);

      Object[] algOptions = {"Simple", "Special"};
      n = JOptionPane.showOptionDialog(null,
                  "Please specify which types of calculations you'd like to use. \nSimple simply calculates based on friendsAtTable/totalFriends, however, the complex and recommended\n calculations realizes that person with 20 friends, seated with five friends is likely similarly happy to a \n person with 5 friends seated with 5 friends.",
                  "Seating Algorithm Settings",
                  JOptionPane.YES_NO_CANCEL_OPTION,
                  JOptionPane.DEFAULT_OPTION,
                  null,
                  algOptions,
                  algOptions[1]); 
      special(n == 1);
    } // end getting user input


    // manage loading screen if applicable 
    JFrame frame = new JFrame();
    if (maxRuntime > 5) {
      JPanel panel = new JPanel();
      panel.setLayout(new GridBagLayout());
      
      URL url = SeatingAlg.class.getResource("/loading.gif");
      ImageIcon loadingIcon = new ImageIcon(url);
      JLabel loadGifLabel = new JLabel(loadingIcon);
      panel.add(loadGifLabel);

      frame.add(panel); 
      frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
      frame.setUndecorated(true);
      frame.setVisible(true);
    }


    // init algorithm
    studentsById = createStudentsById(students);
    long startTime = System.currentTimeMillis();
    numStudents = students.size();  

    // Process settings 
    if (bidirectionalFriendships) {
      students = makeFriendsBidirectional(students);
    }

    res = new ArrayList<Table>(); // result to be returned
    double score = 0; 
    double standardDeviation = 1 << 30; // only used for communism
    ArrayList<Double> lastScores = new ArrayList<Double>(); 
    double degreesOfChange = 1; // "factor" of relative number of changes to make. "1" changes all items.

    // start searching for solutions
    while (((new Date()).getTime() - startTime) < maxRuntime * 1000) {
      students = shuffleStudents(students, degreesOfChange);

      ArrayList<Table> tablesAttempt = new ArrayList<Table>();
      HashMap<String, Integer> studentsPlaced = new HashMap<String, Integer>();  

      int currStu = 0; 
      while (currStu < numStudents) { // while there are students to be seated
        // fill table with as many friends of students as possible
        Table currTable = new Table(tableSize); 
        while(currTable.getNumStudents() != tableSize && currStu < numStudents) {
          boolean f = false; 
          ArrayList<Student> toAdd = new ArrayList<Student>();
          for (Student s : currTable.getStudents()) {
            for (String friendID : s.getFriendStudentNumbers()) {
              if (!studentsPlaced.containsKey(friendID)) {
                toAdd.add(studentsById.get(friendID)); 
                studentsPlaced.put(friendID, 1);
                f = true;
                break;
              }
            }
          }
          if (f) {
            currTable.addStudent(toAdd.get(0));
            continue;
          }

          if (!studentsPlaced.containsKey(students.get(currStu).getStudentNumber())) {
            studentsPlaced.put(students.get(currStu).getStudentNumber(), 1);
            currTable.addStudent(students.get(currStu));
            currStu += 1; 
          } else {
            currStu += 1; 
          }
        }
        tablesAttempt.add(currTable);
      }

      // start running comparisons on this arrangement
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
          degreesOfChange += 0.5; // high learning rate that is likely too high for communism, because communism isn't supposed to "work" anyway. 
        } 
      } else {
        if (attemptScore > score) {
          degreesOfChange += 0.05; // degrees of change represents learning rate
          score = attemptScore;
          res = tablesAttempt;
        }
      }
    }

    frame.setVisible(false); // remove loading 
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
    meanAdjustedPercentSatisfaction /= numStudents; 
    System.out.println("\033[1mPercentage of friendships satisfied\033[0m: " + Math.round((friendshipsSatisfied * 1000.0) / totalFriendships) / 10.0);
    System.out.println("\033[1mMean percent satisfaction per person\033[0m: " + Math.round(meanPercentSatisfaction * 1000) / 10.0);
    System.out.println("\033[1mAdjusted mean percent satisfaction per person\033[0m: " + Math.round(meanAdjustedPercentSatisfaction * 1000) / 10.0);
    System.out.println("\033[1mMedian percent satisfaction per person\033[0m: " + Math.round(medianPercentSatisfaction[numStudents / 2] * 1000) / 10.0);
    System.out.println("\033[1mAdjusted median percent satisfaction per person\033[0m: " + Math.round(medianAdjustedPercentSatisfaction[numStudents / 2] * 1000) / 10.0);
    System.out.println(""); 
    System.out.println("\033[1mStandard deviation of percent satisfaction\033[0m: " + Math.round(standardDeviation(meanPercentSatisfaction, medianPercentSatisfaction) * 1000.0) / 10.0);
    System.out.println("\033[1mAdjusted standard deviation of percent satisfaction\033[0m: " + Math.round(standardDeviation(meanAdjustedPercentSatisfaction, medianAdjustedPercentSatisfaction) * 1000.0) / 10.0);
    System.out.println(""); 
    return;
  }

  /**
  * This method is a builder that sets preferences for your algorithm. If True, then a UI will appear when `generateTables()` is called.<br> Default is true
   * @param setting should be true if you want user to select settings with UI. Should be false to always use defaults / set settings in TicketingSystem.
  */
  public void showUISettings(boolean setting) {
    showUISettings = setting; 
  }

  /**
  * This method is a builder that sets preferences for your algorithm. If True, then friendships will be assumed to go both ways (if Person A is Person B's friend, then Person B is also Person A's friend)
   * @param setting should be true if you want bidirectional friendships, otherwise false. Default is true.
  */
  public void bidirectionalFriendships(boolean setting) {
    bidirectionalFriendships = setting; 
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

  /**
  * This method is a builder that sets preferences for the algorithm. <br>
  * If True, then the algorithm will use it's custom equation for calculating participant satisfaction.
  * @param setting should be true if you want to use the custom equation, otherwise false. Default is true.
  */
  public void special(boolean setting) {
    special = setting; 
  }

  /**
  * This method is a builder that sets preferences for the algorithm. <br>
  * Input should be the approximate number of seconds the algorithm will run. Default is 5.
  * @param setting approximate number of seconds to run algorithm.
  */
  public void maxRuntime(int setting) {
    maxRuntime = setting; 
  }

  private ArrayList<Student> makeFriendsBidirectional(ArrayList<Student> students) {
    // for each friendship, if it doesn't exist the other way, make it exist
    for (Student s : students) { 
      ArrayList<String> friendIDs = s.getFriendStudentNumbers();
      for (String fID : friendIDs) {
        for (int i = 0; i < students.size(); i++) {
          if (students.get(i).getStudentNumber().equals(fID)) {
            if (!(students.get(i).getFriendStudentNumbers()).contains(s.getStudentNumber())){
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


  private ArrayList<Student> shuffleStudents(ArrayList<Student> students, double factor) { 
    int changes = (int)((numStudents + 1) / factor); // this many swaps

    Random rand = new Random();
    for (int i = 0; i < changes; i++) {
      int first = rand.nextInt(numStudents);
      int second = rand.nextInt(numStudents);
      Collections.swap(students, first, second);
    }
    // also swap order of friends
    for (Student student : students) {
      if (student.getFriendStudentNumbers().size() > 1) {
        changes = (int)((factor + 1) / student.getFriendStudentNumbers().size()); 
        for (int i = 0; i < changes; i++) {
          ArrayList<String> friends = student.getFriendStudentNumbers();
          int first = rand.nextInt(friends.size());
          int second = rand.nextInt(friends.size());
          Collections.swap(friends, first, second);
          student.setFriendStudentNumbers(friends);
        }
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
    if (special) {
      double happinessByNumFriends = -0.75 / (2 * friendsAtTable + 1) + 0.75;
      double adjustmentByPercentFriends = 0; 
      if (totalFriends > 0) { 
        adjustmentByPercentFriends = -1/4.0 * (friendsAtTable / (totalFriends * 1.0) - 1) * (friendsAtTable / (totalFriends * 1.0) - 1) + 0.25;
      } else {
        adjustmentByPercentFriends = 1;
      }
      return happinessByNumFriends + adjustmentByPercentFriends;
    }

    // not special
    if (totalFriends == 0) {
      return 1; 
    } else {
      return (friendsAtTable / totalFriends * 1.0);
    }
  }

  private double standardDeviation(double mean, double[] values) {
    double sd = 0; 
    for (int i = 0; i < numStudents; i++) {
      sd = sd + Math.pow(values[i] - mean, 2);
    }
    return sd;
  }
}
