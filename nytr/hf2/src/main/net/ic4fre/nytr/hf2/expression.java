package net.ic4fre.nytr.hf2;

class ExpException extends RuntimeException {}

abstract class Expression {}

abstract class LeafExpression extends Expression {}

abstract class NodeExpression extends Expression {}

class DummyExpression extends Expression {
  DummyExpression(final Object... exp) {}
}

abstract class UnaryExpression extends NodeExpression {  
  private Expression expression;
  
  UnaryExpression(final Expression e) {
    expression = e;
  }
  
  Expression getExpression() {
    return expression;
  }  
  
  Expression getLeftExpression() {
    return getExpression();
  }  
  
  void setLeftExpression(final Expression re) {
    setExpression(re);
  }   
    
  void setExpression(final Expression re) {
    expression = re;
  }   
  
  @Override
  public String toString() {
    return "{ le: " + getLeftExpression() + " }";
  }
} 

abstract class BinaryExpression extends UnaryExpression {  
  private Expression rightExpression;
  
  BinaryExpression(final Expression le, final Expression re) {
    super(le);
    rightExpression = re;
  }
  
  Expression getRightExpression() {
    return rightExpression;
  }   
  
  void setRightExpression(final Expression re) {
    rightExpression = re;
  } 
  
  @Override
  public String toString() {
    return "[ " + super.toString() + ", {re: " + getRightExpression().toString() + " } ]";
  }
} 

class App extends BinaryExpression {
  App(final Expression le, final Expression re) {
    super(le, re);
  }
  
  @Override
  public String toString() {
    return "{ App: " + super.toString() + " }";
  }
}

class Suc extends UnaryExpression {
  Suc(final Expression e) {
    super(e);
  }   
  
  @Override
  public String toString() {
    return "{ Suc: " + super.toString() + " }";
  }   
}

final class Var extends LeafExpression {
  private String name;
  
  Var(final String s) {
    name = s;
  }
  
  String getName() {
    return name;
  }
  
  void setName(final String n) {
    name = n;
  }
  
  @Override
  public boolean equals(Object o) {
    return o!=null && o instanceof Var && ((Var)o).getName().equals(getName());
  } 
  
  @Override
  public String toString() {
    return "{ Var: " + getName() + " }";
  }   
}

class Rec extends BinaryExpression {  
  private final Fun fun;
  
  Rec(final Expression le, final Fun l, final Expression re) {
    super(le, re);
    fun = l;
  }
  
  Fun getFun() {
    return fun;
  } 
  
  @Override
  public String toString() {
    return "{ Rec: [" + super.toString() + ", " + getFun().toString() + "] }";
  }    
}

class Lam extends UnaryExpression {
  private final Var var;
  
  Lam(final Var v, final Expression e) {
    super(e);
    var = v;
  }
  
  Var getVar() {
    return var;
  }
  
  @Override
  public String toString() {
    return "{ Lam: [" + super.toString() + ", " + getVar().toString() + "] }";
  }    
}

class Fun extends Lam {
  Fun(final Var v, final Expression e) {
    super(v, e);
  }
  
  @Override
  public String toString() {
    return "{ Fun: [" + super.toString() + ", " + getVar().toString() + "] }";
  }    
}

final class Zero extends LeafExpression {
  @Override
  public String toString() {
    return "[ZERO]";
  }   
}
