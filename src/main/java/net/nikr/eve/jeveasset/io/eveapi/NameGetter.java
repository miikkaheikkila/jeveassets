/*
 * Copyright 2009, 2010, 2011, 2012 Contributors (see credits.txt)
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

package net.nikr.eve.jeveasset.io.eveapi;

import com.beimin.eveapi.eve.character.ApiCharacterLookup;
import com.beimin.eveapi.eve.character.CharacterLookupResponse;
import com.beimin.eveapi.exception.ApiException;
import java.util.Date;
import java.util.Set;
import net.nikr.eve.jeveasset.data.Account.AccessMask;
import net.nikr.eve.jeveasset.data.Owner;
import net.nikr.eve.jeveasset.data.Settings;
import net.nikr.eve.jeveasset.gui.dialogs.update.UpdateTask;
import net.nikr.eve.jeveasset.io.shared.AbstractApiGetter;


public class NameGetter  extends AbstractApiGetter<CharacterLookupResponse> {

	private final int MAX_SIZE = 200;
	private long[][] ownerIDs;
	private int group;

	private Settings settings;

	public NameGetter() {
		super("OwnerID to Name", false, false);
	}

	public void load(UpdateTask updateTask, Settings settings, Set<Long> list) {
		this.settings = settings;
		ownerIDs = convert(list);
		for (int i = 0; i < ownerIDs.length; i++) {
			group = i;
			for (Long l : ownerIDs[group]) {
				System.out.println(l);
			}
			//FIXME update progress for UpdateTask
			super.load(updateTask, true, "Request "+i+" of "+ownerIDs.length);
		}
	}

	@Override
	protected CharacterLookupResponse getResponse(boolean bCorp) throws ApiException {
		return com.beimin.eveapi.eve.character.CharacterLookupParser.getId2NameInstance().getResponse(ownerIDs[group]);
	}

	@Override
	protected Date getNextUpdate() {
		return new Date();
	}

	@Override
	protected void setNextUpdate(Date nextUpdate) {
		
	}

	@Override
	protected void setData(CharacterLookupResponse response) {
		Set<ApiCharacterLookup> lookups = response.getAll();
		for (ApiCharacterLookup lookup : lookups) {
			settings.getOwners().put(lookup.getCharacterID(), lookup.getName());
		}
	}

	@Override
	protected void updateFailed(Owner ownerFrom, Owner ownerTo) {
		
	}

	@Override
	protected long requestMask(boolean bCorp) {
		return AccessMask.OPEN.getAccessMask();
	}

	protected long[][] convert(Set<Long> list){
		if (list.contains(0L)) { //Remove 0 (zero)
			list.remove(0L);
		}
		if (list.isEmpty()) { //Empty - we do nothing
			return new long[0][];
		}
		int groupSize = (int)Math.ceil((double)list.size()/(double)MAX_SIZE);
		long[][] arrayLong = new long[groupSize][];
		int groupCount = 0;
		int count = 0;
		for (Long value : list) {
			if (count == 0) {
				if ((groupCount + 1) * MAX_SIZE > list.size()) {
					arrayLong[groupCount] = new long[list.size() - (MAX_SIZE * groupCount)];
				} else {
					arrayLong[groupCount] = new long[MAX_SIZE];
				}
			}
			arrayLong[groupCount][count] = value;
			count++;
			if (count >= MAX_SIZE) {
				groupCount++;
				count = 0;
			}
		}
		return arrayLong;
	}
	
}