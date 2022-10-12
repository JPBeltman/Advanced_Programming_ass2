package nl.vu.labs.phoenix.ap;


public class LinkedList<E extends Comparable<E>> implements ListInterface<E> {
    int numElements;
    Node last;
    Node current;
    Node first;

    private class Node {
        E data;
        Node prior, next;

        // if size ==0
        public Node(E data) {
            this(data, null, null);
        }
        //if size >0
        public Node(E data, Node prior, Node next) {
            this.data = data == null ? null : data;
            this.prior = prior;
            this.next = next;
        }

    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public ListInterface<E> init() {
       numElements =0;
       last = null;
       current =null;
       first =null;

       return this;
    }

    @Override
    public int size() {
        return numElements;
    }

    @Override
    // smaller elements first
    // a.compareTo(b): <0 if a before b in order, =0 equal, >0 b be4 a
    public ListInterface<E> insert(E d) {
        if(this.size() == 0) {
            current = first = last = new Node(d);
            numElements +=1;
        // 1. 'f', 2. 'c'
        } else if(d.compareTo(first.data) <=0){// c < f: current = c; c.next = f; first = c;
            current = new Node(d,null, first);
            first = current;
            // items' prior after current item = current item
            current.next.prior = current;
            numElements +=1;
        // 1. 'c', 2. 'f'
        } else if(d.compareTo(last.data) >=0){ // f > c: current = f; f.prior = c,
            current = new Node(d, last,null);
            last = current;
            // items' next be4 current item = current
            current.prior.next = current;
            numElements +=1;
        }else{ // item somewhere within bounds of list
            //start at first item and find spot;
            // iterate while d is larger than the next of current item
            // if not: insert d there
            goToFirst();
            while(d.compareTo(current.next.data)>=0){
                goToNext();
            }
            //inserting d:
            // d is new node where prior is current item and next item is next of current
            // item after new current element has current element as prior
            // item be4 new current element has current element as next
            current = new Node(d, current, current.next);
            current.next.prior = current;
            current.prior.next = current;
            numElements +=1;
        }
        return this;
    }

    @Override
    public E retrieve() {
        return this.current.data;
    }

    @Override
    public ListInterface<E> remove() {
        if(size() <= 1){
            this.init();
        } else {
            // curr = first -> next has no prior, curr = next, first = curr
            if(current == first){
                current.next.prior = null;
                current = current.next;
                first = current;
                numElements -=1;
            }// curr = first -> next has no prior, curr = next, first = curr
            else if(current == last){
                current = current.prior;
                last = current;
                numElements -=1;
            }else{
                // the next item prior to the current item is the next of current
                current.prior.next = current.next;
                // prior item to next of current item is prior of current
                current.next.prior = current.prior;
                // make the new current item the item next to the current current item
                goToNext();
                numElements -=1;}
        }
        return this;
    }


    @Override
    public boolean find(E d) {
        goToFirst();
        while(current != last){
            if(d.compareTo(current.data) == 0){
                return true;
            } else{
                if(d.compareTo(current.data)>0) {
                    goToNext();
                }else{
                    current = current.prior;
                    return false;
                }
            }
        }
        return d.compareTo(current.data) == 0;
    }

    @Override
    public boolean goToFirst() {
        if(numElements ==0){
            return false;
        }
        if(current == first) {
            return true;
        }else{
            current = first;
            return true;
        }
    }

    @Override
    public boolean goToLast() {
        if(numElements ==0){
            return false;
        }
        if(current == last) {
            return true;
        } else{
            current = last;
            return true;
        }
    }

    @Override
    public boolean goToNext() {
        if(numElements <= 1){
            return false;
        }
        if(current != last){
            current = current.next;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean goToPrevious() {
        if(numElements <=1){
            return false;
        }
        if(current != first){
            current = current.prior;
            return true;
        }else{
            return false;
        }
    }

    @Override
    public ListInterface<E> copy() {
        return null;
    }
}