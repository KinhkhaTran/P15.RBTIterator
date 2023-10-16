import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Iterator;

public class IterableMultiKeyRBT<T extends Comparable<T>> extends RedBlackTree<KeyListInterface<T>> implements IterableMultiKeySortedCollectionInterface<T> {

    /**
     * Inserts value into tree that can store multiple objects per key by keeping
     * lists of objects in each node of the tree.
     * @param key object to insert
     * @return true if a new node was inserted, false if the key was added into an existing node
     */
    @Override
    public boolean insertSingleKey(T key) {
        return false;
    }
    /**
     * @return the number of values in the tree.
     */
    @Override
    public int numKeys() {
        return 0;
    }
    /**
     * Returns an iterator that does an in-order iteration over the tree.
     */
    @Override
    public Iterator<T> iterator() {
        return null;
    }
    /**
     * Sets the starting point for iterations. Future iterations will start at the
     * starting point or the key closest to it in the tree. This setting is remembered
     * until it is reset. Passing in null disables the starting point.
     * @param startPoint the start point to set for iterations
     */
    @Override
    public void setIterationStartPoint(Comparable<T> startPoint) {

    }

    /**
     * This method tests numKeys() functionality
     */
    @Test
    public void testNumKeys() {
        IterableMultiKeyRBT<Integer> tree = new IterableMultiKeyRBT<>();
        tree.insertSingleKey(50);
        tree.insertSingleKey(1);
        tree.insertSingleKey(10);
        tree.insertSingleKey(120);

        Assertions.assertEquals(3, tree.size());
        Assertions.assertEquals(4, tree.numKeys());
    }

    /**
     * this method tests iterator functionality
     */
    @Test
    public void testIterator() {
        try {
            IterableMultiKeyRBT<String> tree = new IterableMultiKeyRBT<>();
            String[] keys = new String [] { "red", "blue", "green", "yellow" };
            int[] sequence = new int [] { 1,2,1,0,2 };
            for (int i : sequence) tree.insertSingleKey(keys[i]);

            String last = null;
            for (String key : tree) {
                if (last != null) Assertions.assertTrue(last.compareTo(key) <= 0);
                last = key;
            };
        }
        catch (Exception e) {
            Assertions.fail();
        }
    }

    /**
     * This method tests setIterationStartPoint() functionality
     */
    @Test
    public void testSetIterationStartPoint() {
        try {
            IterableMultiKeyRBT<Integer> tree = new IterableMultiKeyRBT<>();
            tree.insertSingleKey(1);
            tree.insertSingleKey(5);
            tree.insertSingleKey(6);

            tree.setIterationStartPoint(5);
            Assertions.assertEquals(5, tree.iterator().next());
            tree.setIterationStartPoint(6);
            Assertions.assertEquals(6, tree.iterator().next());
        }
        catch (Exception e) {
            Assertions.fail();
        }
    }
}

