package net.mbl.demo;

/**
 * Created by mbl on 31/05/2018.
 */
public class ENumDemo {
  public static void main(String[] args) {
    System.out.println(FailureDomain.NODE.name());
  }

  protected enum FailureDomain {
    NODE, RACK, USERDEFINE;
    public static FailureDomain get(String failureDomainStr) {
      if (failureDomainStr.equals("NODE")) {
        return FailureDomain.NODE;
      } else if (failureDomainStr.equals("RACK")) {
        return FailureDomain.RACK;
      } else {
        return FailureDomain.USERDEFINE;
      }
    }
  }
}
