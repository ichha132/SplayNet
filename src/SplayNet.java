public class SplayNet{      //<Key extends Comparable<Key>, Value> {
    private Node root;   // root of the BST

    // BST helper node data type
    private class Node {
        private int key;            // key
      //  private Value value;        // associated data
        private Node left, right;   // left and right subtrees

        public Node(int key) {
            this.key   = key;
           // this.value = value;
        }
    }

    public boolean contains(int key) {
        return get(key) != 0;
    }

    // return value associated with the given key
    // if no such value, return null
    public int get(int key) {
        root = splay(root, key);
        int cmp = key-root.key;
        if (cmp == 0) return root.key;
        else          return 0;     //key should be anything but zero
    }

    /***************************************************************************
     *  Splay tree insertion.
     ***************************************************************************/
    public void put(int key) {
        // splay key to root
        if (root == null) {
            root = new Node(key);
            return;
        }

        root = splay(root, key);

        int cmp = key-root.key;

        // Insert new node at root
        if (cmp < 0) {
            Node n = new Node(key);
            n.left = root.left;
            n.right = root;
            root.left = null;
            root = n;
        }

        // Insert new node at root
        else if (cmp > 0) {
            Node n = new Node(key);
            n.right = root.right;
            n.left = root;
            root.right = null;
            root = n;
        }

        // It was a duplicate key. Simply replace the value
//        else {
//            root.value = value;
//        }

    }

    /***************************************************************************
     *  Splay tree deletion.
     ***************************************************************************/
    /* This splays the key, then does a slightly modified Hibbard deletion on
     * the root (if it is the node to be deleted; if it is not, the key was
     * not in the tree). The modification is that rather than swapping the
     * root (call it node A) with its successor, it's successor (call it Node B)
     * is moved to the root position by splaying for the deletion key in A's
     * right subtree. Finally, A's right child is made the new root's right
     * child.
     */
    public void remove(int key) {
        if (root == null) return; // empty tree

        root = splay(root, key);

        int cmp = key-root.key;

        if (cmp == 0) {
            if (root.left == null) {
                root = root.right;
            }
            else {
                Node x = root.right;
                root = root.left;
                splay(root, key);
                root.right = x;
            }
        }

        // else: it wasn't in the tree to remove
    }
    public void commute(int u,int v)        //Assuming u<=v
    {
//        if(this.contains(u)==false &&this.contains(v)==false)
//            return ;
        Node nodeSet[] = findNodes( u, v) ;    //Node[0]=common_ancester; Node[1]=Node(u)
        Node common_ancester=nodeSet[0];
        Node uNode=nodeSet[1];
        if(uNode==null)
        {
            return;
        }
        System.out.println("u="+uNode.key+" comm_anc="+ common_ancester.key);
        common_ancester=splay(common_ancester,u);
        this.printPreorder(this.root);
        System.out.println();
        if(u==v)
            return ;
        System.out.println("uNode.right.key="+uNode.right.key);
        uNode.right=splay(uNode.right, v);          //if v is not there, node closest to v will come to uNode
        this.printPreorder(this.root);
        System.out.println();
    }
    public Node[] findNodes(int u,int v)
    {
        Node node=this.root;
        Node[] nodeSet=new Node[2];
        while(node !=null &&((u>node.key &&v>node.key) || (u<node.key && v<node.key)))
        {
            if(u>node.key &&v>node.key)
            {
                node=node.right;
            }
            else if(u<node.key && v<node.key)
            {
                node=node.left;
            }
        }
        nodeSet[0]=node;
        while(node !=null &&node.key!=u)
        {
            if(u<node.key)
            {
                node=node.left;
            }
            else
            {
                node=node.right;
            }
        }
        nodeSet[1]=node;
        return nodeSet;
    }


    /***************************************************************************
     * Splay tree function.
     * **********************************************************************/
    // splay key in the tree rooted at Node h. If a node with that key exists,
    //   it is splayed to the root of the tree. If it does not, the last node
    //   along the search path for the key is splayed to the root.
    private Node splay(Node h, int key) {
        if (h == null) return null;

        int cmp1 = key-h.key;

        if (cmp1 < 0) {
            // key not in tree, so we're done
            if (h.left == null) {
                return h;
            }
            int cmp2 = key-h.left.key;
            if (cmp2 < 0) {
                h.left.left = splay(h.left.left, key);
                h = rotateRight(h);
            }
            else if (cmp2 > 0) {
                h.left.right = splay(h.left.right, key);
                if (h.left.right != null)
                    h.left = rotateLeft(h.left);
            }

            if (h.left == null) return h;
            else                return rotateRight(h);
        }

        else if (cmp1 > 0) {

            if (h.right == null) {      // key not in tree, so we're done
                return h;
            }

            int cmp2 = key-h.right.key;
            if (cmp2 < 0) {
                h.right.left  = splay(h.right.left, key);
                if (h.right.left != null)
                    h.right = rotateRight(h.right);
            }
            else if (cmp2 > 0) {
                h.right.right = splay(h.right.right, key);
                h = rotateLeft(h);
            }

            if (h.right == null) return h;
            else                 return rotateLeft(h);
        }

        else return h;
    }


    /***************************************************************************
     *  Helper functions.
     ***************************************************************************/

    // height of tree (1-node tree has height 0)
    public int height() { return height(root); }
    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.left), height(x.right));
    }


    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return 1 + size(x.left) + size(x.right);
    }

    // right rotate
    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        return x;
    }

    // left rotate
    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        return x;
    }
    public void printPreorder(Node node)
    {
        if (node == null)
            return;

        /* first print data of node */
        System.out.print(node.key + " ");

        /* then recur on left sutree */
        printPreorder(node.left);

        /* now recur on right subtree */
        printPreorder(node.right);
    }
    //    public int compareTo(int x)
//    {
//        this
//    }
    public static void main(String args[])
    {
        SplayNet sn1 = new SplayNet();
        sn1.put(5);
        sn1.put(9);
        sn1.put(13);
        sn1.put(11);
        sn1.put(1);
        sn1.printPreorder(sn1.root);
      //  sn1.commute(5,11);
        sn1.commute(9,13);
       // sn1.contains(13);
       // sn1.printPreorder(sn1.root);
    }
}
