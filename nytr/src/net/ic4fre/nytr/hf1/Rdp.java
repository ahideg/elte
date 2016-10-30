package net.ic4fre.nytr.hf1;

import java.io.InputStream;
import javax.json.Json;
import javax.json.stream.JsonParser;

class Rdp {
  private JsonParser jsonParser;
  
  private JsonParser.Event nextEvent() {
    return jsonParser.next();
  }
  
  private String eventString() {
    return jsonParser.getString();
  }
  
  private void error(final Integer n) {
    jsonParser.close();
    throw new RuntimeException("ERROR_" + n.toString() + 
                               " at line " + jsonParser.getLocation().getLineNumber());
  }
    
  Result parse(final InputStream inputStream, final Object abtBuilder) throws Exception {
    jsonParser = Json.createParser(inputStream);
    if(nextEvent()==JsonParser.Event.START_OBJECT) {
      if(nextEvent()==JsonParser.Event.KEY_NAME && "con".equals(eventString())) {
        inputCon();
        if(nextEvent()==JsonParser.Event.KEY_NAME && "exp".equals(eventString())) {
          inputExp(nextEvent());
          if(nextEvent()!=JsonParser.Event.END_OBJECT) {
            jsonParser.close();
            return null;  
          }
          error(9607);
        }
        error(4545);
      }
      error(5587);
    } 
    error(1235);
    return null;
  }

  private void inputArgs(final String opval) {
    final JsonParser.Event ne = nextEvent();
    switch(ne) {
      case START_OBJECT :
        if(!"Len".equals(opval)) {
          error(9987);
        }
        inputExp(ne);
        break;
      case START_ARRAY :
        if(opval.matches("^Add|Sub|Cat$")) {
          inputExp(nextEvent());
          inputExp(nextEvent());
        } else if("Let".equals(opval)) {
          inputExp(nextEvent());
          if(nextEvent()!=JsonParser.Event.VALUE_STRING) {
            error(6647);
          }
          inputExp(nextEvent());
        } else {
          error(2217);
        }
        if(nextEvent()!=JsonParser.Event.END_ARRAY) {
          error(6210);  
        }        
        break;
      case VALUE_NUMBER :
        if(!"IntLit".equals(opval)) {
          error(1217);
        }
        break;
      case VALUE_STRING :
        if(!"Var".equals(opval) && !"StrLit".equals(opval)) {
          error(5617);
        }
        break;
      default :
        error(3235);
    }
  }
  
  private void inputExp(final JsonParser.Event ne) {
    if(ne==JsonParser.Event.START_OBJECT) {
      if(nextEvent()==JsonParser.Event.KEY_NAME && "op".equals(eventString())) {
        if(nextEvent()==JsonParser.Event.VALUE_STRING) {
          final String opval = eventString();
          if(nextEvent()==JsonParser.Event.KEY_NAME && "args".equals(eventString())) {
            inputArgs(opval);
            if(nextEvent()==JsonParser.Event.END_OBJECT) {
              return;  
            }
            error(7633);
          }
          error(7652);
        }
        error(4512);
      }
      error(2388);
    }
    System.out.println(ne);
    error(3328);
  }
  
  private void inputCon() {
    if(nextEvent()==JsonParser.Event.START_OBJECT) {
      JsonParser.Event ne = nextEvent();
      while(ne==JsonParser.Event.KEY_NAME) {
        ne = nextEvent();
        final String es = eventString();
        if(ne!=JsonParser.Event.VALUE_STRING || (!"Int".equals(es) && !"Str".equals(es)) ) {
          error(2220);
        }
        ne = nextEvent();
      }
      if(ne==JsonParser.Event.END_OBJECT) {
        return;  
      }
      error(4905);
    }
    error(9856);
  }
  
  class Result {
    final Context context;
    final AbtNode abtNode;
    
    Result(final AbtNode a, final Context c) {
      context = c;
      abtNode = a;
    }
    
    Context getContext() {
      return context;
    }
    
    AbtNode getAbtNode() {
      return abtNode;
    }
  }
}

 