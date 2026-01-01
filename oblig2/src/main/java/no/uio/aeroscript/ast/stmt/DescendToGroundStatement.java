// DescendToGroundStatement.java
package no.uio.aeroscript.ast.stmt;

import no.uio.aeroscript.ast.expr.Expression;

public final class DescendToGroundStatement extends Statement {
  public final Expression speed, time;  // both nullable
  public DescendToGroundStatement(Expression speed, Expression time) { 
    this.speed = speed;
    this.time = time; 
   }
}
