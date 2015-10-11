import java.util.Vector;

public class SemanticAnalyzer implements ASTVisitor {

	private TypeEnvironment types;
	private VariableEnvironment variables;
	private FunctionEnvironment functions;
	private AATBuildTree assembly;
	private int offset;
	private Label endLabel;

	public SemanticAnalyzer() {
		types = new TypeEnvironment();
		variables = new VariableEnvironment();
		variables.insert("return", new VariableEntry(null));
		functions = new FunctionEnvironment();
		functions.addBuiltinFunctions();
		assembly = new AATBuildTree();
		offset = 0;
		endLabel = new Label("end");
	}

	@Override
	public Object VisitArrayVariable(ASTArrayVariable array) {
		TypeTree index = (TypeTree) array.index().Accept(this);
		TypeTree base = (TypeTree) array.base().Accept(this);

		if (index.type() != IntegerType.instance())
			CompError.message(array.line(),
					"VisitArrayVariable: index must be int.");

		if (!(base.type() instanceof ArrayType))
			CompError.message(array.line(),
					"VisitArrayVariable: base must be an array.");
		else {
			ArrayType baseType = (ArrayType) base.type();

			return new TypeTree(baseType.type(), assembly.arrayVariable(
					base.tree(), index.tree(), MachineDependent.WORDSIZE));
		}

		return new TypeTree(IntegerType.instance(), null);
	}

	@Override
	public Object VisitAssignmentStatement(ASTAssignmentStatement assign) {
		TypeTree left = (TypeTree) assign.variable().Accept(this);
		TypeTree right = (TypeTree) assign.value().Accept(this);

		if (left.type() != right.type())
			CompError
					.message(assign.line(),
							"VisitAssignmentStatement: variable-type does not match value-type.");

		return assembly.assignmentStatement(left.tree(), right.tree());
	}

	@Override
	public Object VisitBaseVariable(ASTBaseVariable base) {
		VariableEntry varEntry = variables.find(base.name());

		if (varEntry == null)
			CompError.message(base.line(), "VisitBaseVariable: variable "
					+ base.name() + " is not defined.");
		else
			return new TypeTree(varEntry.type(), assembly.baseVariable(varEntry
					.offset()));

		return new TypeTree(IntegerType.instance(), null);

	}

	@Override
	public Object VisitBooleanLiteral(ASTBooleanLiteral boolliteral) {

		int bool = 0;

		if (boolliteral.value() == true)
			bool = 1;

		return new TypeTree(BooleanType.instance(),
				assembly.constantExpression(bool));
	}

	@Override
	public Object VisitClass(ASTClass classs) {
		offset = 0;
		ClassType entry;
		VariableEnvironment localVars = new VariableEnvironment();
		ASTInstanceVariableDefs instances = classs.variabledefs();
		ASTInstanceVariableDef instance;
		Vector<Type> instanceTs = (Vector<Type>) classs.variabledefs().Accept(
				this);

		for (int i = 0; i < instanceTs.size(); i++) {
			instance = instances.elementAt(i);
			offset += MachineDependent.WORDSIZE;
			localVars.insert(instance.name(),
					new VariableEntry(instanceTs.elementAt(i), offset));
		}

		entry = new ClassType(localVars);
		types.insert(classs.name(), entry);

		return assembly.emptyStatement();
	}

	@Override
	public Object VisitClasses(ASTClasses classes) {
		for (int i = 0; i < classes.size(); i++)
			classes.elementAt(i).Accept(this);

		return assembly.emptyStatement();
	}

	@Override
	public Object VisitClassVariable(ASTClassVariable classvariable) {
		TypeTree aat = (TypeTree) classvariable.base().Accept(this);

		if (!(aat.type() instanceof ClassType))
			CompError.message(classvariable.line(),
					"VisitClassVariable: base must be ClassType.");

		else {
			ClassType classT = (ClassType) aat.type();
			VariableEnvironment vars = classT.variables();
			VariableEntry var = vars.find(classvariable.variable());

			if (var == null) {
				CompError
						.message(classvariable.line(),
								"VisitClassVariable: instance variable is not defined in this class.");

				return new TypeTree(IntegerType.instance(), null);
			}

			return new TypeTree(var.type(), assembly.classVariable(aat.tree(),
					var.offset()));
		}

		return new TypeTree(IntegerType.instance(), null);
	}

