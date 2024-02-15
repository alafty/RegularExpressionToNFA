package csen1002.main.task1;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Write your info here
 * 
 * @name Mohamed Moustafa Rabie
 * @id 49-3603
 * @labNumber 021
 */

public class RegExToNfa {

	Stack<String> postFixStack;
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
		this.alphabet = ConvertStringToArray(alpha);
		String regExpression = input.split("#")[1];
		this.expression = ConvertStringToArray(regExpression);
	}

	/**
	 * Converts a string to an ArrayList of chars for easier retrieval.
	 *
	 *
	 * @param input The alphabet of the problem as a concatenated string
	 *
	 */
	public ArrayList<Character> ConvertStringToArray(String input){
		ArrayList<Character> temp = new ArrayList<>(input.length());
		for (int i = 0; i < input.length(); i++) {
			temp.add((input.charAt(i)));
		}
		return temp;
	}
	public void InsertIntoStack(ArrayList<Character> input){
		Stack<Character> stack = new Stack<>();
		for (int i = 0; i < input.size(); i++) {
			if(alphabet.contains(input.get(i)) || input.get(i).equals('e')){
				stack.push(input.get(i));
			} else {
				return;
			}
		}
	}

	/**
	 * @return Returns a formatted string representation of the NFA. The string
	 *         representation follows the one in the task description
	 */
	@Override
	public String toString() {
		InsertIntoStack(expression);
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
	String[] tranistions;
	State firstState;
	State acceptState;

	public NFA(){

	}
}

enum Operation {
	union,
	concat,
	asterisk,


}
