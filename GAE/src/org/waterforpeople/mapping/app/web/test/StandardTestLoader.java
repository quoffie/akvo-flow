package org.waterforpeople.mapping.app.web.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.waterforpeople.mapping.app.web.TestHarnessServlet;
import org.waterforpeople.mapping.dao.AccessPointDao;
import org.waterforpeople.mapping.domain.AccessPoint;
import org.waterforpeople.mapping.domain.AccessPoint.AccessPointType;

import com.gallatinsystems.framework.dao.BaseDAO;
import com.gallatinsystems.standards.dao.LevelOfServiceScoreDao;
import com.gallatinsystems.standards.dao.StandardDao;
import com.gallatinsystems.standards.domain.LOSScoreToStatusMapping;
import com.gallatinsystems.standards.domain.LOSScoreToStatusMapping.LOSColor;
import com.gallatinsystems.standards.domain.LevelOfServiceScore;
import com.gallatinsystems.standards.domain.LevelOfServiceScore.LevelOfServiceScoreType;
import com.gallatinsystems.standards.domain.Standard;
import com.gallatinsystems.standards.domain.Standard.StandardComparisons;
import com.gallatinsystems.standards.domain.Standard.StandardScope;
import com.gallatinsystems.standards.domain.Standard.StandardType;
import com.gallatinsystems.standards.domain.Standard.StandardValueType;

public class StandardTestLoader {
	private HttpServletRequest req;
	private HttpServletResponse resp;

	private static Logger log = Logger.getLogger(TestHarnessServlet.class
			.getName());

	public StandardTestLoader(HttpServletRequest req, HttpServletResponse resp) {
		this.req = req;
		this.resp = resp;
	}

	public void loadWaterPointStandard() {
		StandardDao standardDao = new StandardDao();
		// # of Users Standard
		Standard standard = new Standard();
		standard.setAccessPointType(AccessPointType.WATER_POINT);
		standard.setStandardType(StandardType.WaterPointLevelOfService);
		standard.setStandardScope(StandardScope.Local);
		standard.setCountry("BO");
		ArrayList<String> posValues = new ArrayList<String>();
		posValues.add("500");
		standard.setPositiveValues(posValues);
		standard.setAcessPointAttributeType(StandardValueType.Number);
		standard.setStandardComparison(StandardComparisons.lessthan);
		standard.setStandardDescription("Estimated Number of Users");
		standard.setAccessPointAttribute("extimatedPopulation");
		standardDao.save(standard);

		// hasSystemBeenDown1DayFlag global boolean true=0 false=1
		standard.setAccessPointType(AccessPointType.WATER_POINT);
		standard.setStandardType(StandardType.WaterPointLevelOfService);
		standard.setStandardScope(StandardScope.Global);
		standard.setCountry("");
		posValues.removeAll(posValues);
		posValues.add("false");
		standard.setPositiveValues(posValues);
		standard.setAcessPointAttributeType(StandardValueType.Boolean);
		standard.setStandardComparison(StandardComparisons.equal);
		standard.setStandardDescription("Has System Been down in last 30 days");
		standard.setAccessPointAttribute("hasSystemBeenDown1DayFlag");
		standardDao.save(standard);

		// provideAdequateQuantity global boolean true=1 flase=0
		standard = new Standard();
		standard.setAccessPointType(AccessPointType.WATER_POINT);
		standard.setStandardType(StandardType.WaterPointLevelOfService);
		standard.setStandardScope(StandardScope.Global);
		standard.setCountry("");
		posValues.removeAll(posValues);
		posValues.add("true");
		standard.setPositiveValues(posValues);
		standard.setAcessPointAttributeType(StandardValueType.Boolean);
		standard.setStandardComparison(StandardComparisons.equal);
		standard.setStandardDescription("Does the water source provide enough drinking water for the community every day of the year?");
		standard.setAccessPointAttribute("provideAdequateQuantity");
		standardDao.save(standard);
		// ppmFecalColiform local double <
		standard = new Standard();
		standard.setAccessPointType(AccessPointType.WATER_POINT);
		standard.setStandardType(StandardType.WaterPointLevelOfService);
		standard.setStandardScope(StandardScope.Local);
		standard.setCountry("BO");
		posValues.removeAll(posValues);
		posValues.add("1");
		standard.setPositiveValues(posValues);
		standard.setAcessPointAttributeType(StandardValueType.Number);
		standard.setStandardComparison(StandardComparisons.lessthan);
		standard.setStandardDescription("How much fecal coliform were present on the day of collection?");
		standard.setAccessPointAttribute("ppmFecalColiform");
		standardDao.save(standard);

		// numberOfLitersPerPersonPerDay local < govt standard
		standard = new Standard();
		standard.setAccessPointType(AccessPointType.WATER_POINT);
		standard.setStandardType(StandardType.WaterPointLevelOfService);
		standard.setStandardScope(StandardScope.Local);
		standard.setCountry("BO");
		posValues.removeAll(posValues);
		posValues.add("10");
		standard.setPositiveValues(posValues);
		standard.setAcessPointAttributeType(StandardValueType.Number);
		standard.setStandardComparison(StandardComparisons.greaterthan);
		standard.setStandardDescription("How many liters of water per person per day does this source provide?");
		standard.setAccessPointAttribute("numberOfLitersPerPersonPerDay");
		standardDao.save(standard);

		writeln("Saved: " + standard.toString());

	}

