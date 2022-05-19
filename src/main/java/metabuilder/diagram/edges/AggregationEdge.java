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
package metabuilder.diagram.edges;

import metabuilder.diagram.PropertyName;

/**
 *  An edge that that represents a UML aggregation or 
 *  composition, with optional labels.
 */
public final class AggregationEdge extends ThreeLabelEdge
{
	/**
	 * Type of aggregation relation.
	 */
	public enum Type 
	{Aggregation, Composition}

	private Type aType = Type.Composition;

	/**
	 * Creates an aggregation edge by specifying its type.
	 * 
	 * @param pType the type of aggregation
	 */
	public AggregationEdge( Type pType )
	{
		aType = pType;
	}
	
	/**
	 * Creates a plain aggregation.
	 */
	public AggregationEdge()
	{}
	
	/**
	 * @return The type of aggregation relation.
	 */
	public Type getType()
	{
		return aType;
	}

	/**
	 * @param pType The type of aggregation relation.
	 */
	public void setType(Type pType)
	{
		aType = pType;
	}
	
	@Override
	protected void buildProperties()
	{
		super.buildProperties();
		properties().add(PropertyName.AGGREGATION_TYPE, () -> aType, pType -> aType = Type.valueOf((String) pType));
	}
}
