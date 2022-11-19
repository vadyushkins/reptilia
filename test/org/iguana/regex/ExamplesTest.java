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

import org.iguana.regex.automaton.Automaton;
import org.iguana.regex.automaton.AutomatonOperations;
import org.iguana.regex.matcher.DFAMatcher;
import org.iguana.regex.matcher.Matcher;
import org.iguana.regex.utils.AutomatonToDot;
import org.iguana.utils.input.Input;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ExamplesTest {
	
	@Test
	public void testId() {
		RegularExpression id = RegularExpressionExamples.getId();
		Automaton automaton = id.getAutomaton();
		
		assertEquals(13, automaton.getCountStates());
		
		automaton = AutomatonOperations.makeDeterministic(automaton);
		assertEquals(6, automaton.getCountStates());
		
		Matcher matcher = new DFAMatcher(automaton);
		
		assertTrue(matcher.match(Input.fromString("a")));
		assertFalse(matcher.match(Input.fromString("9")));
		assertTrue(matcher.match(Input.fromString("abc")));
		assertTrue(matcher.match(Input.fromString("Identifier")));
		assertTrue(matcher.match(Input.fromString("Identifier12")));
		assertTrue(matcher.match(Input.fromString("Identifier12Assdfd")));
	}
	
	@Test
	public void testIntersectionKeywordId() {
		Automaton a1 = RegularExpressionExamples.getId().getAutomaton();
		Automaton a2 = Seq.from("for").getAutomaton();
		
		Automaton intersect = AutomatonOperations.intersect(a1, a2);		
		assertFalse(intersect.isLanguageEmpty());
	}
	
	@Test
	public void testFloat() {
		RegularExpression _float = RegularExpressionExamples.getFloat();
		
		Automaton automaton = _float.getAutomaton();
		assertEquals(10, automaton.getCountStates());
		
		automaton = AutomatonOperations.makeDeterministic(automaton);
		assertEquals(6, automaton.getCountStates());
		
		Matcher matcher = new DFAMatcher(automaton);

		assertTrue(matcher.match(Input.fromString("1.2")));
		assertTrue(matcher.match(Input.fromString("1.2"), 0, 3));
		assertFalse(matcher.match(Input.fromString("9")));
		assertFalse(matcher.match(Input.fromString(".9")));
		assertFalse(matcher.match(Input.fromString("123.")));
		assertTrue(matcher.match(Input.fromString("12.2")));
		assertTrue(matcher.match(Input.fromString("1342343.27890")));
		assertTrue(matcher.match(Input.fromString("908397439483.278902433")));
	}
	
	@Test
	public void testJavaUnicodeEscape() {
		RegularExpression regex = RegularExpressionExamples.getJavaUnicodeEscape();
		Automaton automaton = regex.getAutomaton();
		assertEquals(30, automaton.getCountStates());
		
		automaton = AutomatonOperations.makeDeterministic(automaton);
		assertEquals(37, automaton.getCountStates());
		
		Matcher matcher = new DFAMatcher(automaton);
		assertTrue(matcher.match(Input.fromString("\\u0123")));
	}
	
	@Test
	public void testCharacter() {
		RegularExpression regex = RegularExpressionExamples.getCharacter();
		Automaton automaton = regex.getAutomaton();
		assertEquals(13, automaton.getCountStates());

		automaton = AutomatonOperations.makeDeterministic(automaton);
		assertEquals(7, automaton.getCountStates());

		Matcher matcher = new DFAMatcher(automaton);
		assertTrue(matcher.match(Input.fromString("'ab'")));
	}
	
	@Test
	public void testStringPart() {
		RegularExpression regex = RegularExpressionExamples.getStringPart();
		Automaton automaton = regex.getAutomaton();
		assertEquals(19, automaton.getCountStates());

		automaton = AutomatonOperations.makeDeterministic(automaton);
		assertEquals(9, automaton.getCountStates());
		
		Matcher matcher = new DFAMatcher(automaton);
		
		assertTrue(matcher.match(Input.fromString("abcd")));
		assertFalse(matcher.match(Input.fromString("\\aa")));
		assertFalse(matcher.match(Input.fromString("\"aaa")));
	}
	
	@Test
	public void testMultilineComment() {
		Automaton automaton = RegularExpressionExamples.getMultilineComment().getAutomaton();
//		assertEquals(9, automaton.getCountStates());

		try {
			AutomatonToDot.toDot(automaton).generate("/Users/afroozeh/automaton.pdf");
		} catch (Exception e) {
			e.printStackTrace();
		}

		automaton = AutomatonOperations.makeDeterministic(automaton);

		try {
			AutomatonToDot.toDot(automaton).generate("/Users/afroozeh/dfa.pdf");
		} catch (Exception e) {
			e.printStackTrace();
		}
//		assertEquals(5, automaton.getCountStates());
//		assertTrue(matcher.match(Input.fromString("/*a*/")));
	}	
}
