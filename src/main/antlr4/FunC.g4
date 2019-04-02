
/*
    author: Dominik Dagiel
    start: 2019.03.31
*/

grammar FunC;

source: h_namespace include* body? EOF
    ;

// header
h_namespace: KW_NAMESPACE namespace_path ';'
    ;
include: KW_INCLUDE namespace_path ';'
    ;
namespace_path: id
    | id '.' namespace_path
    ;

body: body_statement
    | body_statement body
    ;

body_statement: c_block
    | local_namespace
    | static_val
    | declare_func
    ;

local_namespace: KW_NAMESPACE id '{' body? '}'
    ;

static_val: visibility_modifier? lazy? declare_val
    ;

visibility_modifier: 'private';
lazy: 'lazy';

declare_val: type_id id '=' expression ';'
    ;

declare_func: visibility_modifier? type_id id '(' fun_args? ')' fun_block
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

fun_body: expression
    | declare_val
    ;

expression: '(' expression ')'                          #ex_br
    | id                                                #ex_id
    | call_fun                                          #ex_fun
    | value                                             #ex_val
    | prefix=('+'|'-') expression                       #ex_neg_ex
    | prefix=('~'|'!') expression                       #ex_not_ex
    | expression op=('*'|'/'|'%') expression            #ex_mul_ex
    | expression op=('+'|'-') expression                #ex_plus_ex
    | expression op=('<'|'>'|'<='|'>=') expression      #ex_gt_ex
    | expression op=('=='|'!=') expression              #ex_eq_ex
    | expression op=('&'|'|') expression                #ex_bitand_ex
    | expression op=('&&'|'||') expression              #ex_and_ex
    | if_expr                                           #ex_if
    | lambda_expr                                       #ex_lambda
    | match_expr                                        #ex_match
    ;

match_expr: KW_MATCH '(' expression ')' '{' match_cases '}'
    ;
match_cases: match_case
    | match_case match_cases
    ;
match_case: exp_list '=>' expr_block
    | '?' '=>' expr_block
    ;

lambda_expr: lambda_args '=>' expr_block
    ;

lambda_args: id
    | '(' lambda_args2? ')'
    ;

lambda_args2: id
    | id ',' lambda_args2
    ;

if_expr: KW_IF '(' expression ')' expr_block else_part
    ;
else_part: KW_ELSE expr_block
    ;

expr_block: fun_block
    | expression ';'
    ;

call_fun: id call_args
    ;

call_args: '(' exp_list? ')'
    ;

exp_list: expression
    | expression ',' exp_list
    ;

value: v_num
    | v_char
    | v_str
    | v_null
    ;

c_block: C_BODY
    ;
type_id: id;

id: ID;
v_num: NUM;
v_null: 'null';
v_char: CHAR;
v_str: STR;

C_BODY: '<?c' .*? '?>';

// keywords

KW_NAMESPACE: 'namespace';
KW_INCLUDE: 'include';
KW_IF: 'if';
KW_ELSE: 'else';
KW_MATCH: 'match';

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

//
// Whitespace and comments
//

WS:  [ \t\r\n\u000C]+ -> skip
    ;

COMMENT:   '/*' .*? '*/' -> skip
    ;

LINE_COMMENT:   '//' ~[\r\n]* -> skip
    ;