package net.ic4fre.nytr.hf2;

import java.util.List;
import java.util.ArrayList;

class ExpException extends RuntimeException {}

interface Immutable {}

class BindingTable {
  private final List<String[]> equivalences = new ArrayList<>();
  
  void setEquivalence(final String n1, final String n2) {
    equivalences.add(0, new String[] {n1, n2});
  }
  
  void unsetLastEquivalence() {
    equivalences.remove(0);
  }
  
  boolean compareVars(final String n1, final String n2) {
    return ((!equivalences.stream().filter(e -> e[0].equals(n1) || e[1].equals(n2)).findFirst().isPresent()) && n1.equals(n2)) ||
      equivalences.stream().filter(e -> e[0].equals(n1) && e[1].equals(n2)).findFirst().isPresent();
  }
}

abstract class Expression implements Immutable { 
  boolean equalz(final Expression o, final BindingTable bindingTable) {
    return o!=null && getClass().equals(o.getClass());
  }   
  
  @Override
  public boolean equals(Object o) {
    return equalz((Expression)o, new BindingTable());
  }     
}

abstract class LeafExpression extends Expression {}

abstract class NodeExpression extends Expression { 
  abstract Expression klone(final String varname, final Expression replacement);
}

class DummyExpression extends Expression {
  DummyExpression(final Object... exp) {}
  
  @Override
  boolean equalz(final Expression o, final BindingTable bindingTable) {
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
  Suc klone(final String varname, final Expression replacement) {
    if(getLeftExpression() instanceof Var) {
      return new Suc(replacement);
    }
    return new App(e1, e2);
  } 
  
  @Override
  boolean equalz(final Expression o, final BindingTable bindingTable) {
    boolean result = false;
    if(super.equalz(o, bindingTable)) {
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
  Suc klone(final String varname, final Expression replacement) {
    if(getLeftExpression() instanceof Var && ((Var)getLeftExpression()).getName().equals(varname)) {
      return new Suc(replacement);
    }
    return getExpression().klone(varname, replacement);
  } 
  
  @Override
  boolean equalz(final Expression o, final BindingTable bindingTable) {
    boolean result = false;
    if(super.equalz(o, bindingTable)) {
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
  boolean equalz(final Expression o, final BindingTable bindingTable) {
    boolean result = false;
    if(super.equalz(o, bindingTable)) {
      result = bindingTable.compareVars(getName(), ((Var)o).getName());
    }
    return result;
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
  boolean equalz(final Expression o, final BindingTable bindingTable) {
    boolean result = false;
    if(super.equalz(o, bindingTable)) {
      final Rec rec = (Rec)o;
      result = getLeftExpression().equalz(rec.getLeftExpression(), bindingTable) && 
        getRightExpression().equalz(rec.getRightExpression(), bindingTable) &&
        getFun().equalz(rec.getFun(), bindingTable);
    }
    return result;
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
  boolean equalz(final Expression o, final BindingTable bindingTable) {
    boolean result = false;
    if(super.equalz(o, bindingTable)) {
      final Lam lam = (Lam)o;
      bindingTable.setEquivalence(lam.getVar().getName(), getVar().getName());
      result = getExpression().equalz(lam.getExpression(), bindingTable);
      bindingTable.unsetLastEquivalence();
    }
    return result;
  }    
}

class Fun extends Lam {
  Fun(final Var v, final Expression e) {
    super(v, e);
  }
}

final class Zero extends LeafExpression {
  
}
