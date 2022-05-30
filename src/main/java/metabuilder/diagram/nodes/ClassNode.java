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

package metabuilder.diagram.nodes;

import java.util.ArrayList;

import metabuilder.diagram.PropertyName;

/**
 * A class node in a class diagram.
 */
public final class ClassNode extends TypeNode
{
	private String aAttributes = "";
	
	private boolean isAbstract = false;
	
	private ClassNode extendsClass = null;
	
	private InterfaceNode implementsInterface = null;
	
	private ArrayList<String> attributesArray = new ArrayList<String>();
	
	/**
	 * Constructs a new ClassNode with an empty name and no
	 * attributes or methods.
	 */
	public ClassNode()
	{
		setName("");
	}
	
	public ClassNode(boolean pIsAbstract) {
		setIsAbstract(pIsAbstract);
	}

	/**
     * Sets the attributes property value.
     * @param pNewValue the attributes of this class
     * @pre pNewValue != null
	 */
	public void setAttributes(String pNewValue)
	{
		assert pNewValue != null;
		aAttributes = pNewValue;
	}
	
	public void setIsAbstract(boolean pNewValue) {
		isAbstract = pNewValue;
	}
	
	public void setExtendsClassNode(ClassNode pExtendsClassNode) {
		extendsClass = pExtendsClassNode;
	}
	
	public void setImplementsInterface(InterfaceNode pImplementsInterface) {
		implementsInterface = pImplementsInterface;
	}
	
	public void setAttributesArray(String pAttributes) {
		String[] attrs = pAttributes.split("\n");
		for(String attr : attrs) {
//			if(!attr.matches("^[a-zA-Z]+[a-zA-Z0-9]*\s+[a-zA-Z_$]+[a-zA-Z0-9]*.*=.*;")) {
//				//TODO Í¨Öªpresenter±¨´í
//				;
//			}
			attributesArray.add(attr);
		}
	}

	/**
     * Gets the attributes property value.
     * @return the attributes of this class
	 */
	public String getAttributes()
	{
		return aAttributes;
	}
	
	public boolean getIsAbstract() {
		return isAbstract;
	}
	
	public ClassNode getExtendsClassNode() {
		return extendsClass;
	}
	
	public InterfaceNode getImplementsInterface() {
		return implementsInterface;
	}
	
	public ArrayList<String> getAttributesArray() {
		return attributesArray;
	}

	@Override
	protected void buildProperties()
	{
		super.buildProperties();
		properties().addAt(PropertyName.ATTRIBUTES, () -> aAttributes, pAttributes -> aAttributes = (String)pAttributes, 1);
	}
}
