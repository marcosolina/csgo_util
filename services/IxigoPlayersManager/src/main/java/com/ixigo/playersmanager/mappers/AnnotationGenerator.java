/*
package com.ixigo.playersmanager.mappers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMatchStatsExtended;
import com.ixigo.playersmanager.models.svc.SvcMapStats;

public class AnnotationGenerator {
	public static void main(String[] args) {
        List<String> annotations = generateMappings();

        // Print the generated annotations
        for (String annotation : annotations) {
            System.out.println(annotation);
        }
    }

    public static List<String> generateMappings() {
        List<String> annotations = new ArrayList<>();
        Field[] restFields = RestPlayerMatchStatsExtended.class.getDeclaredFields();
        Field[] svcFields = SvcMapStats.class.getDeclaredFields();

        for (int i = 0; i < restFields.length; i++) {
            String source = svcFields[i].getName();
            String target = restFields[i].getName();
            String type = svcFields[i].getType().getSimpleName();

            String annotation = "@Mapping(source = \"" + source + "\", target = \"" + target + "\")";
            annotations.add(annotation);
        }

        return annotations;
    }
}
*/