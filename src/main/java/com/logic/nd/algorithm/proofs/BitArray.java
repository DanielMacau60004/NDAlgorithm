package com.logic.nd.algorithm.proofs;

import java.util.Arrays;
import java.util.Objects;

public class BitArray {

    private short[] data;

    public BitArray() {
        data = new short[0];
    }

    public BitArray(BitArray bitArray) {
        data = new short[bitArray.length()];
        for (int i = 0; i < bitArray.length(); i++)
            data[i] = bitArray.data[i];
    }

    public boolean contains(short element) {
        for (short elem : data) {
            if (elem == element) return true;
            else if (elem > element) return false;
        }

        return false;
    }

    public int length() {
        return data.length;
    }

    public void set(short newElement) {
        if(contains(newElement))
            return;

        short[] newData = new short[data.length + 1];

        int i = 0;
        while (i < data.length && data[i] < newElement) {
            newData[i] = data[i];
            i++;
        }

        newData[i] = newElement;
        for (int j = i; j < data.length; j++)
            newData[j + 1] = data[j];

        this.data = newData;
    }

    public short[] getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BitArray bitArray = (BitArray) o;
        return Objects.deepEquals(data, bitArray.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
