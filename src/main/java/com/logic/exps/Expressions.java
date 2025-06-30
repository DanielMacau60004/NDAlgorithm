package com.logic.exps;

import com.logic.api.IFOLFormula;
import com.logic.api.IPLFormula;
import com.logic.exps.asts.others.ASTLiteral;
import com.logic.exps.interpreters.FOLWFFInterpreter;
import com.logic.exps.interpreters.PLWFFInterpreter;
import com.logic.exps.exceptions.ExpException;
import com.logic.exps.exceptions.ExpLexicalException;
import com.logic.exps.exceptions.ExpSyntaxException;
import com.logic.parser.ParseException;
import com.logic.parser.Parser;
import com.logic.parser.TokenMgrError;

import java.io.ByteArrayInputStream;

public class Expressions {

    public static IPLFormula parsePLFormula(String formula) throws ExpException {
        try {
            Parser parser = new Parser(new ByteArrayInputStream(formula.getBytes()));
            return PLWFFInterpreter.check(parser.parsePL());
        } catch (ParseException e) {
            throw new ExpSyntaxException(e);
        } catch (TokenMgrError e) {
            throw new ExpLexicalException(e);
        }
    }

    public static IFOLFormula parseFOLFormula(String formula) throws ExpException {
        try {
            Parser parser = new Parser(new ByteArrayInputStream(formula.getBytes()));
            return FOLWFFInterpreter.check(parser.parseFOL());
        } catch (ParseException e) {
            throw new ExpSyntaxException(e);
        } catch (TokenMgrError e) {
            throw new ExpLexicalException(e);
        }
    }

}
