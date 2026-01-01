// MoveToPointStatement

package no.uio.aeroscript.ast.stmt;

import no.uio.aeroscript.ast.expr.Expression;


public final class MoveToPointStatement extends Statement {
  public final Expression x, y;         // required
  public final Expression speed, time;  // nullable
  public MoveToPointStatement(Expression x, Expression y, Expression speed, Expression time) { 
    this.x = x;
    this.y = y;
    this.speed = speed; 
    this.time = time; 
   }
}
