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
    Stack<NFA> nfaStack;

    int globalCount = 0;


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
                nfaStack.push(CreateNFA(expression.get(i)));
            } else {
                HandleOperation(i);
            }
        }
    }

    void HandleOperation(int index) {
        NFA firstParam = new NFA();
        NFA secondParam = new NFA();
        try {
            secondParam = nfaStack.pop();
            firstParam = nfaStack.pop();
        } catch (Exception e)
        {
            System.out.println("Stack is empty");
        }

        switch (expression.get(index)){
            case '|':
                Union(firstParam, secondParam);
                break;
            case '.':
                Concatenate(firstParam, secondParam);
                break;
            case '*':
                Asterisk(secondParam);
                nfaStack.push(firstParam);
                break;
            default:
                break;
        }
    }

    void Union(NFA firstParam, NFA secondParam)
    {
        State startState = new State(globalCount++);
        State acceptState = new State(globalCount++);
        NFA unionNFA  = new NFA();
        unionNFA.startState = startState;
        unionNFA.acceptState = acceptState;

        unionNFA.states.add(startState);
        for (int i = 0; i < firstParam.states.size(); i++) {
            unionNFA.states.add(firstParam.states.get(i));
        }
        for (int i = 0; i < secondParam.states.size(); i++) {
            unionNFA.states.add(secondParam.states.get(i));
        }
        unionNFA.states.add(acceptState);

        String startTransition1 = startState.count + ", e, " + firstParam.startState.count;
        String startTransition2 = startState.count + ", e, " + secondParam.startState.count;

        String acceptTransition1 = firstParam.acceptState.count + ", e, " + acceptState.count;
        String acceptTransition2 = secondParam.acceptState.count + ", e, " + acceptState.count;

        unionNFA.transitions.add(startTransition1);
        unionNFA.transitions.add(startTransition2);
        for (int i = 0; i < firstParam.transitions.size(); i++) {
            unionNFA.transitions.add(firstParam.transitions.get(i));
        }
        for (int i = 0; i < secondParam.transitions.size(); i++) {
            unionNFA.transitions.add(secondParam.transitions.get(i));
        }
        unionNFA.transitions.add(acceptTransition1);
        unionNFA.transitions.add(acceptTransition2);

        nfaStack.push(unionNFA);

    }

    void Concatenate(NFA firstParam, NFA secondParam)
    {
        NFA concatNFA = firstParam;
        for (int i = 1; i < secondParam.states.size(); i++) {
            //skip the startState for concatenation
            concatNFA.states.add(secondParam.states.get(i));
        }

        //skip the first state in the second parameter
        String transition = firstParam.acceptState.count + ", e, " + secondParam.states.get(1).count;
        concatNFA.transitions.add(transition);
        for (int i = 1; i < secondParam.transitions.size(); i++) {
            concatNFA.transitions.add(secondParam.transitions.get(i));
        }
        concatNFA.acceptState = secondParam.acceptState;

        nfaStack.push(concatNFA);
    }

    void Asterisk(NFA param) {
        State newStartState = new State(globalCount++);
        State newAcceptState = new State(globalCount++);
        String startTransition = newStartState.count + ", e, " + param.startState.count;
        String acceptTransition = param.acceptState + ", e, " + newAcceptState;

        NFA asteriskNFA = new NFA();

        asteriskNFA.states.add(newStartState);
        for (int i = 0; i < param.states.size(); i++) {
            asteriskNFA.states.add(param.states.get(i));
        }
        asteriskNFA.states.add(newAcceptState);

        asteriskNFA.startState = newStartState;
        asteriskNFA.acceptState = newAcceptState;

        asteriskNFA.transitions.add(startTransition);
        for (int i = 0; i < param.transitions.size(); i++) {
            asteriskNFA.transitions.add(param.transitions.get(i));
        }
        asteriskNFA.transitions.add(acceptTransition);

        nfaStack.push(asteriskNFA);
    }
    NFA CreateNFA(Character literal){
        State beginState = new State(globalCount++);
        State acceptState = new State(globalCount++);
        NFA temp = new NFA();
        temp.states.add(beginState);
        temp.startState = beginState;
        temp.states.add(acceptState);
        temp.acceptState = acceptState;
        String transition = beginState.count + ", " + literal + ", " + acceptState.count + "; ";
        temp.transitions.add(transition);

        return temp;
    }
}

class State {
    int count;

    public State(int count) {
        this.count = count;
    }
}

class NFA {
    ArrayList<State> states;
    ArrayList<String> transitions;
    State startState;
    State acceptState;

    public NFA(){

    }

}

enum Operations {
    union,
    concat,
    asterisk,

}
