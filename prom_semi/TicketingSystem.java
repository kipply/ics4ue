import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.HashSet;
import java.util.Scanner;
import java.io.PrintWriter;

public class TicketingSystem {
  static HashSet<String> picked = new HashSet<String>();
    final static int NUM_STUDENTS = 100, STUDENTS_PER_TABLE = 40, EDGES_PER_STUDENT = 5;
    static Random rng = new Random();
    static boolean[][] friend;
    static int[] friendCnt;
    static int totFriends=0;

  public static void main(String[] args) throws FileNotFoundException {
    SeatingAlg sa = new SeatingAlg();
    ArrayList<Student> students = new ArrayList<Student>();
    // genFile(5);
    Scanner in = new Scanner(new File("kust.txt"));
    while (in.hasNext()) {
      students.add(parse(in.nextLine()));
    }
    sa.special(false);
    // sa.bidirectionalFriendships(true);
    sa.maxRuntime(2);
    sa.showUISettings(false);
    ArrayList<Table> tables = sa.generateTables(students, STUDENTS_PER_TABLE);

    // output tables with names and student numbers
    for (Table table : tables) { 
      ArrayList<Student> s = table.getStudents();
      for (Student student : s) { 
        System.out.println(student.getName() + " " + student.getStudentNumber());
      }
      System.out.println("----");
    }


    sa.outputStatistics();


  }

  private static Student parse(String in) {
    String[] part = in.split(",");
    //String[] diet = part[2].split(" ");
    String[] friends;
    ArrayList<String> cust = new ArrayList<String>();
    if (part.length > 1){
      friends = part[1].split(" ");
      cust = new ArrayList(Arrays.asList(friends));
    }
    //}
//      return new Student(part[0], part[1], new ArrayList(Arrays.asList(diet)), cust);
      return new Student("", part[0], new ArrayList(), cust);
  }

    // generates student numbers
    public static String genNumber() {
        String number = "";
        for (int j = 0; j < 9; ++j)
            number += Character.toString((char) (rng.nextInt(10) + '0'));
        return number;
    }

  public static void genFile(int bidirChance) throws FileNotFoundException {

    friend = new boolean[NUM_STUDENTS][NUM_STUDENTS];
    friendCnt = new int[NUM_STUDENTS];

    // generates students
    ArrayList<String> students = new ArrayList<String>();
    ArrayList<Student> stus=new ArrayList<Student>();
    for (int i = 0; i < NUM_STUDENTS; ++i) {
        String number = genNumber();
        while (picked.contains(number))
            number = genNumber();
        students.add(number);
    }

    // generates friends
        for (int i = 0; i < NUM_STUDENTS; ++i) {
            // number of friends current person has
            int numFriends = Math.min(rng.nextInt(EDGES_PER_STUDENT + 1),NUM_STUDENTS-1);
            totFriends += numFriends;

            // list storing person's friends
            ArrayList<String> friends = new ArrayList<String>();

            // checks for existing friends
            for (int j = 0; j < NUM_STUDENTS; ++j) {
                if (friend[i][j]) {
                    friends.add(students.get(j));
                }
            }

            // generates remaining friends
            for (int j = friends.size(); j < numFriends; ++j) {
                // generates current friend,if friendship is mutual
                int idx = rng.nextInt(NUM_STUDENTS), bidir = rng.nextInt(bidirChance);
                while (idx == i || friend[i][idx]
                        || (bidir > 0 && (friendCnt[idx] >= EDGES_PER_STUDENT || friend[idx][i])))
                    idx = rng.nextInt(NUM_STUDENTS);

                // if friendship is mutual generates other edge
                if (bidir > 0) {
                    friend[idx][i] = true;
                    ++friendCnt[idx];
                }

                // adds friend
                friend[i][idx] = true;
                ++friendCnt[i];
                friends.add(students.get(idx));
            }
            stus.add(new Student("A",students.get(i),new ArrayList<String>(),friends));
        }

        PrintWriter output = new PrintWriter(new File("kust.txt"));
        for (int i = 0; i < stus.size(); ++i) {
            output.print(stus.get(i).getStudentNumber() + ",");
            for (String j : stus.get(i).getFriendStudentNumbers())
                output.print(j + " ");
            output.println();
        }
        output.close();
    }
}
