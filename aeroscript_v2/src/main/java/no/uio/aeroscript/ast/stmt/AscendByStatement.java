package no.uio.aeroscript.ast.stmt;

import no.uio.aeroscript.ast.expr.Expression;

// AscendByStatement 
public final class AscendByStatement extends Statement {
  public final Expression meters, speed, time; // meters required
  public AscendByStatement(Expression meters, Expression speed, Expression time) { 
    this.meters = meters;
    this.speed = speed; 
    this.time = time;  
    }
}

