package csen1002.tests.task1;

import csen1002.main.task1.RegExToNfa;

public class DummyTesting {

    public static void main (String[] args){
        RegExToNfa test = new RegExToNfa("a;b#eab*|.");
        String bla = test.toString();
        System.out.println(bla);
    }
}
