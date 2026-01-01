// DescendByStatement

package no.uio.aeroscript.ast.stmt;

import no.uio.aeroscript.ast.expr.Expression;


public final class DescendByStatement extends Statement {
  public final Expression meters, speed, time;
  public DescendByStatement(Expression meters, Expression speed, Expression time) {
    this.meters = meters;
    this.speed = speed;
    this.time = time; 
    }
}
