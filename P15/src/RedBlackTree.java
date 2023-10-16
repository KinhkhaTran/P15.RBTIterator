// --== CS400 Fall 2023 File Header Information ==--
// Name: Kinhkha Tran
// Email: Ktran33@wisc.edu
// Group: <your group's name: a letter and two digits>
// TA: <name of your team's ta>
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
*This class is a Red Black Tree
**/

public class RedBlackTree<T extends Comparable<T>> extends BinarySearchTree<T> implements SortedCollectionInterface<T> {
    // You can add Red-Black Tree specific methods and logic here.
    
    // Constructor
    public RedBlackTree() {
        super(); 
    }


    protected static class RBTNode<T> extends Node<T> {
        public int blackHeight = 0;
        public RBTNode(T data) { super(data); }
        public RBTNode<T> getUp() { return (RBTNode<T>)this.up; }
        public RBTNode<T> getDownLeft() { return (RBTNode<T>)this.down[0]; }
        public RBTNode<T> getDownRight() { return (RBTNode<T>)this.down[1]; }
    }

    /**
     * Helper method to enforce red-black tree properties after insertion
     */
    protected void enforceRBTreePropertiesAfterInsert(RBTNode<T> newNode) {


        // Base case: If the parent is null, it means the node is the root, so we color it black.
        if (newNode.getUp() == null) {
            newNode.blackHeight = 1;
            return;
        }

        // Case 2: If the parent is black, there's no violation.
        if (newNode.getUp().blackHeight == 1) {
            return;
        }

        // Case 3: If the parent is red and the uncle is also red, we recolor.
        RBTNode<T> uncle;
        //check if uncle is left or right child
        if (newNode.getUp().isRightChild()) {
            uncle = newNode.getUp().getUp().getDownLeft();
        } else {
            uncle = newNode.getUp().getUp().getDownRight();
        }


        if (uncle != null && uncle.blackHeight == 0) {
            newNode.getUp().blackHeight = 1;
            uncle.blackHeight = 1;
            newNode.getUp().getUp().blackHeight = 0;
            enforceRBTreePropertiesAfterInsert(newNode.getUp().getUp());
            return;
        }

        // Case 4: If the parent is red but the uncle is black or null
        if (uncle == null || uncle.blackHeight == 1) {
            //check if newNode is parent's right or left child, as well as verify if the parent
            //is the left or right child
            if (newNode == newNode.getUp().getDownRight() && newNode.getUp() == newNode.getUp().getUp().getDownLeft()) {
                rotate(newNode, newNode.getUp());
                newNode = newNode.getDownLeft();
            } else if (newNode == newNode.getUp().getDownLeft() && newNode.getUp() == newNode.getUp().getUp().getDownRight()) {
                rotate(newNode,newNode.getUp());
                newNode = newNode.getDownRight();
            }


            //perform final rotation after first rotation
            rotate(newNode.getUp(), newNode.getUp().getUp());

            newNode.getUp().blackHeight = 1;
            newNode.getUp().getUp().blackHeight = 0;
        }

        // Recursive call to handle possible further violations
        enforceRBTreePropertiesAfterInsert(newNode);
    }


    /**
     * Inserts a new data value into the tree. This tree will not hold null references, nor
     * duplicate data values.
     *
     * @param data to be added into this binary search tree
     * @return true if the value was inserted, false if is was in the tree already
     * @throws NullPointerException when the provided data argument is null
     */
    @Override
    public boolean insert(T data) throws NullPointerException {
        if (data == null)
            throw new NullPointerException("Cannot insert data value null into the tree.");
        RBTNode<T> newNode = new RBTNode<>(data);
        boolean valueInserted = super.insertHelper(newNode); // Call the BinarySearchTree's
        // insertHelper method.
        enforceRBTreePropertiesAfterInsert(newNode); // Enforce Red-Black Tree properties.
        root = (RBTNode) root;// Ensure the root is black.
        ((RBTNode<T>) root).blackHeight = 1;
        return valueInserted;
    }

    /**
     * This method tests inserting red node into tree with black parent but red sibling
     */
    @Test
    public void testInsertNodeWithBlackParentAndRedSibling() {
        RBTNode<Integer> root = new RBTNode<>(24);
        root.blackHeight = 1;
        root.down[1] = new RBTNode<>(30);
        root.down[1].up = root;
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.root = root;
        tree.size = 2;

        tree.insert(7);
        Assertions.assertEquals("[ 24, 7, 30 ]", tree.toLevelOrderString());
        Assertions.assertEquals(1, ((RBTNode<Integer>)tree.root).blackHeight);
        Assertions.assertEquals(0, ((RBTNode<Integer>)tree.root).getDownLeft().blackHeight);
        Assertions.assertEquals(0, ((RBTNode<Integer>)tree.root).getDownRight().blackHeight);
    }

    /**
     * Test case to verify insertion of nodes when parent is black and sibling is black.
     */
    @Test
    public void testInsertNodesIntoBlackParentNodeWithBlackSibling() {
        RBTNode<Integer> root = new RBTNode<>(24);
        root.blackHeight = 1;
        root.down[1] = new RBTNode<>(30);
        root.down[1].up = root;
        ((RBTNode<Integer>)root.down[1]).blackHeight = 1;
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.root = root;
        tree.size = 2;

        tree.insert(7);
        Assertions.assertEquals("[ 24, 7, 30 ]", tree.toLevelOrderString());
        Assertions.assertEquals(1, ((RBTNode<Integer>)tree.root).blackHeight);
        Assertions.assertEquals(0, ((RBTNode<Integer>)tree.root).getDownLeft().blackHeight);
        Assertions.assertEquals(1, ((RBTNode<Integer>)tree.root).getDownRight().blackHeight);

    }

    /**
     * Test case to verify insertion when the parent node is red and the aunt/uncle
     * node is red.
     */
    @Test
    public void testInsertWithRedParentandRedUncle() {
        RBTNode<Integer> root = new RBTNode<>(54);
        root.blackHeight = 1;
        root.down[0] = new RBTNode<>(20);
        root.down[0].up = root;
        ((RBTNode<Integer>)root.down[0]).blackHeight = 0;
        root.down[1] = new RBTNode<>(56);
        root.down[1].up = root;
        ((RBTNode<Integer>)root.down[1]).blackHeight = 0;
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.root = root;
        tree.size = 3;

        tree.insert(1);
        Assertions.assertEquals(1, ((RBTNode<Integer>)tree.root).blackHeight);
        Assertions.assertEquals(1, ((RBTNode<Integer>)tree.root).getDownLeft().blackHeight);
        Assertions.assertEquals(1, ((RBTNode<Integer>)tree.root).getDownRight().blackHeight);
        Assertions.assertEquals(0, ((RBTNode<Integer>)tree.root).getDownLeft().getDownLeft().blackHeight);

    }

}

