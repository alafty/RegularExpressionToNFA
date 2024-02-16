package csen1002.main.task1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
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
        for (State s : finalNFA.states) {
            returnString += s.count + ";";
        }
        returnString = returnString.substring(0, returnString.length()-1);
        returnString += "#";
        
        for (Character c : alphabet) {
            returnString += c + ";";
        }
        returnString = returnString.substring(0, returnString.length()-1);
        returnString += "#";

        while (!finalNFA.transitions.isEmpty()){
            Transition t = finalNFA.transitions.remove();
            String temp = t.from + "," + t.literal + "," + t.to + ";";
            returnString += temp;
        }
        returnString = returnString.substring(0, returnString.length()-1);
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


        Transition startTransition1 = new Transition(startState.count ,
                'e', firstParam.startState.count);
        Transition startTransition2 = new Transition(startState.count,
                'e',  secondParam.startState.count);

        Transition acceptTransition1 = new Transition(firstParam.acceptState.count,
                'e', acceptState.count);
        Transition acceptTransition2 = new Transition(secondParam.acceptState.count,
                'e', acceptState.count);

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

        //TODO: MAKE IT THAT YOU REPLACE EVERY TRANSITION THAT INCLUDED THE OLD STARTSTATE

        firstParam.transitions.addAll(secondParam.transitions);
        PriorityQueue<Transition> toAdd = new PriorityQueue<>();
        PriorityQueue<Transition> toRemove = new PriorityQueue<>();
        Iterator i = firstParam.transitions.iterator();
        while(i.hasNext()){
            Transition t = (Transition) i.next();
            if(t.from == secondParam.startState.count){
                Transition replacement = new Transition(firstParam.acceptState.count,
                        t.literal, t.to);
                toRemove.add(t);
                toAdd.add(replacement);
            }
        }
        firstParam.transitions.addAll(toAdd);
        firstParam.transitions.removeAll(toRemove);

        firstParam.acceptState = secondParam.acceptState;

        nfaStack.push(firstParam);
    }

    void Asterisk(NFA param) {
        State newStartState = new State(globalCount++);
        State newAcceptState = new State(globalCount++);
        Transition startTransition = new Transition(newStartState.count,
                'e', param.startState.count);
        Transition acceptTransition = new Transition(param.acceptState.count,
                'e', newAcceptState.count);
        Transition startToEndTransition = new Transition(newStartState.count,
                'e', newAcceptState.count);
        Transition middleTransition = new Transition(param.acceptState.count,
                'e', param.startState.count);

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
        Transition transition = new Transition(beginState.count,
                literal ,acceptState.count);
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
    PriorityQueue<Transition> transitions = new PriorityQueue<>();
    State startState;
    State acceptState;

    public NFA(){

    }

}

class Transition implements Comparable<Transition>{

    int from;
    Character literal;
    int to;
    public Transition(int from, Character literal, int to){
        this.from = from;
        this.to = to;
        this.literal = literal;
    }

    public int compareTo(Transition t) {
        if(from < t.from) return -1;
        else if (from > t.from) return 1;
        else return LiteralBreak(t);
    }

    int LiteralBreak(Transition t){
        if(literal < t.literal) return -1;
        else if (literal > t.literal) return 1;
        else return TieBreak(t);
    }

    int TieBreak(Transition t){
        if(to < t.to) return -1;
        else if (to > t.to) return 1;
        else return 0;
    }
}

