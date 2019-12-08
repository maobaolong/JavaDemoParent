import java.util.LinkedList;

public class LeftTreeToRightTree {

  static class TreeNode {
    TreeNode l;
    TreeNode r;
    String name;
    public TreeNode(String name) {
      this.name = name;
    }
  }
  
  public static void convert(TreeNode root) {
    if (root == null) {
      return;
    }
    TreeNode curNode = root;
    while (curNode.r != null && curNode.l != null) {
      curNode.r.l = curNode.l.l;
      curNode.r.r = curNode.l.r;
      curNode.l.l = null;
      curNode.l.r = null;
      curNode = curNode.r;
    }
  }

  public static void dumpTree(TreeNode root) {
    TreeNode curNode = root;
    if (curNode != null) {
      System.out.println(curNode.name);
      dumpTree(curNode.l);
      dumpTree(curNode.r);
    }
  }
  
  public static void dumpTree2(TreeNode root) {
    LinkedList<TreeNode> stack = new LinkedList<>();
    TreeNode curNode = root;
    while (curNode != null || !stack.isEmpty()) {
      if (curNode != null) {
        System.out.println(curNode.name);
        stack.push(curNode);
        curNode = curNode.l;
      } else {
        curNode = stack.pop().r;
      }
    }
  }
  public static void main(String[] args) {
    TreeNode a = new TreeNode("a");
    TreeNode b = new TreeNode("b");
    TreeNode c = new TreeNode("c");
    TreeNode d = new TreeNode("d");
    TreeNode e = new TreeNode("e");
    TreeNode f = new TreeNode("f");
    TreeNode g = new TreeNode("g");
    a.l = b;
    b.l = c;
    c.l = d;
    a.r = e;
    b.r = f;
    c.r = g;
    dumpTree2(a);
    convert(a);
    System.out.println("after convert:s");
    dumpTree2(a);
    
  }
}
