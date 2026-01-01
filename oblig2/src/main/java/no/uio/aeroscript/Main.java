package no.uio.aeroscript;

import no.uio.aeroscript.antlr.AeroScriptLexer;
import no.uio.aeroscript.antlr.AeroScriptParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Main {
    public static void main(String[] args) {
        // Minimal demo-tekst – bytt til å lese fra fil 
        String src = """
            TAKEOFF
            LET a = 3*4
            MOVE TO POINT (a, 5) SPEED 2
            WAIT 10
            LAND
        """;

        var lexer  = new AeroScriptLexer(CharStreams.fromString(src));
        var tokens = new CommonTokenStream(lexer);
        var parser = new AeroScriptParser(tokens);

        var program = parser.program(); // parse
        new Interpreter().run(program); // kjør direkte på parse-treet
    }
}
