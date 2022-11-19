/*
 * Copyright (c) 2015, Ali Afroozeh and Anastasia Izmaylova, Centrum Wiskunde & Informatica (CWI)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this 
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this 
 *    list of conditions and the following disclaimer in the documentation and/or 
 *    other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY 
 * OF SUCH DAMAGE.
 *
 */

package org.iguana.regex;

import org.iguana.utils.input.Input;
import org.iguana.regex.automaton.Automaton;
import org.iguana.regex.automaton.AutomatonOperations;
import org.iguana.regex.matcher.DFAMatcher;
import org.iguana.regex.matcher.Matcher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StarTest {
	
	@Test
	public void test1() {
		RegularExpression regex = Star.from(Char.from('a'));

		Automaton automaton = regex.getAutomaton();
		assertEquals(4, automaton.getCountStates());
		
		automaton = AutomatonOperations.makeDeterministic(automaton);
		assertEquals(2, automaton.getCountStates());
		
		Matcher matcher = new DFAMatcher(automaton);
		
		assertEquals(0, matcher.match(Input.fromString(""), 0));
		assertEquals(1, matcher.match(Input.fromString("a"), 0));
		assertEquals(2, matcher.match(Input.fromString("aa"), 0));
		assertEquals(3, matcher.match(Input.fromString("aaa"), 0));
		assertEquals(6, matcher.match(Input.fromString("aaaaaa"), 0));
		assertEquals(17, matcher.match(Input.fromString("aaaaaaaaaaaaaaaaa"), 0));
		assertEquals(33, matcher.match(Input.fromString("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"), 0));
	}
		
	@Test
	public void test2() {
		// ([a-a]+)*
		RegularExpression regex = Star.from(Seq.from(Plus.from(Alt.from(CharRange.in('a', 'a')))));
		Automaton automaton = regex.getAutomaton();
		assertEquals(7, automaton.getCountStates());
		
		automaton = AutomatonOperations.makeDeterministic(automaton);
		assertEquals(3, automaton.getCountStates());

		Matcher matcher = new DFAMatcher(automaton);
		
		assertEquals(0, matcher.match(Input.fromString(""), 0));
		assertEquals(1, matcher.match(Input.fromString("a"), 0));
		assertEquals(5, matcher.match(Input.fromString("aaaaa"), 0));
	}
	
	
	@Test
	public void test3() {
		// ([a-z]+ | [(-)] | "*")*
		RegularExpression r1 = Plus.from(Alt.from(CharRange.in('a', 'z')));
		RegularExpression r2 = Plus.from(Alt.from(CharRange.in('(', ')')));
		RegularExpression r3 = Char.from('*');
		
		Automaton automaton = Star.from(Alt.from(r1, r2, r3)).getAutomaton();
		assertEquals(15, automaton.getCountStates());
		
		automaton = AutomatonOperations.makeDeterministic(automaton);
		assertEquals(6, automaton.getCountStates());
		
		Matcher matcher = new DFAMatcher(automaton);
		assertEquals(0, matcher.match(Input.fromString(""), 0));
		assertEquals(1, matcher.match(Input.fromString("a"), 0));
		assertEquals(1, matcher.match(Input.fromString("m"), 0));
		assertEquals(1, matcher.match(Input.fromString("z"), 0));
		assertEquals(1, matcher.match(Input.fromString("*"), 0));
		assertEquals(1, matcher.match(Input.fromString("("), 0));
		assertEquals(1, matcher.match(Input.fromString(")"), 0));
		assertEquals(3, matcher.match(Input.fromString("a)*"), 0));
		assertEquals(3, matcher.match(Input.fromString("*(a"), 0));
		assertEquals(3, matcher.match(Input.fromString(")*a"), 0));
		assertEquals(11, matcher.match(Input.fromString("ab(*za()((a"), 0));
	}
	

}
