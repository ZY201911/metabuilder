package metabuilder.diagram.manager.constraints;

import metabuilder.diagram.Diagram;
import metabuilder.diagram.Edge;
import metabuilder.diagram.Node;
import metabuilder.diagram.edges.AggregationEdge;
import metabuilder.diagram.edges.AssociationEdge;
import metabuilder.diagram.edges.DependencyEdge;
import metabuilder.diagram.edges.GeneralizationEdge;
import metabuilder.geom.Point;

/**
* Methods to create edge addition constraints that only apply to
* class diagrams. CSOFF:
*/
public final class MetaModelDiagramEdgeConstraints
{
	private MetaModelDiagramEdgeConstraints() {}
	
	/*
	 * Self edges are not allowed for Generalization edges.
	 */
	public static Constraint noSelfGeneralization()
	{
		return (Edge pEdge, Node pStart, Node pEnd, Point pStartPoint, Point pEndPoint, Diagram pDiagram)-> 
		{
			return !( pEdge.getClass() == GeneralizationEdge.class && pStart == pEnd );
		};
	}
	
	/*
	 * Self edges are not allowed for Dependency edges.
	 */
	public static Constraint noSelfDependency()
	{
		return (Edge pEdge, Node pStart, Node pEnd, Point pStartPoint, Point pEndPoint, Diagram pDiagram) ->
		{
			return !( pEdge.getClass() == DependencyEdge.class && pStart == pEnd );
		};
	}
	
	/*
	 * There can't be two edges of a given type, one in each direction, between two nodes.
	 */
	public static Constraint noDirectCycles(Class<? extends Edge> pEdgeType)
	{
		return (Edge pEdge, Node pStart, Node pEnd, Point pStartPoint, Point pEndPoint, Diagram pDiagram) ->
		{
			if( pEdge.getClass() != pEdgeType )
			{
				return true;
			}
			for( Edge edge : pStart.getDiagram().get().edgesConnectedTo(pStart) )
			{
				if( edge.getClass() == pEdgeType && edge.getEnd() == pStart && edge.getStart() == pEnd )
				{
					return false;
				}
			}
			return true;
		};
	}
	
	/*
	 * There can't be both an association and an aggregation edge between two nodes
	 */
	public static Constraint noCombinedAssociationAggregation()
	{
		return (Edge pEdge, Node pStart, Node pEnd, Point pStartPoint, Point pEndPoint, Diagram pDiagram) ->
		{
			if( pEdge.getClass() != AssociationEdge.class && pEdge.getClass() != AggregationEdge.class )
			{
				return true;
			}
			for( Edge edge : pStart.getDiagram().get().edgesConnectedTo(pStart) )
			{
				boolean targetEdge = edge.getClass() == AssociationEdge.class || edge.getClass() == AggregationEdge.class;
				boolean sameInOneDirection = edge.getStart() == pStart && edge.getEnd() == pEnd;
				boolean sameInOtherDirection = edge.getStart() == pEnd && edge.getEnd() == pStart;
				if( targetEdge && (sameInOneDirection || sameInOtherDirection))
				{
					return false;
				}
			}
			return true;
		};
	}
}