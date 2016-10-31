package net.ic4fre.nytr.hf1;

import java.io.InputStream;
import javax.json.Json;
import javax.json.stream.JsonParser;

class Rdp {
  private JsonParser jsonParser;
  private Context context;
  private Expression expression;
  private Type type;
  
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
    
  ParsedResult parse(final InputStream inputStream) throws Exception {
    jsonParser = Json.createParser(inputStream);
    context = new Context();
    if(nextEvent()==JsonParser.Event.START_OBJECT) {
      if(nextEvent()==JsonParser.Event.KEY_NAME && "con".equals(eventString())) {
        inputCon();
        if(nextEvent()==JsonParser.Event.KEY_NAME && "exp".equals(eventString())) {
          inputExp(nextEvent());
          inputTy();
          if(nextEvent()==JsonParser.Event.END_OBJECT) {
            jsonParser.close();
            return new ParsedResult(expression, context, type);
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
  
  private void inputTy() {
    if(nextEvent()==JsonParser.Event.KEY_NAME && "ty".equals(eventString())) {
      if(nextEvent()==JsonParser.Event.VALUE_STRING) {
        if("Int".equals(eventString())) {
          type = Type.Int;
        } else if("Str".equals(eventString())) {
          type = Type.Str;
        } else {
          error(5607);
        }
        return; 
      }   
      error(8885);      
    }
    error(7600);
  }

  private Expression inputArgs(final String opval) {
    Expression expression = null;
    final JsonParser.Event ne = nextEvent();
    switch(ne) {
      case START_OBJECT :
        if(!"Len".equals(opval)) {
          error(9987);
        }
        inputExp(ne);
        break;
      case START_ARRAY :
        if("Add".equals(opval)) {
          final Addition add = new Addition();
          add.setLeftOperand(inputExp(nextEvent()));
          add.setRightOperand(inputExp(nextEvent()));
          expression = add;
        } else if("Sub".equals(opval)) {
          final Subtraction sub = new Subtraction();
          sub.setLeftOperand(inputExp(nextEvent()));
          sub.setRightOperand(inputExp(nextEvent()));
          expression = sub;
        } else if("Cat".equals(opval)) {
          final Concatenation cat = new Concatenation();
          cat.setLeftOperand(inputExp(nextEvent()));
          cat.setRightOperand(inputExp(nextEvent()));
        } else if("Let".equals(opval)) {
          final Expression l = inputExp(nextEvent());
          if(nextEvent()!=JsonParser.Event.VALUE_STRING) {
            error(6647);
          }
          final String varname = eventString();
          final Expression r = inputExp(nextEvent());
          expression = new LetDefinition(l, context.findByName(varname), r);
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
        expression = new IntLiteral(eventInteger());
        break;
      case VALUE_STRING :
        if("Var".equals(opval)) {
          expression = context.findByName(eventString());
        } else if("StrLit".equals(opval)) {
          expression = new StringLiteral(eventString());
        } else {
          error(5617);
        }
        break;
      default :
        error(3235);
    }
    return expression;    
  }
  
  private Expression inputExp(final JsonParser.Event ne) {
    Expression result = null;
    if(ne==JsonParser.Event.START_OBJECT) {
      if(nextEvent()==JsonParser.Event.KEY_NAME && "op".equals(eventString())) {
        if(nextEvent()==JsonParser.Event.VALUE_STRING) {
          final String opval = eventString();
          if(nextEvent()==JsonParser.Event.KEY_NAME && "args".equals(eventString())) {
            result = inputArgs(opval);
            if(nextEvent()==JsonParser.Event.END_OBJECT) {
              return result;  
            }
            error(7633);
          }
          error(7652);
        }
        error(4512);
      }
      error(2388);
    }
    error(3328);
    return null;
  }
  
  private void inputCon() {
    if(nextEvent()==JsonParser.Event.START_OBJECT) {
      JsonParser.Event ne = nextEvent();
      while(ne==JsonParser.Event.KEY_NAME) {
        final String varName = eventString();
        ne = nextEvent();
        final String es = eventString();
        if(ne!=JsonParser.Event.VALUE_STRING || (!"Int".equals(es) && !"Str".equals(es)) ) {
          error(2220);
        }
        context.add(new Variable(varName, "Int".equals(es) ? Type.Int : Type.Str));
        ne = nextEvent();
      }
      if(ne==JsonParser.Event.END_OBJECT) {
        return;  
      }
      error(4905);
    }
    error(9856);
  }
  
  class ParsedResult {
    final Context context;
    final Expression expression;
    final Type type;
    
    ParsedResult(final Expression a, final Context c, final Type t) {
      context = c;
      expression = a;
      type = t;
    }
    
    Context getContext() {
      return context;
    }
    
    Type getType() {
      return type;
    }
    
    Expression getExpression() {
      return expression;
    }
  }
}

 