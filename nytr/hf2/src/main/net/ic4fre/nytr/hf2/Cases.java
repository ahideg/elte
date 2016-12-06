package net.ic4fre.nytr.hf2;

public class Cases {
  public static void main(String[] sa) {
    try {
      caseRdp();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  static void caseRdp() throws Exception {
    echo("--- caseRdp()");
    
    new Rdp().parse(System.in).stream().forEach(e -> System.out.println(e));
  }
    
  static void echo(final Object o) {
    System.out.println(o==null ? "null" : o);
  }
}