///*
// * Copyright (c) 2015, Ali Afroozeh and Anastasia Izmaylova, Centrum Wiskunde & Informatica (CWI)
// * All rights reserved.
// *
// * Redistribution and use in source and binary forms, with or without
// * modification, are permitted provided that the following conditions are met:
// *
// * 1. Redistributions of source code must retain the above copyright notice, this
// *    list of conditions and the following disclaimer.
// *
// * 2. Redistributions in binary form must reproduce the above copyright notice, this
// *    list of conditions and the following disclaimer in the documentation and/or
// *    other materials provided with the distribution.
// *
// * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
// * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
// * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
// * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
// * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
// * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
// * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
// * OF SUCH DAMAGE.
// *
// */
//
//package org.iguana.parser.datadependent;
//
//import static org.iguana.datadependent.ast.AST.equal;
//import static org.iguana.datadependent.ast.AST.indent;
//import static org.iguana.datadependent.ast.AST.lExt;
//import static org.iguana.datadependent.ast.AST.less;
//import static org.iguana.datadependent.ast.AST.var;
//import static org.iguana.grammar.condition.DataDependentCondition.predicate;
//import static org.iguana.util.CollectionsUtil.set;
//
//import org.iguana.grammar.Grammar;
//import org.iguana.grammar.GrammarGraph;
//import org.iguana.grammar.condition.ConditionType;
//import org.iguana.grammar.condition.RegularExpressionCondition;
//import org.iguana.grammar.symbol.*;
//import org.iguana.regex.Character;
//import org.iguana.regex.CharacterRange;
//import org.iguana.parser.IguanaParser;
//import org.iguana.parser.ParseResult;
//import org.iguana.regex.Sequence;
//import org.iguana.util.Configuration;
//import org.junit.Test;
//
//import iguana.utils.input.Input;
//
//public class Python {
//
//	public static Grammar grammar =
//			Grammar.builder()
//			//ShortBytesChar ::= (\u0001-\u10FFFF)
//			.addRule(Rule.withHead(new Nonterminal.Builder("ShortBytesChar").build()).addSymbol(Alt.builder(CharacterRange.builder(1, 1114111).build()).addPostConditions(set(new RegularExpressionCondition(ConditionType.NOT_MATCH, Alt.builder(Character.builder(10).build()).build()))).build()).build())
//			//IntPart ::= Digit+
//			.addRule(Rule.withHead(new Nonterminal.Builder("IntPart").build()).addSymbol(new Plus.Builder(new Nonterminal.Builder("Digit").build()).build()).build())
//			//BinInteger ::= "0" ("b" | "B") BinDigit+
//			.addRule(Rule.withHead(new Nonterminal.Builder("BinInteger").build()).addSymbol(Sequence.builder(Character.builder(48).build()).build()).addSymbol(Alt.builder(Sequence.builder(Character.builder(98).build()).build(), Sequence.builder(Character.builder(66).build()).build()).build()).addSymbol(new Plus.Builder(new Nonterminal.Builder("BinDigit").build()).build()).build())
//			//LongBytesChar ::= (\u0001-\u10FFFF)
//			.addRule(Rule.withHead(new Nonterminal.Builder("LongBytesChar").build()).addSymbol(Alt.builder(CharacterRange.builder(1, 1114111).build()).addPostConditions(set(new RegularExpressionCondition(ConditionType.NOT_MATCH, Alt.builder(Character.builder(92).build()).build()))).build()).build())
//			//Term ::= Factor (("/" | "*" | "%" | "//") Factor)*
//			.addRule(Rule.withHead(new Nonterminal.Builder("Term").build()).addSymbol(new Nonterminal.Builder("Factor").build()).addSymbol(Star.builder(Sequence.builder(Alt.builder(Sequence.builder(Character.builder(47).build()).build(), Sequence.builder(Character.builder(42).build()).build(), Sequence.builder(Character.builder(37).build()).build(), Sequence.builder(Character.builder(47).build(), Character.builder(47).build()).build()).build(), new Nonterminal.Builder("Factor").build()).build()).build()).build())
//			//ImportStmt ::= ImportName
//			.addRule(Rule.withHead(new Nonterminal.Builder("ImportStmt").build()).addSymbol(new Nonterminal.Builder("ImportName").build()).build())
//			//ImportStmt ::= ImportFrom
//			.addRule(Rule.withHead(new Nonterminal.Builder("ImportStmt").build()).addSymbol(new Nonterminal.Builder("ImportFrom").build()).build())
//			//Parameters ::= "(" TypedArgsList? ")"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Parameters").build()).addSymbol(Sequence.builder(Character.builder(40).build()).build()).addSymbol(org.iguana.regex.Opt.builder(new Nonterminal.Builder("TypedArgsList").build()).build()).addSymbol(Sequence.builder(Character.builder(41).build()).build()).build())
//			//DottedAsNames ::= DottedAsName ("," DottedAsName)*
//			.addRule(Rule.withHead(new Nonterminal.Builder("DottedAsNames").build()).addSymbol(new Nonterminal.Builder("DottedAsName").build()).addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), new Nonterminal.Builder("DottedAsName").build()).build()).build()).build())
//			//ShortString ::= """ ShortStringItem* """
//			.addRule(Rule.withHead(new Nonterminal.Builder("ShortString").build()).addSymbol(Sequence.builder(Character.builder(34).build()).build()).addSymbol(Star.builder(new Nonterminal.Builder("ShortStringItem").build()).build()).addSymbol(Sequence.builder(Character.builder(34).build()).build()).build())
//			//ShortString ::= "'" ShortStringItem* "'"
//			.addRule(Rule.withHead(new Nonterminal.Builder("ShortString").build()).addSymbol(Sequence.builder(Character.builder(39).build()).build()).addSymbol(Star.builder(new Nonterminal.Builder("ShortStringItem").build()).build()).addSymbol(Sequence.builder(Character.builder(39).build()).build()).build())
//			//ShortStringItem ::= ShortStringChar
//			.addRule(Rule.withHead(new Nonterminal.Builder("ShortStringItem").build()).addSymbol(new Nonterminal.Builder("ShortStringChar").build()).build())
//			//ShortStringItem ::= StringEscapeSeq
//			.addRule(Rule.withHead(new Nonterminal.Builder("ShortStringItem").build()).addSymbol(new Nonterminal.Builder("StringEscapeSeq").build()).build())
//			//EncodingDecl ::= Name
//			.addRule(Rule.withHead(new Nonterminal.Builder("EncodingDecl").build()).addSymbol(new Nonterminal.Builder("Name").build()).build())
//			//LongStringItem ::= LongStringChar
//			.addRule(Rule.withHead(new Nonterminal.Builder("LongStringItem").build()).addSymbol(new Nonterminal.Builder("LongStringChar").build()).build())
//			//LongStringItem ::= StringEscapeSeq
//			.addRule(Rule.withHead(new Nonterminal.Builder("LongStringItem").build()).addSymbol(new Nonterminal.Builder("StringEscapeSeq").build()).build())
//			//empty() ::=
//			.addRule(Rule.withHead(new Nonterminal.Builder("empty()").build()).build())
//			//FloatNumber ::= ExponentFloat
//			.addRule(Rule.withHead(new Nonterminal.Builder("FloatNumber").build()).addSymbol(new Nonterminal.Builder("ExponentFloat").build()).build())
//			//FloatNumber ::= PointFloat
//			.addRule(Rule.withHead(new Nonterminal.Builder("FloatNumber").build()).addSymbol(new Nonterminal.Builder("PointFloat").build()).build())
//			//StringLiteral ::= StringPrefix? (LongString | ShortString)
//			.addRule(Rule.withHead(new Nonterminal.Builder("StringLiteral").build()).addSymbol(Opt.builder(new Nonterminal.Builder("StringPrefix").build()).build()).addSymbol(Alt.builder(new Nonterminal.Builder("LongString").build(), new Nonterminal.Builder("ShortString").build()).build()).build())
//			//WhileStmt ::= "while" Test ":" Suite ("else" ":" Suite)?
//			.addRule(Rule.withHead(new Nonterminal.Builder("WhileStmt").build()).addSymbol(Sequence.builder(Character.builder(119).build(), Character.builder(104).build(), Character.builder(105).build(), Character.builder(108).build(), Character.builder(101).build()).build()).addSymbol(new Nonterminal.Builder("Test").build()).addSymbol(Sequence.builder(Character.builder(58).build()).build()).addSymbol(new Nonterminal.Builder("Suite").build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(101).build(), Character.builder(108).build(), Character.builder(115).build(), Character.builder(101).build()).build(), Sequence.builder(Character.builder(58).build()).build(), new Nonterminal.Builder("Suite").build()).build()).build()).build())
//			//SmallStmt ::= PassStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("SmallStmt").build()).addSymbol(new Nonterminal.Builder("PassStmt").build()).build())
//			//SmallStmt ::= ExprStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("SmallStmt").build()).addSymbol(new Nonterminal.Builder("ExprStmt").build()).build())
//			//SmallStmt ::= DelStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("SmallStmt").build()).addSymbol(new Nonterminal.Builder("DelStmt").build()).build())
//			//SmallStmt ::= NonlocalStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("SmallStmt").build()).addSymbol(new Nonterminal.Builder("NonlocalStmt").build()).build())
//			//SmallStmt ::= ImportStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("SmallStmt").build()).addSymbol(new Nonterminal.Builder("ImportStmt").build()).build())
//			//SmallStmt ::= AssertStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("SmallStmt").build()).addSymbol(new Nonterminal.Builder("AssertStmt").build()).build())
//			//SmallStmt ::= FlowStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("SmallStmt").build()).addSymbol(new Nonterminal.Builder("FlowStmt").build()).build())
//			//SmallStmt ::= GlobalStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("SmallStmt").build()).addSymbol(new Nonterminal.Builder("GlobalStmt").build()).build())
//			//HexDigit ::= (A-F)
//			.addRule(Rule.withHead(new Nonterminal.Builder("HexDigit").build()).addSymbol(Alt.builder(CharacterRange.builder(65, 70).build()).build()).build())
//			//HexDigit ::= Digit
//			.addRule(Rule.withHead(new Nonterminal.Builder("HexDigit").build()).addSymbol(new Nonterminal.Builder("Digit").build()).build())
//			//HexDigit ::= (a-f)
//			.addRule(Rule.withHead(new Nonterminal.Builder("HexDigit").build()).addSymbol(Alt.builder(CharacterRange.builder(97, 102).build()).build()).build())
//			//ImagNumber ::= (IntPart | FloatNumber) ("j" | "J")
//			.addRule(Rule.withHead(new Nonterminal.Builder("ImagNumber").build()).addSymbol(Alt.builder(new Nonterminal.Builder("IntPart").build(), new Nonterminal.Builder("FloatNumber").build()).build()).addSymbol(Alt.builder(Sequence.builder(Character.builder(106).build()).build(), Sequence.builder(Character.builder(74).build()).build()).build()).build())
//			//Keyword ::= "yield"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(121).build(), Character.builder(105).build(), Character.builder(101).build(), Character.builder(108).build(), Character.builder(100).build()).build()).build())
//			//Keyword ::= "lambda"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(108).build(), Character.builder(97).build(), Character.builder(109).build(), Character.builder(98).build(), Character.builder(100).build(), Character.builder(97).build()).build()).build())
//			//Keyword ::= "raise"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(114).build(), Character.builder(97).build(), Character.builder(105).build(), Character.builder(115).build(), Character.builder(101).build()).build()).build())
//			//Keyword ::= "while"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(119).build(), Character.builder(104).build(), Character.builder(105).build(), Character.builder(108).build(), Character.builder(101).build()).build()).build())
//			//Keyword ::= "not"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(110).build(), Character.builder(111).build(), Character.builder(116).build()).build()).build())
//			//Keyword ::= "finally"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(102).build(), Character.builder(105).build(), Character.builder(110).build(), Character.builder(97).build(), Character.builder(108).build(), Character.builder(108).build(), Character.builder(121).build()).build()).build())
//			//Keyword ::= "and"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(97).build(), Character.builder(110).build(), Character.builder(100).build()).build()).build())
//			//Keyword ::= "or"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(111).build(), Character.builder(114).build()).build()).build())
//			//Keyword ::= "class"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(99).build(), Character.builder(108).build(), Character.builder(97).build(), Character.builder(115).build(), Character.builder(115).build()).build()).build())
//			//Keyword ::= "break"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(98).build(), Character.builder(114).build(), Character.builder(101).build(), Character.builder(97).build(), Character.builder(107).build()).build()).build())
//			//Keyword ::= "as"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(97).build(), Character.builder(115).build()).build()).build())
//			//Keyword ::= "import"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(105).build(), Character.builder(109).build(), Character.builder(112).build(), Character.builder(111).build(), Character.builder(114).build(), Character.builder(116).build()).build()).build())
//			//Keyword ::= "from"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(102).build(), Character.builder(114).build(), Character.builder(111).build(), Character.builder(109).build()).build()).build())
//			//Keyword ::= "global"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(103).build(), Character.builder(108).build(), Character.builder(111).build(), Character.builder(98).build(), Character.builder(97).build(), Character.builder(108).build()).build()).build())
//			//Keyword ::= "assert"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(97).build(), Character.builder(115).build(), Character.builder(115).build(), Character.builder(101).build(), Character.builder(114).build(), Character.builder(116).build()).build()).build())
//			//Keyword ::= "None"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(78).build(), Character.builder(111).build(), Character.builder(110).build(), Character.builder(101).build()).build()).build())
//			//Keyword ::= "True"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(84).build(), Character.builder(114).build(), Character.builder(117).build(), Character.builder(101).build()).build()).build())
//			//Keyword ::= "try"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(116).build(), Character.builder(114).build(), Character.builder(121).build()).build()).build())
//			//Keyword ::= "False"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(70).build(), Character.builder(97).build(), Character.builder(108).build(), Character.builder(115).build(), Character.builder(101).build()).build()).build())
//			//Keyword ::= "except"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(101).build(), Character.builder(120).build(), Character.builder(99).build(), Character.builder(101).build(), Character.builder(112).build(), Character.builder(116).build()).build()).build())
//			//Keyword ::= "if"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(105).build(), Character.builder(102).build()).build()).build())
//			//Keyword ::= "return"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(114).build(), Character.builder(101).build(), Character.builder(116).build(), Character.builder(117).build(), Character.builder(114).build(), Character.builder(110).build()).build()).build())
//			//Keyword ::= "else"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(101).build(), Character.builder(108).build(), Character.builder(115).build(), Character.builder(101).build()).build()).build())
//			//Keyword ::= "in"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(105).build(), Character.builder(110).build()).build()).build())
//			//Keyword ::= "is"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(105).build(), Character.builder(115).build()).build()).build())
//			//Keyword ::= "nonlocal"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(110).build(), Character.builder(111).build(), Character.builder(110).build(), Character.builder(108).build(), Character.builder(111).build(), Character.builder(99).build(), Character.builder(97).build(), Character.builder(108).build()).build()).build())
//			//Keyword ::= "continue"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(99).build(), Character.builder(111).build(), Character.builder(110).build(), Character.builder(116).build(), Character.builder(105).build(), Character.builder(110).build(), Character.builder(117).build(), Character.builder(101).build()).build()).build())
//			//Keyword ::= "with"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(119).build(), Character.builder(105).build(), Character.builder(116).build(), Character.builder(104).build()).build()).build())
//			//Keyword ::= "for"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(102).build(), Character.builder(111).build(), Character.builder(114).build()).build()).build())
//			//Keyword ::= "pass"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(112).build(), Character.builder(97).build(), Character.builder(115).build(), Character.builder(115).build()).build()).build())
//			//Keyword ::= "elif"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(101).build(), Character.builder(108).build(), Character.builder(105).build(), Character.builder(102).build()).build()).build())
//			//Keyword ::= "def"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(100).build(), Character.builder(101).build(), Character.builder(102).build()).build()).build())
//			//Keyword ::= "del"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Keyword").build()).addSymbol(Sequence.builder(Character.builder(100).build(), Character.builder(101).build(), Character.builder(108).build()).build()).build())
//			//Stmt ::= CompoundStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("Stmt").build()).addSymbol(new Nonterminal.Builder("CompoundStmt").build()).build())
//			//Stmt ::= SimpleStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("Stmt").build()).addSymbol(new Nonterminal.Builder("SimpleStmt").build()).build())
//			//Classdef ::= "class" Name ("(" Arglist? ")")? ":" Suite
//			.addRule(Rule.withHead(new Nonterminal.Builder("Classdef").build()).addSymbol(Sequence.builder(Character.builder(99).build(), Character.builder(108).build(), Character.builder(97).build(), Character.builder(115).build(), Character.builder(115).build()).build()).addSymbol(new Nonterminal.Builder("Name").build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(40).build()).build(), org.iguana.regex.Opt.builder(new Nonterminal.Builder("Arglist").build()).build(), Sequence.builder(Character.builder(41).build()).build()).build()).build()).addSymbol(Sequence.builder(Character.builder(58).build()).build()).addSymbol(new Nonterminal.Builder("Suite").build()).build())
//			//StringPrefix ::= "u"
//			.addRule(Rule.withHead(new Nonterminal.Builder("StringPrefix").build()).addSymbol(Sequence.builder(Character.builder(117).build()).build()).build())
//			//StringPrefix ::= "U"
//			.addRule(Rule.withHead(new Nonterminal.Builder("StringPrefix").build()).addSymbol(Sequence.builder(Character.builder(85).build()).build()).build())
//			//StringPrefix ::= "R"
//			.addRule(Rule.withHead(new Nonterminal.Builder("StringPrefix").build()).addSymbol(Sequence.builder(Character.builder(82).build()).build()).build())
//			//ShortBytesitem ::= ShortBytesChar
//			.addRule(Rule.withHead(new Nonterminal.Builder("ShortBytesitem").build()).addSymbol(new Nonterminal.Builder("ShortBytesChar").build()).build())
//			//ShortBytesitem ::= BytesEscapeSeq
//			.addRule(Rule.withHead(new Nonterminal.Builder("ShortBytesitem").build()).addSymbol(new Nonterminal.Builder("BytesEscapeSeq").build()).build())
//			//CompIter ::= CompFor
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompIter").build()).addSymbol(new Nonterminal.Builder("CompFor").build()).build())
//			//CompIter ::= CompIf
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompIter").build()).addSymbol(new Nonterminal.Builder("CompIf").build()).build())
//			//StarExpr ::= "*" Expr
//			.addRule(Rule.withHead(new Nonterminal.Builder("StarExpr").build()).addSymbol(Sequence.builder(Character.builder(42).build()).build()).addSymbol(new Nonterminal.Builder("Expr").build()).build())
//			//NonzeroDigit ::= (1-9)
//			.addRule(Rule.withHead(new Nonterminal.Builder("NonzeroDigit").build()).addSymbol(Alt.builder(CharacterRange.builder(49, 57).build()).build()).build())
//			//YieldExpr ::= "yield" YieldArg?
//			.addRule(Rule.withHead(new Nonterminal.Builder("YieldExpr").build()).addSymbol(Sequence.builder(Character.builder(121).build(), Character.builder(105).build(), Character.builder(101).build(), Character.builder(108).build(), Character.builder(100).build()).build()).addSymbol(org.iguana.regex.Opt.builder(new Nonterminal.Builder("YieldArg").build()).build()).build())
//			//ImportFrom ::= "from" (("..." | ".")+ | (("..." | ".")* DottedName)) "import" ("*" | ImportAsNames | ("(" ImportAsNames ")"))
//			.addRule(Rule.withHead(new Nonterminal.Builder("ImportFrom").build()).addSymbol(Sequence.builder(Character.builder(102).build(), Character.builder(114).build(), Character.builder(111).build(), Character.builder(109).build()).build()).addSymbol(Alt.builder(new Plus.Builder(Alt.builder(Sequence.builder(Character.builder(46).build(), Character.builder(46).build(), Character.builder(46).build()).build(), Sequence.builder(Character.builder(46).build()).build()).build()).build(), Sequence.builder(Star.builder(Alt.builder(Sequence.builder(Character.builder(46).build(), Character.builder(46).build(), Character.builder(46).build()).build(), Sequence.builder(Character.builder(46).build()).build()).build()).build(), new Nonterminal.Builder("DottedName").build()).build()).build()).addSymbol(Sequence.builder(Character.builder(105).build(), Character.builder(109).build(), Character.builder(112).build(), Character.builder(111).build(), Character.builder(114).build(), Character.builder(116).build()).build()).addSymbol(Alt.builder(Sequence.builder(Character.builder(42).build()).build(), new Nonterminal.Builder("ImportAsNames").build(), Sequence.builder(Sequence.builder(Character.builder(40).build()).build(), new Nonterminal.Builder("ImportAsNames").build(), Sequence.builder(Character.builder(41).build()).build()).build()).build()).build())
//			//DottedAsName ::= DottedName ("as" Name)?
//			.addRule(Rule.withHead(new Nonterminal.Builder("DottedAsName").build()).addSymbol(new Nonterminal.Builder("DottedName").build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(97).build(), Character.builder(115).build()).build(), new Nonterminal.Builder("Name").build()).build()).build()).build())
//			//BytesPrefix ::= "bR"
//			.addRule(Rule.withHead(new Nonterminal.Builder("BytesPrefix").build()).addSymbol(Sequence.builder(Character.builder(98).build(), Character.builder(82).build()).build()).build())
//			//BytesPrefix ::= "rB"
//			.addRule(Rule.withHead(new Nonterminal.Builder("BytesPrefix").build()).addSymbol(Sequence.builder(Character.builder(114).build(), Character.builder(66).build()).build()).build())
//			//BytesPrefix ::= "Rb"
//			.addRule(Rule.withHead(new Nonterminal.Builder("BytesPrefix").build()).addSymbol(Sequence.builder(Character.builder(82).build(), Character.builder(98).build()).build()).build())
//			//BytesPrefix ::= "B"
//			.addRule(Rule.withHead(new Nonterminal.Builder("BytesPrefix").build()).addSymbol(Sequence.builder(Character.builder(66).build()).build()).build())
//			//BytesPrefix ::= "BR"
//			.addRule(Rule.withHead(new Nonterminal.Builder("BytesPrefix").build()).addSymbol(Sequence.builder(Character.builder(66).build(), Character.builder(82).build()).build()).build())
//			//BytesPrefix ::= "br"
//			.addRule(Rule.withHead(new Nonterminal.Builder("BytesPrefix").build()).addSymbol(Sequence.builder(Character.builder(98).build(), Character.builder(114).build()).build()).build())
//			//BytesPrefix ::= "b"
//			.addRule(Rule.withHead(new Nonterminal.Builder("BytesPrefix").build()).addSymbol(Sequence.builder(Character.builder(98).build()).build()).build())
//			//BytesPrefix ::= "Br"
//			.addRule(Rule.withHead(new Nonterminal.Builder("BytesPrefix").build()).addSymbol(Sequence.builder(Character.builder(66).build(), Character.builder(114).build()).build()).build())
//			//BytesPrefix ::= "RB"
//			.addRule(Rule.withHead(new Nonterminal.Builder("BytesPrefix").build()).addSymbol(Sequence.builder(Character.builder(82).build(), Character.builder(66).build()).build()).build())
//			//BytesPrefix ::= "rb"
//			.addRule(Rule.withHead(new Nonterminal.Builder("BytesPrefix").build()).addSymbol(Sequence.builder(Character.builder(114).build(), Character.builder(98).build()).build()).build())
//			//FlowStmt ::= YieldStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("FlowStmt").build()).addSymbol(new Nonterminal.Builder("YieldStmt").build()).build())
//			//FlowStmt ::= RaiseStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("FlowStmt").build()).addSymbol(new Nonterminal.Builder("RaiseStmt").build()).build())
//			//FlowStmt ::= BreakStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("FlowStmt").build()).addSymbol(new Nonterminal.Builder("BreakStmt").build()).build())
//			//FlowStmt ::= ContinueStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("FlowStmt").build()).addSymbol(new Nonterminal.Builder("ContinueStmt").build()).build())
//			//FlowStmt ::= ReturnStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("FlowStmt").build()).addSymbol(new Nonterminal.Builder("ReturnStmt").build()).build())
//			//CompoundStmt ::= WithStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompoundStmt").build()).addSymbol(new Nonterminal.Builder("WithStmt").build()).build())
//			//CompoundStmt ::= WhileStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompoundStmt").build()).addSymbol(new Nonterminal.Builder("WhileStmt").build()).build())
//			//CompoundStmt ::= Decorated
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompoundStmt").build()).addSymbol(new Nonterminal.Builder("Decorated").build()).build())
//			//CompoundStmt ::= Classdef
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompoundStmt").build()).addSymbol(new Nonterminal.Builder("Classdef").build()).build())
//			//CompoundStmt ::= ForStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompoundStmt").build()).addSymbol(new Nonterminal.Builder("ForStmt").build()).build())
//			//CompoundStmt ::= TryStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompoundStmt").build()).addSymbol(new Nonterminal.Builder("TryStmt").build()).build())
//			//CompoundStmt ::= IfStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompoundStmt").build()).addSymbol(new Nonterminal.Builder("IfStmt").build()).build())
//			//CompoundStmt ::= Funcdef
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompoundStmt").build()).addSymbol(new Nonterminal.Builder("Funcdef").build()).build())
//			//EscapeSeq ::= \ '
//			.addRule(Rule.withHead(new Nonterminal.Builder("EscapeSeq").build()).addSymbol(Character.builder(92).build()).addSymbol(Character.builder(39).build()).build())
//			//EscapeSeq ::= \ f
//			.addRule(Rule.withHead(new Nonterminal.Builder("EscapeSeq").build()).addSymbol(Character.builder(92).build()).addSymbol(Character.builder(102).build()).build())
//			//EscapeSeq ::= \ \
//			.addRule(Rule.withHead(new Nonterminal.Builder("EscapeSeq").build()).addSymbol(Character.builder(92).build()).addSymbol(Character.builder(92).build()).build())
//			//EscapeSeq ::= \ n
//			.addRule(Rule.withHead(new Nonterminal.Builder("EscapeSeq").build()).addSymbol(Character.builder(92).build()).addSymbol(Character.builder(110).build()).build())
//			//EscapeSeq ::= \ r
//			.addRule(Rule.withHead(new Nonterminal.Builder("EscapeSeq").build()).addSymbol(Character.builder(92).build()).addSymbol(Character.builder(114).build()).build())
//			//EscapeSeq ::= \ "
//			.addRule(Rule.withHead(new Nonterminal.Builder("EscapeSeq").build()).addSymbol(Character.builder(92).build()).addSymbol(Character.builder(34).build()).build())
//			//EscapeSeq ::= \ b
//			.addRule(Rule.withHead(new Nonterminal.Builder("EscapeSeq").build()).addSymbol(Character.builder(92).build()).addSymbol(Character.builder(98).build()).build())
//			//EscapeSeq ::= \ OctInteger OctInteger OctInteger
//			.addRule(Rule.withHead(new Nonterminal.Builder("EscapeSeq").build()).addSymbol(Character.builder(92).build()).addSymbol(new Nonterminal.Builder("OctInteger").build()).addSymbol(new Nonterminal.Builder("OctInteger").build()).addSymbol(new Nonterminal.Builder("OctInteger").build()).build())
//			//EscapeSeq ::= \ x HexInteger HexInteger
//			.addRule(Rule.withHead(new Nonterminal.Builder("EscapeSeq").build()).addSymbol(Character.builder(92).build()).addSymbol(Character.builder(120).build()).addSymbol(new Nonterminal.Builder("HexInteger").build()).addSymbol(new Nonterminal.Builder("HexInteger").build()).build())
//			//EscapeSeq ::= \ a
//			.addRule(Rule.withHead(new Nonterminal.Builder("EscapeSeq").build()).addSymbol(Character.builder(92).build()).addSymbol(Character.builder(97).build()).build())
//			//EscapeSeq ::= \ v
//			.addRule(Rule.withHead(new Nonterminal.Builder("EscapeSeq").build()).addSymbol(Character.builder(92).build()).addSymbol(Character.builder(118).build()).build())
//			//EscapeSeq ::= \ t
//			.addRule(Rule.withHead(new Nonterminal.Builder("EscapeSeq").build()).addSymbol(Character.builder(92).build()).addSymbol(Character.builder(116).build()).build())
//			//LambdefNocond ::= "lambda" Varargslist? ":" TestNocond
//			.addRule(Rule.withHead(new Nonterminal.Builder("LambdefNocond").build()).addSymbol(Sequence.builder(Character.builder(108).build(), Character.builder(97).build(), Character.builder(109).build(), Character.builder(98).build(), Character.builder(100).build(), Character.builder(97).build()).build()).addSymbol(org.iguana.regex.Opt.builder(new Nonterminal.Builder("Varargslist").build()).build()).addSymbol(Sequence.builder(Character.builder(58).build()).build()).addSymbol(new Nonterminal.Builder("TestNocond").build()).build())
//			//Atom ::= Name
//			.addRule(Rule.withHead(new Nonterminal.Builder("Atom").build()).addSymbol(new Nonterminal.Builder("Name").build()).build())
//			//Atom ::= String+
//			.addRule(Rule.withHead(new Nonterminal.Builder("Atom").build()).addSymbol(new Plus.Builder(new Nonterminal.Builder("String").build()).build()).build())
//			//Atom ::= "True"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Atom").build()).addSymbol(Sequence.builder(Character.builder(84).build(), Character.builder(114).build(), Character.builder(117).build(), Character.builder(101).build()).build()).build())
//			//Atom ::= "(" (TestlistComp | YieldExpr)? ")"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Atom").build()).addSymbol(Sequence.builder(Character.builder(40).build()).build()).addSymbol(org.iguana.regex.Opt.builder(Alt.builder(new Nonterminal.Builder("TestlistComp").build(), new Nonterminal.Builder("YieldExpr").build()).build()).build()).addSymbol(Sequence.builder(Character.builder(41).build()).build()).build())
//			//Atom ::= "False"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Atom").build()).addSymbol(Sequence.builder(Character.builder(70).build(), Character.builder(97).build(), Character.builder(108).build(), Character.builder(115).build(), Character.builder(101).build()).build()).build())
//			//Atom ::= Number
//			.addRule(Rule.withHead(new Nonterminal.Builder("Atom").build()).addSymbol(new Nonterminal.Builder("Number").build()).build())
//			//Atom ::= "{" Dictorsetmaker? "}"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Atom").build()).addSymbol(Sequence.builder(Character.builder(123).build()).build()).addSymbol(org.iguana.regex.Opt.builder(new Nonterminal.Builder("Dictorsetmaker").build()).build()).addSymbol(Sequence.builder(Character.builder(125).build()).build()).build())
//			//Atom ::= "..."
//			.addRule(Rule.withHead(new Nonterminal.Builder("Atom").build()).addSymbol(Sequence.builder(Character.builder(46).build(), Character.builder(46).build(), Character.builder(46).build()).build()).build())
//			//Atom ::= "[" TestlistComp? "]"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Atom").build()).addSymbol(Sequence.builder(Character.builder(91).build()).build()).addSymbol(org.iguana.regex.Opt.builder(new Nonterminal.Builder("TestlistComp").build()).build()).addSymbol(Sequence.builder(Character.builder(93).build()).build()).build())
//			//Atom ::= "None"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Atom").build()).addSymbol(Sequence.builder(Character.builder(78).build(), Character.builder(111).build(), Character.builder(110).build(), Character.builder(101).build()).build()).build())
//			//Augassign ::= "**="
//			.addRule(Rule.withHead(new Nonterminal.Builder("Augassign").build()).addSymbol(Sequence.builder(Character.builder(42).build(), Character.builder(42).build(), Character.builder(61).build()).build()).build())
//			//Augassign ::= "//="
//			.addRule(Rule.withHead(new Nonterminal.Builder("Augassign").build()).addSymbol(Sequence.builder(Character.builder(47).build(), Character.builder(47).build(), Character.builder(61).build()).build()).build())
//			//Augassign ::= "^="
//			.addRule(Rule.withHead(new Nonterminal.Builder("Augassign").build()).addSymbol(Sequence.builder(Character.builder(94).build(), Character.builder(61).build()).build()).build())
//			//Augassign ::= "|="
//			.addRule(Rule.withHead(new Nonterminal.Builder("Augassign").build()).addSymbol(Sequence.builder(Character.builder(124).build(), Character.builder(61).build()).build()).build())
//			//Augassign ::= "&="
//			.addRule(Rule.withHead(new Nonterminal.Builder("Augassign").build()).addSymbol(Sequence.builder(Character.builder(38).build(), Character.builder(61).build()).build()).build())
//			//Augassign ::= ">>="
//			.addRule(Rule.withHead(new Nonterminal.Builder("Augassign").build()).addSymbol(Sequence.builder(Character.builder(62).build(), Character.builder(62).build(), Character.builder(61).build()).build()).build())
//			//Augassign ::= "/="
//			.addRule(Rule.withHead(new Nonterminal.Builder("Augassign").build()).addSymbol(Sequence.builder(Character.builder(47).build(), Character.builder(61).build()).build()).build())
//			//Augassign ::= "-="
//			.addRule(Rule.withHead(new Nonterminal.Builder("Augassign").build()).addSymbol(Sequence.builder(Character.builder(45).build(), Character.builder(61).build()).build()).build())
//			//Augassign ::= "+="
//			.addRule(Rule.withHead(new Nonterminal.Builder("Augassign").build()).addSymbol(Sequence.builder(Character.builder(43).build(), Character.builder(61).build()).build()).build())
//			//Augassign ::= "*="
//			.addRule(Rule.withHead(new Nonterminal.Builder("Augassign").build()).addSymbol(Sequence.builder(Character.builder(42).build(), Character.builder(61).build()).build()).build())
//			//Augassign ::= "<<="
//			.addRule(Rule.withHead(new Nonterminal.Builder("Augassign").build()).addSymbol(Sequence.builder(Character.builder(60).build(), Character.builder(60).build(), Character.builder(61).build()).build()).build())
//			//Augassign ::= "%="
//			.addRule(Rule.withHead(new Nonterminal.Builder("Augassign").build()).addSymbol(Sequence.builder(Character.builder(37).build(), Character.builder(61).build()).build()).build())
//			//Varargslist ::= "**" Vfpdef
//			.addRule(Rule.withHead(new Nonterminal.Builder("Varargslist").build()).addSymbol(Sequence.builder(Character.builder(42).build(), Character.builder(42).build()).build()).addSymbol(new Nonterminal.Builder("Vfpdef").build()).build())
//			//Varargslist ::= Vfpdef ("=" Test)? ("," Vfpdef ("=" Test)?)* ("," (("**" Vfpdef) | ("*" Vfpdef? ("," Vfpdef ("=" Test)?)* ("," "**" Vfpdef)?))?)?
//			.addRule(Rule.withHead(new Nonterminal.Builder("Varargslist").build()).addSymbol(new Nonterminal.Builder("Vfpdef").build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(61).build()).build(), new Nonterminal.Builder("Test").build()).build()).build()).addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), new Nonterminal.Builder("Vfpdef").build(), org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(61).build()).build(), new Nonterminal.Builder("Test").build()).build()).build()).build()).build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), org.iguana.regex.Opt.builder(Alt.builder(Sequence.builder(Sequence.builder(Character.builder(42).build(), Character.builder(42).build()).build(), new Nonterminal.Builder("Vfpdef").build()).build(), Sequence.builder(Sequence.builder(Character.builder(42).build()).build(), org.iguana.regex.Opt.builder(new Nonterminal.Builder("Vfpdef").build()).build(), Star.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), new Nonterminal.Builder("Vfpdef").build(), org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(61).build()).build(), new Nonterminal.Builder("Test").build()).build()).build()).build()).build(), org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), Sequence.builder(Character.builder(42).build(), Character.builder(42).build()).build(), new Nonterminal.Builder("Vfpdef").build()).build()).build()).build()).build()).build()).build()).build()).build())
//			//Varargslist ::= "*" Vfpdef? ("," Vfpdef ("=" Test)?)* ("," "**" Vfpdef)?
//			.addRule(Rule.withHead(new Nonterminal.Builder("Varargslist").build()).addSymbol(Sequence.builder(Character.builder(42).build()).build()).addSymbol(org.iguana.regex.Opt.builder(new Nonterminal.Builder("Vfpdef").build()).build()).addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), new Nonterminal.Builder("Vfpdef").build(), org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(61).build()).build(), new Nonterminal.Builder("Test").build()).build()).build()).build()).build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), Sequence.builder(Character.builder(42).build(), Character.builder(42).build()).build(), new Nonterminal.Builder("Vfpdef").build()).build()).build()).build())
//			//Fraction ::= "." Digit+
//			.addRule(Rule.withHead(new Nonterminal.Builder("Fraction").build()).addSymbol(Sequence.builder(Character.builder(46).build()).build()).addSymbol(new Plus.Builder(new Nonterminal.Builder("Digit").build()).build()).build())
//			//ForStmt ::= "for" Exprlist "in" TestList ":" Suite ("else" ":" Suite)?
//			.addRule(Rule.withHead(new Nonterminal.Builder("ForStmt").build()).addSymbol(Sequence.builder(Character.builder(102).build(), Character.builder(111).build(), Character.builder(114).build()).build()).addSymbol(new Nonterminal.Builder("Exprlist").build()).addSymbol(Sequence.builder(Character.builder(105).build(), Character.builder(110).build()).build()).addSymbol(new Nonterminal.Builder("TestList").build()).addSymbol(Sequence.builder(Character.builder(58).build()).build()).addSymbol(new Nonterminal.Builder("Suite").build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(101).build(), Character.builder(108).build(), Character.builder(115).build(), Character.builder(101).build()).build(), Sequence.builder(Character.builder(58).build()).build(), new Nonterminal.Builder("Suite").build()).build()).build()).build())
//			//BytesEscapeSeq ::= EscapeSeq
//			.addRule(Rule.withHead(new Nonterminal.Builder("BytesEscapeSeq").build()).addSymbol(new Nonterminal.Builder("EscapeSeq").build()).build())
//			//Expr ::= XorExpr ("|" XorExpr)*
//			.addRule(Rule.withHead(new Nonterminal.Builder("Expr").build()).addSymbol(new Nonterminal.Builder("XorExpr").build()).addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(124).build()).build(), new Nonterminal.Builder("XorExpr").build()).build()).build()).build())
//			//OrTest ::= AndTest ("or" AndTest)*
//			.addRule(Rule.withHead(new Nonterminal.Builder("OrTest").build()).addSymbol(new Nonterminal.Builder("AndTest").build()).addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(111).build(), Character.builder(114).build()).build(), new Nonterminal.Builder("AndTest").build()).build()).build()).build())
//			//TestlistStarExpr ::= (Test | StarExpr) ("," (Test | StarExpr))* (,)?
//			.addRule(Rule.withHead(new Nonterminal.Builder("TestlistStarExpr").build()).addSymbol(Alt.builder(new Nonterminal.Builder("Test").build(), new Nonterminal.Builder("StarExpr").build()).build()).addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), Alt.builder(new Nonterminal.Builder("Test").build(), new Nonterminal.Builder("StarExpr").build()).build()).build()).build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Character.builder(44).build()).build()).build()).build())
//			//Subscript ::= Test? ":" Test? Sliceop?
//			.addRule(Rule.withHead(new Nonterminal.Builder("Subscript").build()).addSymbol(org.iguana.regex.Opt.builder(new Nonterminal.Builder("Test").build()).build()).addSymbol(Sequence.builder(Character.builder(58).build()).build()).addSymbol(org.iguana.regex.Opt.builder(new Nonterminal.Builder("Test").build()).build()).addSymbol(org.iguana.regex.Opt.builder(new Nonterminal.Builder("Sliceop").build()).build()).build())
//			//Subscript ::= Test
//			.addRule(Rule.withHead(new Nonterminal.Builder("Subscript").build()).addSymbol(new Nonterminal.Builder("Test").build()).build())
//			//HexInteger ::= "0" ("x" | "X") HexDigit+
//			.addRule(Rule.withHead(new Nonterminal.Builder("HexInteger").build()).addSymbol(Sequence.builder(Character.builder(48).build()).build()).addSymbol(Alt.builder(Sequence.builder(Character.builder(120).build()).build(), Sequence.builder(Character.builder(88).build()).build()).build()).addSymbol(new Plus.Builder(new Nonterminal.Builder("HexDigit").build()).build()).build())
//			//Power ::= Atom Trailer* ("**" Factor)?
//			.addRule(Rule.withHead(new Nonterminal.Builder("Power").build()).addSymbol(new Nonterminal.Builder("Atom").build()).addSymbol(Star.builder(new Nonterminal.Builder("Trailer").build()).build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(42).build(), Character.builder(42).build()).build(), new Nonterminal.Builder("Factor").build()).build()).build()).build())
//			//NewLine ::= 
//			.addRule(Rule.withHead(new Nonterminal.Builder("NewLine").build()).addSymbol(Character.builder(10).build()).build())
//			//YieldStmt ::= YieldExpr
//			.addRule(Rule.withHead(new Nonterminal.Builder("YieldStmt").build()).addSymbol(new Nonterminal.Builder("YieldExpr").build()).build())
//			//Digit ::= (0-9)
//			.addRule(Rule.withHead(new Nonterminal.Builder("Digit").build()).addSymbol(Alt.builder(CharacterRange.builder(48, 57).build()).build()).build())
//			//DelStmt ::= "del" Exprlist
//			.addRule(Rule.withHead(new Nonterminal.Builder("DelStmt").build()).addSymbol(Sequence.builder(Character.builder(100).build(), Character.builder(101).build(), Character.builder(108).build()).build()).addSymbol(new Nonterminal.Builder("Exprlist").build()).build())
//			//Factor ::= Power
//			.addRule(Rule.withHead(new Nonterminal.Builder("Factor").build()).addSymbol(new Nonterminal.Builder("Power").build()).build())
//			//Factor ::= ("-" | "+" | "~") Factor
//			.addRule(Rule.withHead(new Nonterminal.Builder("Factor").build()).addSymbol(Alt.builder(Sequence.builder(Character.builder(45).build()).build(), Sequence.builder(Character.builder(43).build()).build(), Sequence.builder(Character.builder(126).build()).build()).build()).addSymbol(new Nonterminal.Builder("Factor").build()).build())
//			//Decorator ::= "@" DottedName ("(" Arglist? ")")? NewLine
//			.addRule(Rule.withHead(new Nonterminal.Builder("Decorator").build()).addSymbol(Sequence.builder(Character.builder(64).build()).build()).addSymbol(new Nonterminal.Builder("DottedName").build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(40).build()).build(), org.iguana.regex.Opt.builder(new Nonterminal.Builder("Arglist").build()).build(), Sequence.builder(Character.builder(41).build()).build()).build()).build()).addSymbol(new Nonterminal.Builder("NewLine").build()).build())
//			//LongString ::= """"" LongStringItem* """""
//			.addRule(Rule.withHead(new Nonterminal.Builder("LongString").build()).addSymbol(Sequence.builder(Character.builder(34).build(), Character.builder(34).build(), Character.builder(34).build()).build()).addSymbol(Star.builder(new Nonterminal.Builder("LongStringItem").build()).build()).addSymbol(Sequence.builder(Character.builder(34).build(), Character.builder(34).build(), Character.builder(34).build()).build()).build())
//			//LongString ::= "'''" LongStringItem* "'''"
//			.addRule(Rule.withHead(new Nonterminal.Builder("LongString").build()).addSymbol(Sequence.builder(Character.builder(39).build(), Character.builder(39).build(), Character.builder(39).build()).build()).addSymbol(Star.builder(new Nonterminal.Builder("LongStringItem").build()).build()).addSymbol(Sequence.builder(Character.builder(39).build(), Character.builder(39).build(), Character.builder(39).build()).build()).build())
//			//TestList ::= Test ("," Test)* (,)?
//			.addRule(Rule.withHead(new Nonterminal.Builder("TestList").build()).addSymbol(new Nonterminal.Builder("Test").build()).addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), new Nonterminal.Builder("Test").build()).build()).build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Character.builder(44).build()).build()).build()).build())
//			//Decorators ::= Decorator+
//			.addRule(Rule.withHead(new Nonterminal.Builder("Decorators").build()).addSymbol(new Plus.Builder(new Nonterminal.Builder("Decorator").build()).build()).build())
//			//Dictorsetmaker ::= Test ":" Test (CompFor | (("," Test ":" Test)* (,)?))
//			.addRule(Rule.withHead(new Nonterminal.Builder("Dictorsetmaker").build()).addSymbol(new Nonterminal.Builder("Test").build()).addSymbol(Sequence.builder(Character.builder(58).build()).build()).addSymbol(new Nonterminal.Builder("Test").build()).addSymbol(Alt.builder(new Nonterminal.Builder("CompFor").build(), Sequence.builder(Star.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), new Nonterminal.Builder("Test").build(), Sequence.builder(Character.builder(58).build()).build(), new Nonterminal.Builder("Test").build()).build()).build(), org.iguana.regex.Opt.builder(Sequence.builder(Character.builder(44).build()).build()).build()).build()).build()).build())
//			//Dictorsetmaker ::= Test (CompFor | (("," Test)* (,)?))
//			.addRule(Rule.withHead(new Nonterminal.Builder("Dictorsetmaker").build()).addSymbol(new Nonterminal.Builder("Test").build()).addSymbol(Alt.builder(new Nonterminal.Builder("CompFor").build(), Sequence.builder(Star.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), new Nonterminal.Builder("Test").build()).build()).build(), org.iguana.regex.Opt.builder(Sequence.builder(Character.builder(44).build()).build()).build()).build()).build()).build())
//			//Funcdef ::= "def" Name Parameters ("->" Test)? ":" Suite
//			.addRule(Rule.withHead(new Nonterminal.Builder("Funcdef").build()).addSymbol(Sequence.builder(Character.builder(100).build(), Character.builder(101).build(), Character.builder(102).build()).build()).addSymbol(new Nonterminal.Builder("Name").build()).addSymbol(new Nonterminal.Builder("Parameters").build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(45).build(), Character.builder(62).build()).build(), new Nonterminal.Builder("Test").build()).build()).build()).addSymbol(Sequence.builder(Character.builder(58).build()).build()).addSymbol(new Nonterminal.Builder("Suite").build()).build())
//			//Number ::= FloatNumber
//			.addRule(Rule.withHead(new Nonterminal.Builder("Number").build()).addSymbol(new Nonterminal.Builder("FloatNumber").build()).build())
//			//Number ::= ImagNumber
//			.addRule(Rule.withHead(new Nonterminal.Builder("Number").build()).addSymbol(new Nonterminal.Builder("ImagNumber").build()).build())
//			//Number ::= Integer
//			.addRule(Rule.withHead(new Nonterminal.Builder("Number").build()).addSymbol(new Nonterminal.Builder("Integer").build()).build())
//			//BytesLiteral ::= BytesPrefix (ShortBytes | LongBytes)
//			.addRule(Rule.withHead(new Nonterminal.Builder("BytesLiteral").build()).addSymbol(new Nonterminal.Builder("BytesPrefix").build()).addSymbol(Alt.builder(new Nonterminal.Builder("ShortBytes").build(), new Nonterminal.Builder("LongBytes").build()).build()).build())
//
//			/**
//			 * Data-dependent
//			 *
//			 * @layout(NoNL)
//			 * SimpleStmt ::= SmallStmt (";" SmallStmt)* (;)? NL
//			 */
//			.addRule(Rule.withHead(new Nonterminal.Builder("SimpleStmt").build()).addSymbol(new Nonterminal.Builder("SmallStmt").build()).addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(59).build()).build(), new Nonterminal.Builder("SmallStmt").build()).build()).build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Character.builder(59).build()).build()).build()).addSymbol(new Nonterminal.Builder("NL").build())
//							.setLayout(new Nonterminal.Builder("NoNL").build()).build())
//
//			//Tfpdef ::= Name (":" Test)?
//			.addRule(Rule.withHead(new Nonterminal.Builder("Tfpdef").build()).addSymbol(new Nonterminal.Builder("Name").build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(58).build()).build(), new Nonterminal.Builder("Test").build()).build()).build()).build())
//			//CompIf ::= "if" TestNocond CompIter?
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompIf").build()).addSymbol(Sequence.builder(Character.builder(105).build(), Character.builder(102).build()).build()).addSymbol(new Nonterminal.Builder("TestNocond").build()).addSymbol(org.iguana.regex.Opt.builder(new Nonterminal.Builder("CompIter").build()).build()).build())
//			//ArithExpr ::= Term (("-" | "+") Term)*
//			.addRule(Rule.withHead(new Nonterminal.Builder("ArithExpr").build()).addSymbol(new Nonterminal.Builder("Term").build()).addSymbol(Star.builder(Sequence.builder(Alt.builder(Sequence.builder(Character.builder(45).build()).build(), Sequence.builder(Character.builder(43).build()).build()).build(), new Nonterminal.Builder("Term").build()).build()).build()).build())
//			//WithItem ::= Test ("as" Expr)?
//			.addRule(Rule.withHead(new Nonterminal.Builder("WithItem").build()).addSymbol(new Nonterminal.Builder("Test").build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(97).build(), Character.builder(115).build()).build(), new Nonterminal.Builder("Expr").build()).build()).build()).build())
//			//Subscriptlist ::= Subscript ("," Subscript)* (,)?
//			.addRule(Rule.withHead(new Nonterminal.Builder("Subscriptlist").build()).addSymbol(new Nonterminal.Builder("Subscript").build()).addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), new Nonterminal.Builder("Subscript").build()).build()).build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Character.builder(44).build()).build()).build()).build())
//			//PassStmt ::= "pass"
//			.addRule(Rule.withHead(new Nonterminal.Builder("PassStmt").build()).addSymbol(Sequence.builder(Character.builder(112).build(), Character.builder(97).build(), Character.builder(115).build(), Character.builder(115).build()).build()).build())
//			//Exponent ::= ("e" | "E") ("-" | "+")? Digit+
//			.addRule(Rule.withHead(new Nonterminal.Builder("Exponent").build()).addSymbol(Alt.builder(Sequence.builder(Character.builder(101).build()).build(), Sequence.builder(Character.builder(69).build()).build()).build()).addSymbol(org.iguana.regex.Opt.builder(Alt.builder(Sequence.builder(Character.builder(45).build()).build(), Sequence.builder(Character.builder(43).build()).build()).build()).build()).addSymbol(new Plus.Builder(new Nonterminal.Builder("Digit").build()).build()).build())
//			//ShortBytes ::= "'" ShortBytesitem* "'"
//			.addRule(Rule.withHead(new Nonterminal.Builder("ShortBytes").build()).addSymbol(Sequence.builder(Character.builder(39).build()).build()).addSymbol(Star.builder(new Nonterminal.Builder("ShortBytesitem").build()).build()).addSymbol(Sequence.builder(Character.builder(39).build()).build()).build())
//			//ShortBytes ::= """ ShortBytesitem* """
//			.addRule(Rule.withHead(new Nonterminal.Builder("ShortBytes").build()).addSymbol(Sequence.builder(Character.builder(34).build()).build()).addSymbol(Star.builder(new Nonterminal.Builder("ShortBytesitem").build()).build()).addSymbol(Sequence.builder(Character.builder(34).build()).build()).build())
//			//TryStmt ::= "try" ":" Suite (((ExceptClause ":" Suite)+ ("else" ":" Suite)? ("finally" ":" Suite)?) | ("finally" ":" Suite))
//			.addRule(Rule.withHead(new Nonterminal.Builder("TryStmt").build()).addSymbol(Sequence.builder(Character.builder(116).build(), Character.builder(114).build(), Character.builder(121).build()).build()).addSymbol(Sequence.builder(Character.builder(58).build()).build()).addSymbol(new Nonterminal.Builder("Suite").build()).addSymbol(Alt.builder(Sequence.builder(new Plus.Builder(Sequence.builder(new Nonterminal.Builder("ExceptClause").build(), Sequence.builder(Character.builder(58).build()).build(), new Nonterminal.Builder("Suite").build()).build()).build(), org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(101).build(), Character.builder(108).build(), Character.builder(115).build(), Character.builder(101).build()).build(), Sequence.builder(Character.builder(58).build()).build(), new Nonterminal.Builder("Suite").build()).build()).build(), org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(102).build(), Character.builder(105).build(), Character.builder(110).build(), Character.builder(97).build(), Character.builder(108).build(), Character.builder(108).build(), Character.builder(121).build()).build(), Sequence.builder(Character.builder(58).build()).build(), new Nonterminal.Builder("Suite").build()).build()).build()).build(), Sequence.builder(Sequence.builder(Character.builder(102).build(), Character.builder(105).build(), Character.builder(110).build(), Character.builder(97).build(), Character.builder(108).build(), Character.builder(108).build(), Character.builder(121).build()).build(), Sequence.builder(Character.builder(58).build()).build(), new Nonterminal.Builder("Suite").build()).build()).build()).build())
//			//AndTest ::= NotTest ("and" NotTest)*
//			.addRule(Rule.withHead(new Nonterminal.Builder("AndTest").build()).addSymbol(new Nonterminal.Builder("NotTest").build()).addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(97).build(), Character.builder(110).build(), Character.builder(100).build()).build(), new Nonterminal.Builder("NotTest").build()).build()).build()).build())
//			//LongStringChar ::= (\u0001-\u10FFFF)
//			.addRule(Rule.withHead(new Nonterminal.Builder("LongStringChar").build()).addSymbol(Alt.builder(CharacterRange.builder(1, 1114111).build()).addPostConditions(set(new RegularExpressionCondition(ConditionType.NOT_MATCH, Alt.builder(Character.builder(92).build()).build()))).build()).build())
//			//NonlocalStmt ::= "nonlocal" Name ("," Name)*
//			.addRule(Rule.withHead(new Nonterminal.Builder("NonlocalStmt").build()).addSymbol(Sequence.builder(Character.builder(110).build(), Character.builder(111).build(), Character.builder(110).build(), Character.builder(108).build(), Character.builder(111).build(), Character.builder(99).build(), Character.builder(97).build(), Character.builder(108).build()).build()).addSymbol(new Nonterminal.Builder("Name").build()).addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), new Nonterminal.Builder("Name").build()).build()).build()).build())
//			//ContinueStmt ::= "continue"
//			.addRule(Rule.withHead(new Nonterminal.Builder("ContinueStmt").build()).addSymbol(Sequence.builder(Character.builder(99).build(), Character.builder(111).build(), Character.builder(110).build(), Character.builder(116).build(), Character.builder(105).build(), Character.builder(110).build(), Character.builder(117).build(), Character.builder(101).build()).build()).build())
//			//Argument ::= Test "=" Test
//			.addRule(Rule.withHead(new Nonterminal.Builder("Argument").build()).addSymbol(new Nonterminal.Builder("Test").build()).addSymbol(Sequence.builder(Character.builder(61).build()).build()).addSymbol(new Nonterminal.Builder("Test").build()).build())
//			//Argument ::= Test CompFor?
//			.addRule(Rule.withHead(new Nonterminal.Builder("Argument").build()).addSymbol(new Nonterminal.Builder("Test").build()).addSymbol(org.iguana.regex.Opt.builder(new Nonterminal.Builder("CompFor").build()).build()).build())
//			//PointFloat ::= IntPart "."
//			.addRule(Rule.withHead(new Nonterminal.Builder("PointFloat").build()).addSymbol(new Nonterminal.Builder("IntPart").build()).addSymbol(Sequence.builder(Character.builder(46).build()).build()).build())
//			//PointFloat ::= IntPart? Fraction
//			.addRule(Rule.withHead(new Nonterminal.Builder("PointFloat").build()).addSymbol(org.iguana.regex.Opt.builder(new Nonterminal.Builder("IntPart").build()).build()).addSymbol(new Nonterminal.Builder("Fraction").build()).build())
//			//Test ::= OrTest ("if" OrTest "else" Test)?
//			.addRule(Rule.withHead(new Nonterminal.Builder("Test").build()).addSymbol(new Nonterminal.Builder("OrTest").build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(105).build(), Character.builder(102).build()).build(), new Nonterminal.Builder("OrTest").build(), Sequence.builder(Character.builder(101).build(), Character.builder(108).build(), Character.builder(115).build(), Character.builder(101).build()).build(), new Nonterminal.Builder("Test").build()).build()).build()).build())
//			//Test ::= Lambdef
//			.addRule(Rule.withHead(new Nonterminal.Builder("Test").build()).addSymbol(new Nonterminal.Builder("Lambdef").build()).build())
//			//ShiftExpr ::= ArithExpr (("<<" | ">>") ArithExpr)*
//			.addRule(Rule.withHead(new Nonterminal.Builder("ShiftExpr").build()).addSymbol(new Nonterminal.Builder("ArithExpr").build()).addSymbol(Star.builder(Sequence.builder(Alt.builder(Sequence.builder(Character.builder(60).build(), Character.builder(60).build()).build(), Sequence.builder(Character.builder(62).build(), Character.builder(62).build()).build()).build(), new Nonterminal.Builder("ArithExpr").build()).build()).build()).build())
//			//Exprlist ::= (Expr | StarExpr) ("," (Expr | StarExpr))* (,)?
//			.addRule(Rule.withHead(new Nonterminal.Builder("Exprlist").build()).addSymbol(Alt.builder(new Nonterminal.Builder("Expr").build(), new Nonterminal.Builder("StarExpr").build()).build()).addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), Alt.builder(new Nonterminal.Builder("Expr").build(), new Nonterminal.Builder("StarExpr").build()).build()).build()).build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Character.builder(44).build()).build()).build()).build())
//			//ImportAsName ::= Name ("as" Name)?
//			.addRule(Rule.withHead(new Nonterminal.Builder("ImportAsName").build()).addSymbol(new Nonterminal.Builder("Name").build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(97).build(), Character.builder(115).build()).build(), new Nonterminal.Builder("Name").build()).build()).build()).build())
//			//DecimalInteger ::= (0)+
//			.addRule(Rule.withHead(new Nonterminal.Builder("DecimalInteger").build()).addSymbol(new Plus.Builder(Sequence.builder(Character.builder(48).build()).build()).build()).build())
//			//DecimalInteger ::= NonzeroDigit Digit*
//			.addRule(Rule.withHead(new Nonterminal.Builder("DecimalInteger").build()).addSymbol(new Nonterminal.Builder("NonzeroDigit").build()).addSymbol(Star.builder(new Nonterminal.Builder("Digit").build()).build()).build())
//			//ImportAsNames ::= ImportAsName ("," ImportAsName)* (,)?
//			.addRule(Rule.withHead(new Nonterminal.Builder("ImportAsNames").build()).addSymbol(new Nonterminal.Builder("ImportAsName").build()).addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), new Nonterminal.Builder("ImportAsName").build()).build()).build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Character.builder(44).build()).build()).build()).build())
//			//GlobalStmt ::= "global" Name ("," Name)*
//			.addRule(Rule.withHead(new Nonterminal.Builder("GlobalStmt").build()).addSymbol(Sequence.builder(Character.builder(103).build(), Character.builder(108).build(), Character.builder(111).build(), Character.builder(98).build(), Character.builder(97).build(), Character.builder(108).build()).build()).addSymbol(new Nonterminal.Builder("Name").build()).addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), new Nonterminal.Builder("Name").build()).build()).build()).build())
//			//OctInteger ::= "0" ("o" | "O") OctDigit+
//			.addRule(Rule.withHead(new Nonterminal.Builder("OctInteger").build()).addSymbol(Sequence.builder(Character.builder(48).build()).build()).addSymbol(Alt.builder(Sequence.builder(Character.builder(111).build()).build(), Sequence.builder(Character.builder(79).build()).build()).build()).addSymbol(new Plus.Builder(new Nonterminal.Builder("OctDigit").build()).build()).build())
//			//Decorated ::= Decorators (Classdef | Funcdef)
//			.addRule(Rule.withHead(new Nonterminal.Builder("Decorated").build()).addSymbol(new Nonterminal.Builder("Decorators").build()).addSymbol(Alt.builder(new Nonterminal.Builder("Classdef").build(), new Nonterminal.Builder("Funcdef").build()).build()).build())
//			//Integer ::= BinInteger
//			.addRule(Rule.withHead(new Nonterminal.Builder("Integer").build()).addSymbol(new Nonterminal.Builder("BinInteger").build()).build())
//			//Integer ::= OctInteger
//			.addRule(Rule.withHead(new Nonterminal.Builder("Integer").build()).addSymbol(new Nonterminal.Builder("OctInteger").build()).build())
//			//Integer ::= DecimalInteger
//			.addRule(Rule.withHead(new Nonterminal.Builder("Integer").build()).addSymbol(new Nonterminal.Builder("DecimalInteger").build()).build())
//			//Integer ::= HexInteger
//			.addRule(Rule.withHead(new Nonterminal.Builder("Integer").build()).addSymbol(new Nonterminal.Builder("HexInteger").build()).build())
//			//TestlistComp ::= (Test | StarExpr) (CompFor | (("," (Test | StarExpr))* (,)?))
//			.addRule(Rule.withHead(new Nonterminal.Builder("TestlistComp").build()).addSymbol(Alt.builder(new Nonterminal.Builder("Test").build(), new Nonterminal.Builder("StarExpr").build()).build()).addSymbol(Alt.builder(new Nonterminal.Builder("CompFor").build(), Sequence.builder(Star.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), Alt.builder(new Nonterminal.Builder("Test").build(), new Nonterminal.Builder("StarExpr").build()).build()).build()).build(), org.iguana.regex.Opt.builder(Sequence.builder(Character.builder(44).build()).build()).build()).build()).build()).build())
//			//AssertStmt ::= "assert" Test ("," Test)?
//			.addRule(Rule.withHead(new Nonterminal.Builder("AssertStmt").build()).addSymbol(Sequence.builder(Character.builder(97).build(), Character.builder(115).build(), Character.builder(115).build(), Character.builder(101).build(), Character.builder(114).build(), Character.builder(116).build()).build()).addSymbol(new Nonterminal.Builder("Test").build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), new Nonterminal.Builder("Test").build()).build()).build()).build())
//			//Sliceop ::= ":" Test?
//			.addRule(Rule.withHead(new Nonterminal.Builder("Sliceop").build()).addSymbol(Sequence.builder(Character.builder(58).build()).build()).addSymbol(org.iguana.regex.Opt.builder(new Nonterminal.Builder("Test").build()).build()).build())
//			//YieldArg ::= "from" Test
//			.addRule(Rule.withHead(new Nonterminal.Builder("YieldArg").build()).addSymbol(Sequence.builder(Character.builder(102).build(), Character.builder(114).build(), Character.builder(111).build(), Character.builder(109).build()).build()).addSymbol(new Nonterminal.Builder("Test").build()).build())
//			//YieldArg ::= TestList
//			.addRule(Rule.withHead(new Nonterminal.Builder("YieldArg").build()).addSymbol(new Nonterminal.Builder("TestList").build()).build())
//			//BreakStmt ::= "break"
//			.addRule(Rule.withHead(new Nonterminal.Builder("BreakStmt").build()).addSymbol(Sequence.builder(Character.builder(98).build(), Character.builder(114).build(), Character.builder(101).build(), Character.builder(97).build(), Character.builder(107).build()).build()).build())
//			//LongBytesItem ::= BytesEscapeSeq
//			.addRule(Rule.withHead(new Nonterminal.Builder("LongBytesItem").build()).addSymbol(new Nonterminal.Builder("BytesEscapeSeq").build()).build())
//			//LongBytesItem ::= LongBytesChar
//			.addRule(Rule.withHead(new Nonterminal.Builder("LongBytesItem").build()).addSymbol(new Nonterminal.Builder("LongBytesChar").build()).build())
//
//			/**
//			 * Data-dependent
//			 *
//			 * @layout(NoNL)
//			 * IfStmt ::= a:"if" Test ":" Suite(a.lExt) (b:"elif" Test ":" Suite(b.lExt))* (c:"else" ":" Suite(c.lExt))?
//			 */
//			.addRule(Rule.withHead(new Nonterminal.Builder("IfStmt").build())
//									.addSymbol(Sequence.builder(Character.builder(105).build(), Character.builder(102).build()).setLabel("a").build())
//									.addSymbol(new Nonterminal.Builder("Test").build())
//									.addSymbol(Sequence.builder(Character.builder(58).build()).build())
//									.addSymbol(new Nonterminal.Builder("Suite").apply(lExt("a")).build())
//									.addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(101).build(), Character.builder(108).build(), Character.builder(105).build(), Character.builder(102).build()).setLabel("b").build(),
//																							  new Nonterminal.Builder("Test").build(),
//																							  Sequence.builder(Character.builder(58).build()).build(),
//																							  new Nonterminal.Builder("Suite").apply(lExt("b")).build()).build()).build())
//									.addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(101).build(), Character.builder(108).build(), Character.builder(115).build(), Character.builder(101).build()).setLabel("c").build(),
//																						   Sequence.builder(Character.builder(58).build()).build(),
//																						   new Nonterminal.Builder("Suite").apply(lExt("c")).build()).build()).build())
//
//							.setLayout(new Nonterminal.Builder("NoNL").build()).build())
//
//			//ReturnStmt ::= "return" TestList?
//			.addRule(Rule.withHead(new Nonterminal.Builder("ReturnStmt").build()).addSymbol(Sequence.builder(Character.builder(114).build(), Character.builder(101).build(), Character.builder(116).build(), Character.builder(117).build(), Character.builder(114).build(), Character.builder(110).build()).build()).addSymbol(org.iguana.regex.Opt.builder(new Nonterminal.Builder("TestList").build()).build()).build())
//			//Vfpdef ::= Name
//			.addRule(Rule.withHead(new Nonterminal.Builder("Vfpdef").build()).addSymbol(new Nonterminal.Builder("Name").build()).build())
//
//			/**
//			 * Data-dependent
//			 *
//			 * @layout(NoNL)
//			 * Suite(lExt) ::= SimpleStmt
//			 *               | NL a:(b:Stmt [indent(a.lExt) == indent(b.lExt)])+ [indent(lExt) < indent(a.lExt)]
//			 */
//			.addRule(Rule.withHead(new Nonterminal.Builder("Suite").addParameters("lExt").build()).addSymbol(new Nonterminal.Builder("SimpleStmt").build()).build())
//
//			.addRule(Rule.withHead(new Nonterminal.Builder("Suite").addParameters("lExt").build())
//									.addSymbol(new Nonterminal.Builder("NL").build())
//									.addSymbol(new Plus.Builder(new Nonterminal.Builder("Stmt").setLabel("b")
//																.addPreCondition(predicate(equal(indent(lExt("a")), indent(lExt("b"))))).build()).setLabel("a")
//													.addPreCondition(predicate(less(indent(var("lExt")), indent(lExt("a"))))).build())
//						.setLayout(new Nonterminal.Builder("NoNL").build()).build())
//
//
//			//TestNocond ::= OrTest
//			.addRule(Rule.withHead(new Nonterminal.Builder("TestNocond").build()).addSymbol(new Nonterminal.Builder("OrTest").build()).build())
//			//TestNocond ::= LambdefNocond
//			.addRule(Rule.withHead(new Nonterminal.Builder("TestNocond").build()).addSymbol(new Nonterminal.Builder("LambdefNocond").build()).build())
//			//EvalInput ::= TestList NewLine*
//			.addRule(Rule.withHead(new Nonterminal.Builder("EvalInput").build()).addSymbol(new Nonterminal.Builder("TestList").build()).addSymbol(Star.builder(new Nonterminal.Builder("NewLine").build()).build()).build())
//			//NotTest ::= Comparison
//			.addRule(Rule.withHead(new Nonterminal.Builder("NotTest").build()).addSymbol(new Nonterminal.Builder("Comparison").build()).build())
//			//NotTest ::= "not" NotTest
//			.addRule(Rule.withHead(new Nonterminal.Builder("NotTest").build()).addSymbol(Sequence.builder(Character.builder(110).build(), Character.builder(111).build(), Character.builder(116).build()).build()).addSymbol(new Nonterminal.Builder("NotTest").build()).build())
//			//CompOp ::= "!="
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompOp").build()).addSymbol(Sequence.builder(Character.builder(33).build(), Character.builder(61).build()).build()).build())
//			//CompOp ::= "<"
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompOp").build()).addSymbol(Sequence.builder(Character.builder(60).build()).build()).build())
//			//CompOp ::= ">"
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompOp").build()).addSymbol(Sequence.builder(Character.builder(62).build()).build()).build())
//			//CompOp ::= "not" "in"
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompOp").build()).addSymbol(Sequence.builder(Character.builder(110).build(), Character.builder(111).build(), Character.builder(116).build()).build()).addSymbol(Sequence.builder(Character.builder(105).build(), Character.builder(110).build()).build()).build())
//			//CompOp ::= "is" "not"
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompOp").build()).addSymbol(Sequence.builder(Character.builder(105).build(), Character.builder(115).build()).build()).addSymbol(Sequence.builder(Character.builder(110).build(), Character.builder(111).build(), Character.builder(116).build()).build()).build())
//			//CompOp ::= "in"
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompOp").build()).addSymbol(Sequence.builder(Character.builder(105).build(), Character.builder(110).build()).build()).build())
//			//CompOp ::= "is"
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompOp").build()).addSymbol(Sequence.builder(Character.builder(105).build(), Character.builder(115).build()).build()).build())
//			//CompOp ::= ">="
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompOp").build()).addSymbol(Sequence.builder(Character.builder(62).build(), Character.builder(61).build()).build()).build())
//			//CompOp ::= "=="
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompOp").build()).addSymbol(Sequence.builder(Character.builder(61).build(), Character.builder(61).build()).build()).build())
//			//CompOp ::= "<="
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompOp").build()).addSymbol(Sequence.builder(Character.builder(60).build(), Character.builder(61).build()).build()).build())
//			//CompOp ::= "<>"
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompOp").build()).addSymbol(Sequence.builder(Character.builder(60).build(), Character.builder(62).build()).build()).build())
//			//RaiseStmt ::= "raise" (Test ("from" Test)?)?
//			.addRule(Rule.withHead(new Nonterminal.Builder("RaiseStmt").build()).addSymbol(Sequence.builder(Character.builder(114).build(), Character.builder(97).build(), Character.builder(105).build(), Character.builder(115).build(), Character.builder(101).build()).build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(new Nonterminal.Builder("Test").build(), org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(102).build(), Character.builder(114).build(), Character.builder(111).build(), Character.builder(109).build()).build(), new Nonterminal.Builder("Test").build()).build()).build()).build()).build()).build())
//			//StringEscapeSeq ::= \ u HexInteger HexInteger HexInteger HexInteger HexInteger HexInteger HexInteger HexInteger
//			.addRule(Rule.withHead(new Nonterminal.Builder("StringEscapeSeq").build()).addSymbol(Character.builder(92).build()).addSymbol(Character.builder(117).build()).addSymbol(new Nonterminal.Builder("HexInteger").build()).addSymbol(new Nonterminal.Builder("HexInteger").build()).addSymbol(new Nonterminal.Builder("HexInteger").build()).addSymbol(new Nonterminal.Builder("HexInteger").build()).addSymbol(new Nonterminal.Builder("HexInteger").build()).addSymbol(new Nonterminal.Builder("HexInteger").build()).addSymbol(new Nonterminal.Builder("HexInteger").build()).addSymbol(new Nonterminal.Builder("HexInteger").build()).build())
//			//StringEscapeSeq ::= \ u HexInteger HexInteger HexInteger HexInteger
//			.addRule(Rule.withHead(new Nonterminal.Builder("StringEscapeSeq").build()).addSymbol(Character.builder(92).build()).addSymbol(Character.builder(117).build()).addSymbol(new Nonterminal.Builder("HexInteger").build()).addSymbol(new Nonterminal.Builder("HexInteger").build()).addSymbol(new Nonterminal.Builder("HexInteger").build()).addSymbol(new Nonterminal.Builder("HexInteger").build()).build())
//			//StringEscapeSeq ::= EscapeSeq
//			.addRule(Rule.withHead(new Nonterminal.Builder("StringEscapeSeq").build()).addSymbol(new Nonterminal.Builder("EscapeSeq").build()).build())
//			//AndExpr ::= ShiftExpr ("&" ShiftExpr)*
//			.addRule(Rule.withHead(new Nonterminal.Builder("AndExpr").build()).addSymbol(new Nonterminal.Builder("ShiftExpr").build()).addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(38).build()).build(), new Nonterminal.Builder("ShiftExpr").build()).build()).build()).build())
//			//XorExpr ::= AndExpr ("^" AndExpr)*
//			.addRule(Rule.withHead(new Nonterminal.Builder("XorExpr").build()).addSymbol(new Nonterminal.Builder("AndExpr").build()).addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(94).build()).build(), new Nonterminal.Builder("AndExpr").build()).build()).build()).build())
//			//FileInput ::= (NewLine | Stmt)*
//			.addRule(Rule.withHead(new Nonterminal.Builder("FileInput").build()).addSymbol(Star.builder(Alt.builder(new Nonterminal.Builder("NewLine").build(), new Nonterminal.Builder("Stmt").build()).build()).build()).build())
//			//String ::= StringLiteral
//			.addRule(Rule.withHead(new Nonterminal.Builder("String").build()).addSymbol(new Nonterminal.Builder("StringLiteral").build()).build())
//			//String ::= BytesLiteral
//			.addRule(Rule.withHead(new Nonterminal.Builder("String").build()).addSymbol(new Nonterminal.Builder("BytesLiteral").build()).build())
//			// ::=
//			.addRule(Rule.withHead(new Nonterminal.Builder("").build()).build())
//			//TypedArgsList ::= "*" Tfpdef? ("," Tfpdef ("=" Test)?)* ("," "**" Tfpdef)?
//			.addRule(Rule.withHead(new Nonterminal.Builder("TypedArgsList").build()).addSymbol(Sequence.builder(Character.builder(42).build()).build()).addSymbol(org.iguana.regex.Opt.builder(new Nonterminal.Builder("Tfpdef").build()).build()).addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), new Nonterminal.Builder("Tfpdef").build(), org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(61).build()).build(), new Nonterminal.Builder("Test").build()).build()).build()).build()).build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), Sequence.builder(Character.builder(42).build(), Character.builder(42).build()).build(), new Nonterminal.Builder("Tfpdef").build()).build()).build()).build())
//			//TypedArgsList ::= Tfpdef ("=" Test)? ("," Tfpdef ("=" Test)?)* ("," (("**" Tfpdef) | ("*" Tfpdef? ("," Tfpdef ("=" Test)?)* ("," "**" Tfpdef)?))?)?
//			.addRule(Rule.withHead(new Nonterminal.Builder("TypedArgsList").build()).addSymbol(new Nonterminal.Builder("Tfpdef").build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(61).build()).build(), new Nonterminal.Builder("Test").build()).build()).build()).addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), new Nonterminal.Builder("Tfpdef").build(), org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(61).build()).build(), new Nonterminal.Builder("Test").build()).build()).build()).build()).build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), org.iguana.regex.Opt.builder(Alt.builder(Sequence.builder(Sequence.builder(Character.builder(42).build(), Character.builder(42).build()).build(), new Nonterminal.Builder("Tfpdef").build()).build(), Sequence.builder(Sequence.builder(Character.builder(42).build()).build(), org.iguana.regex.Opt.builder(new Nonterminal.Builder("Tfpdef").build()).build(), Star.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), new Nonterminal.Builder("Tfpdef").build(), org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(61).build()).build(), new Nonterminal.Builder("Test").build()).build()).build()).build()).build(), org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), Sequence.builder(Character.builder(42).build(), Character.builder(42).build()).build(), new Nonterminal.Builder("Tfpdef").build()).build()).build()).build()).build()).build()).build()).build()).build())
//			//TypedArgsList ::= "**" Tfpdef
//			.addRule(Rule.withHead(new Nonterminal.Builder("TypedArgsList").build()).addSymbol(Sequence.builder(Character.builder(42).build(), Character.builder(42).build()).build()).addSymbol(new Nonterminal.Builder("Tfpdef").build()).build())
//			//Comparison ::= Expr (CompOp Expr)*
//			.addRule(Rule.withHead(new Nonterminal.Builder("Comparison").build()).addSymbol(new Nonterminal.Builder("Expr").build()).addSymbol(Star.builder(Sequence.builder(new Nonterminal.Builder("CompOp").build(), new Nonterminal.Builder("Expr").build()).build()).build()).build())
//			//ExceptClause ::= "except" (Test ("as" Name)?)?
//			.addRule(Rule.withHead(new Nonterminal.Builder("ExceptClause").build()).addSymbol(Sequence.builder(Character.builder(101).build(), Character.builder(120).build(), Character.builder(99).build(), Character.builder(101).build(), Character.builder(112).build(), Character.builder(116).build()).build()).addSymbol(org.iguana.regex.Opt.builder(Sequence.builder(new Nonterminal.Builder("Test").build(), org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(97).build(), Character.builder(115).build()).build(), new Nonterminal.Builder("Name").build()).build()).build()).build()).build()).build())
//			//LongBytes ::= """"" LongBytesItem* """""
//			.addRule(Rule.withHead(new Nonterminal.Builder("LongBytes").build()).addSymbol(Sequence.builder(Character.builder(34).build(), Character.builder(34).build(), Character.builder(34).build()).build()).addSymbol(Star.builder(new Nonterminal.Builder("LongBytesItem").build()).build()).addSymbol(Sequence.builder(Character.builder(34).build(), Character.builder(34).build(), Character.builder(34).build()).build()).build())
//			//LongBytes ::= "'''" LongBytesItem* "'''"
//			.addRule(Rule.withHead(new Nonterminal.Builder("LongBytes").build()).addSymbol(Sequence.builder(Character.builder(39).build(), Character.builder(39).build(), Character.builder(39).build()).build()).addSymbol(Star.builder(new Nonterminal.Builder("LongBytesItem").build()).build()).addSymbol(Sequence.builder(Character.builder(39).build(), Character.builder(39).build(), Character.builder(39).build()).build()).build())
//			//ImportName ::= "import" DottedAsNames
//			.addRule(Rule.withHead(new Nonterminal.Builder("ImportName").build()).addSymbol(Sequence.builder(Character.builder(105).build(), Character.builder(109).build(), Character.builder(112).build(), Character.builder(111).build(), Character.builder(114).build(), Character.builder(116).build()).build()).addSymbol(new Nonterminal.Builder("DottedAsNames").build()).build())
//			//OctDigit ::= (0-7)
//			.addRule(Rule.withHead(new Nonterminal.Builder("OctDigit").build()).addSymbol(Alt.builder(CharacterRange.builder(48, 55).build()).build()).build())
//			//Arglist ::= (Argument ",")* ((Argument (,)?) | ("*" Test ("," Argument)* ("," "**" Test)?) | ("**" Test))
//			.addRule(Rule.withHead(new Nonterminal.Builder("Arglist").build()).addSymbol(Star.builder(Sequence.builder(new Nonterminal.Builder("Argument").build(), Sequence.builder(Character.builder(44).build()).build()).build()).build()).addSymbol(Alt.builder(Sequence.builder(new Nonterminal.Builder("Argument").build(), org.iguana.regex.Opt.builder(Sequence.builder(Character.builder(44).build()).build()).build()).build(), Sequence.builder(Sequence.builder(Character.builder(42).build()).build(), new Nonterminal.Builder("Test").build(), Star.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), new Nonterminal.Builder("Argument").build()).build()).build(), org.iguana.regex.Opt.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), Sequence.builder(Character.builder(42).build(), Character.builder(42).build()).build(), new Nonterminal.Builder("Test").build()).build()).build()).build(), Sequence.builder(Sequence.builder(Character.builder(42).build(), Character.builder(42).build()).build(), new Nonterminal.Builder("Test").build()).build()).build()).build())
//			//BinDigit ::= "0"
//			.addRule(Rule.withHead(new Nonterminal.Builder("BinDigit").build()).addSymbol(Sequence.builder(Character.builder(48).build()).build()).build())
//			//BinDigit ::= "1"
//			.addRule(Rule.withHead(new Nonterminal.Builder("BinDigit").build()).addSymbol(Sequence.builder(Character.builder(49).build()).build()).build())
//			//DottedName ::= Name ("." Name)*
//			.addRule(Rule.withHead(new Nonterminal.Builder("DottedName").build()).addSymbol(new Nonterminal.Builder("Name").build()).addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(46).build()).build(), new Nonterminal.Builder("Name").build()).build()).build()).build())
//			//SingleInput ::= NewLine
//			.addRule(Rule.withHead(new Nonterminal.Builder("SingleInput").build()).addSymbol(new Nonterminal.Builder("NewLine").build()).build())
//			//SingleInput ::= CompoundStmt NewLine
//			.addRule(Rule.withHead(new Nonterminal.Builder("SingleInput").build()).addSymbol(new Nonterminal.Builder("CompoundStmt").build()).addSymbol(new Nonterminal.Builder("NewLine").build()).build())
//			//SingleInput ::= SimpleStmt
//			.addRule(Rule.withHead(new Nonterminal.Builder("SingleInput").build()).addSymbol(new Nonterminal.Builder("SimpleStmt").build()).build())
//			//ShortStringChar ::= (\u0001-\u10FFFF)
//			.addRule(Rule.withHead(new Nonterminal.Builder("ShortStringChar").build()).addSymbol(Alt.builder(CharacterRange.builder(1, 1114111).build()).addPostConditions(set(new RegularExpressionCondition(ConditionType.NOT_MATCH, Alt.builder(Character.builder(10).build()).build()))).build()).build())
//			//WithStmt ::= "with" WithItem ("," WithItem)* ":" Suite
//			.addRule(Rule.withHead(new Nonterminal.Builder("WithStmt").build()).addSymbol(Sequence.builder(Character.builder(119).build(), Character.builder(105).build(), Character.builder(116).build(), Character.builder(104).build()).build()).addSymbol(new Nonterminal.Builder("WithItem").build()).addSymbol(Star.builder(Sequence.builder(Sequence.builder(Character.builder(44).build()).build(), new Nonterminal.Builder("WithItem").build()).build()).build()).addSymbol(Sequence.builder(Character.builder(58).build()).build()).addSymbol(new Nonterminal.Builder("Suite").build()).build())
//			//ExponentFloat ::= (IntPart | PointFloat) Exponent
//			.addRule(Rule.withHead(new Nonterminal.Builder("ExponentFloat").build()).addSymbol(Alt.builder(new Nonterminal.Builder("IntPart").build(), new Nonterminal.Builder("PointFloat").build()).build()).addSymbol(new Nonterminal.Builder("Exponent").build()).build())
//			//Trailer ::= "(" Arglist? ")"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Trailer").build()).addSymbol(Sequence.builder(Character.builder(40).build()).build()).addSymbol(org.iguana.regex.Opt.builder(new Nonterminal.Builder("Arglist").build()).build()).addSymbol(Sequence.builder(Character.builder(41).build()).build()).build())
//			//Trailer ::= "[" Subscriptlist "]"
//			.addRule(Rule.withHead(new Nonterminal.Builder("Trailer").build()).addSymbol(Sequence.builder(Character.builder(91).build()).build()).addSymbol(new Nonterminal.Builder("Subscriptlist").build()).addSymbol(Sequence.builder(Character.builder(93).build()).build()).build())
//			//Trailer ::= "." Name
//			.addRule(Rule.withHead(new Nonterminal.Builder("Trailer").build()).addSymbol(Sequence.builder(Character.builder(46).build()).build()).addSymbol(new Nonterminal.Builder("Name").build()).build())
//			//CompFor ::= "for" Exprlist "in" OrTest CompIter?
//			.addRule(Rule.withHead(new Nonterminal.Builder("CompFor").build()).addSymbol(Sequence.builder(Character.builder(102).build(), Character.builder(111).build(), Character.builder(114).build()).build()).addSymbol(new Nonterminal.Builder("Exprlist").build()).addSymbol(Sequence.builder(Character.builder(105).build(), Character.builder(110).build()).build()).addSymbol(new Nonterminal.Builder("OrTest").build()).addSymbol(org.iguana.regex.Opt.builder(new Nonterminal.Builder("CompIter").build()).build()).build())
//			//Lambdef ::= "lambda" Varargslist? ":" Test
//			.addRule(Rule.withHead(new Nonterminal.Builder("Lambdef").build()).addSymbol(Sequence.builder(Character.builder(108).build(), Character.builder(97).build(), Character.builder(109).build(), Character.builder(98).build(), Character.builder(100).build(), Character.builder(97).build()).build()).addSymbol(org.iguana.regex.Opt.builder(new Nonterminal.Builder("Varargslist").build()).build()).addSymbol(Sequence.builder(Character.builder(58).build()).build()).addSymbol(new Nonterminal.Builder("Test").build()).build())
//			//ExprStmt ::= TestlistStarExpr ((Augassign (TestList | YieldExpr)) | ("=" (TestlistStarExpr | YieldExpr))*)
//			.addRule(Rule.withHead(new Nonterminal.Builder("ExprStmt").build()).addSymbol(new Nonterminal.Builder("TestlistStarExpr").build()).addSymbol(Alt.builder(Sequence.builder(new Nonterminal.Builder("Augassign").build(), Alt.builder(new Nonterminal.Builder("TestList").build(), new Nonterminal.Builder("YieldExpr").build()).build()).build(), Star.builder(Sequence.builder(Sequence.builder(Character.builder(61).build()).build(), Alt.builder(new Nonterminal.Builder("TestlistStarExpr").build(), new Nonterminal.Builder("YieldExpr").build()).build()).build()).build()).build()).build())
//			//Name ::= (A-Z | _ | a-z) (0-9 | A-Z | _ | a-z)*
//			.addRule(Rule.withHead(new Nonterminal.Builder("Name").build()).addSymbol(Alt.builder(CharacterRange.builder(65, 90).build(), CharacterRange.builder(95, 95).build(), CharacterRange.builder(97, 122).build()).build()).addSymbol(Star.builder(Alt.builder(CharacterRange.builder(48, 57).build(), CharacterRange.builder(65, 90).build(), CharacterRange.builder(95, 95).build(), CharacterRange.builder(97, 122).build()).build()).build()).build())
//
//
//			/**
//			 * Data-dependent
//			 *
//			 *  NoNL ::= (' ' | '\t')* !>> (' ' | '\t')
//			 */
//			.addRule(Rule.withHead(new Nonterminal.Builder("NoNL").build())
//						.addSymbol(Star.builder(Alt.from(Character.from(' '), Character.from('\t')))
//								.addPostCondition(RegularExpressionCondition.notFollow(Character.from(' ')))
//								.addPostCondition(RegularExpressionCondition.notFollow(Character.from('\t'))).build()).build())
//
//			/**
//			 *  L ::= (' ' | '\t' | '\r' | '\n')* !>> (' ' | '\t' | '\r' | '\n')
//			 */
//			.addRule(Rule.withHead(new Nonterminal.Builder("L").build())
//						.addSymbol(Star.builder(Alt.from(Character.from(' '), Character.from('\t'), Character.from('\r'), Character.from('\n')))
//								.addPostCondition(RegularExpressionCondition.notFollow(Character.from(' ')))
//								.addPostCondition(RegularExpressionCondition.notFollow(Character.from('\t')))
//								.addPostCondition(RegularExpressionCondition.notFollow(Character.from('\r')))
//								.addPostCondition(RegularExpressionCondition.notFollow(Character.from('\n'))).build()).build())
//
//			/**
//			 ********** LOGICAL NEW LINE:
//			 *
//			 * NL ::= ('\n' | '\r') !>> ('\n' | '\r')
//			 *      | ('\n' | '\r') (' ' | '\t' | '\n' | '\r')* ('\n' | '\r') !>> ('\n' | '\r')
//			 */
//			.addRule(Rule.withHead(new Nonterminal.Builder("NL").build())
//						.addSymbol(Alt.builder(Character.from('\n'), Character.from('\r'))
//										.addPostCondition(RegularExpressionCondition.notFollow(Character.from('\n')))
//										.addPostCondition(RegularExpressionCondition.notFollow(Character.from('\r'))).build()).build())
//			.addRule(Rule.withHead(new Nonterminal.Builder("NL").build())
//						.addSymbol(Alt.from(Character.from('\n'), Character.from('\r')))
//						.addSymbol(Star.from(Alt.from(Character.from(' '), Character.from('\t'), Character.from('\n'), Character.from('\r'))))
//						.addSymbol(Alt.builder(Character.from('\n'), Character.from('\r'))
//										.addPostCondition(RegularExpressionCondition.notFollow(Character.from('\n')))
//										.addPostCondition(RegularExpressionCondition.notFollow(Character.from('\r'))).build()).build())
//
//			.build();
//
//
//	@Test
//	public void test() {
//
//		Input input = Input.fromString("if a  :  "   + "\n"
//				                     + "    "        + "\n"
//				                     + "  "          + "\n"
//				                     + "   x=0"      + "\n"
//				                     + "        "    + "\n"
//				                     + "  "          + "\n"
//				                     + "   y=0  "    + "\n"
//				                     + "   z=0   "   + "\n"
//				                     + "   if b: "   + "\n"
//				                     + "    "        + "\n"
//				                     + "         "   + "\n"
//				                     + "      l=0"   + "\n"
//				                     + "        "    + "\n"
//				                     + "  "          + "\n"
//				                     + "      k=0  " + "\n"
//				                     + "   w=0   "   + "\n");
//
//		GrammarGraph graph = GrammarGraph.from(grammar, input, Configuration.DEFAULT);
//		ParseResult result = IguanaParser.getParserTree(input, graph, Nonterminal.withName("IfStmt"));
//	}
//
//}
