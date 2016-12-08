package net.ic4fre.nytr.hf2;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import javax.json.Json;
import javax.json.stream.JsonParser;

class Rdp {
  private final String NO = "NO";
  private final String YES = "YES";
  private JsonParser jsonParser;
  private boolean success;
    
  List<String> parse(final InputStream inputStream){
    final List<String> results = new ArrayList<>();
    for(final InputStream is : splitInput(inputStream)) {
      success = true;
      jsonParser = Json.createParser(is); 
      results.add(parseJson());
      jsonParser.close();
    }
    return results;
  }   
  
  private List<InputStream> splitInput(final InputStream inputStream){
    final List<InputStream> inputStreamList = new ArrayList<>();
    try {    
      int bread = 0;
      int bracketDiff = 0;
      List<Byte> byteList = new ArrayList<>();
      while((bread = inputStream.read())!=-1) {
        byteList.add((byte)bread);
        if(bread==123) {
          bracketDiff++;
        } else if(bread==125 && --bracketDiff==0) {
          final byte[] ba = new byte[byteList.size()];
          for(int i=0; i<byteList.size(); i++) {
            ba[i] = byteList.get(i);
          }
          inputStreamList.add(new ByteArrayInputStream(ba));
          byteList = new ArrayList<>();
        }
      }
      if(bracketDiff!=0) {
        error(2486);
      }
    } catch(Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    return inputStreamList;
  }

  private JsonParser.Event nextEvent() {
    return jsonParser.next();
  }
  
  private String eventString() {
    return jsonParser.getString();
  }
    
  private Integer eventInteger() {
    return jsonParser.getInt();
  }
  
  private void error(final Integer n) {
    jsonParser.close();
    throw new RuntimeException("ERROR_" + n.toString() + 
                               " at line " + jsonParser.getLocation().getLineNumber());
  }
  
  private Expression failure() {
    success = false;
    return new DummyExpression();
  }

  private String inputVar() {
    final JsonParser.Event ne = nextEvent();
    if(JsonParser.Event.VALUE_STRING!=ne) {
      error(6767);
    }
    return eventString();
  }
  
  private Expression inputArgs(final String opval){
    Expression expression = null;
    final JsonParser.Event ne = nextEvent();
    switch(ne) {
      case START_OBJECT :
        if(!"Suc".equals(opval)) {
          error(9987);
        }
        expression = new Suc(inputExp(ne));
        break;
      case START_ARRAY :
        if("App".equals(opval)) {
          expression = new App(inputExp(nextEvent()), inputExp(nextEvent()));
        } else if("Lam".equals(opval)) {
          expression = new Lam(new Var(inputVar()), inputExp(nextEvent()));
        } else if("Rec".equals(opval)) {
          expression = new Rec(inputExp(nextEvent()), new Fun(new Var(inputVar()), inputExp(nextEvent())), inputExp(nextEvent())); 
        } else if("Zero".equals(opval)) {
          expression = new Zero();
        } else {
          error(2217);
        }
        if(nextEvent()!=JsonParser.Event.END_ARRAY) {
          error(6210);  
        }        
        break;
      case VALUE_STRING :
        if("Var".equals(opval)) {
          expression = new Var(eventString());
        } else {
          error(5617);
        }
        break;
      default :
        error(3235);
    }
    return expression;    
  }  
  
  Expression inputExp(final JsonParser.Event ne){
    if(ne==JsonParser.Event.START_OBJECT) {
      if(nextEvent()==JsonParser.Event.KEY_NAME && "op".equals(eventString())) {
        if(nextEvent()==JsonParser.Event.VALUE_STRING) {
          final String opval = eventString();
          if(nextEvent()==JsonParser.Event.KEY_NAME && "args".equals(eventString())) {
            final Expression result = inputArgs(opval);
            if(nextEvent()==JsonParser.Event.END_OBJECT) {
              return result;  
            }
            error(7633);
          }
          error(1652);
        }
        error(4512);
      }
      error(2388);
    } 
    System.out.println(ne);
    System.out.println(eventString());
    error(3328);
    return null;
  }
  
  String parseJson(){
    if(nextEvent()==JsonParser.Event.START_OBJECT) {
      if(nextEvent()==JsonParser.Event.KEY_NAME && "e1".equals(eventString())) {
        final Expression e1 = inputExp(nextEvent());
        if(nextEvent()==JsonParser.Event.KEY_NAME && "e2".equals(eventString())) {
          final Expression e2 = inputExp(nextEvent());
          if(nextEvent()==JsonParser.Event.END_OBJECT) {
            return success && e1.equals(e2) ? YES : NO;
          }
          error(9607);
        }
        error(4545);
      }
      error(5587);
    } 
    error(1235);
    return NO;
  } 
}
  
 