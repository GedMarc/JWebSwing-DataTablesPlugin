/*
 * Copyright (C) 2017 Marc Magon
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

package com.jwebmp.plugins.datatable;

import com.google.inject.Singleton;
import com.jwebmp.core.base.servlets.JWDefaultServlet;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.logger.LogFactory;
import com.jwebmp.plugins.datatable.events.DataTableDataFetchEvent;
import com.jwebmp.plugins.datatable.search.DataTableSearchRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.jwebmp.core.utilities.StaticStrings.*;

@Singleton
public class DataTablesServlet
		extends JWDefaultServlet
{
	private static final Logger log = LogFactory.getInstance()
	                                            .getLogger("DataTablesServlet");

	@Override
	public void perform()
	{
		HttpServletRequest request = GuiceContext.get(HttpServletRequest.class);
		StringBuilder output = new StringBuilder();
		Set<Class<? extends DataTableDataFetchEvent>> allEvents = GuiceContext.reflect()
		                                                                      .getSubTypesOf(DataTableDataFetchEvent.class);
		Map<String, String[]> params = request.getParameterMap();
		String className = params.get("c")[0];
		allEvents.removeIf(a -> !a.getCanonicalName()
		                          .equals(className.replace(CHAR_UNDERSCORE, CHAR_DOT)));
		if (allEvents.isEmpty())
		{
			writeOutput(output, HTML_HEADER_JAVASCRIPT, UTF8_CHARSET);
			log.fine("DataTablesServlet could not find any specified data search class");
		}
		else
		{
			DataTableSearchRequest searchRequest = new DataTableSearchRequest();
			searchRequest.fromRequestMap(params);
			try
			{
				Class<? extends DataTableDataFetchEvent> event = allEvents.iterator()
				                                                          .next();
				DataTableDataFetchEvent dtd = GuiceContext.getInstance(event);
				DataTableData d = dtd.returnData(searchRequest);
				output.append(d.toString());
				writeOutput(output, HTML_HEADER_JSON, UTF8_CHARSET);
			}
			catch (Exception e)
			{
				output.append(ExceptionUtils.getStackTrace(e));
				writeOutput(output, HTML_HEADER_JAVASCRIPT, UTF8_CHARSET);
				log.log(Level.SEVERE, "Unable to execute datatables ajax data fetch", e);
			}
		}
	}

}
