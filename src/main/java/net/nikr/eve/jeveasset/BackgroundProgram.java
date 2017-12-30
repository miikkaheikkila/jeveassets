package net.nikr.eve.jeveasset;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import net.nikr.eve.jeveasset.gui.shared.Formater;
import net.nikr.eve.jeveasset.gui.tabs.values.DataSetCreator;
import net.nikr.eve.jeveasset.gui.tabs.values.Value;

public class BackgroundProgram extends AbstractProgram {

	public BackgroundProgram() {
		
		profileData.updateEventLists();
		profileManager.saveProfile();
		LOG.info("Total assets: " + profileData.getAssetsEventList().size());
		Map<String, Value> values = DataSetCreator.createDataSet(profileData, new Date());
		for (Entry<String, Value> entry : values.entrySet()) {
			LOG.info("Grand total for " + entry.getKey() + " is " 
					+ Formater.iskFormat(entry.getValue().getTotal()) 
					+ ", assets: " + Formater.iskFormat(entry.getValue().getAssetsTotal()) 
					+ " balance: "+ Formater.iskFormat(entry.getValue().getBalanceTotal()) 
					+ " manufacturing: " + Formater.iskFormat(entry.getValue().getManufacturing()));
		}
		LOG.info("All profiles updated as far as possible, quitting job");
		System.exit(0);
	}
}
