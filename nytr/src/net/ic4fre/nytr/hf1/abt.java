package net.ic4fre.nytr.hf1;

import java.util.Set;
import java.util.HashSet;

enum Type {
  Int, Str
}

abstract class Expression {
  private final Type type;
  
  Expression(final Type t) {
    type = t;
  }
  
  Type getType() {
    return type;
  } 
  
  protected void error(final Integer n) {
    throw new RuntimeException("ERROR_" + n.toString());
  }
}

abstract class NullaryExpression<T> extends Expression {
  private final T value;
  
  NullaryExpression(final T v, final Type t) {
    super(t);
    value = v;
  }
  
  T getValue() {
    return value;
  }
}

abstract class UnaryExpression extends Expression {
  private Expression leftOperand;
  private final Type leftOpType;
  
  UnaryExpression(final Type ot, final Type t) {
    super(t);
    leftOpType = ot;
  }
  
  void setOperand(final Expression e) {
    setLeftOperand(e);
  }
  
  void setLeftOperand(final Expression e) {
    if(e.getType()!=leftOpType) {
      error(7288);
    }
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
  private Expression rightOperand;
  private final Type rightOpType;
  
  BinaryExpression(final Type lt, final Type rt, final Type t) {
    super(lt, t);
    rightOpType = rt;
  }
  
  void setRightOperand(final Expression e) {
    if(e.getType()!=rightOpType) {
      error(4559);
    }
    rightOperand = e;
  } 

  Expression getRightOperand() {
    return rightOperand;
  }  
}

class Length extends UnaryExpression {
  Length() {
    super(Type.Str, Type.Int);
  }
}

class IntLiteral extends NullaryExpression<Integer> {
  IntLiteral(final Integer v) {
    super(v, Type.Int);
  }
}

class StringLiteral extends NullaryExpression<String> {
  StringLiteral(final String v) {
    super(v, Type.Str);
  }
}

class Addition extends BinaryExpression {
  Addition() {
    super(Type.Int, Type.Int, Type.Int);
  }
}

class Subtraction extends BinaryExpression {
  Subtraction() {
    super(Type.Int, Type.Int, Type.Int);
  }
}

class Concatenation extends BinaryExpression {
  Concatenation() {
    super(Type.Str, Type.Str, Type.Str);
  }
}

class LetDefinition extends Expression {
  private final Expression rightOperand;
  private final Variable middleOperand;
  private final Expression leftOperand;
  private final Type retType;
  
  LetDefinition(final Expression l, final Variable v, final Expression r) {
    super(Type.Str);
    if(l.getType()!=v.getType() || v.getType()!=r.getType()) {
      error(8624);
    }
    rightOperand = r;
    middleOperand = v;
    leftOperand = l;
    retType = leftOperand.getType();
  }
  
  @Override
  Type getType() {
    return retType;
  } 
}

class Variable extends NullaryExpression<String> {  
  Variable(final String n, final Type t) {
    super(n, t);
  }
  
  String getName() {
    return getValue();
  }
  
  @Override
  public String toString() {
    return "[" + getName() + ", " + getType() + "]";
  }
  
  @Override
  public boolean equals(Object o) {
    if(o==this) {
      return true;
    }
    if(o==null || !(o instanceof Variable)) {
      return false;
    }
    final Variable v = (Variable)o;
    return getName().equals(v.getName()) && getType()==v.getType();
  }
}

class Context {
  private final Set<Variable> variables = new HashSet<>();
  
  Variable findByName(final String n) {
    Variable result = null;
    for(final Variable v : variables) {
      if(v.getName().equals(n)) {
        result = v;
        break;
      }
    }
    return result;
  }
  
  boolean add(final Variable v) {
    return variables.add(v);
  }
  
  @Override
  public String toString() {
    return variables.toString();
  }
}

 