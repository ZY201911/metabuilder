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

package metabuilder.diagram.manager.constraints;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import metabuilder.annotations.Immutable;
import metabuilder.diagram.Diagram;
import metabuilder.diagram.Edge;
import metabuilder.diagram.Node;
import metabuilder.geom.Point;

/**
 * Represents a set of constraints.
 */
@Immutable
public class ConstraintSet
{
	private final Set<Constraint> aConstraints = new HashSet<>();
	
	/**
	 * Initializes a ConstraintSet with all the constraints in 
	 * pConstraints.
	 * 
	 * @param pConstraints The constraints to put in this set.
	 * @pre pConstraints != null.
	 */
	public ConstraintSet( Constraint... pConstraints )
	{
		assert pConstraints != null;
		aConstraints.addAll(Arrays.asList(pConstraints));
	}
		
	/**
	 * Returns True if and only if all the constraints in the set are satisfied.
	 * 
	 * @param pEdge The edge on which the constraints are applied.
	 * @param pStart The start node for the edge.
	 * @param pEnd The end node for the edge.
	 * @param pStartPoint The point on the canvas where the edge rubber band starts.
	 * @param pEndPoint The point on the canvas where the edge rubber band ends.
	 * @param pDiagram The diagram in which the edge is to be added.
	 * @return True if and only if all the constraints in the set are satisfied.
	 */
	public boolean satisfied(Edge pEdge, Node pStart, Node pEnd, Point pStartPoint, Point pEndPoint, Diagram pDiagram)
	{
		for( Constraint constraint : aConstraints )
		{
			if( !constraint.satisfied(pEdge, pStart, pEnd, pStartPoint, pEndPoint, pDiagram))
			{
				return false;
			}
		}
		return true;
	}
}