package net.ic4fre.nytr.hf2;

import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

class TraversalInfo {
  private static int varId = 0;
  private Map<String, String> aliases;
  
  private TraversalInfo(final Map<String, String> a) {
    aliases = a;
  }
  
  TraversalInfo() {
    this(new HashMap<>());
  }
  
  String getUniqueName(final String varname) {
    final String n = aliases.get(varname);
    return n==null ? varname : n;
  }
  
  String addBoundVar(final String varname) {
    final String id = "var_" + varId++;
    aliases.put(varname, id);
    return id;
  }
  
  TraversalInfo klone() {
    return new TraversalInfo(aliases);
  }
}

class Reduction { 
  void setUniqueBoundVarNames(final Expression ex, final TraversalInfo ti) {
    if(ex instanceof Zero) {
      return;
    }
    
    if(ex instanceof Var) {
      final Var var = (Var)ex;
      var.setName(ti.getUniqueName(var.getName()));
      return;
    }
    
    if(ex instanceof Lam) {
      final Lam lam = (Lam)ex;
      lam.getVar().setName(ti.addBoundVar(lam.getVar().getName()));
      setUniqueBoundVarNames(lam.getExpression(), ti.klone());
    } else if(ex instanceof Rec) {
      final Rec rec = (Rec)ex;
      setUniqueBoundVarNames(rec.getLeftExpression(), ti.klone());
      setUniqueBoundVarNames(rec.getRightExpression(), ti.klone());
      final Fun fun = rec.getFun();
      fun.getVar().setName(ti.addBoundVar(fun.getVar().getName()));
      setUniqueBoundVarNames(fun.getExpression(), ti.klone());
    } else if(ex instanceof UnaryExpression) {
      final UnaryExpression uex = (UnaryExpression)ex;
      if(uex.getExpression() instanceof Var) {
        final Var var = (Var)(uex.getExpression());
        var.setName(ti.getUniqueName(var.getName()));
      } 
      setUniqueBoundVarNames(uex.getExpression(), ti.klone());  
     
      if(ex instanceof BinaryExpression) {
        final BinaryExpression bex = (BinaryExpression)ex;
        if(bex.getRightExpression() instanceof Var) {
          final Var var = (Var)(bex.getRightExpression());
          var.setName(ti.getUniqueName(var.getName()));
        } 
        setUniqueBoundVarNames(bex.getRightExpression(), ti.klone());
      }  
    }
  }  
  
  Optional<Expression> reduce(final Expression exp) {    
    Optional<Expression> result = Optional.of(exp);
    boolean finito = false;
    while(!finito) {     
      final Expression ex = result.get();      
      result = reduce_7_15(ex);
      if(!result.isPresent()) {  
        result = reduce_7_14(ex);
        if(!result.isPresent()) {
          result = reduce_7_13(ex);
          if(!result.isPresent()) {
            result = reduce_7_12(ex);
            if(!result.isPresent()) {
              result = reduce_7_11(ex);
              if(!result.isPresent()) {
                result = reduce_7_10(ex);
                if(!result.isPresent()) {
                  result = reduce_7_9(ex);                
                  if(!result.isPresent()) {
                    finito = true;
                    result = Optional.of(ex);
                  }  
                }  
              }
            }            
          }     
        }        
      }
    }
    return result;
  }
  
  private boolean isVal(final Expression ex) {
    return ex instanceof Zero ||
      ex instanceof Lam ||
      (ex instanceof Suc && isVal(((Suc)ex).getExpression()));
  }  
  
  private Optional<Expression> reduce_7_9(final Expression ex) {
    Optional<Expression> result = Optional.empty();
/*if(ex instanceof Suc) {
      final Suc suc = (Suc)ex;
      final Optional<Expression> ev = reduce(suc.getExpression());          
      if(ev.isPresent()) {
        result = Optional.of(new Suc(ev.get()));
      }
    }*/
    return result;
  }     
  
  private Optional<Expression> reduce_7_10(final Expression ex) {
    Optional<Expression> result = Optional.empty();
    if(ex instanceof Rec) {
      final Rec rec = (Rec)ex; 
      final Optional<Expression> red = reduce(rec.getRightExpression());  
      if(red.isPresent()) {
        result = Optional.of(new Rec(rec.getLeftExpression(), rec.getFun(), red.get())); 
      }
    }
    return result;
  }  
  
