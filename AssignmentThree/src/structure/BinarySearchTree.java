package structure;

import exceptions.EmptyListException;
import interfaces.BinaryTreeInterface;
import structures.BoundedQueue;

import java.util.Iterator;

/**
 * Created by REA on 7/2/2017.
 */
public class BinarySearchTree<E extends Comparable<E>> implements BinaryTreeInterface<E> {

    private Node<E> tree = null;
    private boolean found = false;
    private int numberOfElements = 0;
    private static final int QSIZE = 200;

    enum TraversalOrder{INORDER, PREORDER, POSTORDER}

    Iterator<E> treeIterator;

    /**
     * Throws an exception when duplicate elements are found.
     * Otherwise, adds passed element to the correct place given the order
     * of the list.
     *
     * @param element
     */
    @Override
    public void add(E element) {
        tree = recursiveAdd(element, tree);
        numberOfElements++;
    }

    // Our recursive add returns the root of our new tree.
    private Node<E> recursiveAdd(E element, Node<E> root){
        if (root == null){
            root = new Node<E>(element);
        }
        else if (element.compareTo(root.getElement()) <= 0){
            root.setLeftLink(recursiveAdd(element, root.getLeftLink()));
        }
        // if (element.compareTo(root.getElement()) > 0)
        else {
            root.setRightLink(recursiveAdd(element, root.getRightLink()));
        }
        return root;
    }

    /**
     * Removes the first occurrence of the specified element from the list.
     * Returns true if an element was removes.  Otherwise, returns false.
     *
     * @param element
     */
    @Override
    public boolean remove(E element) {
        tree = recursiveRemove(element, tree);
        if (found){
            numberOfElements--;
        }
        return found;
    }

    /* Our recursive remove starts in a similar way to our add method.
       Once we reach the appropriate node, we rely on other methods to actually remove the node or replace the
       information within the node.

       There are three cases we need to handle in removing from a binary tree.
       Case 1: Remove a leaf
       - This can be taken care of by simply setting the node to null
       Case 2: Remove a root with one child.
       - We return the link to the child node in order to "hop over" the node we want to remove.
       Case 3: Remove a root with two children.
       - This is the trickiest situation.  We'll use the book's method of replacing the root's element with
       its predecessor's element and then removing the predecessor node instead.

     */
    private Node<E> recursiveRemove(E element, Node<E> node){
        if (node == null){
            // This method should not reach a null node unless the element doesn't exist in our tree.
            found = false;
        }
        else if (element.compareTo(node.getElement()) < 0){
            node.setLeftLink(recursiveRemove(element, node.getLeftLink()));
        }
        else if (element.compareTo(node.getElement()) > 0){
            node.setRightLink(recursiveRemove(element, node.getRightLink()));
        }
        else{
            node = removeNode(node);
            found = true;
        }
        return node;
    }

    // This will remove the passed element from the tree
    private Node<E> removeNode(Node<E> node){
        E element;
        // If our node only has one child, return the link for the child.
        // This also takes care of a leaf.
        if (node.getLeftLink() == null){
            return node.getRightLink();
        }
        else if (node.getRightLink() == null){
            return node.getLeftLink();
        }
        // If the above isn't true then we are dealing with a root with two children
        // This situation is handled by replacing the target node with its predecessor in order
        // to maintain our binary tree logic.
        else{
            // The node's predecessor is the highest value in its left subtree.
            element = getPredecessor(node.getLeftLink());
            node.setElement(element);
            // We can remove the predecessor after grabbing the information.
            node.setLeftLink(recursiveRemove(element, node.getLeftLink()));
            return node;
        }
    }
    // We are passing the left subtree of the node.  We need the closest/highest element in this subtree.
    // Because we're already at the left subtree we can simply iterate down
    // the linked list as far to the right as possible to find the highest element.
    private E getPredecessor(Node<E> subtree){
        Node<E> temp = subtree;

        while (temp.getRightLink() != null){
            temp = temp.getRightLink();
        }
        return temp.getElement();
    }

    /**
     * Searches the list to see whether or not an occurrence of the given element
     * already exists.
     *
     * @param element
     * @return True if the list contains the given element.
     */
    @Override
    public boolean contains(E element) {
        return recursiveContains(element, tree);
    }

