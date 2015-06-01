package org.structures.Tree;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bene
 */
public class TreeNode {
    private Object nodeObject;
    private String identifier;
    private TreeNode parent;
    private final List<TreeNode> children;

    public TreeNode(Object object, TreeNode parent, String identifier){
        this.nodeObject = object;
        this.parent = parent;
        this.identifier = identifier;
        this.children = new ArrayList<>();
    }
    
    public TreeNode getChildNodeAt(int index){
        return this.children.get(index);
    }
    
    public TreeNode getParentNode(){
        return this.parent;
    }
    
    public Object getNodeObject(){
        return this.nodeObject;
    }
    
    public String getIdentifier(){
        return this.identifier;
    }
    
    public List<TreeNode> getChildren(){
        return this.children;
    }
    
    public void addChildNode(TreeNode child){
        children.add(child);
    }
    
    public void setParentNode(TreeNode parentNode){
        this.parent = parentNode;
    }
    
    public void setIdentifier(String identifier){
        this.identifier = identifier;
    }
}
