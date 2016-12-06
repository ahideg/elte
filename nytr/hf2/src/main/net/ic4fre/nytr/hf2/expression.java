package net.ic4fre.nytr.hf2;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

class ExpException extends RuntimeException {}

interface Immutable {}

abstract class Expression implements Immutable { 
  @Override
  public boolean equals(Object o) {
    return o!=null && getClass().equals(o.getClass());
  }   
}

abstract class LeafExpression extends Expression {
}

abstract class NodeExpression extends Expression {
}

class DummyExpression extends Expression {
  DummyExpression(final Object... exp) {}
  
  @Override
  public boolean equals(Object o) {
    return true;
  }  
}

abstract class UnaryExpression extends NodeExpression {  
  private final Expression expression;
  
  UnaryExpression(final Expression e) {
    expression = e;
  }
  
  Expression getExpression() {
    return expression;
  }  
  
  Expression getLeftExpression() {
    return getExpression();
  }
} 

abstract class BinaryExpression extends UnaryExpression {  
  private final Expression rightExpression;
  
  BinaryExpression(final Expression le, final Expression re) {
    super(le);
    rightExpression = re;
  }
  
  Expression getRightExpression() {
    return rightExpression;
  }  
} 

class App extends BinaryExpression {
  App(final Expression le, final Expression re) {
    super(le, re);
  }
  
  @Override
  public boolean equals(Object o) {
    boolean result = false;
    if(super.equals(o)) {
      final App app = (App)o;
      result = ( (app.getLeftExpression().equals(getLeftExpression()) && 
                  app.getRightExpression().equals(getRightExpression())) );
    }
    return result;
  }    
}

class Suc extends NodeExpression {
  private final Expression expression;
  
  Suc(final Expression e) {
    expression = e;
  }
  
  Expression getExpression() {
    return expression;
  }
  
  @Override
  public boolean equals(Object o) {
    boolean result = false;
    if(super.equals(o)) {
      result = getExpression().equals(((Suc)o).getExpression());
    }
    return result;
  }    
}

final class Var extends LeafExpression {
  private final String name;
  
  Var(final String s) {
    name = s;
  }
  
  String getName() {
    return name;
  }
  
  @Override
  public boolean equals(Object o) {
    return super.equals(o);
  }    
}

class Rec extends BinaryExpression {  
  private final Lam lam;
  
  Rec(final Expression le, final Lam l, final Expression re) {
    super(le, re);
    lam = l;
  }
  
  @Override
  public boolean equals(Object o) {
    return true;
  }    
}

class Lam extends UnaryExpression {
  private final Var var;
  
  Lam(final Expression e, final Var v) {
    super(e);
    var = v;
  }
  
  @Override
  public boolean equals(Object o) {
    return true;
  }    
}

final class Zero extends LeafExpression {
  @Override
  public boolean equals(Object o) {
    return o!=null && getClass().equals(o.getClass());
  }    
}