	@Override
	public Object VisitDoWhileStatement(ASTDoWhileStatement dowhile) {
		TypeTree test = (TypeTree) dowhile.test().Accept(this);
		AATStatement body = (AATStatement) dowhile.body().Accept(this);

		if (test.type() != BooleanType.instance())
			CompError
					.message(dowhile.line(),
							"VisitDoWhileStatement: do-while condition must be boolean.");

		return assembly.dowhileStatement(test.tree(), body);
	}

	@Override
	public Object VisitEmptyStatement(ASTEmptyStatement empty) {
		return assembly.emptyStatement();
	}

	@Override
	public Type VisitFormal(ASTFormal formal) {
		Type type = types.find(formal.type());

		if (formal.arraydimension() > 0)
			type = arrayTypeHelper(type, formal.arraydimension(), formal.type());

		return type;
	}

	@Override
	public Vector<Type> VisitFormals(ASTFormals formals) {
		Vector<Type> types = new Vector<Type>();
		for (int i = 0; i < formals.size(); i++)
			types.add((Type) formals.elementAt(i).Accept(this));

		return types;
	}

	@Override
	public Object VisitForStatement(ASTForStatement forstmt) {
		AATStatement init = (AATStatement) forstmt.initialize().Accept(this);
		AATStatement incr;
		AATStatement body;
		TypeTree test = (TypeTree) forstmt.test().Accept(this);

		if (test.type() != BooleanType.instance())
			CompError.message(forstmt.line(),
					"VisitForStatement: for-loop condition must be boolean.");

		body = (AATStatement) forstmt.body().Accept(this);
		incr = (AATStatement) forstmt.increment().Accept(this);

		return assembly.forStatement(init, test.tree(), incr, body);
	}

	@Override
	public Object VisitFunction(ASTFunction function) {
		offset = 0;
		int formalOffset = 0;
		Label startLabel;
		Type type = types.find(function.type());
		FunctionEntry checkPrototype = functions.find(function.name());
		Vector<Type> formalTs = (Vector<Type>) function.formals().Accept(this);
		int size = function.formals().size();

		if (checkPrototype == null) {
			startLabel = new Label(function.name());
			endLabel = new Label(function.name() + "end");

			functions.insert(function.name(), new FunctionEntry(type, formalTs,
					startLabel, endLabel));

		} else {
			startLabel = checkPrototype.startlabel();
			endLabel = checkPrototype.endlabel();
			int prototypeSize = checkPrototype.formals().size();

			if (size > prototypeSize) {
				CompError.message(function.line(), "VisitFunction: function "
						+ function.name() + " has too many formals.");

			} else if (size < prototypeSize) {
				CompError.message(function.line(), "VisitFunction: function "
						+ function.name() + " has too few formals.");

			} else {
				for (int i = 0; i < size; i++)
					if (formalTs.elementAt(i) != checkPrototype.formals()
							.elementAt(i)) {
						CompError.message(function.line(),
								"VisitFunction: function " + function.name()
										+ "'s formals do not match prototype.");
					}
			}
		}

		String key;
		VariableEntry enterVar;
		variables.beginScope();

		for (int j = 0; j < size; j++) {
			key = function.formals().elementAt(j).name();
			formalOffset -= 4;
			enterVar = new VariableEntry(formalTs.elementAt(j), formalOffset);
			variables.insert(key, enterVar);
		}

		variables.find("return").settype(type);
		AATStatement body = (AATStatement) function.body().Accept(this);
		variables.endScope();

		return assembly.functionDefinition(body, offset, startLabel, endLabel);
	}

	@Override
	public Object VisitFunctionCallExpression(
			ASTFunctionCallExpression functioncall) {
		Vector<AATExpression> actualsTrees = new Vector<AATExpression>();
		FunctionEntry func = functions.find(functioncall.name());

		if (func == null)
			CompError.message(
					functioncall.line(),
					"VisitFunctionCallExpression: function "
							+ functioncall.name() + " is not declared.");

		else {
			TypeTree aat;
			Type protoAAt;

			if (functioncall.size() > 0) {

				if (functioncall.size() < func.formals().size())
					CompError.message(functioncall.line(),
							"VisitFunctionCallExpression: function "
									+ functioncall.name()
									+ " has too few args.");

				if (functioncall.size() > func.formals().size())
					CompError.message(functioncall.line(),
							"VisitFunctionCallExpression: function "
									+ functioncall.name()
									+ " has too many args.");

				else {
					for (int i = 0; i < functioncall.size(); i++) {
						aat = (TypeTree) functioncall.elementAt(i).Accept(this);
						protoAAt = (Type) func.formals().elementAt(i);
						actualsTrees.add(aat.tree());

						if (aat.type() != protoAAt)
							CompError
									.message(
											functioncall.line(),
											"VisitFunctionCallExpression: function "
													+ functioncall.name()
													+ "'s args do not match prototype.");
					}
				}
			}
			return new TypeTree(func.result(), assembly.callExpression(
					actualsTrees, functions.find(functioncall.name())
							.startlabel()));
		}

		return new TypeTree(IntegerType.instance(), null);
	}

