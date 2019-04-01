import java.util.Random;
import java.util.Arrays;

class Sorting {
  public static void main(String[] args) {     

    int data[] = generateNumberArray(10000);
    int[] tempArray; // a temp holder for data as it is passed to methods
    long startTime, endTime;
    double elapsedTime;

    System.out.println("Array generated: ");
    displayArray(data);


    //Testing Selection Sort -----------------
    System.out.println("\nSorting with Selection sort:");
    tempArray = Arrays.copyOf(data,data.length); //to keep original arrays safe from modification
    startTime = System.nanoTime();     //start time

    tempArray = selectionSort(tempArray);

    endTime = System.nanoTime();  //end time
    elapsedTime = (endTime - startTime) / 1000000.0;

    // displayArray(tempArray);
    System.out.println("The sort took: " + elapsedTime + "ms");


    //Testing Bubble Sort -----------------
    System.out.println("\nSorting with Bubble sort:");  
    tempArray = Arrays.copyOf(data,data.length); //to keep original arrays safe from modification
    startTime = System.nanoTime();     //start time

    tempArray = bubbleSort(tempArray);

    endTime = System.nanoTime();  //end time
    elapsedTime = (endTime - startTime) / 1000000.0;

    // displayArray(tempArray);
    System.out.println("The sort took: " + elapsedTime + "ms");

    //Testing Insertion Sort -----------------
    System.out.println("\nSorting with Insertion sort:");  
    tempArray = Arrays.copyOf(data,data.length); //to keep original arrays safe from modification
    startTime = System.nanoTime();     //start time

    tempArray = insertionSort(tempArray);

    endTime = System.nanoTime();  //end time
    elapsedTime = (endTime - startTime) / 1000000.0;

    // displayArray(tempArray);
    System.out.println("The sort took: " + elapsedTime + "ms");

    //Testing Merge Sort -----------------
    System.out.println("\nSorting with Merge sort:");  
    tempArray = Arrays.copyOf(data,data.length); //to keep original arrays safe from modification
    startTime = System.nanoTime();     //start time

    tempArray = mergeSort(tempArray);

    endTime = System.nanoTime();  //end time
    elapsedTime = (endTime - startTime) / 1000000.0;

    // displayArray(tempArray);
    System.out.println("The sort took: " + elapsedTime + "ms");

    //Testing Quick Sort -----------------
    System.out.println("\nSorting with Quick sort:");  
    tempArray = Arrays.copyOf(data,data.length); //to keep original arrays safe from modification
    startTime = System.nanoTime();     //start time

    tempArray = quickSort(tempArray);

    endTime = System.nanoTime();  //end time
    elapsedTime = (endTime - startTime) / 1000000.0;

    // displayArray(tempArray);
    System.out.println("The sort took: " + elapsedTime + "ms");

      
    //Testing Arrays.sort -----------------
    System.out.println("\nSorting with Arrays.sort sort:");
    tempArray = Arrays.copyOf(data,data.length);  //to keep original arrays safe from modification
    startTime = System.nanoTime();     //start time

    tempArray = javaBuiltInSort(tempArray);

    endTime = System.nanoTime();  //end time
    elapsedTime = (endTime - startTime) / 1000000.0;

    // displayArray(tempArray);
    System.out.println("The sort took: " + elapsedTime + "ms");

  } //end of main method
   

  /** generateNumberArray *******************************************
  * Creates a random array on integers
  * @param size of array
  * @return the generated integer array
  */
  public static int[] generateNumberArray(int numOfElements) { 

    int[] generated = new int[numOfElements];

    Random randomNumber = new Random();
    for (int i = 0 ; i< generated.length;i++)
      generated[i]=randomNumber.nextInt(10000);

    return generated;
  } //end of generateNumberArray

   
  /** displayArray *******************************************
  * Sorts a random array on integers using selection sort
  * @param the  integer array
  */
  public static void displayArray(int[] numbers) { 
   for (int i = 0 ; i< numbers.length;i++) {
     System.out.print(numbers[i]+" ");
   }
   System.out.println("");
  }

  /** selectionSort *******************************************
   * Sorts a random array on integers using selection sort
   * @param the unsorted integer array
   * @return the sorted integer array
   */
  public static int[] selectionSort(int[] numbers) { 
    int n = numbers.length; 
    int[] res = new int[n]; 
    for (int i = 0; i < n; i++) {
      int min = 1 << 30; 
      int minIndex = 0; 
      for (int j = 0; j < n; j++) {
        if (numbers[j] < min) {
          min = numbers[j]; 
          minIndex = j;
        }
      }
      numbers[minIndex] = Integer.MAX_VALUE;
      res[i] = min;
    }
    return res;
  }
   
