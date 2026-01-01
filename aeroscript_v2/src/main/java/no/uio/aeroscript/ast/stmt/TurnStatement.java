// TurnStatement

package no.uio.aeroscript.ast.stmt;

import no.uio.aeroscript.ast.expr.Expression;



public final class TurnStatement extends Statement {
  public enum Dir { RIGHT, LEFT, NONE }
  public final Dir dir;
  public final Expression degrees;      // required
  public final Expression speed, time;  // nullable

  public TurnStatement(Dir dir, Expression degrees, Expression speed, Expression time) { 
    this.dir = dir;
    this.degrees = degrees;
    this.speed = speed;
    this.time = time;
    }
}
