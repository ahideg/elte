package net.ic4fre.nytr.hf2;

import java.util.Optional;

class Reduction {
  static Optional<Expression> reduce(final Expression e) {
    Optional<Expression> result = reduce_7_11(e);
    if(result.isPresent()) {
      return result;
    }
    return result;
  }
  
  private static boolean isVal(final Expression e) {
    return e instanceof Zero ||
    (e instanceof Lam && isVal(((Lam)e).getExpression())) ||
    (e instanceof Suc && isVal(((Suc)e).getExpression()));
  }  
  
  private static Optional<Expression> reduce_7_11(final Expression e) {
    Optional result = Optional.empty();
    if(e instanceof Rec) {
      final Rec rec = (Rec)e;
      if(rec.getRightExpression() instanceof Zero) {
        result = Optional.of(rec.getLeftExpression());
      }
    }
    return result;
  }
  
  private static Optional<Expression> reduce_7_15(final Expression e) {
    Optional result = Optional.empty();
    if(e instanceof App) {
      final App app = (App)e;
      final Expression e1 = app.getRightExpression();
      if(isVal(e1)) {
        final Expression el = app.getLeftExpression();
        if(el instanceof Lam) {
          final Lam lam = (Lam)el;
          final Expression e2 = e1.klone(lam.getVar().getName(), e1);
        }
      }
    }
    return result;
  }

}




