package no.uio.aeroscript;

import no.uio.aeroscript.antlr.AeroScriptLexer;
import no.uio.aeroscript.antlr.AeroScriptParser;
import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProgramParseTest {

    private AeroScriptParser parserOf(String src) {
        var input  = CharStreams.fromString(src);
        var lexer  = new AeroScriptLexer(input);
        var tokens = new CommonTokenStream(lexer);
        return new AeroScriptParser(tokens);
    }

    @Test
    void parseRepresentativeExpression() {
        String src = "--(2 + 3 * (4 + 1))";
        AeroScriptParser p = parserOf(src);
        var tree = p.expression();
        assertEquals(0, p.getNumberOfSyntaxErrors(), "Parser should accept a nested arithmetic expression");
        assertNotNull(tree);
    }
}
