package net.mbl.demo;


/**
 * Created by mbl on 31/05/2018.
 */
public class ENumDemo {
  public static void main(String[] args) {
    System.out.println(FailureDomain.NODE.name());
    FailureDomain failureDomain = FailureDomain.valueOf("RACK");
    System.out.println(failureDomain);

    String enumName = "nonono";
    try {
      System.out.println(FailureDomain.valueOf("nonono"));
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("No such component " + enumName);
    }
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
