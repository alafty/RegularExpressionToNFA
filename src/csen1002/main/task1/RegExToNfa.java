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
    Stack<NFA> nfaStack = new Stack<>();

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
            if(input.charAt(i) != ';'){
                temp.add(input.charAt(i));
            }
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
        NFA finalNFA = new NFA();
        try{
            finalNFA = nfaStack.pop();
        } catch (Exception e){
            System.out.println("Stack is empty at the end; incorrect input or implementation might be the cause.");
        }
        String returnString = "";
        for (int i = 0; i < finalNFA.states.size(); i++) {
            returnString += finalNFA.states.get(i).count + ";";
        }
        returnString += "#";
        
        for (int i = 0; i < alphabet.size(); i++) {
            returnString += alphabet.get(i) + ";";
        }
        returnString += "#";

        for (int i = 0; i < finalNFA.transitions.size(); i++) {
            returnString += finalNFA.transitions.get(i);
        }
        returnString += "#";

        returnString += finalNFA.startState.count;
        returnString += "#";

        returnString += finalNFA.acceptState.count;
        returnString += "#";

        return returnString;
    }

    void HandleStack(){
        for (int i = 0; i < expression.size(); i++) {
            if(alphabet.contains(expression.get(i)) || expression.get(i) == 'e'){
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
            if(expression.get(index) != '*'){
                firstParam = nfaStack.pop();
            }
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


        unionNFA.states.addAll(firstParam.states);
        unionNFA.states.addAll(secondParam.states);

        unionNFA.states.add(startState);
        unionNFA.states.add(acceptState);


        String startTransition1 = startState.count + ",e," + firstParam.startState.count + ";";
        String startTransition2 = startState.count + ",e," + secondParam.startState.count + ";";

        String acceptTransition1 = firstParam.acceptState.count + ",e," + acceptState.count + ";";
        String acceptTransition2 = secondParam.acceptState.count + ",e," + acceptState.count + ";";

        unionNFA.transitions.addAll(firstParam.transitions);
        unionNFA.transitions.addAll(secondParam.transitions);

        unionNFA.transitions.add(startTransition1);
        unionNFA.transitions.add(startTransition2);
        unionNFA.transitions.add(acceptTransition1);
        unionNFA.transitions.add(acceptTransition2);

        nfaStack.push(unionNFA);

    }

    void Concatenate(NFA firstParam, NFA secondParam)
    {

        firstParam.states.addAll(secondParam.states);
        firstParam.states.remove(secondParam.startState);

        String transition = firstParam.acceptState.count + ",e," + secondParam.states.get(1).count + ";";

        firstParam.transitions.addAll(secondParam.transitions);
        firstParam.transitions.add(transition);

        firstParam.acceptState = secondParam.acceptState;

        nfaStack.push(firstParam);
    }

    void Asterisk(NFA param) {
        State newStartState = new State(globalCount++);
        State newAcceptState = new State(globalCount++);
        String startTransition = newStartState.count + ",e," + param.startState.count + ";";
        String acceptTransition = param.acceptState.count + ",e," + newAcceptState.count + ";";
        String startToEndTransition = newStartState.count + ",e," + newAcceptState.count + ";";
        String middleTransition = param.acceptState.count + ",e," + param.startState.count + ";";

        NFA asteriskNFA = new NFA();


        asteriskNFA.states.addAll(param.states);
        asteriskNFA.states.add(newStartState);
        asteriskNFA.states.add(newAcceptState);

        asteriskNFA.startState = newStartState;
        asteriskNFA.acceptState = newAcceptState;

        asteriskNFA.transitions.addAll(param.transitions);
        asteriskNFA.transitions.add(middleTransition);
        asteriskNFA.transitions.add(startTransition);
        asteriskNFA.transitions.add(startToEndTransition);
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
        String transition = beginState.count + "," + literal + "," + acceptState.count + ";";
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
    ArrayList<State> states = new ArrayList<>();
    ArrayList<String> transitions = new ArrayList<>();
    State startState;
    State acceptState;

    public NFA(){

    }

}

