package net.nikr.eve.jeveasset.io.shared;

import java.util.ArrayList;
import java.util.List;

import net.nikr.eve.jeveasset.data.api.accounts.EsiOwner;
import net.nikr.eve.jeveasset.data.api.accounts.EveApiAccount;
import net.nikr.eve.jeveasset.data.api.accounts.EveApiOwner;
import net.nikr.eve.jeveasset.data.api.accounts.EveKitOwner;
import net.nikr.eve.jeveasset.data.profile.Profile;
import net.nikr.eve.jeveasset.data.profile.ProfileManager;
import net.nikr.eve.jeveasset.data.settings.Settings;
import net.nikr.eve.jeveasset.gui.dialogs.update.UpdateTask;
import net.nikr.eve.jeveasset.io.esi.EsiAccountBalanceGetter;
import net.nikr.eve.jeveasset.io.esi.EsiAssetsGetter;
import net.nikr.eve.jeveasset.io.esi.EsiBlueprintsGetter;
import net.nikr.eve.jeveasset.io.esi.EsiContractItemsGetter;
import net.nikr.eve.jeveasset.io.esi.EsiContractsGetter;
import net.nikr.eve.jeveasset.io.esi.EsiIndustryJobsGetter;
import net.nikr.eve.jeveasset.io.esi.EsiJournalGetter;
import net.nikr.eve.jeveasset.io.esi.EsiMarketOrdersGetter;
import net.nikr.eve.jeveasset.io.esi.EsiOwnerGetter;
import net.nikr.eve.jeveasset.io.esi.EsiTransactionsGetter;
import net.nikr.eve.jeveasset.io.eveapi.AccountBalanceGetter;
import net.nikr.eve.jeveasset.io.eveapi.AccountGetter;
import net.nikr.eve.jeveasset.io.eveapi.AssetsGetter;
import net.nikr.eve.jeveasset.io.eveapi.BlueprintsGetter;
import net.nikr.eve.jeveasset.io.eveapi.ContractItemsGetter;
import net.nikr.eve.jeveasset.io.eveapi.ContractsGetter;
import net.nikr.eve.jeveasset.io.eveapi.IndustryJobsGetter;
import net.nikr.eve.jeveasset.io.eveapi.JournalGetter;
import net.nikr.eve.jeveasset.io.eveapi.MarketOrdersGetter;
import net.nikr.eve.jeveasset.io.eveapi.TransactionsGetter;
import net.nikr.eve.jeveasset.io.evekit.EveKitAccountBalanceGetter;
import net.nikr.eve.jeveasset.io.evekit.EveKitAssetGetter;
import net.nikr.eve.jeveasset.io.evekit.EveKitBlueprintsGetter;
import net.nikr.eve.jeveasset.io.evekit.EveKitContractItemsGetter;
import net.nikr.eve.jeveasset.io.evekit.EveKitContractsGetter;
import net.nikr.eve.jeveasset.io.evekit.EveKitIndustryJobsGetter;
import net.nikr.eve.jeveasset.io.evekit.EveKitJournalGetter;
import net.nikr.eve.jeveasset.io.evekit.EveKitMarketOrdersGetter;
import net.nikr.eve.jeveasset.io.evekit.EveKitOwnerGetter;
import net.nikr.eve.jeveasset.io.evekit.EveKitTransactionsGetter;

public class AllTasksUpdater extends UpdateTask {
	private final List<Runnable> updates = new ArrayList<>();

	public AllTasksUpdater(String name, ProfileManager profileManager) {
		super(name);
		updateAllProfiles(profileManager);
	}

	@Override
	public void update() {
		ThreadWoker.start(this, updates);
	}

	/**
	 * TODO: add some way to mark profiles that can be ignored in background
	 * update
	 */
	private void updateAllProfiles(ProfileManager profileManager) {
		for (Profile profile : profileManager.getProfiles()) {
			profile.setActiveProfile(true);
			updateViaApiAccount(profileManager.getAccounts());
			updateViaEsi(profileManager.getEsiOwners());
			updateViaEveKit(profileManager.getEveKitOwners());
		}
	}

	/**
	 * TODO: entirely untested as I've nothing to do with evekit
	 */
	private void updateViaEveKit(List<EveKitOwner> owners) {
		for (EveKitOwner owner : owners) {
			updates.add(new EveKitOwnerGetter(this, owner));
			updates.add(new EveKitAccountBalanceGetter(this, owner));
			updates.add(new EveKitAssetGetter(this, owner));
			updates.add(new EveKitBlueprintsGetter(this, owner));
			updates.add(new EveKitContractItemsGetter(this, owner));
			updates.add(new EveKitContractsGetter(this, owner));
			updates.add(new EveKitIndustryJobsGetter(this, owner));
			updates.add(new EveKitJournalGetter(this, owner));
			updates.add(new EveKitMarketOrdersGetter(this, owner));
			updates.add(new EveKitTransactionsGetter(this, owner));
		}
	}

	private void updateViaEsi(List<EsiOwner> owners) {
		for (EsiOwner owner : owners) {
			updates.add(new EsiOwnerGetter(this, owner));
			updates.add(new EsiAccountBalanceGetter(this, owner));
			updates.add(new EsiAssetsGetter(this, owner));
			updates.add(new EsiBlueprintsGetter(this, owner));
			updates.add(new EsiContractItemsGetter(this, owner, owners));
			updates.add(new EsiContractsGetter(this, owner));
			updates.add(new EsiIndustryJobsGetter(this, owner));
			updates.add(new EsiJournalGetter(this, owner, true));
			updates.add(new EsiMarketOrdersGetter(this, owner, true));
			updates.add(new EsiTransactionsGetter(this, owner, true));
		}
	}

	private void updateViaApiAccount(List<EveApiAccount> accounts) {
		for (EveApiAccount account : accounts) {
			updates.add(new AccountGetter(this, account));
			for (EveApiOwner owner : account.getOwners()) {
				updates.add(new AccountBalanceGetter(this, owner));
				updates.add(new AssetsGetter(this, owner));
				updates.add(new BlueprintsGetter(this, owner));
				updates.add(new ContractItemsGetter(this, owner));
				updates.add(new ContractsGetter(this, owner));
				updates.add(new IndustryJobsGetter(this, owner));
				updates.add(new JournalGetter(this, owner, Settings.get().isJournalHistory()));
				updates.add(new MarketOrdersGetter(this, owner, true));
				updates.add(new TransactionsGetter(this, owner, Settings.get().isJournalHistory()));
			}
		}
	}
}
