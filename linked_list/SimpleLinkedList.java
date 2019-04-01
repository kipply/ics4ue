// ********************** Simple Linked List class in the linked list *********
class SimpleLinkedList<E> { 
  private Node<E> head;

  public void add(E item) { 
    Node temp = head;

    if (head == null) {
      head = new Node<E>(item, null);
      return;
    }
    
    while (temp.getNext() != null) {
      temp = temp.getNext();
    }
    
    temp.setNext(new Node<E>(item));
  }
  
  public E get(int index) { 
    Node temp = head;
    for (int i = 0; i < index; i++) {
      temp = temp.getNext();
    }
    return (E)temp.getItem();
  }
  
  public int indexOf(E item) { 
    Node t = head;
    int index = -1;
    while(true) {
      index++;
      if (t == null) {
        index = -1;
        break;
      }
      if (t.getItem().equals(item)) {
        break;
      } 
      t = t.getNext();
    }
    return index;
  }
  
  public E remove(int index) { 
    Node temp = getNode(index - 1);
    E res = get(index);
    if (index == 0) {
      head = head.getNext();
    } else if (index == size() - 1) {
      temp.setNext(null);
    } else {
      temp.setNext(temp.getNext().getNext());
    }
    return res;

  }

  public boolean remove(String item) { 
    if (indexOf((E)item) != -1) {
      remove(indexOf((E)item));
      return true; 
    }
    return false;
  }

  
  public void clear() { 
    head = null;
  }
  
  public int size() { 
    int res = 0; 
    Node temp = head;
    if (temp == null) {
      return 0;
    }
    while (temp.getNext() != null) {
      temp = temp.getNext();
      res++;
    }
    return res + 1;
  }


  private Node<E> getNode(int index) { 
    Node temp = head;
    for (int i = 0; i < index; i++) {
      temp = temp.getNext();
    }
    return temp;
  }
}



// ********************** A Node in the linked list *********
class Node<T> { 
  private T item;
  private Node<T> next;

  public Node(T item) {
    this.item = item;
    this.next = null;
  }

  public Node(T item, Node<T> next) {
    this.item = item;
    this.next = next;
  }

  public Node<T> getNext() {
    return this.next;
  }

  public void setNext(Node<T> next) {
    this.next = next;
  }

  public T getItem() {
    return this.item;
  }

}