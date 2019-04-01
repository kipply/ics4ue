import java.util.ArrayList;

public class Test {

 private static final int MAX = 100;
 private static final boolean canSet = true;

 public static void main(String[] args) throws Exception {
  int countAdd = 0;
  int countRemoveByIndex = 0;
  int countRemoveByT = 0;
  int countSet = 0;
  int countClear = 0;

  SimpleLinkedList<String> list = new SimpleLinkedList<String>();

  // System.out.println(list.size());
  ArrayList<String> listShadow = new ArrayList<String>();
  for (int i = 0; i < MAX; i++) {
   int oper = (int) (9 * Math.random());
   switch (oper) {
   case (0):
   case (1):
   case (2):
   case (3):
   case (4):
   case (5): {
    list.add(Integer.toString(i));
    listShadow.add(Integer.toString(i));
    countAdd++;
    // System.out.println("Added " + Integer.toString(i));
    break;
   }
   case (6): {
    if (list.size() != 0) {
     int removeIndex = (int) ((list.size() - 1) * Math.random());
     list.remove(removeIndex);
     listShadow.remove(removeIndex);
     countRemoveByIndex++;
     // System.out.println("Removed index " + removeIndex);
    }
    break;
   }
   case (7): {
    if (i < MAX / 4) {
     list.clear();
     listShadow.clear();
     countClear++;
      // System.out.println("Cleared");
    }
    break;
   }
   case (8): {
    if (list.size() != 0) {
     int removeT = (int) ((list.size() - 1) * Math.random());
     if(list.remove(Integer.toString(removeT))){
      countRemoveByT++;
       listShadow.remove(Integer.toString(removeT));
     }
     // System.out.println("Removed " + removeT);
    }
    break;
   }
   case (9): {
  /*  if (list.size() != 0 && canSet) {
     int removeIndex = (int) ((list.size() - 1) * Math.random());
     list.set(removeIndex, Integer.toString(i));
     listShadow.set(removeIndex, Integer.toString(i));
     countSet++;
    } */
    break;
   }
   }

    // int countCorrect = 0;
    // if (listShadow.size() != list.size()) {
    //   System.out.println("L!");
    //   for (int e = 0; e < list.size(); e++ ) { System.out.print(list.get(e) + " ");} System.out.println();
    //   for (int e = 0; e < listShadow.size(); e++ ) { System.out.print(listShadow.get(e) + " ");} System.out.println();
    //     break;
    // }
    // for (int j = 0; j < list.size(); j++) {
    //  if (list.get(j).equals(listShadow.get(j))) {
    //   countCorrect++;
    //  }
    // }
    // if (countCorrect != list.size()) {
    //   System.out.println("L!");
    //   for (int e = 0; e < list.size(); e++ ) { System.out.print(list.get(e) + " ");} System.out.println();
    //   for (int e = 0; e < listShadow.size(); e++ ) { System.out.print(listShadow.get(e) + " ");} System.out.println();
    //     break;
    // }
  }

  int countCorrect = 0;
  System.out.println(list.size() + " " + listShadow.size());
  for (int j = 0; j < list.size(); j++) {
    System.out.print(list.get(j) + " ");
   if (list.get(j).equals(listShadow.get(j))) {
    countCorrect++;
   }
  }
  System.out.println();
  System.out.println("Your data score: " + countCorrect + "/"
    + list.size());

  int countIndexOf = 0;
  for (int k = 0; k < MAX; k++) {
   if (list.indexOf(Integer.toString(k)) == listShadow.indexOf(Integer.toString(k))) {
    countIndexOf++;
   } else {
    System.out.println(k + " " + list.indexOf(Integer.toString(k)) + " " + listShadow.indexOf(Integer.toString(k)));
   }
  }

  System.out.println("Your indexOf score: " + countIndexOf + "/" + MAX);

  System.out.println();
  System.out.println(countAdd + " add operations.");
  System.out.println(countRemoveByIndex + " remove by index operations.");
  System.out.println(countRemoveByT + " remove by T operations.");
  System.out.println(countClear + " clear operations.");
  System.out.println(countSet + " set operations.");
 }
}
