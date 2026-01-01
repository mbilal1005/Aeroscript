package no.uio.aeroscript.runtime;

import no.uio.aeroscript.antlr.AeroScriptLexer;
import no.uio.aeroscript.antlr.AeroScriptParser;
import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExpressionSemanticsTest {

    private AeroScriptParser.ExpressionContext parseExpr(String e) {
        var input  = CharStreams.fromString(e);
        var lexer  = new AeroScriptLexer(input);
        var tokens = new CommonTokenStream(lexer);
        var parser = new AeroScriptParser(tokens);
        return parser.expression();
    }

    @Test
    void precedenceAndUnary() {
        Interpreter I = new Interpreter(null, null);
        // 2 + 3 * 4 = 14
        assertEquals(14.0f,
            Float.parseFloat(I.visitExpression(parseExpr("2 + 3 * 4")).evaluate().toString()));
        // --(2 + 3) = -5
        assertEquals(-5.0f,
            Float.parseFloat(I.visitExpression(parseExpr("-- (2 + 3)")).evaluate().toString()));
    }

    @Test
    void pointInNumericContextThrows() {
        Interpreter I = new Interpreter(null, null);
        assertThrows(IllegalArgumentException.class, () ->
            I.visitExpression(parseExpr("point (2,3) + 1")).evaluate());
    }
}