	@Override
	public Object VisitFunctionCallStatement(
			ASTFunctionCallStatement functioncall) {
		FunctionEntry func = functions.find(functioncall.name());
		Vector<AATExpression> actualsTrees = new Vector<AATExpression>();

		if (func == null)
			CompError.message(
					functioncall.line(),
					"VisitFunctionCallStatement: function "
							+ functioncall.name() + " is not declared.");

		else {
			TypeTree aat;
			Type prototype;

			if (functioncall.size() > 0) {

				if (functioncall.size() < func.formals().size())
					CompError.message(functioncall.line(),
							"VisitFunctionCallStatement: function "
									+ functioncall.name()
									+ " has too few args.");

				if (functioncall.size() > func.formals().size())
					CompError.message(functioncall.line(),
							"VisitFunctionCallStatement: function "
									+ functioncall.name()
									+ " has too many args.");

				else {
					for (int i = 0; i < functioncall.size(); i++) {
						aat = (TypeTree) functioncall.elementAt(i).Accept(this);
						prototype = (Type) func.formals().elementAt(i);
						actualsTrees.add(aat.tree());

						if (aat.type() != prototype)
							CompError
									.message(
											functioncall.line(),
											"VisitFunctionCallStatement: function "
													+ functioncall.name()
													+ "'s args do not match prototype.");
					}
				}
			}
		}

		return assembly.callStatement(actualsTrees,
				functions.find(functioncall.name()).startlabel());
	}

	@Override
	public Object VisitFunctionDefinitions(
			ASTFunctionDefinitions functiondefinitions) {

		AATStatement sequence = (AATStatement) functiondefinitions.elementAt(0)
				.Accept(this);

		for (int j = 1; j < functiondefinitions.size(); j++)
			sequence = new AATSequential(sequence,
					(AATStatement) functiondefinitions.elementAt(j)
							.Accept(this));

		return sequence;
	}

	@Override
	public Object VisitIfStatement(ASTIfStatement ifsmt) {
		TypeTree test = (TypeTree) ifsmt.test().Accept(this);
		AATStatement then = (AATStatement) ifsmt.thenstatement().Accept(this);
		AATStatement instead = assembly.emptyStatement();

		if (test.type() != BooleanType.instance())
			CompError.message(ifsmt.line(),
					"VisitIfStatement: if test must be boolean.");

		if (ifsmt.elsestatement() != null)
			instead = (AATStatement) ifsmt.elsestatement().Accept(this);

		return assembly.ifStatement(test.tree(), then, instead);
	}

	@Override
	public Object VisitIntegerLiteral(ASTIntegerLiteral literal) {

		return new TypeTree(IntegerType.instance(),
				assembly.constantExpression(literal.value()));
	}

	@Override
	public Type VisitInstanceVariableDef(ASTInstanceVariableDef variabledef) {
		Type type = types.find(variabledef.type());
		offset += 4;

		if (variabledef.arraydimension() > 0)
			type = arrayTypeHelper(type, variabledef.arraydimension(),
					variabledef.type());

		return type;
	}

	@Override
	public Vector<Type> VisitInstanceVariableDefs(
			ASTInstanceVariableDefs variabledefs) {
		Vector<Type> types = new Vector<Type>();

		for (int i = 0; i < variabledefs.size(); i++)
			types.add((Type) variabledefs.elementAt(i).Accept(this));

		return types;
	}

