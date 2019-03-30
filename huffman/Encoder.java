
/**
 * [Encoder.java]
 * The encoder main class
 * 
 * @author Carol Chen
 * 28-Mar-2019
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;

/**
* The Encoder class will run a program to take input and output a compressed file
*
* @author  Carol Chen
* @version 4.2.0
* @since   28-Mar-2019
*/
public class Encoder {
    /**
    * Main method that's run. 
    */
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.println("Please input the file name");
        // String fileName = scan.next(); 
        // DEBUG LINE
        String fileName = "test.jpeg"; 
        long time = System.nanoTime();

        BufferedInputStream in = null;
        int[] counts = new int[257];

        // count bytes
        try {
            in = new BufferedInputStream(new FileInputStream(fileName));
            int c;
            while ((c = in.read()) != -1) {
              counts[c] += 1;
            }
            in.close();
        } catch(Exception e) {
            System.out.println("file not found");
        } 
        System.out.println("Frequencies done counting at " + (System.nanoTime() - time) / 1e9);

        // start constructing tree
        HuffmanNodePriorityQueue pq = new HuffmanNodePriorityQueue();

        // add a node for each type of byte
        for (int i = 0; i < 257; i++) {
            if (counts[i] != 0) {
                HuffmanNode node = new HuffmanNode(counts[i]); 
                node.setValue(i); 
                pq.add(node);
            }
        }

        // Handle case with only one character
        if (pq.size() == 1) { 
            HuffmanNode node = pq.pop(); 
            HuffmanNode parent = new HuffmanNode(node.getData()); 
            parent.setLeft(node); 
            pq.add(parent);
        }

        // iterate using the pq until tree is done constructing. 
        while (pq.size() > 1) {
            HuffmanNode firstHuffmanNode = pq.pop(); 
            HuffmanNode secondHuffmanNode = pq.pop(); 
            HuffmanNode parent = new HuffmanNode(firstHuffmanNode.getData() + secondHuffmanNode.getData()); 
            parent.setLeft(firstHuffmanNode); 
            parent.setRight(secondHuffmanNode); 
            pq.add(parent);
        }

        // last node in queue is the root
        HuffmanNode treeRoot = pq.pop();

        System.out.println("Tree created at " + (System.nanoTime() - time) / 1e9);

        String tree = stringifyTree(treeRoot);

        System.out.println("Tree stringified at " + (System.nanoTime() - time) / 1e9);

        String[] compressionMap = genCompress(treeRoot, new String[257], "");
        System.out.println("Compression map built at " + (System.nanoTime() - time) / 1e9);

        // read file again to count expected number of trailing bytes  
        int trailing = 0;
        try {
           int numBytes = 0; 
           in = new BufferedInputStream(new FileInputStream(fileName));
           int c;
           while ((c = in.read()) != -1) {
               numBytes += compressionMap[c].length();
           }
           trailing = 8 - (numBytes % 8); 
           if (trailing == 8) {
               trailing = 0; 
           } 
        } catch (IOException e) {
            System.out.println("reading and counting trailing bits needed failed");
        }

        System.out.println("Trailing bytes counted at " + (System.nanoTime() - time) / 1e9);
        // start writing
        try {
            FileOutputStream clearer = new FileOutputStream("out.mzip");

            System.out.println("Any existing file cleared at " + (System.nanoTime() - time) / 1e9);

            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("out.mzip", true));
            out.write(((fileName + "_compressed").toUpperCase()).getBytes());
            out.write(13);
            out.write(10);
            out.write((tree).getBytes());
            out.write(13);
            out.write(10);

            out.write(Integer.toString(trailing).getBytes());
            out.write(13);
            out.write(10);

            System.out.println("File name, trailing bytes and tree written to file at " + (System.nanoTime() - time) / 1e9);

            in = new BufferedInputStream(new FileInputStream(fileName));
            String currByte = "";

            // this is very slow, but it's fine. 
            int c;
            while ((c = in.read()) != -1 || currByte.length() >= 8) {
                if (c != -1) {
                    currByte += compressionMap[c];
                }
                if (currByte.length() >= 8) {
                    out.write(getByteByString(currByte.substring(0, 8))); 
                    currByte = currByte.substring(8, currByte.length()); 
                }
            }
            in.close();

            if (currByte.length() > 0) {

                while(currByte.length() != 8){
                    currByte += "1"; 
                }
                out.write(getByteByString(currByte)); 
            }

            out.close();
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Writing to file failed :( ");
        }
        System.out.println("Finished at " + (System.nanoTime() - time) / 1e9);
    }

    /**
    * Takes a binary string of 0 and 1 and converts it to a byte array
    */
    private static byte getByteByString(String binaryString){
        byte ret;
        Integer byteAsInt = Integer.parseInt(binaryString, 2);
        ret = byteAsInt.byteValue();
        return ret;
    }

    /**
    * Takes a tree and returns a map with the compression mapping
    */
    private static String[] genCompress(HuffmanNode node, String[] res, String curr) {
        if (node == null) { 
            return res; 
        }

        if (node.getValue() != Integer.MIN_VALUE) {
            res[node.getValue()] = curr; 
        }

        if (node.getLeft() == null && node.getRight() == null) {
            return res;
        }

        res = genCompress(node.getLeft(), res, curr + "0");     
        if (node.getRight() != null) {
            res = genCompress(node.getRight(), res, curr + "1");     
        } 

        return res;
    }

    /**
    * Stringifies the tree for writing
    */
    private static String stringifyTree(HuffmanNode node) {
        String str = "";
        if (node == null) {
            return ""; 
        } 
        if (node.getValue() != Integer.MIN_VALUE) {
            str += (node.getValue());
        }

        if (node.getLeft() == null && node.getRight() == null) {
            return str; 
        }

        str += "(";
        str += stringifyTree(node.getLeft());
        if (node.getRight() != null) {
            if (node.getRight().getValue() != Integer.MIN_VALUE) {
                str += " ";
            }
            str += stringifyTree(node.getRight());
        } 
        str += ")";
        return str;
    }
}

