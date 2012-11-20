// ***********************************************//
//                 controllers
// ***********************************************//

// Define the main application controller. This is automatically picked up by
// the application and initialized.
require('akvo-flow/core');
FLOW.ApplicationController = Ember.Controller.extend({
  init:function(){
    Ember.STRINGS=Ember.STRINGS_EN;
    //this.set("dashboardLanguage","en");
  }
});

// Navigation controllers
FLOW.NavigationController = Em.Controller.extend({ selected: null });
FLOW.NavHomeController = Ember.Controller.extend();
FLOW.NavSurveysController = Ember.Controller.extend();
FLOW.NavDevicesController = Ember.Controller.extend();
FLOW.DevicesSubnavController = Em.Controller.extend();
FLOW.DevicesTableHeaderController = Em.Controller.extend({ selected:null });
FLOW.NavDataController = Ember.Controller.extend();
FLOW.DatasubnavController = Em.Controller.extend();
FLOW.InspectDataController = Ember.ArrayController.extend();
FLOW.ImportSurveyController = Ember.Controller.extend();
FLOW.ExcelImportController = Ember.Controller.extend();
FLOW.ExcelExportController = Ember.Controller.extend();
FLOW.NavReportsController = Ember.Controller.extend();
FLOW.NavMapsController = Ember.Controller.extend();
FLOW.NavUsersController = Ember.Controller.extend();
FLOW.NavAdminController = Ember.Controller.extend();


// ***********************************************//
//                Type controllers
// ***********************************************//
FLOW.languageControl = Ember.Object.create({
  dashboardLanguage:null,

  content:[
    Ember.Object.create({label: "English", value: "en"}),
    Ember.Object.create({label: "Dutch", value: "nl"}),
    Ember.Object.create({label: "Spanish", value: "sp"}),
    Ember.Object.create({label: "French", value: "fr"})],

  changeLanguage:function(){
    locale=this.get("dashboardLanguage.value");
    console.log('changing language to ',locale);
    
    if (locale == "nl") {Ember.STRINGS=Ember.STRINGS_NL;}
    else if (locale == "fr") {Ember.STRINGS=Ember.STRINGS_FR;}
    else if (locale == "sp") {Ember.STRINGS=Ember.STRINGS_SP;}
    else {Ember.STRINGS=Ember.STRINGS_EN;}
  }.observes('dashboardLanguage')
});

FLOW.questionTypeControl = Ember.Object.create({
content:[
  Ember.Object.create({label: "Free text", value: "freeText"}),
    Ember.Object.create({label: "Option", value: "option"}),
    Ember.Object.create({label: "Number", value: "number"}),
    Ember.Object.create({label: "Geolocation", value: "geoLoc"}),
    Ember.Object.create({label: "Photo", value: "photo"}),
    Ember.Object.create({label: "Video", value: "video"}),
    Ember.Object.create({label: "Date", value: "date"}),
    Ember.Object.create({label: "Barcode", value: "barcode"})
]});

FLOW.surveyTypeControl = Ember.Object.create({
content:[
  Ember.Object.create({label: "Point", value: "point"}),
    Ember.Object.create({label: "Household", value: "household"}),
    Ember.Object.create({label: "Public institution", value: "publicInstitution"}),
    Ember.Object.create({label: "Community", value: "community"})
   
]});

FLOW.surveySectorTypeControl = Ember.Object.create({
content:[
  Ember.Object.create({label: "Water and Sanitation", value: "waterAndSanitation"}),
    Ember.Object.create({label: "Education", value: "education"}),
    Ember.Object.create({label: "Economic development", value: "economicDevelopment"}),
    Ember.Object.create({label: "Health care", value: "healthCare"}),
    Ember.Object.create({label: "IT and Communication", value: "ItAndCommunication"}),
    Ember.Object.create({label: "Food security", value: "foodSecurity"}),
    Ember.Object.create({label: "Other", value: "other"})
]});


FLOW.selectedControl = Ember.Controller.create({
  selectedSurveyGroup: null,
  selectedSurvey: null,
  selectedQuestionGroup: null,
  selectedQuestion: null,
  selectedOption: null,
  selectedForMoveQuestionGroup:null,
  selectedForCopyQuestionGroup:null,
  selectedCreateNewGroup:false,

  // when selected survey changes, deselect selected surveys and question groups
  deselectSurveyGroupChildren:function(){
    FLOW.selectedControl.set('selectedSurvey', null);
    FLOW.selectedControl.set('selectedQuestionGroup', null);
  }.observes('this.selectedSurveyGroup'),

   deselectSurveyChildren:function(){
    FLOW.selectedControl.set('selectedQuestionGroup', null);
  }.observes('this.selectedSurvey')
});



// ***********************************************//
//                Data controllers
// ***********************************************//
FLOW.surveyGroupControl = Ember.ArrayController.create({
  content:null,
  populate:function(){
    console.log("populating surveygroups");    
    this.set('content',FLOW.store.find(FLOW.SurveyGroup));
  }
});


FLOW.surveyControl = Ember.ArrayController.create({
  reloadSurveys:false,
  content:null,
  populate: function() {
    console.log("populating surveys");
    if (FLOW.selectedControl.get('selectedSurveyGroup')) {
      var id = FLOW.selectedControl.selectedSurveyGroup.get('keyId');
      this.set('content',FLOW.store.findQuery(FLOW.Survey, {surveyGroupId: id}));
    }
  }.observes('FLOW.selectedControl.selectedSurveyGroup','this.reloadSurveys')
});


FLOW.questionGroupControl = Ember.ArrayController.create({
  sortProperties:['order'],
  sortAscending:true,
  content:null,
  populate: function() {
    if (FLOW.selectedControl.get('selectedSurvey')) {
      var id = FLOW.selectedControl.selectedSurvey.get('keyId');
      this.set('content',FLOW.store.findQuery(FLOW.QuestionGroup, {surveyId: id}));
    }
  }.observes('FLOW.selectedControl.selectedSurvey')

});


FLOW.questionControl = Ember.ArrayController.create({
  content:null,
  populate: function() {
    if (FLOW.selectedControl.get('selectedQuestionGroup')) {
      var id = FLOW.selectedControl.selectedQuestionGroup.get('keyId');
      this.set('content',FLOW.store.findQuery(FLOW.Question, {questionGroupId: id}));
    }
  }.observes('FLOW.selectedControl.selectedQuestionGroup')
});


FLOW.optionControl = Ember.ArrayController.create({
});


FLOW.deviceControl = Ember.ArrayController.create({
  sortProperties:['phoneNumber'],
  pleaseShow:true,
  sortAscending:true,
  sortSelected:null,
  content:null,

  populate:function(){

  },

  allAreSelected: function(key, value) {
    if (arguments.length === 2) {
      this.setEach('isSelected', value);
      return value;
    }
    else {
      return !this.get('isEmpty') && this.everyProperty('isSelected', true);
    }
  }.property('@each.isSelected'),

  atLeastOneSelected: function() {
    return this.filterProperty('isSelected', true).get('length');
  }.property('@each.isSelected')
});

