package com.logic.exps.exceptions;

import com.logic.feedback.FeedbackException;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.parser.ParseException;
import com.logic.parser.ParserConstants;
import com.logic.parser.Token;

import java.util.Map;

import static com.logic.parser.ParserConstants.EOF;
import static com.logic.parser.ParserConstants.tokenImage;

public class ExpSyntaxException extends ExpException {

    private final Map<Integer, String> tokensMapping = Map.of(
            ParserConstants.LITERAL, "literal",
            ParserConstants.GENERIC, "generic",
            ParserConstants.VARIABLE, "variable",
            ParserConstants.PREDICATE, "predicate",
            ParserConstants.FUNCTION, "function");

    private final ParseException exception;

    public ExpSyntaxException(ParseException exception) {
        super(FeedbackType.SYNTAX_ERROR);

        this.exception = exception;
    }

    private String expected(boolean showExpected) {
        Token currentToken = exception.currentToken;
        int[][] array = exception.expectedTokenSequences;
        StringBuilder expected = new StringBuilder();

        int maxSize = 0;
        for (int[] cArray : array) {
            if (maxSize < cArray.length)
                maxSize = cArray.length;

            for (int token : cArray) {
                if (token != ParserConstants.DOT && token != EOF) {
                    if(!expected.isEmpty()) expected.append(", ");
                    if(tokensMapping.containsKey(token)) expected.append(tokensMapping.get(token));
                    else expected.append(tokenImage[token]);
                }
            }
        }

        StringBuilder retVal = new StringBuilder("Syntax error at column ")
                .append(currentToken.next.beginColumn).append(".");

        if(showExpected)
            retVal.append(" Expecting: ").append(expected);

        return retVal.toString();
    }

    protected String produceFeedback(FeedbackLevel level) {
        return switch (level) {
            case NONE -> "";
            case LOW -> "Invalid expression!";
            case MEDIUM -> "Syntax error!";
            case HIGH -> expected(false);
            case SOLUTION -> expected(true);
        };
    }
}
