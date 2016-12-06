package net.ic4fre.nytr.hf1;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;


interface Immutable {}

enum Type {Int, Str, Nil}

abstract class Expression {
  private final Type type;
  
  Expression(final Type t) {
    type = t;
  }
  
  Type getType() {
    return type;
  }
  
  void error(int n) {
    throw new RuntimeException("TypeErr #" + n);
  }
}

abstract class NullaryExpression extends Expression {
  NullaryExpression(final Type t) {
    super(t);
  }
}

abstract class ValueExpression extends NullaryExpression {
  ValueExpression(final Type t) {
    super(t);
  }  
}

abstract class UnaryExpression extends NullaryExpression {
  private final Expression leftOperand;

  UnaryExpression(final Type t, final Expression e) {
    super(t);
    leftOperand = e;
  } 

  Expression getLeftOperand() {
    return leftOperand;
  }
  
  Expression getOperand() {
    return getLeftOperand();
  }    
}

abstract class BinaryExpression extends UnaryExpression {
  private final Expression rightOperand;
  
  BinaryExpression(final Type t, final Expression l, final Expression r) {
    super(t, l);
    rightOperand = r;
  } 

  Expression getRightOperand() {
    return rightOperand;
  }  
}

class Length extends UnaryExpression {
  Length(final Expression e) {
    super(Type.Int, e);
    if(Type.Str!=e.getType()) {
      error(2320);
    }    
  }   
}

class IntLiteral extends ValueExpression {
  IntLiteral() {
    super(Type.Int);
  }  
}

class StringLiteral extends ValueExpression {
  StringLiteral() {
    super(Type.Str);
  }  
}

class Addition extends BinaryExpression {
  Addition(final Expression l, final Expression r) {
    super(Type.Int, l, r);
    if(Type.Int!=r.getType() || Type.Int!=l.getType()) {
      error(4432);
    }     
  }
}

class Subtraction extends BinaryExpression {
  Subtraction(final Expression l, final Expression r) {
    super(Type.Int, l, r);
    if(Type.Int!=r.getType() || Type.Int!=l.getType()) {
      error(6566);
    }
  }
}

class Concatenation extends BinaryExpression {
  Concatenation(final Expression l, final Expression r) {
    super(Type.Str, l, r);
    if(Type.Str!=r.getType() || Type.Str!=l.getType()) {
      error(198);
    }
  }
}

class LetDefinition extends BinaryExpression { 
  private final Variable boundVariable;
  
  LetDefinition(final Expression l, final Variable b, final Expression r) {
    super(r.getType(), l, r);
    boundVariable = b;
    if(b.getType()!=l.getType()) {
      error(3987);
    }
  }

  Variable getBoundVariable() {
    return boundVariable;
  }  
}

class Variable extends NullaryExpression {  
  private final String name;
  
  Variable(Type t, final String n) {
    super(t);
    name = n;
  }
  
  String getName() {
    return name;
  }
  
  Variable klone() {
    return new Variable(getType(), getName());
  }
  
  @Override
  public int hashCode() {
    return name.hashCode();
  }
  
  @Override
  public String toString() {
    return "variable " + getName() + "  kind: " + getType().toString();
  }
}

class Context {
  private final Set<Variable> variables = new HashSet<>();
  
  Optional<Variable> findVariableByName(final String n) {
    Optional<Variable> result = Optional.empty();
    for(final Variable v : variables) {
      if(v.getName().equals(n)) {
        result = Optional.of(v.klone());
        break;
      }
    }
    return result;
  }
  
  boolean add(final Variable v) {
    if(findVariableByName(v.getName()).isPresent()) {
      throw new RuntimeException("ERROR_8701");
    }
    return variables.add(v);
  }
    
  void remove(final String name) {
    for(final Variable v : variables) {
      if(v.getName().equals(name)) {
        variables.remove(v);
        break;
      }
    }
  }
  
  @Override
  public String toString() {
    return variables.toString();
  } 
}
 
