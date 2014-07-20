/*
 *  Copyright (C) 2010-2012 Stichting Akvo (Akvo Foundation)
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

package org.waterforpeople.mapping.analytics.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;

import net.sf.jsr107cache.Cache;

import org.waterforpeople.mapping.analytics.domain.SurveyQuestionSummary;
import org.waterforpeople.mapping.domain.QuestionAnswerStore;

import com.gallatinsystems.common.util.MemCacheUtils;
import com.gallatinsystems.framework.dao.BaseDAO;
import com.gallatinsystems.framework.servlet.PersistenceFilter;
import com.gallatinsystems.survey.dao.QuestionDao;
import com.gallatinsystems.survey.domain.Question;

/**
 * updates survey question objects
 *
 * @author Christopher Fagiani
 */
public class SurveyQuestionSummaryDao extends BaseDAO<SurveyQuestionSummary> {

    private Cache cache;

    public SurveyQuestionSummaryDao() {
        super(SurveyQuestionSummary.class);
        cache = MemCacheUtils.initCache(4 * 60 * 60); // cache summary objects list for 4 hours
    }

    /**
     * synchronized static method so that only 1 thread can be updating a summary at a time. This is
     * inefficient but is the only way we can be sure we're keeping the count consistent since there
     * is no "select for update" or sql dml-like construct
     *
     * @param answer
     */
    @SuppressWarnings("rawtypes")
    public static synchronized void incrementCount(QuestionAnswerStore answer,
            int unit) {
        PersistenceManager pm = PersistenceFilter.getManager();
        String answerText = answer.getValue();
        String[] answers;
        if (answerText != null && answerText.contains("|")) {
            answers = answerText.split("\\|");
        } else {
            answers = new String[] {
                answerText
            };
        }
        for (int i = 0; i < answers.length; i++) {
            // find surveyQuestionSummary objects with the right question id and answer text
            javax.jdo.Query query = pm.newQuery(SurveyQuestionSummary.class);
            query
                    .setFilter("questionId == questionIdParam && response == answerParam");
            query
                    .declareParameters("String questionIdParam, String answerParam");
            List results = (List) query.execute(answer.getQuestionID(),
                    answers[i]);
            SurveyQuestionSummary summary = null;
            if ((results == null || results.size() == 0) && unit > 0) {
                // no previous surveyQuestionSummary for this answer, make a new one
                summary = new SurveyQuestionSummary();
                summary.setCount(new Long(unit));
                summary.setQuestionId(answer.getQuestionID());
                summary.setResponse(answers[i]);
            } else if (results != null && results.size() > 0) {
                // update an existing questionAnswerSummary
                summary = (SurveyQuestionSummary) results.get(0);
                summary.setCount(summary.getCount() + unit);
            }
            if (summary != null) {
                // if we have updated or created a surveyQuestionSummary, save it
                SurveyQuestionSummaryDao summaryDao = new SurveyQuestionSummaryDao();
                if (summary.getCount() > 0) {
                    summaryDao.save(summary);
                } else if (summary.getKey() != null) {
                    // if count has been decremented to 0 and the object is
                    // already persisted, delete it
                    summaryDao.delete(summary);
                }
            }
        }
    }

    /**
     * this method will list all the summary objects for a given question id
     */
    public List<SurveyQuestionSummary> listByQuestion(String qId) {
        return listByProperty("questionId", qId, "String");
    }

    /**
     * List all the summary objects for a given survey id
     */
    public List<SurveyQuestionSummary> listBySurvey(Long surveyId) {
        List<SurveyQuestionSummary> summaryList = new ArrayList<SurveyQuestionSummary>();
        for(Question q : new QuestionDao().listQuestionsBySurvey(surveyId)) {
            String questionIdStr = Long.toString(q.getKey().getId());
            List<SurveyQuestionSummary> questionSummaryList = listByProperty("questionId", questionIdStr, "String");
            if(questionSummaryList != null && !questionSummaryList.isEmpty()) {
                summaryList.addAll(questionSummaryList);
            }
        }
        return summaryList;
    }

    /**
     * This method returns a map containing all the summary objects for a survey. The
     * keys are the strings of the question option text.
     *
     * @param surveyId
     * @return
     */
    public Map<String, SurveyQuestionSummary> mapSurveyQuestionSummary(Long surveyId) {
        List<SurveyQuestionSummary> surveyQuestionSummaryList = listBySurvey(surveyId);

        Map<String, SurveyQuestionSummary> questionSummary = new HashMap<String, SurveyQuestionSummary>();
        for(SurveyQuestionSummary summary : surveyQuestionSummaryList) {
            questionSummary.put(summary.getResponse(), summary);
        }
        return questionSummary;
    }
}
