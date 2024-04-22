grammar AgentInstruction;

// Symbols
ASS_OP: ':';
PAREN_OPEN: '(';
PAREN_CLOSE: ')';
LT: 'less than';
GT: 'greater than';
EQ: '=';
NOT: 'not';
COMMA: ',';
AND: 'and';
OR: 'or';
SCALAR: [0-9]+;

foundBehaviours:
  sentence EOF;

sentence:
  (expression) ( (AND | COMMA) (expression))*;

expression:
    ifElseExpression | logicalExpression;

ifElseExpression:
  'if' condition 'then' sentence ('else' sentence)?;

logicalExpression:
  logicalOperator? behaviour;

logicalOperator:
  NOT #NotOperator
  ;

comparisonOperator:
    LT      #LessThan
    | GT    #GreaterThan
    ;

condition:
  agentFeature comparisonOperator scalarLiteral;

scalarLiteral:
    SCALAR;

agentFeature:
    'health'      # Health
    | 'money'     # Money
    ;

behaviour:
  'aggressive'    # Aggressive
  | 'defensive'   # Defensive
  | 'passive'     # Passive
  ;

WHITESPACE: [ \t\r\n]+ -> skip;