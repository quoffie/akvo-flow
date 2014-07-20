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

package org.waterforpeople.mapping.domain;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.apache.commons.lang.StringUtils;
import org.waterforpeople.mapping.analytics.dao.SurveyQuestionSummaryDao;
import org.waterforpeople.mapping.analytics.domain.SurveyQuestionSummary;
import org.waterforpeople.mapping.app.gwt.client.survey.QuestionDto.QuestionType;
import org.waterforpeople.mapping.dao.SurveyInstanceDAO;

import com.gallatinsystems.device.domain.DeviceFiles;
import com.gallatinsystems.framework.domain.BaseDomain;
import com.gallatinsystems.gis.map.MapUtils;
import com.gallatinsystems.survey.dao.QuestionDao;
import com.gallatinsystems.survey.domain.Question;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class SurveyInstance extends BaseDomain {

    private static final long serialVersionUID = 5840846001731305734L;

    @Persistent
    private Long userID;

    @Persistent
    private Date collectionDate;
    private Long deviceFileId;

    @NotPersistent
    private DeviceFiles deviceFile;

    @NotPersistent
    private ArrayList<QuestionAnswerStore> questionAnswersStore;

    private Long surveyId;

    private String deviceIdentifier;
    private String submitterName;
    private String approvedFlag;
    private String uuid;
    private String approximateLocationFlag;
    private Long surveyedLocaleId;
    private String surveyedLocaleIdentifier;
    private String surveyedLocaleDisplayName;
    private String countryCode;
    private String community;
    private String localeGeoLocation;
    private String sublevel1;
    private String sublevel2;
    private String sublevel3;
    private String sublevel4;
    private String sublevel5;
    private String sublevel6;

    private Long surveyalTime;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getSublevel1() {
        return sublevel1;
    }

    public void setSublevel1(String sublevel1) {
        this.sublevel1 = sublevel1;
    }

    public String getSublevel2() {
        return sublevel2;
    }

    public void setSublevel2(String sublevel2) {
        this.sublevel2 = sublevel2;
    }

    public String getSublevel3() {
        return sublevel3;
    }

    public void setSublevel3(String sublevel3) {
        this.sublevel3 = sublevel3;
    }

    public String getSublevel4() {
        return sublevel4;
    }

    public void setSublevel4(String sublevel4) {
        this.sublevel4 = sublevel4;
    }

    public String getSublevel5() {
        return sublevel5;
    }

    public void setSublevel5(String sublevel5) {
        this.sublevel5 = sublevel5;
    }

    public String getSublevel6() {
        return sublevel6;
    }

    public void setSublevel6(String sublevel6) {
        this.sublevel6 = sublevel6;
    }

    public Long getSurveyedLocaleId() {
        return surveyedLocaleId;
    }

    public void setSurveyedLocaleId(Long surveyedLocaleId) {
        this.surveyedLocaleId = surveyedLocaleId;
    }

    public String getApproximateLocationFlag() {
        return approximateLocationFlag;
    }

    public void setApproximateLocationFlag(String approximateLocationFlag) {
        this.approximateLocationFlag = approximateLocationFlag;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getApprovedFlag() {
        return approvedFlag;
    }

    public void setApprovedFlag(String approvedFlag) {
        this.approvedFlag = approvedFlag;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Date getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }

    public DeviceFiles getDeviceFile() {
        return deviceFile;
    }

    public void setDeviceFile(DeviceFiles deviceFile) {
        this.deviceFile = deviceFile;
        if (deviceFile.getKey() != null)
            deviceFileId = deviceFile.getKey().getId();
    }

    public ArrayList<QuestionAnswerStore> getQuestionAnswersStore() {
        return questionAnswersStore;
    }

    public void setQuestionAnswersStore(
            ArrayList<QuestionAnswerStore> questionAnswersStore) {
        this.questionAnswersStore = questionAnswersStore;
    }

    public void setSubmitterName(String name) {
        submitterName = name;
    }

    public String getSubmitterName() {
        return submitterName;
    }

    public void setDeviceIdentifier(String id) {
        deviceIdentifier = id;
    }

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append(this.getClass().getName());
        result.append(" Object {");
        result.append(newLine);

        // determine fields declared in this class only (no fields of
        // superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        // print field names paired with their values
        for (Field field : fields) {
            result.append("  ");
            try {
                result.append(field.getName());
                result.append(": ");
                // requires access to private field:
                result.append(field.get(this));
            } catch (IllegalAccessException ex) {
                System.out.println(ex);
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }

    public void setDeviceFileId(Long deviceFileId) {
        this.deviceFileId = deviceFileId;
    }

    public Long getDeviceFileId() {
        return deviceFileId;
    }

    public void setSurveyalTime(Long survetalTime) {
        this.surveyalTime = survetalTime;
    }

    public Long getSurveyalTime() {
        return surveyalTime;
    }

    public String getSurveyedLocaleIdentifier() {
        return surveyedLocaleIdentifier;
    }

    public void setSurveyedLocaleIdentifier(String surveyedLocaleIdentifier) {
        this.surveyedLocaleIdentifier = surveyedLocaleIdentifier;
    }

    public String getLocaleGeoLocation() {
        return localeGeoLocation;
    }

    public void setLocaleGeoLocation(String localeGeoLocation) {
        this.localeGeoLocation = localeGeoLocation;
    }

    public String getSurveyedLocaleDisplayName() {
        return surveyedLocaleDisplayName;
    }

    public void setSurveyedLocaleDisplayName(String surveyedLocaleDisplayName) {
        this.surveyedLocaleDisplayName = surveyedLocaleDisplayName;
    }

    /**
     * Extract geolocation information from a survey instance
     *
     * @param surveyInstance
     * @return a map containing latitude and longitude entries null if a null string is provided
     */
    public static Map<String, Object> retrieveGeoLocation(
            SurveyInstance surveyInstance) throws NumberFormatException {
        Map<String, Object> geoLocationMap = null;

        // retrieve geo location string
        String geoLocationString = null;
        // if the GEO information was present as Meta data, get it from there
        if (StringUtils.isNotBlank(surveyInstance.getLocaleGeoLocation())) {
            geoLocationString = surveyInstance.getLocaleGeoLocation();
        } else {
            // else, try to look for a GEO question
            List<QuestionAnswerStore> geoAnswers = new SurveyInstanceDAO()
                    .listQuestionAnswerStoreByType(surveyInstance.getKey()
                            .getId(), QuestionType.GEO.toString());
            if (geoAnswers != null && !geoAnswers.isEmpty()) {
                geoLocationString = geoAnswers.get(0).getValue();
            }
        }

        String[] tokens = StringUtils.split(geoLocationString, "\\|");
        if (tokens != null && tokens.length >= 2) {
            geoLocationMap = new HashMap<String, Object>();
            geoLocationMap.put(MapUtils.LATITUDE, Double.parseDouble(tokens[0]));
            geoLocationMap.put(MapUtils.LONGITUDE, Double.parseDouble(tokens[1]));
            // if(tokens.length > 2) {
            // TODO: currently a string is generated for altitude. need to fix
            // geoLocationMap.put(ALTITUDE, Long.parseLong(tokens[2]));
            // }
        }

        return geoLocationMap;
    }

    /**
     * Update counts of SurveyQuestionSummary entities related to responses from this survey
     * instance.
     */
    public void updateSummaryCounts(boolean increment) {

        // retrieve all summary objects
        SurveyQuestionSummaryDao summaryDao = new SurveyQuestionSummaryDao();
        Map<String, SurveyQuestionSummary> surveySummaries = summaryDao.mapSurveyQuestionSummary(surveyId);
        Map<Long, Question> surveyQuestionsMap = new QuestionDao().mapQuestionsBySurvey(surveyId);

        for(QuestionAnswerStore response : questionAnswersStore) {
            Long questionId = Long.parseLong(response.getQuestionID());
            Question question = surveyQuestionsMap.get(questionId);
            if (!question.canBeCharted()) {
                continue;
            }

            final String responseValue = response.getValue();
            SurveyQuestionSummary summary = null;
            if(surveySummaries.containsKey(responseValue)) {
                summary = surveySummaries.get(responseValue);
            } else {
                summary = new SurveyQuestionSummary();
                summary.setQuestionId(response.getQuestionID());
                summary.setResponse(responseValue);
                summary.setCount(0L);
            }


            // update and save or delete
            if(increment) {
                summary.setCount(summary.getCount() + 1);
                surveySummaries.put(responseValue, summary);
            } else {
                surveySummaries.remove(responseValue);
            }
        }

        // cache all objects and save in datastore
        summaryDao.save(surveySummaries.values());
    }
}
