package no.uio.aeroscript.runtime;

import no.uio.aeroscript.antlr.AeroScriptLexer;
import no.uio.aeroscript.antlr.AeroScriptParser;
import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorHandlingTest {

    private AeroScriptParser.ExpressionContext parseExpr(String e) {
        var input  = CharStreams.fromString(e);
        var lexer  = new AeroScriptLexer(input);
        var tokens = new CommonTokenStream(lexer);
        var parser = new AeroScriptParser(tokens);
        return parser.expression(); // <-- riktig type
    }

    @Test
    void randomWithInvalidRangeThrows() {
        Interpreter I = new Interpreter(null, null);
        assertThrows(IllegalArgumentException.class, () ->
            I.visitExpression(parseExpr("random [3,2]")).evaluate());
    }

    @Test
    void undefinedVariableThrows() {
        Interpreter I = new Interpreter(null, null);
        // Hvis ID ikke er lov i uttrykk hos deg, kan dette feile allerede i parsing.
        assertThrows(IllegalArgumentException.class, () ->
            I.visitExpression(parseExpr("point (2,3) + x")).evaluate());
    }
}
