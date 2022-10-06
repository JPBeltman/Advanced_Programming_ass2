package nl.vu.labs.phoenix.ap;

import sun.awt.image.ImageWatched;

public class LinkedList<E extends Comparable<E>> implements ListInterface<E> {
    int numElements;
    Node lastItem;
    Node currentItem;
    Node firstItem;

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
       lastItem = null;
       currentItem =null;
       firstItem=null;

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
            currentItem = firstItem = lastItem = new Node(d);
            numElements +=1;
        // 1. 'f', 2. 'c'
        } else if(d.compareTo(firstItem.data) <=0){// c < f: current = c; c.next = f; first = c;
            currentItem = new Node(d,null,firstItem);
            firstItem = currentItem;
            // items' prior after current item = current item
            currentItem.next.prior = currentItem;
            numElements +=1;
        // 1. 'c', 2. 'f'
        } else if(d.compareTo(lastItem.data) >=0){ // f > c: current = f; f.prior = c,
            currentItem = new Node(d,lastItem,null);
            lastItem=currentItem;
            // items' next be4 current item = current
            currentItem.prior.next = currentItem;
            numElements +=1;
        }else{ // item somewhere within bounds of list
            //start at first item and find spot;
            // iterate while d is larger than the next of current item
            // if not: insert d there
            goToFirst();
            while(d.compareTo(currentItem.next.data)>=0){
                goToNext();
            }
            //inserting d:
            // d is new node where prior is current item and next item is next of current
            // item after new current element has current element as prior
            // item be4 new current element has current element as next
            currentItem = new Node(d,currentItem,currentItem.next);
            currentItem.next.prior = currentItem;
            currentItem.prior.next = currentItem;
            numElements +=1;
        }
        return this;
    }

    @Override
    public E retrieve() {
        return this.currentItem.data;
    }

    @Override
    public ListInterface<E> remove() {
        if(size() <= 1){
            this.init();
        } else {
            // curr = first -> next has no prior, curr = next, first = curr
            if(currentItem == firstItem){
                currentItem.next.prior = null;
                currentItem=currentItem.next;
                firstItem=currentItem;
                numElements -=1;
            }// curr = first -> next has no prior, curr = next, first = curr
            else if(currentItem == lastItem){
                currentItem=currentItem.prior;
                lastItem = currentItem;
                numElements -=1;
            }else{
                // the next item prior to the current item is the next of current
                currentItem.prior.next = currentItem.next;
                // prior item to next of current item is prior of current
                currentItem.next.prior = currentItem.prior;
                // make the new current item the item next to the current current item
                goToNext();
                numElements -=1;}
        }
        return this;
    }

    @Override
    public boolean find(E d) {
        // if d.comparetocurr ==0; found
        goToFirst();
        while(currentItem != lastItem){
            if(d.compareTo(currentItem.data) == 0){
                return true;
            } else{
                if(d.compareTo(currentItem.data)>0) {
                    goToNext();
                }else{
                    return false;
                }
            }
        }
        return d.compareTo(currentItem.data) == 0;
    }

    @Override
    public boolean goToFirst() {
        if(numElements ==0){
            return false;
        }
        if(currentItem == firstItem) {
            return true;
        }else{
            currentItem = firstItem;
            return true;
        }
    }

    @Override
    public boolean goToLast() {
        if(numElements ==0){
            return false;
        }
        if(currentItem == lastItem) {
            return true;
        } else{
            currentItem = lastItem;
            return true;
        }
    }

    @Override
    public boolean goToNext() {
        if(numElements <= 1){
            return false;
        }
        if(currentItem != lastItem){
            currentItem = currentItem.next;
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
        if(currentItem != firstItem){
            currentItem = currentItem.prior;
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