
/*
    author: Dominik Dagiel 2019.03.31
*/

grammar FunC;

source:
    namespace include* body* EOF
    ;

// header
namespace: KW_NAMESPACE namespace_path SEMI
    ;
include: KW_INCLUDE namespace_path SEMI
    ;
namespace_path: id
    | id '.' namespace_path
    ;

body:
    c_block
    ;


c_block: C_BODY
    ;

id: ID;

C_BODY: '<?c' .*? '?>';

// keywords

KW_NAMESPACE: 'namespace';
KW_INCLUDE: 'include';

ID: [a-zA-Z_]+[a-zA-Z_0-9]*
  ;

NUM: ('0'..'9')+
  | '0x' ('0'..'9' | 'a'..'f' | 'A'..'F')+
  ;

CHAR:	'\'' ANYCHAR '\''
	;

fragment ANYCHAR:	~['\\\r\n]
	;

fragment ESC_DQUOTE: '\\"'
  ;

STR
  : '"'
      ( ~('"' | '\n' | '\\')
      | ESC_DQUOTE
      | '\n'
      )*
    '"' // "
  ;

NULL:	'null'
	;

LBRACE : '{';
RBRACE : '}';
LBRACK : '[';
RBRACK : ']';
LPAREN : '(';
RPAREN : ')';
SEMI : ';';
COMMA : ',';
DOT : '.';
COLON: ':';

ASSIGN: '=';

EQ: '==';
NEQ: '!=';
GT: '>';
LT: '<';
GE: '>=';
LE: '<=';

AND : '&&';
OR : '||';

ADD : '+';
SUB : '-';
MUL : '*';
DIV : '/';
BITAND : '&';
BITOR : '|';
MOD : '%';
COLONCOLON : '::';

//
// Whitespace and comments
//

WS:  [ \t\r\n\u000C]+ -> skip
    ;

COMMENT:   '/*' .*? '*/' -> skip
    ;

LINE_COMMENT:   '//' ~[\r\n]* -> skip
    ;