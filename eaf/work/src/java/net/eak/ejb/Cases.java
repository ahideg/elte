package net.eak.ejb;

import javax.ejb.EJBHome;

public class Cases {
  public static void main(String[] sa) {
    echo("Hello from " + Cases.class);
  }

  static void echo(final Object o) {
    System.out.println(o==null ? "null" : o.toString());
  }  
}