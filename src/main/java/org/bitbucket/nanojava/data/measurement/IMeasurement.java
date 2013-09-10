/* Copyright (C) 2011-2013  Egon Willighagen <egonw@users.sf.net>
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.bitbucket.nanojava.data.measurement;

import java.util.List;

import com.github.jqudt.Unit;

public interface IMeasurement {
	
	public void setEndPoint(IEndPoint endPoint);
	public IEndPoint getEndPoint();

	public void setUnit(Unit unit);
	public Unit getUnit();
	public String getString();

	public List<ICondition> getConditions();
	public void setConditions(List<ICondition> conditions);

}
