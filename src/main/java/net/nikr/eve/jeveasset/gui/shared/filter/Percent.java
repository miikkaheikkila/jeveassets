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

package net.nikr.eve.jeveasset.gui.shared.filter;

import net.nikr.eve.jeveasset.gui.shared.Formater;


public class Percent implements Comparable<Percent> {
	private double percent;

	public Percent(final double percent) {
		this.percent = percent;
	}

	public double getPercent() {
		return round(percent * 100.0, 2);
	}

	private static double round(double Rval, int Rpl) {
		double p = Math.pow(10,Rpl);
		Rval = Rval * p;
		double tmp = Math.round(Rval);
		return tmp / p;
	}

	@Override
	public String toString() {
		if (Double.isInfinite(percent)) {
			return Formater.integerFormat(percent);
		} else {
			return Formater.percentFormat(percent);
		}
	}

	@Override
	public int compareTo(final Percent o) {
		return Double.compare(percent, o.percent);
	}
}