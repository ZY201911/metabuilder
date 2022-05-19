/*******************************************************************************
 * JetUML - A desktop application for fast UML diagramming.
 *
 * Copyright (C) 2020, 2021 by McGill University.
 *     
 * See: https://github.com/prmr/JetUML
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
 * along with this program.  If not, see http://www.gnu.org/licenses.
 *******************************************************************************/
package metabuilder.utils;

import java.util.EnumMap;

import metabuilder.diagram.DiagramElement;
import metabuilder.diagram.Properties;
import metabuilder.diagram.Property;
import metabuilder.diagram.PropertyName;
import metabuilder.diagram.manager.CompoundOperation;
import metabuilder.diagram.manager.SimpleOperation;

/**
 * Tracks modification to the properties of a DiagramElement.
 * Should be discarded after a call to stopTracking().
 */
public class PropertyChangeTracker 
{
	private EnumMap<PropertyName, Object> aOldValues = new EnumMap<>(PropertyName.class);
	private Properties aProperties;
	
	/**
	 * Creates a new tracker for pEdited.
	 *  
	 * @param pEdited The element to track.
	 * @pre pEdited != null;
	 */
	public PropertyChangeTracker(DiagramElement pEdited)
	{
		assert pEdited != null;
		aProperties = pEdited.properties();
	}

	/**
	 * Makes a snapshot of the properties values of the tracked element.
	 */
	public void startTracking()
	{
		for( Property property : aProperties )
		{
			aOldValues.put(property.name(), property.get());
		}
	}
	
	/**
	 * Creates and returns a CompoundOperation that represents any change
	 * in properties detected between the time startTracking
	 * and stopTracking were called.
	 * 
	 * @return A CompoundOperation describing the property changes.
	 */
	public CompoundOperation stopTracking()
	{
		CompoundOperation operation = new CompoundOperation();
		for( Property property : aProperties )
		{
			if( !aOldValues.get(property.name()).equals(property.get()))
			{
				final Object newValue = property.get();
				final Object oldValue = aOldValues.get(property.name());
				operation.add(new SimpleOperation(
						()-> property.set(newValue),
						()-> property.set(oldValue)));
			}
		}
		return operation;
	}
}
