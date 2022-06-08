grammar compiladores;

@header {
package compiladores;
}

fragment DIGIT:[0-9];
fragment LETTER: [A-Za-z];
TYPE_INT: 'int';
TYPE_DOUBLE: 'double';
TYPE_CHAR: 'char';
TYPE_FLOAT: 'float';
TYPE_VOID: 'void';
CTRL_WHILE: 'while';
CTRL_FOR: 'for';
CTRL_IF: 'if';
NUMBER: DIGIT+;
DECIMAL_NUMBER: NUMBER '.' NUMBER;
CHARACTER: '("|\')' LETTER '("|\')';
ID: (LETTER | '_') (LETTER | DIGIT | '_')*;
COMMA: ',';
SEMICOLON: ';';
EQ: '=';
EEQ: '==';
NEQ: '!=';
SUM: '+';
SUB: '-';
MUL: '*';
DIV: '/';
MOD: '%';
GT: '>';
GTE: '>=';
LT: '<';
LTE: '<=';
AND: '&&';
OR: '||';
LA: '{';
LC: '}';
PA: '(';
PC: ')';
WS: [ \t\n\r] -> skip;

prog: procedures EOF;

procedures:
	functionDeclaration procedures
	| functionForwardDeclaration SEMICOLON procedures
	| declaration SEMICOLON
	|;

functionDeclaration:
	functionId PA parametersDeclaration PC block;

functionForwardDeclaration:
	functionId PA (parametersDeclaration | typesDeclaration) PC;

functionId: (vartype | TYPE_VOID) ID;

block: LA instructions LC;

instructions: instruction instructions | block |;

instruction:
	declaration SEMICOLON
	| assignation SEMICOLON
	| controlStructure
	| functionCall SEMICOLON;

declaration: vartype simpleDeclaration;

simpleDeclaration: (declaredVariable | assignation) extendedDeclaration;

extendedDeclaration:
	COMMA declaredVariable extendedDeclaration
	| COMMA assignation extendedDeclaration
	|;

declaredVariable: ID;

parametersDeclaration:
	vartype ID COMMA parametersDeclaration
	| vartype ID
	|;

typesDeclaration:
	vartype ID? COMMA typesDeclaration
	| vartype ID?
	|;
vartype: TYPE_DOUBLE | TYPE_INT | TYPE_CHAR | TYPE_FLOAT;

assignation: ID EQ (value | alop);

value: number | ID | functionCall | CHARACTER;

number: NUMBER | DECIMAL_NUMBER;

controlStructure: iwhile | ifor | iif;

functionCall: ID PA parameters PC;

parameters: value | value COMMA parameters |;

ifor:
	CTRL_FOR PA assignation SEMICOLON alop SEMICOLON assignation PC (
		block
		| instruction
	);

iwhile: CTRL_WHILE PA alop PC (block | instruction);

iif: CTRL_IF PA alop PC (block | instruction);

alop: logOr lo;

lo: OR logOr lo |;

logOr: logAnd la;

la: AND logAnd la |;

logAnd: eq e;

e: EEQ eq | NEQ eq |;

eq: rel r;

r: GT rel | GTE rel | LT rel | LTE rel |;

rel: term exp;

exp: SUM term exp | SUB term exp |;

term: factor t;

t: MUL factor t | DIV factor t | MOD factor t |;

factor: value | PA alop PC;