    private boolean recursiveContains(E element, Node<E> root){
        // First base case returns false if we reach a null root before we find our passed element.
        if (root == null){
            return false;
        }
        // If our element is less than our current root, go to the left.
        else if (element.compareTo(root.getElement()) < 0){
            return recursiveContains(element, root.getLeftLink());
        }
        // If our element is greater than our current root, go to the right.
        else if (element.compareTo(root.getElement()) > 0){
            return recursiveContains(element, root.getRightLink());
        }
        // If we don't meet any of the above conditions then we've managed to find the element we wanted.
        else{
            return true;
        }
    }

    /**
     * Passed an argument, it returns an equivalent object if one exists on the list.
     * If not, returns null.
     *
     * @param element
     * @return matching element, if found
     */
    @Override
    public E get(E element) throws EmptyListException {

        return recursiveGet(element, tree);
    }

    private E recursiveGet(E element, Node<E> root){

        // First base case returns false if we reach a null root before we find our passed element.
        if (root == null){
            return null;
        }
        // If our element is less than our current root, go to the left.
        else if (element.compareTo(root.getElement()) < 0){
            return recursiveGet(element, root.getLeftLink());
        }
        // If our element is greater than our current root, go to the right.
        else if (element.compareTo(root.getElement()) > 0){
            return recursiveGet(element, root.getRightLink());
        }
        // If we don't meet any of the above conditions then we've managed to find the element we wanted.
        else{
            return root.getElement();
        }
    }

    /**
     * Returns the next element in the list and then updates the current position.
     *
     * PreCondition: Must reset before using getNext() for the first time.
     *
     * @return The next element in the list
     */
    @Override
    public E getNext() {
        // If our iterator is empty, return null.
        if (!treeIterator.hasNext()){
            return null;
        }
        // Else, return the next element
        else{
            return treeIterator.next();
        }


    }

    /**
     * Gives us a new iterator to traverse the most recent snapshot of our tree.
     */
    @Override
    public void reset() {
        treeIterator = getIterator(TraversalOrder.INORDER);
    }



    /**
     * Returns the size of our list.
     *
     * @return size of our list
     */
    @Override
    public int size() {
        return numberOfElements;
    }

    /**
     * Returns true if the list is empty.  Otherwise, returns false.
     *
     * @return true if the list is empty
     */
    @Override
    public boolean isEmpty() {
        return (tree == null);
    }

    @Override
    public String toString(){
        reset();
        String fullString = "";

        if (!treeIterator.hasNext()){
            System.out.println("Tried to print an empty binary search tree");
        }
        else{
            while (treeIterator.hasNext()){
                fullString += treeIterator.next();
            }
        }
        return fullString;
    }

    /* There's technically no need to use an interator according to our assignment requirements, but I wanted to
       practice using the iterators in the book and using anonymous inner classes.
       This comes from Chapter 7.6 pages 452-455 of the book.
     */

    private Iterator<E> getIterator(TraversalOrder orderType){
        final BoundedQueue<E> infoQueue = new BoundedQueue<>(QSIZE);

        if (orderType == TraversalOrder.PREORDER){
            preOrder(tree, infoQueue);
        }
        else if (orderType == TraversalOrder.INORDER){
            inOrder(tree, infoQueue);
        }
        else if (orderType == TraversalOrder.POSTORDER){
            postOrder(tree, infoQueue);
        }
        // As we create the Iterator that we want to return, we also create a class to override the methods iterator
        // comes with.
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return !infoQueue.isEmpty();
            }

            @Override
            public E next() {
                if (!hasNext()){
                    throw new IndexOutOfBoundsException("Illegal invocation of next in the iterator.");
                }
                return infoQueue.dequeue();
            }

            @Override
            public void remove(){
                throw new UnsupportedOperationException("Remove is not supported through the iterator in our binary list.");
            }
        };
    }

    private void inOrder(Node<E> node, BoundedQueue<E> queue){
        if (node != null){
            inOrder(node.getLeftLink(), queue);
            queue.enqueue(node.getElement());
            inOrder(node.getRightLink(), queue);
        }
    }

    private void preOrder(Node<E> node, BoundedQueue<E> queue){
        if (node != null){
            queue.enqueue(node.getElement());
            preOrder(node.getLeftLink(), queue);
            preOrder(node.getRightLink(), queue);
        }
    }

    private void postOrder(Node<E> node, BoundedQueue<E> queue){
        if (node != null){
            postOrder(node.getLeftLink(), queue);
            postOrder(node.getRightLink(), queue);
            queue.enqueue(node.getElement());
        }
    }




}
