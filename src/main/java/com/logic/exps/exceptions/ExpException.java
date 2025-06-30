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

public abstract class ExpException extends FeedbackException {

    public ExpException(FeedbackType type) {
        super(type);
    }
}
