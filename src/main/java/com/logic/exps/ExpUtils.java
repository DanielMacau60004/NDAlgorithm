package com.logic.exps;

import com.logic.api.IFormula;
import com.logic.exps.asts.IASTExp;
import com.logic.exps.asts.PLExp;
import com.logic.exps.asts.binary.ASTAnd;
import com.logic.exps.asts.binary.ASTBiconditional;
import com.logic.exps.asts.binary.ASTConditional;
import com.logic.exps.asts.binary.ASTOr;
import com.logic.exps.asts.others.ASTBottom;
import com.logic.exps.asts.others.ASTLiteral;
import com.logic.exps.asts.unary.ASTNot;
import com.logic.exps.asts.unary.ASTParenthesis;
import com.logic.exps.interpreters.FOLWFFInterpreter;

public class ExpUtils {

    public static final IASTExp BOT = new ASTBottom();
    public static final IFormula BOTF = FOLWFFInterpreter.check(new ASTBottom());

    public static boolean isLiteral(IASTExp exp) {
        if(exp instanceof ASTLiteral) return true;
        if(exp instanceof ASTNot not) return  isLiteral(not.getExp());
        return false;
    }

    public static IASTExp negate(IASTExp exp) {
        if (exp instanceof ASTOr || exp instanceof ASTAnd || exp instanceof ASTConditional
                || exp instanceof ASTBiconditional)
            return new ASTNot(new ASTParenthesis(exp));
        return new ASTNot(exp);
    }

    public static IASTExp invert(IASTExp exp) {
        exp = removeParenthesis(exp);
        if (exp instanceof ASTNot not)
            return removeParenthesis(not.getExp());
        return negate(exp);
    }

    public static IASTExp removeParenthesis(IASTExp exp) {
        if (exp instanceof ASTParenthesis par)
            exp = par.getExp();
        return exp;
    }

}
