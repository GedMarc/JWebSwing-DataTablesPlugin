/*
 * Copyright (C) 2017 GedMarc
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jwebmp.plugins.datatable.enumerations;

public enum DataTableButtons
{
	Copy,
	Excel,
	Print,
	Csv,
	Pdf,
	Colvis,
	ColumnsToggle("columnsToggle");

	private String data;

	DataTableButtons()
	{
	}

	DataTableButtons(String data)
	{
		this.data = data;
	}

	@Override
	public String toString()
	{
		if(data == null)
		return name().toLowerCase();
		else return data;
	}
}
