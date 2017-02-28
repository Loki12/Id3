package sample.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sergej on 27.02.2017.
 */
public class MyTree {

    private boolean isRoot;
    public Set<MyTree> leaves;

    public MyTree() {
        this.isRoot = false;
        this.leaves = new HashSet<>();
    }

    boolean isLeaf()
    {
        return leaves.isEmpty();
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public Set<MyTree> getLeaves() {
        return leaves;
    }

    public void setLeaves(Set<MyTree> leaves) {
        this.leaves = leaves;
    }
}
