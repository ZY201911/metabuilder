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
package metabuilder.diagram.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An operation that is composed of other operations, following
 * the Composite Design Pattern.
 * 
 * Executing a compound operation executes all the sub-operations
 * in the order they were added. Undoing a compound operation
 * undoes all the sub-operation in the reverse order in which 
 * they were added.
 */
public class CompoundOperation implements DiagramOperation
{
	private List<DiagramOperation> aOperations = new ArrayList<>();
	
	/**
	 * Adds a sub-operation.
	 * 
	 * @param pOperation The operation to add. 
	 * @pre pOperation != null;
	 */
	public void add(DiagramOperation pOperation)
	{
		aOperations.add(pOperation);
	}

	@Override
	public void execute()
	{
		for( DiagramOperation operation : aOperations)
		{
			operation.execute();
		}
	}

	@Override
	public void undo()
	{
		ArrayList<DiagramOperation> reverse = new ArrayList<>(aOperations);
		Collections.reverse(reverse);
		for( DiagramOperation operation : reverse)
		{
			operation.undo();
		}
	}
	
	/**
	 * @return True if this CompoundOperation contains
	 *     no sub-operation.
	 */
	public boolean isEmpty()
	{
		return aOperations.isEmpty();
	}
}
