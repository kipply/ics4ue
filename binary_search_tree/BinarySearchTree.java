import java.util.Scanner;

class BinarySortTree {
  private BinaryTreeNode root;

  public BinarySortTree() {
    root = null;
  }

  public boolean isEmpty() {
    return root == null; 
  } 

  public void insert(int data) {
    root = insert(root, data);
  }

  private BinaryTreeNode insert(BinaryTreeNode node, int data) {
    if (node == null) {
      node = new BinaryTreeNode(data); 
    } else {
      if (node.getRight() == null) {
        node.right = insert(node.right, data); 
      } else {
        node.left = insert(node.left, data); 
      }
    }
    return node;
  }

  public int size() {
    return countNodes(root);
  }

  private int countNodes(BinaryTreeNode r) {
    if (r == null){
      return 0;
    } else {
      int l = 1;
      l += countNodes(r.getLeft());
      l += countNodes(r.getRight());
      return l;
    }
  }
}

class BinaryTreeNode {    
  BinaryTreeNode left, right;
  int data;

  public BinaryTreeNode() {
    left = null;
    right = null;
    data = 0;
  }

  public BinaryTreeNode(int n){
    left = null;
    right = null;
    data = n;
  }

  public void setLeft(BinaryTreeNode n) {
    left = n;
  }

  public void setRight(BinaryTreeNode n) {
    right = n;
  }

  public BinaryTreeNode getLeft() {
    return left;
  }

  public BinaryTreeNode getRight() {
    return right;
  }

  public void setData(int d) {
    data = d;
  }

  public int getData() {
    return data;
  }   

  public boolean isLeaf(BinaryTreeNode n) {
    return getLeft().equals(n) || getRight().equals(n);
  }     
}