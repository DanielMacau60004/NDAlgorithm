package com.logic.nd;

import com.logic.api.IFormula;
import com.logic.api.INDProof;
import com.logic.exps.asts.IASTExp;
import com.logic.nd.asts.IASTND;
import com.logic.nd.checkers.NDMarksChecker;
import com.logic.nd.checkers.NDSideCondChecker;
import com.logic.nd.checkers.NDWWFChecker;
import com.logic.nd.checkers.NDWWFExpsChecker;
import com.logic.nd.exceptions.NDException;
import com.logic.nd.exceptions.NDLexicalException;
import com.logic.nd.exceptions.NDSyntaxException;
import com.logic.nd.interpreters.NDInterpretProblem;
import com.logic.nd.interpreters.NDInterpreter;
import com.logic.others.Utils;
import com.logic.parser.ParseException;
import com.logic.parser.Parser;
import com.logic.parser.TokenMgrError;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NDProofs {

    public static INDProof verifyNDPLProof(IASTND proof) throws NDException {
        Map<IASTExp, IFormula> formulas = NDWWFExpsChecker.checkPL(proof);
        NDWWFChecker.check(proof, formulas);

        Map<String, IASTExp> premises = new HashMap<>();
        Map<String, IASTExp> hypotheses = new HashMap<>();
        NDMarksChecker.check(proof, formulas, premises, hypotheses);

        return NDInterpreter.interpret(proof, formulas, premises, hypotheses);
    }

    public static INDProof parseNDPLProof(String expression) throws NDException {
        try {
            IASTND proof = new Parser(new ByteArrayInputStream(expression.getBytes())).parseNDPL();
            return verifyNDPLProof(proof);
        } catch (ParseException e) {
            throw new NDSyntaxException(e);
        } catch (TokenMgrError e) {
            throw new NDLexicalException(e);
        }
    }

    public static INDProof verifyNDFOLProof(IASTND proof) throws NDException {
        Map<IASTExp, IFormula> formulas = NDWWFExpsChecker.checkFOL(proof);
        NDWWFChecker.check(proof, formulas);

        Map<String, IASTExp> premises = new HashMap<>();
        Map<String, IASTExp> hypotheses = new HashMap<>();
        NDMarksChecker.check(proof, formulas, premises, hypotheses);

        NDSideCondChecker.check(proof, formulas, premises);

        return NDInterpreter.interpret(proof, formulas, premises, hypotheses);
    }

    public static INDProof parseNDFOLProof(String expression) throws NDException {
        try {
            IASTND proof = new Parser(new ByteArrayInputStream(expression.getBytes())).parseNDFOL();
            return verifyNDFOLProof(proof);
        } catch (ParseException e) {
            throw new NDSyntaxException(e);
        } catch (TokenMgrError e) {
            throw new NDLexicalException(e);
        }
    }

    public static INDProof checkNDProblem(INDProof proof, Set<IFormula> premises, IFormula conclusion) throws NDException {
        return NDInterpretProblem.solve(proof, premises, conclusion);
    }

}
