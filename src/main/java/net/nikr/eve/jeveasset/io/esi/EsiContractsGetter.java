/*
 * Copyright 2009-2017 Contributors (see credits.txt)
 *
 * This file is part of jEveAssets.
 *
 * jEveAssets is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * jEveAssets is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jEveAssets; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package net.nikr.eve.jeveasset.io.esi;

import java.util.Date;
import java.util.List;
import net.nikr.eve.jeveasset.data.api.accounts.EsiOwner;
import net.nikr.eve.jeveasset.gui.dialogs.update.UpdateTask;
import net.troja.eve.esi.ApiClient;
import net.troja.eve.esi.ApiException;
import net.troja.eve.esi.model.CharacterContractsResponse;

public class EsiContractsGetter extends AbstractEsiGetter {

	public EsiContractsGetter(UpdateTask updateTask, EsiOwner owner) {
		super(updateTask, owner, false,  owner.getContractsNextUpdate(), TaskType.CONTRACTS);
	}

	@Override
	protected void get(ApiClient apiClient) throws ApiException {
		List<CharacterContractsResponse> contracts = getContractsApiAuth(apiClient).getCharactersCharacterIdContracts((int) owner.getOwnerID(), DATASOURCE, null, null, null);
		owner.setContracts(EsiConverter.toContracts(contracts, owner));
	}

	@Override
	protected void setNextUpdate(Date date) {
		owner.setContractsNextUpdate(date);
	}

	@Override
	protected boolean inScope() {
		return owner.isContracts();
	}

	@Override
	protected boolean enabled() {
		if (owner.isCorporation()) {
			return false;
		} else {
			return EsiScopes.CHARACTER_CONTRACTS.isEnabled();
		}
	}

}