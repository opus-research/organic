package br.pucrio.opus.tests.visitor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.pucrio.opus.smells.ast.ASTBuilder;
import br.pucrio.opus.smells.visitors.FieldAccessCollector;
import br.pucrio.opus.smells.visitors.FieldDeclarationCollector;
import br.pucrio.opus.smells.visitors.PublicMethodCollector;

public class FieldAccessCollectorTest {
	
	private CompilationUnit compilationUnit;
	
	private List<MethodDeclaration> publicMethods;
	
	private List<FieldDeclaration> fields;
	
	private MethodDeclaration findMethodByName(String name) {
		for (MethodDeclaration decls : publicMethods) {
			if (decls.getName().toString().equals(name)) {
				return decls;
			}
		}
		return null;
	}

	@Before
	public void setUp() throws IOException{
		File file = new File("test");
		String[] srcPaths = new String[]{file.getAbsolutePath()};
		ASTBuilder builder = new ASTBuilder(srcPaths);
		ASTParser parser = builder.create();
		
		String source = FileUtils.readFileToString(new File("test/br/pucrio/opus/tests/dummy/FieldAccessedByMethod.java"));
		parser.setSource(source.toCharArray());
		this.compilationUnit = (CompilationUnit)parser.createAST(null);
		
		FieldDeclarationCollector fieldCollector = new FieldDeclarationCollector();
		compilationUnit.accept(fieldCollector);
		this.fields = fieldCollector.getNodesCollected();
		
		PublicMethodCollector visitor = new PublicMethodCollector();
		compilationUnit.accept(visitor);
		this.publicMethods = visitor.getNodesCollected();
	}
	
	@Test
	public void methodXFieldAccessesTest() {
		MethodDeclaration declaration = findMethodByName("x");
		FieldAccessCollector visitor = new FieldAccessCollector(this.fields);
		declaration.accept(visitor);
		List<FieldDeclaration> accesses = visitor.getNodesCollected();
		Assert.assertEquals(3, accesses.size());
	}
	
	@Test
	public void methodYFieldAccessesTest() {
		MethodDeclaration declaration = findMethodByName("y");
		FieldAccessCollector visitor = new FieldAccessCollector(this.fields);
		declaration.accept(visitor);
		List<FieldDeclaration> accesses = visitor.getNodesCollected();
		Assert.assertEquals(1, accesses.size());
	}
	
	@Test
	public void methodZFieldAccessesTest() {
		MethodDeclaration declaration = findMethodByName("z");
		FieldAccessCollector visitor = new FieldAccessCollector(this.fields);
		declaration.accept(visitor);
		List<FieldDeclaration> accesses = visitor.getNodesCollected();
		Assert.assertEquals(2, accesses.size());
	}
	
}