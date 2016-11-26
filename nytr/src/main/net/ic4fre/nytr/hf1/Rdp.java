package net.ic4fre.nytr.hf1;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.function.Function;
import javax.json.Json;
import javax.json.stream.JsonParser;

class Rdp {
  private JsonParser jsonParser;
  private Context context;
  private Expression expression;
  private Class<? extends Type> type;
  private boolean success;
  
  private List<InputStream> splitInput(final InputStream inputStream) throws Exception {
    final List<InputStream> inputStreamList = new ArrayList<>();
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
  
  private Variable getVariable(final String name) {
    final Optional<Variable<? extends Type>> var = context.findVariableByName(name);
    Variable variable = null;  
    if(var.isPresent()) {
      variable = var.get();
    } else {
      success = false;
      variable = new Variable("", null);
    }
    return variable;
  }
  
  private void error(final Integer n) {
    jsonParser.close();
    throw new RuntimeException("ERROR_" + n.toString() + 
                               " at line " + jsonParser.getLocation().getLineNumber());
  }
    
  List<ParsedResult> parse(final InputStream inputStream) throws Exception {
    final List<ParsedResult> results = new ArrayList<>();
    for(final InputStream is : splitInput(inputStream)) {
      success = true;
      jsonParser = Json.createParser(is); 
      results.add(parseJson());
      jsonParser.close();
    }
    return results;
  } 
  
  ParsedResult parseJson() throws Exception {
    context = new Context();
    if(nextEvent()==JsonParser.Event.START_OBJECT) {
      if(nextEvent()==JsonParser.Event.KEY_NAME && "con".equals(eventString())) {
        inputCon();
        if(nextEvent()==JsonParser.Event.KEY_NAME && "exp".equals(eventString())) {
          inputExp(nextEvent());
          inputTy();
          if(nextEvent()==JsonParser.Event.END_OBJECT) {
            return new ParsedResult(expression, context, type, success);
          }
          error(9607);
        }
        error(4545);
      }
      error(5587);
    } 
    error(1235);
    return ParsedResult.failure;
  }
  
  private void inputTy() {
    if(nextEvent()==JsonParser.Event.KEY_NAME && "ty".equals(eventString())) {
      if(nextEvent()==JsonParser.Event.VALUE_STRING) {
        if("Int".equals(eventString())) {
          type = Int.class;
        } else if("Str".equals(eventString())) {
          type = Str.class;
        } else {
          success = false;
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
          expression = new Addition(inputExp(nextEvent()), inputExp(nextEvent()));
        } else if("Sub".equals(opval)) {
          expression = new Subtraction(inputExp(nextEvent()), inputExp(nextEvent()));
        } else if("Cat".equals(opval)) {
          expression = new Concatenation(inputExp(nextEvent()), inputExp(nextEvent()));
        } else if("Let".equals(opval)) {
          final Expression l = inputExp(nextEvent());
          if(nextEvent()!=JsonParser.Event.VALUE_STRING) {
            error(6647);
          }
          final String varname = eventString();
          final Expression r = inputExp(nextEvent());
          expression = new LetDefinition(l, getVariable(varname), r);
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
        expression = new IntLiteral(new Int(eventInteger()));
        break;
      case VALUE_STRING :
        if("Var".equals(opval)) {
          expression = getVariable(eventString());
        } else if("StrLit".equals(opval)) {
          expression = new StringLiteral(new Str(eventString()));
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
          error(1652);
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
        if("Int".equals(es)) {
          context.add(new Variable<Int>(varName, Int.class));  
        } else if("Str".equals(es)) {
          context.add(new Variable<Str>(varName, Str.class));
        } else {
          success = false;
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
  
  static class ParsedResult {
    final Context context;
    final Expression expression;
    final Class<? extends Type> type;
    final boolean success;
    
    static final ParsedResult failure = new ParsedResult(null, null, null, false);
    
    ParsedResult(final Expression a, final Context c, final Class<? extends Type> t, final boolean s) {
      context = c;
      expression = a;
      type = t;
      success = s;
    }
    
    Context getContext() {
      return context;
    }
    
    Class<? extends Type> getType() {
      return type;
    }
    
    Expression getExpression() {
      return expression;
    }
    
    String success() {
      return success ? "YES" : "NO";
    }
  }
}
  
 