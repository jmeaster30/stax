# stax
Stax - Stack Based Programming Language

This is a toy programming language so I could figure out stack-based programming and play around with how it works. The control flow in stax is driven by the "evalif" command that conditionally evaluates an expression. A usual if or if-else expression can be built up from this command. Likewise with loops, they can be built up using a combination of evalif and recursion. 

These are features I would like to add:

- [ ] Better string functionality

- [ ] Better Error Handling

- [ ] Type checking

- [ ] Compile-time Stack Underflow and Overflow Error Capturing

- [ ] Generate Java Bytecode 

Instruction | Behavior
------------|---------
def | defines a function. Takes an id, a parameter type list, a return type list, and an expression then it assigns the expression to the supplied id and enforces the typing and stack modification from the type lists. 
set | sets a variable to some value. Takes an id and some other value and assigns that value to the supplied id.
dup | duplicates the top value on the stack
dupn | duplicates the top n values on the stack
pop | removes the top value from the stack
popn | removes the top n values from the stack
swap | swaps the top two values on the stack
bubbn | takes the n-th value on the stack and bubbles it up to the top
sinkn | takes the top value on the stack and sinks it down so it becomes the n-th value on the stack
eval | takes an expression that is on the stack and evaluates the expression
evalif | takes an expression and a boolean value and evaluates the expression if the boolean is true. This also leaves a boolean value telling whether or not the expression was evaluated
first | takes a list from the stack and returns the first value in the list
rest | takes a list from the stack and returns the list without the first value in the list
not | takes a boolean value from the stack and returns the opposite boolean value
or | takes two boolean values from the stack and returns the result of or'ing those values
and | takes two boolean values from the stack and returns the result of and'ing those values
add | takes two numbers from the stack and returns the sum of the two numbers
sub | takes two numbers from the stack and returns the difference of the two numbers
mult | takes two numbers from the stack and returns the product of the two numbers
div | takes two numbers from the stack and returns the quotient of the two numbers
divmod | takes two numbers from the stack and returns the modulus and then the integer quotient of the two numbers
eq | takes two numbers from the stack and returns a boolean signifying if the numbers are equal
neq | takes two numbers from the stack and returns a boolean signifying if the numbers are not equal
lt | takes two numbers from the stack and returns a boolean signifying if the second number is less than the first
gt | takes two numbers from the stack and returns a boolean signifying if the second number is greater than the first
lte | takes two numbers from the stack and returns a boolean signifying if the second number is less than or equal to the first
gte | takes two numbers from the stack and returns a boolean signifying if the second number is greater than or equal to the first
print | takes any value from the stack and prints it to standard out
input | waits for input from the user and pushes the string value of that input to the top of the stack

Types | Info
------|----
num | Number with no limit
str | String of characters
list | List of values
bool | Boolean value (true or false)
char | Character value or string of length 1
id | Identifier value. These get evaluated immediately if the value has been defined ie the value gets pushed to the stack or if the identifier points to a function that function is called
expr | Expression value. Expressions are lazily evaluated so the contents only run when you call 'eval' or 'evalif'

Values | Info
---------------|----
true | Pushes "true" on the stack
false | Pushes "false" on the stack
"string" | Pushes the string "string" on the stack
1234.5 | Pushes the number 1234.5 onto the stack
[1, 2, 3] | Pushes 1 on the stack, pushes 2 on the stack, pushes 3 on the stack then pops those 3 items and pushes a length 3 list. This behavior means all values in the list are evaluated before they get added to the list.
