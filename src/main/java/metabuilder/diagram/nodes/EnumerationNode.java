package metabuilder.diagram.nodes;

import metabuilder.diagram.PropertyName;

public final class EnumerationNode extends DataTypeNode {
	// TODO ö��Literals
	private String aLiterals = "";
	
	public void setLiterals(String pLiterals)
	{
		aLiterals = pLiterals;
	}

	public String getLiterals()
	{
		return aLiterals;
	}
	
	@Override
	protected void buildProperties()
	{
		super.buildProperties();
		properties().add(PropertyName.LITERALS, () -> aLiterals, pLiterals -> aLiterals = (String)pLiterals);
	}
}
