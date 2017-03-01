package sample.model;

import java.util.HashSet;
import java.util.Set;

public class MyPoint {

    private boolean isRoot;
    public String name;
    public Set<MyPoint> leaves;

    public MyPoint(String name) {
        this.name = name;
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

    public Set<MyPoint> getLeaves() {
        return leaves;
    }

    public void setLeaves(Set<MyPoint> leaves) {
        this.leaves = leaves;
    }
}
