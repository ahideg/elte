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
  private Type typeTy;
  private Type typeFound;
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
    final Optional<Variable> var = context.findVariableByName(name);
    Variable variable = null;  
    if(var.isPresent()) {
      variable = var.get();
    } else {
      success = false;
      variable = new Variable(Type.Str, "bogusVar");
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
          typeFound = inputExp(nextEvent()).getType();
          inputTy();
          if(nextEvent()==JsonParser.Event.END_OBJECT) {
            if(typeTy!=typeFound) {
              success = false;
            }
            return new ParsedResult(expression, context, typeTy, success);
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
          typeTy = Type.Int;
        } else if("Str".equals(eventString())) {
          typeTy = Type.Str;
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
        try {
          expression = new Length(inputExp(ne));
        } catch(Exception e) {
          success = false;
          expression = new IntLiteral();
        }
        break;
      case START_ARRAY :
        if("Add".equals(opval)) {
          try {
            expression = new Addition(inputExp(nextEvent()), inputExp(nextEvent()));
          } catch(Exception e) {
            success = false;
            expression = new IntLiteral();
          }
        } else if("Sub".equals(opval)) {
          try {
            expression = new Subtraction(inputExp(nextEvent()), inputExp(nextEvent()));
          } catch(Exception e) {
            success = false;
            expression = new IntLiteral();
          }
        } else if("Cat".equals(opval)) {
          try {
            expression = new Concatenation(inputExp(nextEvent()), inputExp(nextEvent()));
          } catch(Exception e) {
            success = false;
            expression = new StringLiteral();
          }
        } else if("Let".equals(opval)) {
          final Expression l = inputExp(nextEvent());
          if(nextEvent()!=JsonParser.Event.VALUE_STRING) {
            error(6647);
          }
          final String varname = eventString();
          if(context.findVariableByName(varname).isPresent()) {
            success = false;
          } else {
            context.add(new Variable(l.getType(), varname));
          }
          final Expression r = inputExp(nextEvent());
          try {
            expression = new LetDefinition(l, new Variable(l.getType(), varname), r);
          } catch(Exception e) {
            success = false;
            expression = new StringLiteral();
          }
          context.remove(varname);
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
        expression = new IntLiteral();
        break;
      case VALUE_STRING :
        if("Var".equals(opval)) {
          expression = getVariable(eventString());
        } else if("StrLit".equals(opval)) {
          expression = new StringLiteral();
        } else if("Len".equals(opval)) {
          expression = new Length(new StringLiteral());
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
          context.add(new Variable(Type.Int, varName));  
        } else if("Str".equals(es)) {
          context.add(new Variable(Type.Str, varName));
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
    final Type type;
    final boolean success;
    
    static final ParsedResult failure = new ParsedResult(null, null, null, false);
    
    ParsedResult(final Expression a, final Context c, final Type t, final boolean s) {
      context = c;
      expression = a;
      type = t;
      success = s;
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
    
    String success() {
      return success ? "YES" : "NO";
    }
  }
}
  
 