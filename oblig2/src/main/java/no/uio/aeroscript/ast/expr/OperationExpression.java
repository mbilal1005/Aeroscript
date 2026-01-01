package no.uio.aeroscript.ast.expr;

import no.uio.aeroscript.type.Point;
import no.uio.aeroscript.type.Range;

public class OperationExpression extends Expression {
    private final String operation;
    private final Expression left;
    private final Expression right;

    public OperationExpression(String operation, Expression left, Expression right) {
        this.operation = operation;
        this.left = left;
        this.right = right;
    }

    @Override
    public void print(){
        System.out.print("OperationNode(" + operation + ", ");
        if (left != null)  left.print();  else System.out.print("null");
        System.out.print(", ");
        if (right != null) right.print(); else System.out.print("null");
        System.out.print(")\n");
    }

    private static float asFloat(Object o) {
        if (o instanceof Number n) return n.floatValue();
        throw new IllegalArgumentException("Expected number, got: " + o);
    }

    @Override
    public Object evaluate() {
        final String op = operation == null ? "" : operation.toUpperCase();

        // Unær negasjon
        if ("NEG".equals(op)) {
            if (left == null) throw new IllegalArgumentException("NEG needs one operand");
            return -asFloat(left.evaluate());
        }

        // RANDOM i [lo, hi]
        if ("RANDOM".equals(op)) {
            if (left == null || right == null) {
                throw new IllegalArgumentException("RANDOM needs two operands");
            }
            float lo = asFloat(left.evaluate());
            float hi = asFloat(right.evaluate());
            return new Range(lo, hi).getRandomNumber();
        }

        // POINT(x, y) -> Point
        if ("POINT".equals(op)) {
            if (left == null || right == null) {
                throw new IllegalArgumentException("POINT needs two operands");
            }
            float x = asFloat(left.evaluate());
            float y = asFloat(right.evaluate());
            return new Point(x, y);
        }

        // Binære aritmetiske
        if (left == null || right == null)
            throw new IllegalArgumentException(op + " needs two operands");

        float l = asFloat(left.evaluate());
        float r = asFloat(right.evaluate());

        return switch (op) {
            // Navn testene typisk bruker
            case "PLUS", "+", "SUM", "ADD"      -> l + r;
            case "SUB",  "-", "MINUS"           -> l - r;
            case "MUL",  "*", "TIMES", "MULTIPLY" -> l * r;

            case "DIV", "/" -> l / r;
            case "MOD", "%" -> l % r;

            default -> throw new IllegalArgumentException("Invalid operation: " + operation);
        };
    }
}
