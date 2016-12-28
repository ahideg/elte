package net.eak.test;

import java.lang.reflect.Method;

public class Cases {
  final static String[] testClasses = new String[] {
    "net.eak.ejb.Cases",
    "net.eak.rmi.Cases",
    "net.eak.jpa.Cases",
    "net.eak.jdbc.Cases",
    "net.eak.jms.Cases"
  };
  
  public static void main(String[] sa) {
    echo("Hello from " + Cases.class);
    try {
      for(final String cn : testClasses) {
        final Class klass = Class.forName(cn);
        final Method m = klass.getDeclaredMethod("main", new String[0].getClass());
        m.invoke(null, (Object)null);
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  static void echo(final Object o) {
    System.out.println(o==null ? "null" : o.toString());
  }  
}