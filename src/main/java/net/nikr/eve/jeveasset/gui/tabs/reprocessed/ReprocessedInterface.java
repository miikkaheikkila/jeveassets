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

package net.nikr.eve.jeveasset.gui.tabs.reprocessed;


public interface ReprocessedInterface extends Comparable<ReprocessedInterface> {
	public String getName();
	public long getPortionSize();
	public long getQuantityMax();
	public long getQuantitySkill();
	public double getPrice();
	public double getValueMax();
	public double getValueSkill();
	public double getValueDifference();
	public int getTypeID();
	public boolean isMarketGroup();
	public boolean isTotal();
	public ReprocessedTotal getTotal();
}