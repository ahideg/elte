package net.ic4fre.nytr.hf1;

import java.io.InputStream;
import javax.json.Json;
import javax.json.stream.JsonParser;

public class Cases {
  public static void main(String[] sa) {
    try {
      //caseInputStream();
      //caseJsonStreaming();
      caseRdp();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  static void caseRdp() throws Exception {
    echo("--- caseRdp()");
    
    final Rdp rdp = new Rdp();
    final Rdp.ParsedResult r = rdp.parse(System.in);
    echo(r.getType());
  }
  
  static void caseInputStream() throws Exception {
    echo("--- caseInputStream()");
    
    int c;
    while((c=System.in.read())!=-1) {
      System.out.print((char)c);
    }
  }
  
  static void caseJsonStreaming() throws Exception {
    echo("--- caseInputStream()");
    
    final JsonParser jsonParser = Json.createParser(System.in);
    while(jsonParser.hasNext()) {
      final JsonParser.Event event = jsonParser.next();
      echo(event);
      if(event==JsonParser.Event.KEY_NAME 
         || event==JsonParser.Event.VALUE_STRING
         || event==JsonParser.Event.VALUE_NUMBER) {
        echo("    " + jsonParser.getString());
      }
    }
  }
  
  static void echo(final Object o) {
    System.out.println(o==null ? "null" : o);
  }
}