	@Override
	public Object VisitNewArrayExpression(ASTNewArrayExpression newarray) {
		Type type = types.find(newarray.type());
		TypeTree elements = (TypeTree) newarray.elements().Accept(this);

		if (newarray.arraydimension() > 0)
			type = arrayTypeHelper(type, newarray.arraydimension(),
					newarray.type());

		if (elements.type() != IntegerType.instance())
			CompError.message(newarray.line(),
					"VisitNewArrayExpression: elements must be int.");

		ArrayType array = (ArrayType) type;

		return new TypeTree(array, assembly.allocate(assembly
				.operatorExpression(elements.tree(),
						assembly.constantExpression(4), AATOperator.MULTIPLY)));
	}

	@Override
	public Object VisitNewClassExpression(ASTNewClassExpression newclass) {
		Type type = types.find(newclass.type());

		if (!(type instanceof ClassType))
			CompError
					.message(newclass.line(), "must be instance of ClassType.");

		ClassType classs = (ClassType) type;

		return new TypeTree(classs, assembly.allocate(new AATOperator(
				new AATConstant(offset), new AATConstant(
						MachineDependent.WORDSIZE), AATOperator.MULTIPLY)));
	}

	@Override
	public Object VisitOperatorExpression(ASTOperatorExpression opexpr) {
		TypeTree left = (TypeTree) opexpr.left().Accept(this);
		TypeTree right = (TypeTree) opexpr.right().Accept(this);
		Type ltype = left.type();
		Type rtype = right.type();
		AATExpression ltree = left.tree();
		AATExpression rtree = right.tree();

		switch (opexpr.operator()) {

		case ASTOperatorExpression.PLUS:
		case ASTOperatorExpression.MINUS:
		case ASTOperatorExpression.MULTIPLY:
		case ASTOperatorExpression.DIVIDE:

			if (ltype != IntegerType.instance())
				CompError
						.message(opexpr.line(),
								"VisitOperatorExpression: left sub-expressions must be int.");

			if (rtype != IntegerType.instance())
				CompError
						.message(opexpr.line(),
								"VisitOperatorExpression: right sub-expressions must be int.");

			return new TypeTree(
					IntegerType.instance(),
					assembly.operatorExpression(ltree, rtree, opexpr.operator()));

		case ASTOperatorExpression.LESS_THAN:
		case ASTOperatorExpression.LESS_THAN_EQUAL:
		case ASTOperatorExpression.GREATER_THAN:
		case ASTOperatorExpression.GREATER_THAN_EQUAL:

			if (ltype != IntegerType.instance())
				CompError
						.message(opexpr.line(),
								"VisitOperatorExpression: left sub-expressions must be int.");

			if (rtype != IntegerType.instance())
				CompError
						.message(opexpr.line(),
								"VisitOperatorExpression: right sub-expressions must be int.");

			return new TypeTree(
					BooleanType.instance(),
					assembly.operatorExpression(ltree, rtree, opexpr.operator()));

		case ASTOperatorExpression.AND:
		case ASTOperatorExpression.OR:

			if (ltype != BooleanType.instance())
				CompError
						.message(opexpr.line(),
								"VisitOperatorExpression: left sub-expressions must be boolean.");

			if (rtype != BooleanType.instance())
				CompError
						.message(opexpr.line(),
								"VisitOperatorExpression: right sub-expressions must be boolean.");

			return new TypeTree(
					BooleanType.instance(),
					assembly.operatorExpression(ltree, rtree, opexpr.operator()));

		case ASTOperatorExpression.EQUAL:
			if (ltype != rtype)
				CompError
						.message(opexpr.line(),
								"VisitOperatorExpression: sub-expressions must be the same type.");
			return new TypeTree(
					BooleanType.instance(),
					assembly.operatorExpression(ltree, rtree, opexpr.operator()));

		case ASTOperatorExpression.NOT_EQUAL:
			if (ltype != rtype)
				CompError
						.message(opexpr.line(),
								"VisitOperatorExpression: LHS and RHS of != must be the same type.");

			return new TypeTree(
					BooleanType.instance(),
					assembly.operatorExpression(ltree, rtree, opexpr.operator()));

		default:
			System.out.println("Error -- Illegal Operator.");
			System.out.println("Operator: " + opexpr.operator());
			return new Integer(-1);
		}
	}

	@Override
	public Object VisitProgram(ASTProgram program) {
		program.classes().Accept(this);
		return (AATSequential) program.functiondefinitions().Accept(this);
	}

