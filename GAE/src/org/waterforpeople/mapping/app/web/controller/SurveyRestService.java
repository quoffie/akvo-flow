package org.waterforpeople.mapping.app.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.waterforpeople.mapping.app.gwt.client.survey.SurveyDto;
import org.waterforpeople.mapping.app.gwt.client.survey.SurveyGroupDto;
import org.waterforpeople.mapping.app.util.DtoMarshaller;

import com.gallatinsystems.common.Constants;
import com.gallatinsystems.framework.exceptions.IllegalDeletionException;
import com.gallatinsystems.survey.dao.SurveyDAO;
import com.gallatinsystems.survey.domain.Survey;
import com.gallatinsystems.survey.domain.SurveyGroup;

@Controller
@RequestMapping("/survey")
public class SurveyRestService {

	@Inject
	private SurveyDAO surveyDao;
	
	// list all surveys
	@RequestMapping(method = RequestMethod.GET, value = "/all")
	@ResponseBody
	public List<SurveyDto> listSurveys() {
		List<SurveyDto> results = new ArrayList<SurveyDto>();
		List<Survey> surveys = surveyDao.list(Constants.ALL_RESULTS);
		if (surveys != null) {
			for (Survey s : surveys) {
				SurveyDto dto = new SurveyDto();
				DtoMarshaller.copyToDto(s, dto);
				
				// needed because of different names for description in survey and surveyDto
				dto.setDescription(s.getDesc());
				results.add(dto);
			}
		}
		return results;
	}
	
	// list surveys by surveyGroup id
	@RequestMapping(method = RequestMethod.GET, value = "")
	@ResponseBody
	public List<SurveyDto> listSurveysByGroupId(@RequestParam("surveyGroupId") Long surveyGroupId) {
		List<SurveyDto> results = new ArrayList<SurveyDto>();
		List<Survey> surveys = surveyDao.listSurveysByGroup(surveyGroupId);
		if (surveys != null) {
			for (Survey s : surveys) {
				SurveyDto dto = new SurveyDto();
				DtoMarshaller.copyToDto(s, dto);
				
				// needed because of different names for description in survey and surveyDto
				dto.setDescription(s.getDesc());
				results.add(dto);
			}
		}
		return results;
	}
	
	// find a single survey by the surveyId
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseBody
	public SurveyDto findSurvey(@PathVariable("id") Long id){
		Survey s =surveyDao.getByKey(id);		
		SurveyDto dto = null;
		if(s != null){
			dto = new SurveyDto();
			DtoMarshaller.copyToDto(s, dto);
			
			// needed because of different names for description in survey and surveyDto
			dto.setDescription(s.getDesc());

		}
		return dto;
		
	}
	
	// delete survey by id
		@RequestMapping(method = RequestMethod.DELETE, value = "/del/{id}")
		@ResponseBody
		public RestStatusDto deleteSurveyById(@PathVariable("id") Long id){
			Survey s = surveyDao.getByKey(id);		
			RestStatusDto dto = null;
			dto = new RestStatusDto();
			dto.setStatus("failed");
			  
			// check if survey exists in the datastore
			if (s != null){
				// delete survey group
				try {
					surveyDao.delete(s);
					dto.setStatus("ok");	
				} catch (IllegalDeletionException e) {
					dto.setStatus("failed");
					dto.setMessage(e.getMessage());
					// e.printStackTrace();
				}
			}
			return dto;
		}
	
	
	
	@RequestMapping(method = RequestMethod.POST, value="")
	@ResponseBody
	public SurveyDto saveSurvey(@RequestBody SurveyDto surveyDto){
		SurveyDto dto = null;
		
		// if the POST data contains a valid surveyDto, continue. Otherwise, server will respond with 400 Bad Request 
		if (surveyDto != null){
			Long keyId = surveyDto.getKeyId();
			Survey s;
					
			// if the surveyDto has a key, try to get the surveyGroup.
			if (keyId != null) {
				s = surveyDao.getByKey(keyId);
				// if the surveyGroup doesn't exist, create a new surveyGroup
				if (s == null) {
					s = new Survey();
				}
			} else {
				s = new Survey();
			}
			
			// copy the properties, except the createdDateTime property, because it is set in the Dao.
			BeanUtils.copyProperties(surveyDto, s, new String[] {"createdDateTime","status","version","lastUpdateDateTime","displayName","questionGroupList"});
			s = surveyDao.save(s);
					
			dto = new SurveyDto();
			DtoMarshaller.copyToDto(s, dto);
		}
		return dto;
	}
}
