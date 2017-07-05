package tests;

import model.Restaurant;
import structure.BinarySearchTree;

import static org.junit.Assert.*;

/**
 * Created by REA on 7/3/2017.
 */
public class BinarySearchTreeTest {
    BinarySearchTree<Restaurant> tree = new BinarySearchTree<Restaurant>();

    Restaurant res1 = new Restaurant("Restaurant 1", "Super Fake Address 1", "Woodbridge", "VA", "22150", "34.0000", "-71.0000", "7031114444", "fakePhoto1" );
    Restaurant res2 = new Restaurant("Restaurant 2", "Super Fake Address 2", "Woodbridge", "VA", "22150", "34.0000", "-72.0000", "7031114444", "fakePhoto1" );
    Restaurant res3 = new Restaurant("Restaurant 3", "Super Fake Address 3", "Woodbridge", "VA", "22150", "34.0003", "-73.0000", "7031114444", "fakePhoto1" );
    Restaurant res4 = new Restaurant("Restaurant 4", "Super Fake Address 4", "Woodbridge", "VA", "22150", "34.0004", "-74.0000", "7031114444", "fakePhoto1" );
    Restaurant res5 = new Restaurant("Restaurant 5", "Super Fake Address 5", "Woodbridge", "VA", "22150", "34.0005", "-75.0000", "7031114444", "fakePhoto1" );

    @org.junit.Test
    public void add() throws Exception {

        tree.add(res3);
        tree.add(res2);
        tree.add(res1);
        tree.add(res4);
        tree.add(res5);

        System.out.print(tree);
    }

    @org.junit.Test
    public void remove() throws Exception {
        assertFalse(tree.remove(res1));

        tree.add(res3);
        tree.add(res2);
        tree.add(res1);

        System.out.println(tree);

        assertTrue(tree.remove(res2));
        assertTrue(tree.remove(res1));

        System.out.print(tree);


    }

    @org.junit.Test
    public void contains() throws Exception {
        assertFalse(tree.contains(res4));

        tree.add(res4);
        assertTrue(tree.contains(res4));

    }

    @org.junit.Test
    public void get() throws Exception {
        assertTrue(tree.get(res1) == null);

        tree.add(res1);
        assertTrue(tree.get(res1).compareTo(res1) == 0);
    }

    @org.junit.Test
    public void getNext() throws Exception {
        tree.add(res5);
        tree.add(res2);
        tree.add(res1);
        tree.add(res4);

        System.out.println(tree);

        tree.reset();

        assertTrue(tree.getNext().compareTo(res1) == 0);
        assertTrue(tree.getNext().compareTo(res2) == 0);
        assertTrue(tree.getNext().compareTo(res4) == 0);
        assertTrue(tree.getNext().compareTo(res5) == 0);
        assertTrue(tree.getNext() == null);
    }

    @org.junit.Test
    public void reset() throws Exception {
        tree.add(res5);
        tree.add(res2);
        tree.add(res3);
        tree.add(res4);

        System.out.println(tree);
        tree.reset();

        assertTrue(tree.getNext().compareTo(res2) == 0);

        tree.add(res1);
        tree.reset();

        assertTrue(tree.getNext().compareTo(res1) == 0);

        System.out.println(tree);

    }

    @org.junit.Test
    public void size() throws Exception {
        assertTrue(tree.size() == 0);
        tree.add(res1);
        assertTrue(tree.size() == 1);
        tree.add(res2);
        assertTrue(tree.size() == 2);
        tree.remove(res1);
        assertTrue(tree.size() == 1);
    }

    @org.junit.Test
    public void isEmpty() throws Exception {
        assertTrue(tree.isEmpty());

        tree.add(res1);
        assertFalse(tree.isEmpty());
        tree.remove(res1);
        assertTrue(tree.isEmpty());

    }

}