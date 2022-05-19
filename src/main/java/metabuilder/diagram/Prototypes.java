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
package metabuilder.diagram;

import static metabuilder.resources.MetaBuilderResources.RESOURCES;

import java.util.IdentityHashMap;
import java.util.Map;

import metabuilder.diagram.edges.AggregationEdge;
import metabuilder.diagram.edges.AssociationEdge;
import metabuilder.diagram.edges.DependencyEdge;
import metabuilder.diagram.edges.GeneralizationEdge;
import metabuilder.diagram.edges.NoteEdge;
import metabuilder.diagram.nodes.ClassNode;
import metabuilder.diagram.nodes.EnumerationNode;
import metabuilder.diagram.nodes.InterfaceNode;
import metabuilder.diagram.nodes.NoteNode;
import metabuilder.diagram.nodes.PackageDescriptionNode;
import metabuilder.diagram.nodes.PackageNode;
import metabuilder.diagram.nodes.PrimitiveTypeNode;
import metabuilder.diagram.edges.ObjectReferenceEdge;

/**
 * Prototype objects for creating diagram elements.
 */
public final class Prototypes
{   // CSOFF:
	private static final Prototypes INSTANCE = new Prototypes();
	private final Map<DiagramElement, String> aKeys = new IdentityHashMap<>();
	
	public static final DiagramElement NOTE = create(new NoteNode(), "note");
	public static final DiagramElement NOTE_CONNECTOR = create(new NoteEdge(), "note_connector");
	
	public static final DiagramElement CLASS = create(new ClassNode(), "class");
	public static final DiagramElement ABSTRACTCLASS = create(new ClassNode(true), "abstract_class");
	public static final DiagramElement INTERFACE = create(new InterfaceNode(), "interface");
	public static final DiagramElement PACKAGE = create(new PackageNode(), "package");
	public static final DiagramElement PRIMITIVETYPE = create(new PrimitiveTypeNode(), "primitive_type");
	public static final DiagramElement ENUMERATION = create(new EnumerationNode(), "enumeration");
	public static final DiagramElement PACKAGE_DESCRIPTION = 
			create(new PackageDescriptionNode(), "package_description");
	public static final DiagramElement DEPENDENCY = create(new DependencyEdge(), "dependency");
	public static final DiagramElement GENERALIZATION = create(new GeneralizationEdge(), "generalization");
	public static final DiagramElement REALIZATION = 
			create(new GeneralizationEdge(GeneralizationEdge.Type.Implementation), "realization");
	public static final DiagramElement ASSOCIATION = create(new AssociationEdge(), "association");
	public static final DiagramElement AGGREGATION = create(new AggregationEdge(), "aggregation");
	public static final DiagramElement COMPOSITION = 
			create(new AggregationEdge(AggregationEdge.Type.Composition), "composition");

	public static final DiagramElement REFERENCE = create(new ObjectReferenceEdge(), "reference");

	private Prototypes() {}
	
	/**
	 * @return The singleton instance of this class.
	 */
	public static Prototypes instance()
	{
		return INSTANCE;
	}
	
	private static DiagramElement create(DiagramElement pElement, String pKey)
	{
		INSTANCE.aKeys.put(pElement, pKey);
		return pElement;
	}
	
	/**
	 * @param pPrototype The requested prototype
	 * @param pVerbose true if we want the verbose version of this tooltip.
	 * @return The tooltip associated with this prototype.
	 * @pre pPrototype != null
	 */
	public String tooltip(DiagramElement pPrototype, boolean pVerbose)
	{
		if( !aKeys.containsKey(pPrototype))
		{
			return "[tooltip not found]";
		}
		String basicKey = aKeys.get(pPrototype) + ".tooltip";
		String verboseKey = basicKey + ".verbose";
		if( pVerbose && RESOURCES.containsKey(verboseKey))
		{
			return RESOURCES.getString(basicKey) + ": " + RESOURCES.getString(verboseKey);
		}
		else
		{
			return RESOURCES.getString(basicKey);
		}
	}
}
