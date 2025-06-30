package com.logic.others;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class AlphabetSequenceIterator implements Iterator<String> {
    private char currentChar = 'a';
    private int currentSuffix = 0;
    private final int maxSuffix;

    public AlphabetSequenceIterator(int maxSuffix) {
        this.maxSuffix = maxSuffix;
    }

    @Override
    public boolean hasNext() {
        return currentSuffix <= maxSuffix;
    }

    @Override
    public String next() {
        if (!hasNext()) throw new NoSuchElementException();

        String result = (currentSuffix == 0)
                ? String.valueOf(currentChar)
                : currentChar + String.valueOf(currentSuffix);

        if (currentChar < 'z') {
            currentChar++;
        } else {
            currentChar = 'a';
            currentSuffix++;
        }

        return result;
    }
}
