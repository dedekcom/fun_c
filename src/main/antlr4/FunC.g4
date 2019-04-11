
/*
    author: Dominik Dagiel
    start: 2019.03.31
*/

grammar FunC;

source: h_namespace include* body_statement* EOF
    ;

// header
h_namespace: KW_NAMESPACE namespace_path ';'
    ;
include: KW_INCLUDE namespace_path ';'
    ;
namespace_path: id
    | id '.' namespace_path
    ;

body_statement: c_block
    | local_namespace
    | static_val
    | declare_func
    | declare_struct
    ;

local_namespace: KW_NAMESPACE id '{' body_statement* '}'
    ;

static_val: extern? declare_val
    ;


declare_val: kwlazy? type_id id '=' expr_block
    ;

declare_func: extern? local_func
    ;

local_func: type_id id '(' fun_args? ')' fun_block
    ;

declare_struct: extern? local_struct
    ;

local_struct: KW_STRUCT id '(' fun_single_args ')' ';'
    ;

fun_single_args: fun_single_arg
    | fun_single_arg ',' fun_single_args
    ;

fun_args: fun_single_args
    | fun_varargs
    | fun_single_args ',' fun_varargs
    ;

fun_single_arg: type_id id;

fun_varargs: type_id '...' id;

fun_block: '{' fun_body* '}'
    ;

fun_body: expr_block
    | declare_val
    | local_struct
    | local_func
    ;

expression: '(' expression ')'                          #ex_br
    | call_fun                                          #ex_fun
    | value                                             #ex_val
    | prefix=('+'|'-') expression                       #ex_neg_ex
    | prefix=('~'|'!') expression                       #ex_not_ex
    | expression op=('*'|'/'|'%') expression            #ex_mul_ex
    | expression op=('+'|'-'|'::') expression           #ex_plus_ex
    | expression op=('<'|'>'|'<='|'>=') expression      #ex_gt_ex
    | expression op=('=='|'!=') expression              #ex_eq_ex
    | expression op=('&'|'|') expression                #ex_bitand_ex
    | expression op=('&&'|'||') expression              #ex_and_ex
    | complex_expr                                      #ex_complex
    ;

complex_expr: if_expr
          | match_expr
          | lambda_expr
    ;

match_expr: KW_MATCH '(' expression ')' '{' match_cases '}'
    ;
match_cases: match_case
    | match_case match_cases
    ;
match_case:  any '=>' expr_block
    | exp_list   '=>' expr_block
    ;

lambda_expr: lambda_args '=>' lambda_block
    ;

lambda_args: id
    | '(' lambda_args2? ')'
    ;

lambda_args2: lambda_arg
    | lambda_arg ',' lambda_args2
    ;
lambda_arg: any
    | id
    ;

if_expr: KW_IF '(' expression ')' expr_block else_part
    ;
else_part: KW_ELSE expr_block
    ;

expr_block: complex_expr
    | expression ';'
    | fun_block
    ;

lambda_block: expression
    | fun_block
    ;

call_fun: namespace_path call_args ('.' call_fun)?
    | namespace_path
    ;

call_args: '(' exp_list? ')'
    ;

exp_list: expression
    | expression ',' exp_list
    ;

extern: KW_EXTERN;

kwlazy: KW_LAZY;

value: v_num
    | v_char
    | v_str
    | v_null
    | v_float
    ;

c_block: C_BODY
    ;
type_id: id                 #typ_id
    | id '<' type_id '>'    #typ_templ
    ;

id: ID;
any: UNDERSCORE;

v_num: NUM;
v_float: FLOAT;
v_null: 'null';
v_char: CHAR;
v_str: StringLiteral;

C_BODY: '<?c' .*? '?>';

// keywords

KW_NAMESPACE: 'namespace';
KW_INCLUDE: 'include';
KW_IF: 'if';
KW_ELSE: 'else';
KW_MATCH: 'match';
KW_EXTERN: 'extern';
KW_LAZY: 'lazy';
KW_STRUCT: 'struct';

UNDERSCORE: '_';

ID: [a-zA-Z_]+[a-zA-Z_0-9]*
  ;

NUM: [0-9]+
  | '0' [xX] [0-9a-fA-F]+
  | '0' [bB] [0-1]+
  ;

FLOAT: [0-9]+ '.' [0-9]+
    ;

CHAR:	'\'' ANYCHAR '\''
	;

fragment ANYCHAR:	~['\\\r\n]
	;

fragment ESC_DQUOTE: '\\"'
  ;

StringLiteral:	'"' StringCharacters? '"'
  	;
fragment StringCharacters:	StringCharacter+
  	;
fragment StringCharacter:	~["\\\r\n]
  	|	EscapeSequence
  	;
fragment EscapeSequence:	'\\' [btnfr"'\\]
	;
//
// Whitespace and comments
//

WS:  [ \t\r\n\u000C]+ -> skip
    ;

COMMENT:   '/*' .*? '*/' -> skip
    ;

LINE_COMMENT:   '//' ~[\r\n]* -> skip
    ;