package csen1002.main.task1;

import java.util.ArrayList;

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
        // TODO Auto-generated method stub
        return null;
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
