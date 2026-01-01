package no.uio.aeroscript.ast.stmt;

import java.util.ArrayList;
import java.util.List;

public class Execution {
    private final String name;
    private final boolean start;           // om modusen har leading '->'
    private final List<Statement> body;    // statements i modusen
    private final String nextMode;         // navn etter '} -> Next' (kan være null)

    public Execution(String name, boolean start, List<Statement> body, String nextMode) {
        this.name = name;
        this.start = start;
        this.body = body;
        this.nextMode = nextMode;
    }
    public String name() { return name; }
    public boolean isStart() { return start; }

    // forbedring potensiale; send en kopi 
    public List<Statement> body() { return body; }
    public String nextMode() { return nextMode; }
}
