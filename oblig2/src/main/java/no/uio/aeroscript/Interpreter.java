package no.uio.aeroscript;

import no.uio.aeroscript.antlr.AeroScriptBaseVisitor;
import no.uio.aeroscript.antlr.AeroScriptParser;

import java.util.HashMap;
import java.util.Map;

public class Interpreter extends AeroScriptBaseVisitor<Void> {

    protected final Map<String, Double> env = new HashMap<>();

    public void run(AeroScriptParser.ProgramContext program) {
        visit(program);
    }

    @Override
    public Void visitProgram(AeroScriptParser.ProgramContext ctx) {
        for (var st : ctx.statement()) visit(st);
        return null;
    }

    @Override
    public Void visitStatement(AeroScriptParser.StatementContext ctx) {
        if (ctx.moveStmt()   != null) return visit(ctx.moveStmt());
        if (ctx.waitStmt()   != null) return visit(ctx.waitStmt());
        if (ctx.letStmt()    != null) return visit(ctx.letStmt());
        if (ctx.actionStmt() != null) return visit(ctx.actionStmt());
        throw new IllegalStateException("Unknown statement");
    }

    @Override
    public Void visitMoveStmt(AeroScriptParser.MoveStmtContext ctx) {
        double x = evaluateExpression(ctx.point().expression(0));
        double y = evaluateExpression(ctx.point().expression(1));
        Double speed = ctx.optSpeed() != null ? evaluateExpression(ctx.optSpeed().expression()) : null;
        Double time  = ctx.optTime()  != null ? evaluateExpression(ctx.optTime().expression())  : null;
        System.out.printf("MOVE TO POINT (%.2f, %.2f)%s%s%n",
                x, y,
                speed != null ? " SPEED " + speed : "",
                time  != null ? " TIME "  + time  : "");
        return null;
    }

    @Override
    public Void visitWaitStmt(AeroScriptParser.WaitStmtContext ctx) {
        double duration = evaluateExpression(ctx.expression());
        Double time = ctx.optTime() != null ? evaluateExpression(ctx.optTime().expression()) : null;
        System.out.printf("WAIT %.2f%s%n", duration, time != null ? " TIME " + time : "");
        return null;
    }

    @Override
    public Void visitLetStmt(AeroScriptParser.LetStmtContext ctx) {
        String name = ctx.ID().getText();
        double value = evaluateExpression(ctx.expression());
        env.put(name, value);
        System.out.printf("LET %s = %.4f%n", name, value);
        return null;
    }

    @Override
    public Void visitActionStmt(AeroScriptParser.ActionStmtContext ctx) {
        if (ctx.TAKEOFF() != null) { System.out.println("TAKEOFF"); return null; }
        if (ctx.LAND()    != null) { System.out.println("LAND");    return null; }
        throw new IllegalStateException("Unknown actionStmt");
    }


    public double evaluateExpression(AeroScriptParser.ExpressionContext ctx) {
        return evalAdd(ctx.addExpr());
    }

    protected double evalAdd(AeroScriptParser.AddExprContext ctx) {
        double left = evalMul(ctx.mulExpr(0));
        for (int i = 1; i < ctx.mulExpr().size(); i++) {
            String op = ctx.getChild(2 * i - 1).getText(); // '+' | '-'
            double right = evalMul(ctx.mulExpr(i));
            left = op.equals("+") ? (left + right) : (left - right);
        }
        return left;
    }

    protected double evalMul(AeroScriptParser.MulExprContext ctx) {
        double left = evalUnary(ctx.unaryExpr(0));
        for (int i = 1; i < ctx.unaryExpr().size(); i++) {
            String op = ctx.getChild(2 * i - 1).getText(); // '*' | '/'
            double right = evalUnary(ctx.unaryExpr(i));
            left = op.equals("*") ? (left * right) : (left / right);
        }
        return left;
    }

protected double evalUnary(no.uio.aeroscript.antlr.AeroScriptParser.UnaryExprContext ctx) {
    // 1) Labeled alternativer 
    if (ctx instanceof no.uio.aeroscript.antlr.AeroScriptParser.UnaryNegContext neg) {
        return -evalUnary(neg.unaryExpr());
    }
    if (ctx instanceof no.uio.aeroscript.antlr.AeroScriptParser.UnaryPrimContext prim) {
        return evalPrimary(prim.primary());
    }

    // 2) Fallback: les barna direkte
    int n = ctx.getChildCount();
    if (n == 2) {
        // forventet: ("--" eller "-") 
        String opText = ctx.getChild(0).getText();
        var child = ctx.getChild(1);

        double v;
        if (child instanceof no.uio.aeroscript.antlr.AeroScriptParser.UnaryExprContext u) {
            v = evalUnary(u);
        } else if (child instanceof no.uio.aeroscript.antlr.AeroScriptParser.PrimaryContext p) {
            v = evalPrimary(p);
        } else {
            throw new IllegalStateException("Unexpected unary child node type: " + child.getClass());
        }

        if ("--".equals(opText) || "-".equals(opText)) return -v;
        throw new IllegalStateException("Unknown unary operator: " + opText);
    }

    if (n == 1) {
        var child = ctx.getChild(0);
        if (child instanceof no.uio.aeroscript.antlr.AeroScriptParser.PrimaryContext p) {
            return evalPrimary(p);
        }
        if (child instanceof no.uio.aeroscript.antlr.AeroScriptParser.UnaryExprContext u) {
            return evalUnary(u); 
        }
        throw new IllegalStateException("Unexpected unary single child: " + child.getClass());
    }

    throw new IllegalStateException("Unknown unaryExpr structure");
}


    protected double evalPrimary(AeroScriptParser.PrimaryContext ctx) {
        if (ctx.NUMBER() != null) {
            return Double.parseDouble(ctx.NUMBER().getText());
        }
        if (ctx.ID() != null) {
            String name = ctx.ID().getText();
            Double v = env.get(name);
            if (v == null) throw new IllegalArgumentException("Undefined variable: " + name);
            return v;
        }
        if (ctx.expression() != null) {
            return evaluateExpression(ctx.expression());
        }
        if (ctx.RANDOM() != null) {
            if (ctx.range() == null) return Math.random();
            double lo = evaluateExpression(ctx.range().expression(0));
            double hi = evaluateExpression(ctx.range().expression(1));
            return lo + Math.random() * (hi - lo);
        }
        if (ctx.POINTKW() != null) {
            throw new IllegalArgumentException("Point used where a number was expected.");
        }
        throw new IllegalStateException("Unknown primary alternative");
    }
}
