package metabuilder.codegen.controller;

import com.jfinal.template.Engine;
import com.jfinal.template.Template;

import metabuilder.diagram.Diagram;

import com.jfinal.kit.Kv;

public class CodeGenController {
	
	public void test(Diagram pDiagram) {
		Engine engine = Engine.use();

		engine.setDevMode(true);
		engine.setToClassPathSourceFactory();
		Template tem = engine.getTemplate("ClassTemplate.txt");
		Kv kv = Kv.by("pClass", pDiagram.rootNodes().get(0));
		kv = Kv.by("metamodelname", pDiagram.getName());
		String str = tem.renderToString(kv);
		System.out.println(str);
	}

}
