package com.example.testmicro.business;

import com.example.testmicro.delegate.TestDelegate;
import com.example.testmicro.model.Point;
import com.example.testmicro.utils.CostantiTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Operation {
    private static final Logger logger = LogManager.getLogger(Operation.class);
    public static Map<String, Object> findPermutations(List<Point> points, Integer n) {
        Map<String, Object> result = new HashMap<>();
        String descr = "";
        short esito =-1;
        Integer permutationCount = 0;
        List<List<Point>> permutations = generatePermutations(points, n);
        if(permutations.size()>0) {
            esito=0;
            descr="Successo";
            permutationCount = permutations.size();
        }else {
            esito = 1;
            descr = "permutazioni non trovate";
        }
        result.put(CostantiTest.RESULTDESC, descr);
        result.put(CostantiTest.ESITO, esito);
        result.put(CostantiTest.TOTALE_PERMUTAZIONI, permutationCount);
        return result;
    }
    private static List<List<Point>> generatePermutations(List<Point> points, int n) {
        List<List<Point>> result = new ArrayList<>();
        permute(points, n, 0, new ArrayList<>(), result);
        return result;
    }
    private static void permute(List<Point> points, int n, int start, List<Point> current, List<List<Point>> result) {
        if (current.size() == n) {
            // Quando raggiungiamo una permutazione di n punti, la aggiungiamo al risultato
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < points.size(); i++) {
            logger.info("Aggiungiamo il punto corrente alla permutazione");
            current.add(points.get(i));
            //Chiamata ricorsiva per il prossimo punto
            permute(points, n, i + 1, current, result);
            logger.info("Rimuoviamo l'ultimo punto per fare nuove permutazioni");
            current.remove(current.size() - 1);
        }
    }
}
