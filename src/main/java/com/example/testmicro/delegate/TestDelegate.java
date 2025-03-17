package com.example.testmicro.delegate;

import com.example.testmicro.business.Operation;
import com.example.testmicro.model.Point;
import com.example.testmicro.utils.ApplicationContexHolder;

import java.util.List;
import java.util.Map;

public class TestDelegate {

    public static Map<String, Object> findLines(Integer n, List<Point> points) {
        //delega operazione
        Map<String, Object> result = ApplicationContexHolder.getContext().getBean(Operation.class).findPermutations(points, n);
        return result;
    }
}
