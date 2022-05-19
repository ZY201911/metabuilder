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
package metabuilder.viewers.nodes;

import java.util.IdentityHashMap;

import metabuilder.diagram.Node;
import metabuilder.diagram.nodes.ClassNode;
import metabuilder.diagram.nodes.EnumerationNode;
import metabuilder.diagram.nodes.InterfaceNode;
//import metabuilder.diagram.nodes.NoteNode;
//import metabuilder.diagram.nodes.PackageDescriptionNode;
import metabuilder.diagram.nodes.PackageNode;
import metabuilder.diagram.nodes.PointNode;
import metabuilder.diagram.nodes.PrimitiveTypeNode;
import metabuilder.geom.Direction;
import metabuilder.geom.Point;
import metabuilder.geom.Rectangle;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * Keeps track of the association between a node type and the viewer
 * that needs to be used to view it.
 */
public final class NodeViewerRegistry
{	
	private static final NodeViewerRegistry INSTANCE = new NodeViewerRegistry();
	
	private IdentityHashMap<Class<? extends Node>, NodeViewer> aRegistry = 
			new IdentityHashMap<>();
	
	private NodeViewerRegistry() 
	{
//		aRegistry.put(AbstractClassNode.class, new AbstractClassNodeViewer());
		aRegistry.put(ClassNode.class, new TypeNodeViewer());
		aRegistry.put(InterfaceNode.class, new InterfaceNodeViewer());
//		aRegistry.put(NoteNode.class, new NoteNodeViewer());
		aRegistry.put(PackageNode.class, new PackageNodeViewer());
//		aRegistry.put(PackageDescriptionNode.class, new PackageDescriptionNodeViewer());
		aRegistry.put(PrimitiveTypeNode.class, new TypeNodeViewer());
		aRegistry.put(EnumerationNode.class, new TypeNodeViewer());
		aRegistry.put(PointNode.class, new PointNodeViewer());
	}
	
	/**
	 * @param pNode The node to view.
	 * @return A viewer for pNode
	 * @pre pNode != null;
	 */
	private NodeViewer viewerFor(Node pNode)
	{
		assert pNode != null && aRegistry.containsKey(pNode.getClass());
		return aRegistry.get(pNode.getClass());
	}
	
   	/**
     * Tests whether pNode contains a point.
     * @param pNode the node to test
     * @param pPoint the point to test
     * @return true if this element contains aPoint
     */
	public static boolean contains(Node pNode, Point pPoint)
	{
		return INSTANCE.viewerFor(pNode).contains(pNode, pPoint);
	}
	
  	/**
   	 * Returns an icon that represents pNode.
   	 * @param pNode The node for which we need an icon.
     * @return A canvas object on which the icon is painted.
     * @pre pNode != null
   	 */
   	public static Canvas createIcon(Node pNode)
   	{
   		return INSTANCE.viewerFor(pNode).createIcon(pNode);
   	}
   	
	/**
     * Draws pNode.
     * @param pNode The node to draw.
     * @param pGraphics the graphics context
     * @pre pNode != null
	 */
   	public static void draw(Node pNode, GraphicsContext pGraphics)
   	{
   		INSTANCE.viewerFor(pNode).draw(pNode, pGraphics);
   	}
   	
   	/**
     * Draw selection handles around pNode.
     * @param pNode The target node
     * @param pGraphics the graphics context
     * @pre pNode != null && pGraphics != null
	 */
   	public static void drawSelectionHandles(Node pNode, GraphicsContext pGraphics)
   	{
   		INSTANCE.viewerFor(pNode).drawSelectionHandles(pNode, pGraphics);
   	}
   	
	/**
     * Gets the smallest rectangle that bounds pNode.
     * The bounding rectangle contains all labels.
     * @param pNode The node whose bounds we wish to compute.
     * @return the bounding rectangle
     * @pre pNode != null
   	 */
	public static Rectangle getBounds(Node pNode)
	{
		return INSTANCE.viewerFor(pNode).getBounds(pNode);
	}
	
  	/**
     * Gets the points at which pNode is connected to
     * its nodes.
     * @param pNode The target node
     * @param pDirection The desired direction.
     * @return A connection point on the node.
     * @pre pNode != null && pDirection != null
     * 
     */
   	public static Point getConnectionPoints(Node pNode, Direction pDirection)
   	{
		return INSTANCE.viewerFor(pNode).getConnectionPoint(pNode, pDirection);
   	}
   	
   	/**
   	 * Activates all the NodeStorages of the NodeViewers present in the registry. 
   	 */
   	public static void activateNodeStorages()
   	{
   		for (NodeViewer nodeViewer : INSTANCE.aRegistry.values())
   		{
   			nodeViewer.activateNodeStorage();
   		}
   	}
   	
   	/**
   	 * Deactivates and clears all the NodeStorages of the NodeViewers present in the registry. 
   	 */
   	public static void deactivateAndClearNodeStorages()
   	{
   		for (NodeViewer nodeViewer : INSTANCE.aRegistry.values())
   		{
   			nodeViewer.deactivateAndClearNodeStorage();
   		}
   	}
}