  /** bubbleSort *******************************************
  * Sorts a random array on integers using bubble sort
  * @param the unsorted integer array
  * @return the sorted integer array
  */
  public static int[] bubbleSort(int[] numbers) {    
    int n = numbers.length; 
    boolean swapped; 
    for (int i = 0; i < n - 1; i++) {
      swapped = false; 
      for (int j = 0; j < n - i - 1; j++) {
        if (numbers[j] > numbers[j + 1]) {
          int temp = numbers[j];
          numbers[j] = numbers[j + 1];
          numbers[j + 1] = temp; 
          swapped = true;
        }
      }
      if (!swapped) {
        break;
      }
    }
    return numbers;     
  }

  /** insertionSort *******************************************
  * Sorts a random array on integers using bubble sort
  * @param the unsorted integer array
  * @return the sorted integer array
  */
  public static int[] insertionSort(int[] numbers) {    
    int n = numbers.length;

    for (int i = 1; i < n; i++) {
      int curr = numbers[i];
      int j;
      for (j = i - 1; j >= 0; j--) {
        numbers[j + 1] = numbers[j];
        if (numbers[j] < curr) {
          break;
        }
      }
      numbers[j + 1] = curr;
    }
    return numbers;     
  }
   
  /** mergeSort *******************************************
  * Sorts a random array on integers using bubble sort
  * @param the unsorted integer array
  * @return the sorted integer array
  */
  public static int[] mergeSort(int[] numbers) {    
    int n = numbers.length; 
    if (n == 1) {
      return numbers; 
    } else if (n == 2) {
      if (numbers[0] > numbers[1]) {
        int temp = numbers[0]; 
        numbers[0] = numbers[1]; 
        numbers[1] = temp;
      }
      return numbers;
    }

    int mid = n / 2; 
    int[] left = new int[mid];
    int[] right = new int[n - mid];
    for (int i = 0; i < n; i++) {
      if (i < mid) {
        left[i] = numbers[i];
      } else {
        right[i - mid] = numbers[i];
      }
    }

    int[] sortedLeft = mergeSort(left);
    int[] sortedRight = mergeSort(right);
    int l = 0; 
    int r = 0; 
    for (int i = 0; i < n; i++) {
      if (!(l < mid)) {
        numbers[i] = sortedRight[r]; 
        r++;
      } else if(!(r < n - mid)) {
        numbers[i] = sortedLeft[l];
        l++;
      } else if (sortedLeft[l] < sortedRight[r]) {
        numbers[i] = sortedLeft[l]; 
        l++; 
      } else {
        numbers[i] = sortedRight[r];
        r++;
      }
    }
    return numbers;     
  }

  /** quickSort *******************************************
   * Sorts a random array on integers using bubble sort
   * @param the unsorted integer array
   * @return the sorted integer array
   */
  public static int[] quickSort(int[] numbers) {    
    int n = numbers.length; 

    if (n == 1 || n == 0) {
      return numbers; 
    } else if (n == 2) {
      if (numbers[0] > numbers[1]) {
        int temp = numbers[0]; 
        numbers[0] = numbers[1]; 
        numbers[1] = temp;
      }
      return numbers;
    }

    int pivot = numbers[n - 1]; 
    int curr = 0; 
    for (int i = 0; i < n - 1; i++) {
      if (numbers[i] <= pivot) {
        int temp = numbers[curr]; 
        numbers[curr] = numbers[i]; 
        numbers[i] = temp;
        curr++;
      }
    }
    int temp = numbers[curr]; 
    numbers[curr] = numbers[n - 1]; 
    numbers[n - 1] = temp;


    int[] left = new int[curr]; 
    int[] right = new int[n - curr]; 
    for (int i = 0; i < n; i++) { 
      if (i < curr) {
        left[i] = numbers[i]; 
      } else {
        right[i - curr] = numbers[i];
      }
    }

    int[] leftSorted = quickSort(left);
    int[] rightSorted = quickSort(right);
    for (int i = 0; i < curr; i++) {
      numbers[i] = leftSorted[i];
    } 
    for (int i = curr; i < n; i++) {
      numbers[i] = rightSorted[i - curr];
    }
    return numbers;     
  }

   /** javaBuiltInSort *******************************************
     * Sorts a random array on integers using Arrays.sort
     * @param the unsorted integer array
     * @return the sorted integer array
     */
   public static int[] javaBuiltInSort( int[] numbers) { 
     Arrays.sort(numbers);  //sort
     return numbers;
   }
   
}