/**
* The HuffManNode class handles a node used in the trees and queues
*
* @author  Carol Chen
* @version 4.2.0
* @since   28-Mar-2019
*/
class HuffmanNode implements Comparable<HuffmanNode>{    
    HuffmanNode left, right;
    int data;
    int value;
    HuffmanNode next; // used for lists

    /**
    * Constructor for HuffmanNode.  
    * @param n data / weighting / occurences of HuffmanNode 
    */
    public HuffmanNode(int n){
        left = null;
        right = null;
        data = n;
        value = Integer.MIN_VALUE;
    }

    /**
    * Sets a left HuffmanNode
    * @param n HuffmanNode to set. 
    */
    public void setLeft(HuffmanNode n) {
        left = n;
    }

    /**
    * Sets a left HuffmanNode
    * @param n HuffmanNode to set. 
    */
    public void setRight(HuffmanNode n) {
        right = n;
    }

    /**
    * Gets left HuffmanNode
    * @return HuffmanNode the left HuffmanNode
    */
    public HuffmanNode getLeft() {
        return left;
    }

    /**
    * Gets right HuffmanNode
    * @return HuffmanNode the right HuffmanNode
    */
    public HuffmanNode getRight() {
        return right;
    }

    /**
    * Sets integer value (convert byte to int)
    * @param v value to set
    */
    public void setValue(int v) {
        value = v;
    }

    /**
    * Gets data
    * @return data
    */
    public int getData() {
        return data;
    }   

    /**
    * Gets value
    * @return value
    */
    public int getValue() {
        return value;
    }   

    //
    // Start Queue-related methods
    //
    /**
    * Gets next node
    * @return node
    */
    public HuffmanNode getNext() {
      return this.next;
    }

    /**
    * Sets next node
    * @param next node to set
    */
    public void setNext(HuffmanNode next) {
      this.next = next;
    }

    @Override 
    public int compareTo(HuffmanNode other){
        if (this.getData() == other.getData()) { 
            return 0; 
        } else  if (this.getData() < other.getData()) { 
            return -1; 
        } else {
            return 1;
        }
    }
}


/**
* The HuffmanNodePriorityQueue class is a maxqueue for HuffmanNodes (based on data).
*
* @author  Carol Chen
* @version 4.2.0
* @since   28-Mar-2019
*/
class HuffmanNodePriorityQueue { 
    private HuffmanNode head;

    /**
    * Adds node to priority queue
    * @return node
    */
    public void add(HuffmanNode item) { 
        HuffmanNode temp = head;

        if (head == null) {
            head = item;
            return;
        }
        
        while (temp.getNext() != null && temp.getNext().compareTo(item) != 1) {
            temp = temp.getNext();
        }
        HuffmanNode tail = temp.getNext();
        temp.setNext(item);
        item.setNext(tail);
    }
    
    /**
    * Gets node at index in queue
    * @return node
    */
    public HuffmanNode get(int index) { 
        HuffmanNode temp = head;
        for (int i = 0; i < index; i++) {
            temp = temp.getNext();
        }
        return temp;
    }
    
    /**
    * Gets index of node
    * @return index
    */
    public int indexOf(HuffmanNode item) { 
        HuffmanNode t = head;
        int index = -1;
        while(true) {
            index++;
            if (t == null) {
                index = -1;
                break;
            }
            if (t.equals(item)) {
                break;
            } 
            t = t.getNext();
        }
        return index;
    }
    
    /**
    * Removes node at index
    * @param index index to remove
    * @return node that was removed
    */
    public HuffmanNode remove(int index) { 
        HuffmanNode temp = get(index - 1);
        HuffmanNode res = get(index);
        if (index == 0) {
            head = head.getNext();
        } else if (index == size() - 1) {
            temp.setNext(null);
        } else {
            temp.setNext(temp.getNext().getNext());
        }
        return res;

    }

    /**
    * Removes node
    * @return boolean, with true if that node exists, false otherwise. 
    */
    public boolean remove(HuffmanNode item) { 
        if (indexOf(item) != -1) {
            remove(indexOf(item));
            return true; 
        }
        return false;
    }

    /**
    * Removes node at head of queue
    * @return node
    */
    public HuffmanNode pop() { 
        return remove(size() - 1);
    }
    
    /**
    * Shows node at head of queue
    * @return node
    */
    public HuffmanNode peek() { 
        return get(size() - 1);
    }
    
    /**
    * Clears the queue
    */
    public void clear() { 
        head = null;
    }
    
    /**
    * Gets size of queue
    * @return int size
    */
    public int size() { 
        int res = 0; 
        HuffmanNode temp = head;
        if (temp == null) {
            return 0;
        }
        while (temp.getNext() != null) {
            temp = temp.getNext();
            res++;
        }
        return res + 1;
    }
}
