/*
 *  Copyright (C) 2018 Stichting Akvo (Akvo Foundation)
 *
 *  This file is part of Akvo FLOW.
 *
 *  Akvo FLOW is free software: you can redistribute it and modify it under the terms of
 *  the GNU Affero General Public License (AGPL) as published by the Free Software Foundation,
 *  either version 3 of the License or any later version.
 *
 *  Akvo FLOW is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Affero General Public License included below for more details.
 *
 *  The full license text can also be seen at <http://www.gnu.org/licenses/agpl.html>.
 */

package org.akvo.gae.remoteapi;

import static org.akvo.gae.remoteapi.DataUtils.batchSaveEntities;
import static org.akvo.gae.remoteapi.DataUtils.batchDelete;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/*
 * - Checks that all surveysInstances, surveyedLocales are consistent
 * - TODO qas and surveyedLocaleData
 */
public class CheckResponseStructure implements Process {

    private int orphanSurveyInstances = 0, orphanSurveyedLocales = 0;
    private Map<Long, String> surveys = new HashMap<>();
    private Map<Long, String> surveyedLocales = new HashMap<>();
    private Map<Long, String> surveyInstances = new HashMap<>();

    private boolean fixSurveyPointers = false; // Make question survey pointer match the group's
    private boolean deleteOrphans = false;

    @Override
    public void execute(DatastoreService ds, String[] args) throws Exception {

//        System.out.printf("#Arguments: FIX to correct survey pointers, GC to delete orphaned entites.\n");
        for (int i = 0; i < args.length; i++) {
            //System.out.printf("#Argument %d: %s\n", i, args[i]);
            if (args[i].equalsIgnoreCase("FIX")) {
                fixSurveyPointers = true;
            }
            if (args[i].equalsIgnoreCase("GC")) {
                deleteOrphans = true;
            }
        }

        processSurveys(ds);
        processSurveyedLocales(ds);
        processSurveyInstances(ds);

        System.out.printf("#Surveys:         %5d \n", surveys.size());
        System.out.printf("#SurveyedLocales: %5d good, %4d orphans\n", surveyedLocales.size(), orphanSurveyedLocales);
        System.out.printf("#SurveyInstances: %5d good, %4d orphans\n", surveyInstances.size(),   orphanSurveyInstances);

    }

    private void processSurveys(DatastoreService ds) {

        System.out.println("#Processing Surveys");

        final Query survey_q = new Query("Survey");
        final PreparedQuery survey_pq = ds.prepare(survey_q);

        for (Entity s : survey_pq.asIterable(FetchOptions.Builder.withChunkSize(500))) {

            Long surveyId = s.getKey().getId();
            String surveyName = (String) s.getProperty("name");
            Long surveyGroup = (Long) s.getProperty("surveyGroupId");
            surveys.put(surveyId,surveyName); //ok to have questions in
        }
    }

    private void processSurveyInstances(DatastoreService ds) {

        System.out.println("#Processing Survey Instances");

        final Query survey_q = new Query("SurveyInstance");
        final PreparedQuery survey_pq = ds.prepare(survey_q);

        for (Entity si : survey_pq.asIterable(FetchOptions.Builder.withChunkSize(500))) {

            Long id = si.getKey().getId();
            String name = (String) si.getProperty("name");
            Long surveyedLocationId = (Long) si.getProperty("surveyedLocaleId");
            if (surveyedLocationId == null || !surveyedLocales.containsKey(surveyedLocationId)) {
                System.out.printf("#ERR SurveyInstance %d '%s' is not in a SurveyedLocale\n",
                        id, name);
                orphanSurveyInstances++;
            } else {
                surveyInstances.put(id, name); //ok to have answers in
            }
        }
    }

    private void processSurveyedLocales(DatastoreService ds) {

        System.out.println("#Processing SurveyedLocales (Data Points)");

        final Query survey_q = new Query("SurveyedLocale");
        final PreparedQuery survey_pq = ds.prepare(survey_q);

        for (Entity sl : survey_pq.asIterable(FetchOptions.Builder.withChunkSize(500))) {

            Long id = sl.getKey().getId();
            String name = (String) sl.getProperty("identifier");
            Long surveyId = (Long) sl.getProperty("creationSurveyId");
            if (surveyId == null || surveys.containsKey(surveyId)) {
                System.out.printf("#ERR SurveyedLocale %d '%s' was not created by a known survey (%d)\n",
                        id, name, surveyId);
                orphanSurveyedLocales++;
            } else {
                //??
            }
            surveyedLocales.put(id,name); //always remember
        }
    }

}
