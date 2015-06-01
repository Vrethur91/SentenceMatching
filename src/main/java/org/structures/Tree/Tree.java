package org.structures.Tree;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Bene
 */
public class Tree {

    private TreeNode rootNode;

    public Tree(Object object){
        TreeNode rootNode = new TreeNode(object, null, "0");
        this.rootNode = rootNode;
    }
    
    public Tree(TreeNode rootNode) {
        this.rootNode = rootNode;
    }

    public void addNode(TreeNode node) {
        rootNode.addChildNode(node);
    }

    public void addNode(String identifier, TreeNode node) {
        TreeNode currentNode = null;
        Matcher m = Pattern.compile("\\.?(\\d)").matcher(identifier);
        if (m.find()) {
            if (Integer.parseInt(m.group(1)) == 0) {
                currentNode = rootNode;
            }
        }
        while (m.find()) {
            int nextNodeIndex = Integer.parseInt(m.group(1));
            if (currentNode.getChildren().size() < nextNodeIndex) {
                currentNode = currentNode.getChildNodeAt(nextNodeIndex);
            }
        }

        if (currentNode != null) {
            node.setParentNode(currentNode);
            node.setIdentifier(identifier + "." + currentNode.getChildren().size());
            currentNode.addChildNode(node);
        } else {
            System.out.println("Node doesn't exist");
        }

    }
    
    public TreeNode getRoot(){
        return rootNode;
    }

    public TreeNode getNode(String identifier) {
        TreeNode currentNode = null;
        Matcher m = Pattern.compile("\\.?(\\d+)").matcher(identifier);
        if (m.find()) {
            if (Integer.parseInt(m.group(1)) == 0) {
                currentNode = rootNode;
            }
        }
        while (m.find()) {
            int nextNodeIndex = Integer.parseInt(m.group(1));
            if (currentNode.getChildren().size() < nextNodeIndex) {
                currentNode = currentNode.getChildNodeAt(nextNodeIndex);
            }
        }
        
        if(currentNode == null){
            System.out.println("Cannot find node at "+identifier);
        }
        
        return currentNode;
    }
}
