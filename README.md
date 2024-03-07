# RegExToNfa

## Description

The `RegExToNfa` Java class provides functionality to convert a regular expression into a corresponding Non-Deterministic Finite Automata (NFA) using Thompson's construction algorithm. This conversion is a crucial step in various applications, such as pattern matching and lexical analysis.

## Usage

To use the `RegExToNfa` class, follow these steps:

1. Include the `RegExToNfa.java` file in your Java project.
2. Create an instance of the `RegExToNfa` class, providing the regular expression in postfix notation and the alphabet as input.
3. Call the `toString()` method of the `RegExToNfa` instance to obtain a formatted string representation of the resulting NFA.

Example:

```java
String regex = "abcu#abc|.∪.*";
RegExToNfa converter = new RegExToNfa(regex);
String nfaString = converter.toString();
System.out.println(nfaString);
```

## Input Format

The input to the `RegExToNfa` class should be a regular expression in postfix notation, where:

- Characters represent literals in the regular expression.
- `.` represents concatenation.
- `∪` represents union.
- `*` represents Kleene star (zero or more occurrences).
- `e` represents an empty string (epsilon transition).

## Output Format

The output of the `toString()` method is a formatted string representation of the resulting NFA. The string representation follows a specific format as described in the task description.

## Classes

- `RegExToNfa`: Main class responsible for converting a regular expression to an NFA.
- `State`: Represents a state in the NFA.
- `NFA`: Represents the NFA, containing states, transitions, start state, and accept state.
- `Transition`: Represents a transition between states in the NFA.

## Authors

- [Mohamed El-Alafty](https://github.com/alafty)
