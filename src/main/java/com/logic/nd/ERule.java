package com.logic.nd;

import com.logic.others.Utils;
import com.logic.parser.Parser;
import com.logic.parser.ParserConstants;

public enum ERule {

    INTRO_CONJUNCTION(ParserConstants.IAND),
    ELIM_CONJUNCTION_LEFT(ParserConstants.ELAND),
    ELIM_CONJUNCTION_RIGHT(ParserConstants.ERAND),
    ELIM_IMPLICATION(ParserConstants.ECOND),
    INTRO_DISJUNCTION_LEFT(ParserConstants.ILOR),
    INTRO_DISJUNCTION_RIGHT(ParserConstants.IROR),
    INTRO_NEGATION(ParserConstants.INEG),
    ELIM_NEGATION(ParserConstants.ENEG),
    ELIM_DISJUNCTION(ParserConstants.EOR),
    INTRO_IMPLICATION(ParserConstants.ICOND),
    INTRO_UNIVERSAL(ParserConstants.IUNI),
    ELIM_UNIVERSAL(ParserConstants.EUNI),
    INTRO_EXISTENTIAL(ParserConstants.IEXIST),
    ELIM_EXISTENTIAL(ParserConstants.EEXIST),
    ABSURDITY(ParserConstants.BOTTOM),
    HYPOTHESIS(ParserConstants.HYPOTHESIS);

    private final int kind;

    ERule(int kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return Utils.getToken(Parser.tokenImage[kind].replace("\"", ""));
    }
}
