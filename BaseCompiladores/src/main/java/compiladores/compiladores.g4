grammar compiladores;

@header {
package compiladores;
}

fragment DIGIT:[0-9];
fragment LETTER: [A-Za-z];
TYPE_INT: 'int';
TYPE_DOUBLE: 'double';
CTRL_WHILE: 'while';
CTRL_FOR: 'for';
NUMBER: DIGIT+;
DECIMAL_NUMBER: NUMBER '.' NUMBER;
COMP: 'comp'; // Solo para cumplir con el ejercicio
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

prog: instructions EOF;

instructions: instruction SEMICOLON? instructions | block |;

instruction: declaration | assignation | controlStructure;

declaration: iinteger | idouble;

iinteger: TYPE_INT integerDeclaration;

idouble: TYPE_DOUBLE doubleDeclaration;

integerDeclaration:
	ID extendedIntegerDeclaration
	| assignation extendedIntegerDeclaration;

extendedIntegerDeclaration:
	COMMA ID extendedIntegerDeclaration
	| COMMA assignation extendedIntegerDeclaration
	|;

doubleDeclaration:
	ID doubleDeclaration
	| assignation doubleDeclaration
	|;

extendedDoubleDeclaration:
	COMMA ID extendedDoubleDeclaration
	| COMMA assignation extendedDoubleDeclaration
	|;

assignation: ID EQ (value | alop);

value: number | ID;

number: NUMBER | DECIMAL_NUMBER;

controlStructure: iwhile | ifor;

ifor:
	CTRL_FOR PA assignation SEMICOLON alop SEMICOLON assignation PC (
		block
		| instruction
	);

iwhile: CTRL_WHILE PA alop PC (block | instruction);

block: LA instructions LC;

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