  private Optional<Expression> reduce_7_11(final Expression ex) {
    Optional<Expression> result = Optional.empty();
    if(ex instanceof Rec) {
      final Rec rec = (Rec)ex;
      if(rec.getRightExpression() instanceof Zero) {
        result = Optional.of(rec.getLeftExpression());
      }
    }
    return result;
  }
    
  private Optional<Expression> reduce_7_12(final Expression ex) {
    Optional<Expression> result = Optional.empty();
    if(ex instanceof Rec) {
      final Rec rec = (Rec)ex;
      final Expression se = rec.getRightExpression();
      if(se instanceof Suc && isVal(se)) {
        final Expression e = ((Suc)se).getExpression();
        final Fun fun = (Fun)(rec.getFun());
        final Expression e0 = rec.getLeftExpression();
        final Expression e1 = fun.getExpression();
        if(e1 instanceof Var) {            
          if(fun.getVar().getName().equals(((Var)e1).getName())) {
            result = Optional.of(new Rec(e0, fun, e));
          } else {
            result = Optional.of(e1);
          }
        } else {
          rec.setRightExpression(e);
          substituteVar(e1, fun.getVar().getName(), new Rec(e0, fun, e));
          result = Optional.of(e1); 
        }               
      }
    }
    return result;
  } 
  
  private Optional<Expression> reduce_7_13(final Expression ex) {
    Optional<Expression> result = Optional.empty();
    if(ex instanceof App) {
      final App app = (App)ex;
      final Optional<Expression> ev = reduce(app.getLeftExpression());        
      if(ev.isPresent()) {
        result = Optional.of(new App(ev.get(), app.getRightExpression()));
      }
    }
    return result;
  }  
  
  private Optional<Expression> reduce_7_14(final Expression ex) {
    Optional<Expression> result = Optional.empty();
    /*if(ex instanceof App) {
      final App app = (App)ex;
      final Expression e = app.getLeftExpression();
      final Expression e1 = app.getRightExpression();
      if(isVal(e)) {  
        final Optional<Expression> ev = reduce(e1);  
        if(ev.isPresent()) {
          result = Optional.of(new App(e, ev.get()));
        }
      }
    }*/
    return result;
  }  

  private Optional<Expression> reduce_7_15(final Expression ex) {
    Optional<Expression> result = Optional.empty();
    if(ex instanceof App) {
      final App app = (App)ex;
      final Expression e1 = app.getRightExpression();
      //if(isVal(e1)) {
        final Expression el = app.getLeftExpression();
        if(el instanceof Lam) {
          final Lam lam = (Lam)el;
          final Expression e2 = lam.getExpression();
          if(e2 instanceof Var) {            
            if(lam.getVar().getName().equals(((Var)e2).getName())) {
              result = Optional.of(e1);
            } else {
              result = Optional.of(e2);
            }
          } else {
            substituteVar(e2, lam.getVar().getName(), e1);
            result = Optional.of(e2); 
          }
        }
      //}
    }
    return result;
  }
  
  private void substituteVar(final Expression where, final String varName, final Expression toWhat) {
    if(where instanceof LeafExpression) {
      return;
    }

    if(where instanceof UnaryExpression) {
      final UnaryExpression uex = (UnaryExpression)where;
      final Expression exp = uex.getExpression();
      if(exp instanceof Var) {
        final Var var = (Var)exp;
        if(var.getName().equals(varName)) {
          uex.setExpression(toWhat);
        }
      } else {
        substituteVar(exp, varName, toWhat);    
      }
      
      if(where instanceof BinaryExpression) {
        final BinaryExpression bex = (BinaryExpression)where;
        final Expression exp1 = bex.getRightExpression();
        if(exp1 instanceof Var) {
          final Var var = (Var)exp1;
          if(var.getName().equals(varName)) {
            bex.setRightExpression(toWhat);
          }
        } else {
          substituteVar(exp, varName, toWhat);    
        }
        
        if(where instanceof Rec) {
          substituteVar(((Rec)where).getFun().getExpression(), varName, toWhat);
        }
      }
    }
  }
}






