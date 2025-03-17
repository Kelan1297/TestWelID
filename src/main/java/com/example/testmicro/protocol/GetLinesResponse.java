package com.example.testmicro.protocol;

import com.example.testmicro.model.Point;

import java.util.HashSet;
import java.util.Set;


public class GetLinesResponse extends Response{
    private Integer lines;

    public Integer getLines() {
        return lines;
    }

    public void setLines(Integer lines) {
        this.lines = lines;
    }

    @Override
    public String toString() {
        return "GetLinesResponse{" +
                "Lines=" + lines +
                '}';
    }
}