	private void loadWaterPointScoreToStatus() {
		ArrayList<LOSScoreToStatusMapping> losList = new ArrayList<LOSScoreToStatusMapping>();

		LOSScoreToStatusMapping losScoreToStatusMapping = new LOSScoreToStatusMapping();
		losScoreToStatusMapping
				.setLevelOfServiceScoreType(LevelOfServiceScoreType.WaterPointLevelOfService);
		losScoreToStatusMapping.setFloor(0);
		losScoreToStatusMapping.setCeiling(0);
		losScoreToStatusMapping.setColor(LOSColor.Black);
		losList.add(losScoreToStatusMapping);

		losScoreToStatusMapping = new LOSScoreToStatusMapping();
		losScoreToStatusMapping
				.setLevelOfServiceScoreType(LevelOfServiceScoreType.WaterPointLevelOfService);
		losScoreToStatusMapping.setFloor(1);
		losScoreToStatusMapping.setCeiling(1);
		losScoreToStatusMapping.setColor(LOSColor.Red);
		losList.add(losScoreToStatusMapping);

		losScoreToStatusMapping = new LOSScoreToStatusMapping();
		losScoreToStatusMapping
				.setLevelOfServiceScoreType(LevelOfServiceScoreType.WaterPointLevelOfService);
		losScoreToStatusMapping.setFloor(2);
		losScoreToStatusMapping.setCeiling(5);
		losScoreToStatusMapping.setColor(LOSColor.Yellow);
		losList.add(losScoreToStatusMapping);

		losScoreToStatusMapping = new LOSScoreToStatusMapping();
		losScoreToStatusMapping
				.setLevelOfServiceScoreType(LevelOfServiceScoreType.WaterPointLevelOfService);
		losScoreToStatusMapping.setFloor(6);
		losScoreToStatusMapping.setCeiling(7);
		losScoreToStatusMapping.setColor(LOSColor.Green);
		losList.add(losScoreToStatusMapping);

		BaseDAO<LOSScoreToStatusMapping> losBaseDao = new BaseDAO<LOSScoreToStatusMapping>(
				LOSScoreToStatusMapping.class);
		losBaseDao.save(losList);
	}

	public void setReq(HttpServletRequest req) {
		this.req = req;
	}

	public HttpServletRequest getReq() {
		return req;
	}

	private void writeln(String message) {
		try {
			log.info(message);
			resp.getWriter().println(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void runTest() {
		clearAPs();
		loadWaterPointStandard();
		loadWaterPointScoreToStatus();
		AccessPointTest apt = new AccessPointTest();
		apt.loadLots(resp, 1);
	}

	private void clearAPs() {
		DeleteObjectUtil dou = new DeleteObjectUtil();
		dou.deleteAllObjects("AccessPoint");
		writeln("Deleted APs");
		dou.deleteAllObjects("AccessPointScoreComputationItem");
		writeln("Deleted APSCI");
		dou.deleteAllObjects("AccessPointScoreDetail");
		writeln("Deleted APSD");
		dou.deleteAllObjects("AccessPointsStatusSummary");
		writeln("Deleted AccessPointsStatusSummary");
		dou.deleteAllObjects("Standard");
		writeln("Deleted All the Standards");
		dou.deleteAllObjects("LevelOfServiceScore");
		writeln("Deleted All the LevelOfServiceScore");
		dou.deleteAllObjects("LOSScoreToStatusMapping");
		writeln("Deleted All LevelOfServiceScoreToStatusMappings");
	}

	
	public void listResults(){
		listAPScoreAndStatus();
	}
	private void listAPScoreAndStatus() {
		AccessPointDao apDao = new AccessPointDao();
		List<AccessPoint> apList = apDao.list("all");
		LevelOfServiceScoreDao lesScoreDao = new LevelOfServiceScoreDao();
		for (AccessPoint item : apList) {
			List<LevelOfServiceScore> losScoreList = lesScoreDao
					.listByAccessPoint(item.getKey());
			writeln("AP: " + item.getKeyString());
			for (LevelOfServiceScore losItem : losScoreList) {
				writeln("   LevelOfServiceScore: " + losItem.getScore()
						+ " Score Date: " + losItem.getLastUpdateDateTime());
				for(String detail:losItem.getScoreDetails()){
					writeln("      Details: " + detail);
				}
			}
		}
	}
}
