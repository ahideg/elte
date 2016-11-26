package net.ic4fre.nytr.hf1;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;


interface Immutable {}

abstract class Type<J> implements Immutable {
  private final J representative;
  
  Type(final J r) {
    representative = r;
  }
  
  J getRepresentative() {
    return representative;
  }
  
  @Override 
  public String toString() {
    return getRepresentative().toString();
  }
}    
  
class Int extends Type<Integer> {
  Int(final Integer i) {
    super(i);
  }
}

class Str extends Type<String> {
  Str(final String s) {
    super(s);
  }
}

abstract class Expression<T extends Type> implements Immutable {
  T evaluate() {
    return null;
  }  
}

abstract class NullaryExpression<T extends Type> extends Expression<T> {}

abstract class ValueExpression<T extends Type> extends NullaryExpression<T> {
  private final T value;

  ValueExpression(final T v) {
    value = v;
  }

  @Override
  T evaluate() {
    return value;
  }
}

abstract class UnaryExpression<L extends Type, T extends Type> extends NullaryExpression<T> {
  private final Expression leftOperand;

  UnaryExpression(final Expression<L> e) {
    leftOperand = e;
  } 

  Expression<L> getLeftOperand() {
    return leftOperand;
  }
  
  Expression<L> getOperand() {
    return getLeftOperand();
  }    
}

abstract class BinaryExpression<L extends Type, R extends Type, T extends Type> extends UnaryExpression<L, T> {
  private final Expression<R> rightOperand;
  
  BinaryExpression(final Expression<L> l, final Expression<R> r) {
    super(l);
    rightOperand = r;
  } 

  Expression<R> getRightOperand() {
    return rightOperand;
  }  
}

class Length extends UnaryExpression<Str, Int> {
  Length(final Expression<Str> e) {
    super(e);
  }   
}

class IntLiteral extends ValueExpression<Int> {
  IntLiteral(final Int v) {
    super(v);
  }  
}

class StringLiteral extends ValueExpression<Str> {
  StringLiteral(final Str v) {
    super(v);
  }  
}

class Addition extends BinaryExpression<Int, Int, Int> {
  Addition(final Expression<Int> l, final Expression<Int> r) {
    super(l, r);
  }
}

class Subtraction extends BinaryExpression<Int, Int, Int> {
  Subtraction(final Expression<Int> l, final Expression<Int> r) {
    super(l, r);
  }
}

class Concatenation extends BinaryExpression<Str, Str, Str> {
  Concatenation(final Expression<Str> l, final Expression<Str> r) {
    super(l, r);
  }
}

class LetDefinition <L extends Type, R extends Type> extends Expression<L> {
  private final Expression<R> rightOperand;
  private final Expression<L> leftOperand;
  private final Variable<L> boundVariable;
  
  LetDefinition(final Expression<L> l, final Variable<L> b, final Expression<R> r) {
    rightOperand = r;
    boundVariable = b;
    leftOperand = l;
  }
  
  Expression<R> getRightOperand() {
    return rightOperand;
  }

  Expression<L> getLeftOperand() {
    return leftOperand;
  }

  Variable<L> getBoundVariable() {
    return boundVariable;
  }  
}

class Variable<T extends Type> extends Expression<T> {  
  private final String name;
  private final Class<T> kind;
  
  Variable(final String n, final Class<T> k) {
    name = n;
    kind = k;
  }
  
  String getName() {
    return name;
  }
  
  Class<T> getKind() {   
    return kind;
  }
  
  Variable<T> klone() {
    return new Variable<>(getName(), kind);
  }
  
  @Override
  public int hashCode() {
    return name.hashCode();
  }
  
  @Override
  public String toString() {
    return "variable " + getName() + "  kind: " + getKind().toString();
  }
}

class Context {
  private final Set<Variable<? extends Type>> variables = new HashSet<>();
  
  Optional<Variable<? extends Type>> findVariableByName(final String n) {
    Optional<Variable<? extends Type>> result = Optional.empty();
    for(final Variable<? extends Type> v : variables) {
      if(v.getName().equals(n)) {
        result = Optional.of(v.klone());
        break;
      }
    }
    return result;
  }
  
  boolean add(final Variable<? extends Type> v) {
    if(findVariableByName(v.getName()).isPresent()) {
      throw new RuntimeException("ERROR_8701");
    }
    return variables.add(v);
  }
  
  @Override
  public String toString() {
    return variables.toString();
  }
}

 
