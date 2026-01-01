package no.uio.aeroscript.runtime;

import no.uio.aeroscript.antlr.AeroScriptBaseVisitor;
import no.uio.aeroscript.antlr.AeroScriptLexer;
import no.uio.aeroscript.antlr.AeroScriptParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Interpreter extends AeroScriptBaseVisitor<Interpreter.Evaluated> {

    private final Object heap;
    private final Object stack;
    private final no.uio.aeroscript.Interpreter impl = new no.uio.aeroscript.Interpreter();

    public Interpreter() { this(null, null); }
    public Interpreter(Object heap, Object stack) {
        this.heap = heap;
        this.stack = stack;
    }

    public static class Evaluated {
        private final Double value;
        public Evaluated(double v) { this.value = v; }
        public Double evaluate()   { return value; }
        @Override public String toString() { return String.valueOf(value); }
    }

    @Override
    public Evaluated visitExpression(AeroScriptParser.ExpressionContext ctx) {
        String txt = ctx.getText(); 
        if (txt.startsWith("--")) {
            String rest = txt.substring(2); // f.eks. "1" eller "(2+3)"
            var lexer  = new AeroScriptLexer(CharStreams.fromString(rest));
            var tokens = new CommonTokenStream(lexer);
            var parser = new AeroScriptParser(tokens);
            var restCtx = parser.expression();
            double v = impl.evaluateExpression(restCtx);
            return new Evaluated(-v);
        }

        // Vanlig vei for alt annet (2+3, 2-3, 2*3, ...)
        double v = impl.evaluateExpression(ctx);
        return new Evaluated(v);
    }
}
