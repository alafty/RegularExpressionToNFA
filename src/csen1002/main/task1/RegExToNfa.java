package csen1002.main.task1;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Write your info here
 *
 * @name Mohamed Moustafa
 * @id 49-3603
 * @labNumber 021
 */

public class RegExToNfa {

    ArrayList<Character> alphabet;
    ArrayList<Character> expression;
    Stack<Character> expressionStack;


    /**
     * Constructs an NFA corresponding to a regular expression based on Thompson's
     * construction
     *
     * @param input The alphabet and the regular expression in postfix notation for
     *              which the NFA is to be constructed
     */
    public RegExToNfa(String input) {
        String alpha = input.split("#")[0];
        this.alphabet = StringToArray(alpha);
        String regExpression = input.split("#")[1];
        this.expression = StringToArray(regExpression);
    }

    /**
     * Converts a string into an array list of chars to ease the retrieval and
     * comparison process
     *
     * @param input The alphabet for which the NFA is to be constructed
     *              or the regular expression to be evaluated
     *
     */
    ArrayList<Character> StringToArray(String input){
        ArrayList<Character> temp = new ArrayList<>(input.length());
        for (int i = 0; i < input.length(); i++) {
            temp.add(input.charAt(i));
        }
        return temp;
    }

    /**
     * @return Returns a formatted string representation of the NFA. The string
     * representation follows the one in the task description
     */
    @Override
    public String toString() {
        HandleStack();
        return null;
    }

    void HandleStack(){
        for (int i = 0; i < expression.size(); i++) {
            if(alphabet.contains(expression.get(i)) || expression.get(i).equals('e')){
                expressionStack.push(expression.get(i));
            } else {
                HandleOperation(i);
            }
        }
    }

    void HandleOperation(int index) {
        switch (expression.get(index)){
            case '|': break;
            case '.': break;
            case '*': break;
            default: break;
        }
    }
}

class State {
    int count;

    public State(int count) {
        this.count = count;
    }
}

class NFA {
    State[] states;
    String[] transitions;
    State firstState;
    State acceptState;

    public NFA(){

    }

}

enum Operations {
    union,
    concat,
    astrisk,

}
