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
package metabuilder.viewers.edges;

import java.util.IdentityHashMap;

import metabuilder.diagram.Edge;
import metabuilder.diagram.edges.AggregationEdge;
import metabuilder.diagram.edges.AssociationEdge;
import metabuilder.diagram.edges.DependencyEdge;
import metabuilder.diagram.edges.GeneralizationEdge;
import metabuilder.diagram.edges.NoteEdge;
import metabuilder.diagram.edges.ObjectReferenceEdge;
import metabuilder.geom.Line;
import metabuilder.geom.Point;
import metabuilder.geom.Rectangle;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * Keeps track of the association between an edge type and the viewer
 * that needs to be used to view it.
 */
public final class EdgeViewerRegistry
{	
	private static final EdgeViewerRegistry INSTANCE = new EdgeViewerRegistry();
	
	private IdentityHashMap<Class<? extends Edge>, EdgeViewer> aRegistry = 
			new IdentityHashMap<>();
	
	private EdgeViewerRegistry() 
	{
		aRegistry.put(NoteEdge.class, new NoteEdgeViewer());
		aRegistry.put(DependencyEdge.class, new DependencyEdgeViewer());
		aRegistry.put(AssociationEdge.class,  new AssociationEdgeViewer());
		aRegistry.put(GeneralizationEdge.class, new GeneralizationEdgeViewer());
		aRegistry.put(AggregationEdge.class, new AggregationEdgeViewer());
		aRegistry.put(ObjectReferenceEdge.class, new ObjectReferenceEdgeViewer());
	}
	
	/**
	 * @param pEdge The edge to view.
	 * @return A viewer for pEdge
	 * @pre pEdge != null;
	 */
	private EdgeViewer viewerFor(Edge pEdge)
	{
		assert pEdge != null && aRegistry.containsKey(pEdge.getClass());
		return aRegistry.get(pEdge.getClass());
	}
	
   	/**
     * Tests whether pEdge contains a point.
     * @param pEdge the edge to test
     * @param pPoint the point to test
     * @return true if this element contains aPoint
     */
	public static boolean contains(Edge pEdge, Point pPoint)
	{
		return INSTANCE.viewerFor(pEdge).contains(pEdge, pPoint);
	}
	
  	/**
   	 * Returns an icon that represents pEdge.
   	 * @param pEdge The edge for which we need an icon.
     * @return A canvas object on which the icon is painted.
     * @pre pEdge != null
   	 */
   	public static Canvas createIcon(Edge pEdge)
   	{
   		return INSTANCE.viewerFor(pEdge).createIcon(pEdge);
   	}
   	
	/**
     * Draws pEdge.
     * @param pEdge The edge to draw.
     * @param pGraphics the graphics context
     * @pre pEdge != null
	 */
   	public static void draw(Edge pEdge, GraphicsContext pGraphics)
   	{
   		INSTANCE.viewerFor(pEdge).draw(pEdge, pGraphics);
   	}
   	
   	/**
     * Draw selection handles around pEdge.
     * @param pEdge The target edge
     * @param pGraphics the graphics context
     * @pre pEdge != null && pGraphics != null
	 */
   	public static void drawSelectionHandles(Edge pEdge, GraphicsContext pGraphics)
   	{
   		INSTANCE.viewerFor(pEdge).drawSelectionHandles(pEdge, pGraphics);
   	}
   	
	/**
     * Gets the smallest rectangle that bounds pEdge.
     * The bounding rectangle contains all labels.
     * @param pEdge The edge whose bounds we wish to compute.
     * @return the bounding rectangle
     * @pre pEdge != null
   	 */
	public static Rectangle getBounds(Edge pEdge)
	{
		return INSTANCE.viewerFor(pEdge).getBounds(pEdge);
	}
	
  	/**
     * Gets the points at which pEdge is connected to
     * its nodes.
     * @param pEdge The target edge
     * @return a line joining the two connection points
     * @pre pEdge != null
     * 
     */
   	public static Line getConnectionPoints(Edge pEdge)
   	{
		return INSTANCE.viewerFor(pEdge).getConnectionPoints(pEdge);
   	}
}
