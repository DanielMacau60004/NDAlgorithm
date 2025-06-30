package com.logic.nd.exceptions;

import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.FeedbackType;
import com.logic.parser.ParseException;
import com.logic.parser.ParserConstants;
import com.logic.parser.Token;

import static com.logic.parser.ParserConstants.EOF;
import static com.logic.parser.ParserConstants.tokenImage;

public class NDSyntaxException extends NDRuleException {

    private final ParseException exception;

    public NDSyntaxException(ParseException exception) {
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
                    expected.append(tokenImage[token]);
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
            case LOW -> "Invalid proof!";
            case MEDIUM -> "Syntax error!";
            case HIGH -> expected(false);
            case SOLUTION -> expected(true);
        };
    }
}