	@Override
	public Object VisitPrototype(ASTPrototype prototype) {
		Type result = types.find(prototype.type());
		Vector<Type> formals = (Vector<Type>) prototype.formals().Accept(this);
		Label startLabel = new Label(prototype.name());
		endLabel = new Label(prototype.name() + "end");

		if (result == null)
			CompError.message(prototype.line(), "VisitPrototype: type "
					+ prototype.type() + " is not defined.");
		else
			functions.insert(prototype.name(), new FunctionEntry(result,
					formals, startLabel, endLabel));

		return assembly.emptyStatement();
	}

	@Override
	public Object VisitReturnStatement(ASTReturnStatement ret) {
		VariableEntry result = variables.find("return");

		if (result.type() == VoidType.instance()) {
			return assembly.emptyStatement();

		} else {
			TypeTree aat = (TypeTree) ret.value().Accept(this);

			if (result.type() != aat.type())
				CompError.message(ret.line(),
						"Return type doesn't match function definition.");

			return assembly.returnStatement(aat.tree(), endLabel);
		}

	}

	@Override
	public Object VisitStatements(ASTStatements statements) {
		variables.beginScope();

		AATStatement result = assembly.emptyStatement();

		if (statements.size() == 1) {
			result = (AATStatement) statements.elementAt(0).Accept(this);

		} else if (statements.size() > 1) {

			AATSequential sequence = new AATSequential(
					(AATStatement) statements.elementAt(0).Accept(this),
					(AATStatement) statements.elementAt(1).Accept(this));

			for (int i = 2; i < statements.size(); i++)
				sequence = new AATSequential(sequence,
						(AATStatement) statements.elementAt(i).Accept(this));

			result = (AATStatement) sequence;
		}

		variables.endScope();

		return result;
	}

	@Override
	public Object VisitUnaryOperatorExpression(
			ASTUnaryOperatorExpression operator) {
		TypeTree exp = (TypeTree) operator.operand().Accept(this);

		if (exp.type() != BooleanType.instance()) {
			CompError.message(operator.operand().line(),
					"VisitUnaryOperatorExpression: operand must be boolean.");

			return new TypeTree(IntegerType.instance(), null);
		}

		return new TypeTree(BooleanType.instance(),
				assembly.operatorExpression(exp.tree(), null, AATOperator.NOT));

	}

	@Override
	public Object VisitVariableDefStatement(ASTVariableDefStatement vardef) {
		Type type = types.find(vardef.type());
		ASTExpression initialize = vardef.init();

		if (type == null)
			CompError.message(vardef.line(), "VisitVariableDefStatement: type "
					+ vardef.type() + " is not defined.");

		else {
			if (vardef.arraydimension() > 0)
				type = arrayTypeHelper(type, vardef.arraydimension(),
						vardef.type());

			variables.insert(vardef.name(), new VariableEntry(type, offset));
			offset += MachineDependent.WORDSIZE;
		}

		if (initialize != null) {

			TypeTree initial = (TypeTree) vardef.init().Accept(this);

			if (type != initial.type())
				CompError.message(vardef.init().line(),
						"initialization-type does not match defclaration-type");

			vardef.setinit(initialize);

			return assembly.assignmentStatement(assembly.baseVariable(variables
					.find(vardef.name()).offset()), initial.tree());
		}

		return assembly.emptyStatement();
	}

	@Override
	public Object VisitVariableExpression(
			ASTVariableExpression variableexpression) {
		return (TypeTree) variableexpression.variable().Accept(this);
	}

	@Override
	public Object VisitWhileStatement(ASTWhileStatement whilestatement) {
		TypeTree test = (TypeTree) whilestatement.test().Accept(this);
		AATStatement body;

		if (test.type() != BooleanType.instance())
			CompError
					.message(whilestatement.line(),
							"VisitWhileStatement: while-loop condition must be boolean.");

		body = (AATStatement) whilestatement.body().Accept(this);

		return assembly.whileStatement(test.tree(), body);
	}

	public Type arrayTypeHelper(Type type, int dim, String key) {
		Type check;

		for (int i = 0; i < dim; i++) {
			key += "[]";
			check = types.find(key);

			if (check == null) {
				type = new ArrayType(type);
				types.insert(key, type);

			} else
				type = check;
		}

		return type;
	}

	public class TypeTree {
		Type type;
		AATExpression tree;

		public TypeTree(Type type, AATExpression tree) {
			this.type = type;
			this.tree = tree;
		}

		public Type type() {
			return type;
		}

		public AATExpression tree() {
			return tree;
		}

		public void setType(Type type) {
			this.type = type;
		}
	}

}