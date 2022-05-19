/*******************************************************************************
 * JetUML - A desktop application for fast UML diagramming.
 *
 * Copyright (C) 2020 by McGill University.
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
package metabuilder.file;

import metabuilder.diagram.Diagram;
import metabuilder.diagram.Node;

/**
 * A deserialization context allows clients to incrementally build
 * up the context. The identifiers that correspond to objects must be 
 * specified explicitly. 
 */
public class DeserializationContext extends AbstractContext
{
	/**
	 * Initializes an empty context and associates it with
	 * pDiagram.
	 * 
	 * @param pDiagram The diagram associated with the context.
	 * @pre pDiagram != null.
	 */
	public DeserializationContext(Diagram pDiagram)
	{
		super( pDiagram );
	}
	
	/**
	 * Attach all deserialized nodes to their diagram.
	 */
	public void attachNodes()
	{
		for( Node node : this )
		{
			node.attach(pDiagram());
		}
	}
	
	/**
	 * Adds a node to the context.
	 * 
	 * @param pNode The node to add.
	 * @param pId The id to associated with this node.
	 * @pre pNode != null;
	 */
	public void addNode(Node pNode, int pId)
	{
		assert pNode != null;
		aNodes.put(pNode, pId);
	}
	
	/**
	 * @param pId The identifier to search for.
	 * @return The node associated with this identifier.
	 * @pre pId exists as a value.
	 */
	public Node getNode(int pId)
	{
		for( Node node : this )
		{
			if( aNodes.get(node) == pId )
			{
				return node;
			}
		}
		assert false;
		return null;
	}
}
