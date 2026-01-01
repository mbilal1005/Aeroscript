// MoveByStatement

package no.uio.aeroscript.ast.stmt;

import no.uio.aeroscript.ast.expr.Expression;


public final class MoveByStatement extends Statement {
  public final Expression meters;       // required
  public final Expression speed, time;  // nullable
  public MoveByStatement(Expression meters, Expression speed, Expression time) { 
    this.meters = meters; 
    this.speed = speed; 
    this.time = time; 
   }
}
