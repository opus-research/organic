package br.pucrio.opus.smells.collector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ITypeBinding;

import br.pucrio.opus.smells.ast.visitors.ClassMethodInvocationVisitor;
import br.pucrio.opus.smells.resources.Method;
import br.pucrio.opus.smells.resources.Resource;

/**
 * Feature envy: All methods having more calls with another class than the one they are implemented.
 * @author Diego Cedrim
 */
public class FeatureEnvy extends SmellDetector {
	
	@Override
	public List<Smell> detect(Resource resource) {
		Method method = (Method)resource;
		ITypeBinding declaringClass = method.getBinding().getDeclaringClass();

		ClassMethodInvocationVisitor visitor = new ClassMethodInvocationVisitor(declaringClass);
		method.getNode().accept(visitor);
		Map<ITypeBinding, Integer> methodCalls = visitor.getMethodsCalls();

		//checks if the method made more calls to another specific calss than its declaring class
		Integer localCalls = methodCalls.get(declaringClass);
		for (ITypeBinding type : methodCalls.keySet()) {
			Integer calls = methodCalls.get(type);
			if (localCalls == null || calls > localCalls) {
				Smell smell = super.createSmell(resource);
				return Arrays.asList(smell);
			}
		}
		
		return new ArrayList<>();
	}
	
	@Override
	protected SmellName getSmellName() {
		return SmellName.FeatureEnvy;
	}

}
