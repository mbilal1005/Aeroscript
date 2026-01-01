grammar AeroScript;

@header { package no.uio.aeroscript.antlr; }

program
  : (statement)* EOF
  ;

statement
  : moveStmt
  | waitStmt
  | letStmt
  | actionStmt
  ;

moveStmt
  : MOVE TO point optSpeed? optTime?
  ;

waitStmt
  : WAIT expression optTime?
  ;

letStmt
  : LET ID ASSIGN expression
  ;

actionStmt
  : TAKEOFF
  | LAND
  ;

optSpeed
  : SPEED expression
  ;

optTime
  : TIME expression
  ;

expression
  : addExpr
  ;

addExpr
  : mulExpr ((PLUS | MINUS) mulExpr)*
  ;

mulExpr
  : unaryExpr ((TIMES | DIV) unaryExpr)*
  ;

unaryExpr
  : MINUS unaryExpr          #unaryNeg
  | primary                  #unaryPrim
  ;

primary
  : NUMBER
  | ID
  | LPAREN expression RPAREN
  | RANDOM range?            // RANDOM eller RANDOM [e1, e2]
  | POINTKW point            // POINT (e1, e2)
  ;

point
  : LPAREN expression COMMA expression RPAREN
  ;

range
  : LSQUARE expression COMMA expression RSQUARE
  ;


MOVE     : 'MOVE';
TO       : 'TO';
WAIT     : 'WAIT';
LET      : 'LET';
ASSIGN   : '=';
TAKEOFF  : 'TAKEOFF';
LAND     : 'LAND';
SPEED    : 'SPEED';
TIME     : 'TIME';
RANDOM   : 'RANDOM';
POINTKW  : 'POINT';

PLUS     : '+';
MINUS    : '-';
TIMES    : '*';
DIV      : '/';
LPAREN   : '(';
RPAREN   : ')';
LSQUARE  : '[';
RSQUARE  : ']';
COMMA    : ',';

ID       : [a-zA-Z_] [a-zA-Z_0-9]* ;
NUMBER   : [0-9]+ ('.' [0-9]+)? ;

WS           : [ \t\r\n\u000C]+   -> channel(HIDDEN);
COMMENT      : '/*' .*? '*/'      -> channel(HIDDEN);
LINE_COMMENT : '//' ~[\r\n]*      -> channel(HIDDEN);
