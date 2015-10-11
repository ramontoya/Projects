import java.util.Vector;

public class SemanticAnalyzer implements ASTVisitor {

	private TypeEnvironment types;
	private VariableEnvironment variables;
	private FunctionEnvironment functions;

	public SemanticAnalyzer() {
		types = new TypeEnvironment();
		variables = new VariableEnvironment();
		functions = new FunctionEnvironment();
		functions.addBuiltinFunctions();
	}

	@Override
	public Object VisitArrayVariable(ASTArrayVariable array) {
		Type index = (Type) array.index().Accept(this);
		Type type = (Type) array.base().Accept(this);

		if (index != IntegerType.instance())
			CompError.message(array.line(),
					"VisitArrayVariable: index must be int.");

		if (!(type instanceof ArrayType))
			CompError.message(array.line(),
					"VisitArrayVariable: base must be an array.");
		else {
			ArrayType baseType = (ArrayType) type;

			return baseType.type();
		}

		return IntegerType.instance();
	}

	@Override
	public Object VisitAssignmentStatement(ASTAssignmentStatement assign) {
		Type left = (Type) assign.variable().Accept(this);
		Type right = (Type) assign.value().Accept(this);

		if (left != right)
			CompError
					.message(assign.line(),
							"VisitAssignmentStatement: variable-type does not match value-type.");

		return null;
	}

	@Override
	public Object VisitBaseVariable(ASTBaseVariable base) {
		VariableEntry varEntry = variables.find(base.name());

		if (varEntry == null)
			CompError.message(base.line(), "VisitBaseVariable: variable "
					+ base.name() + " is not defined.");
		else
			return varEntry.type();

		return IntegerType.instance();

	}

	@Override
	public Object VisitBooleanLiteral(ASTBooleanLiteral boolliteral) {
		return BooleanType.instance();
	}

	@Override
	public Object VisitClass(ASTClass classs) {
		ClassType entry;
		VariableEnvironment localVars = new VariableEnvironment();
		ASTInstanceVariableDefs instances = classs.variabledefs();
		ASTInstanceVariableDef instance;
		Vector<Type> instanceTs = (Vector<Type>) classs.variabledefs().Accept(
				this);

		for (int i = 0; i < instanceTs.size(); i++) {
			instance = instances.elementAt(i);
			localVars.insert(instance.name(),
					new VariableEntry(instanceTs.elementAt(i)));
		}

		entry = new ClassType(localVars);
		types.insert(classs.name(), entry);

		return null;
	}

	@Override
	public Object VisitClasses(ASTClasses classes) {
		for (int i = 0; i < classes.size(); i++)
			classes.elementAt(i).Accept(this);

		return null;
	}

	@Override
	public Object VisitClassVariable(ASTClassVariable classvariable) {
		Type type = (Type) classvariable.base().Accept(this);

		if (!(type instanceof ClassType))
			CompError.message(classvariable.line(),
					"VisitClassVariable: base must be ClassType.");

		else {
			ClassType classT = (ClassType) type;
			VariableEnvironment vars = classT.variables();
			VariableEntry var = vars.find(classvariable.variable());

			if (var == null) {
				CompError
						.message(classvariable.line(),
								"VisitClassVariable: instance variable is not defined in this class.");

				return IntegerType.instance();
			}

			return var.type();
		}

		return IntegerType.instance();
	}

	@Override
	public Object VisitDoWhileStatement(ASTDoWhileStatement dowhile) {
		dowhile.body().Accept(this);

		Type condition = (Type) dowhile.test().Accept(this);

		if (condition != BooleanType.instance())
			CompError
					.message(dowhile.line(),
							"VisitDoWhileStatement: do-while condition must be boolean.");

		return null;
	}

	@Override
	public Object VisitEmptyStatement(ASTEmptyStatement empty) {
		return null;
	}

	@Override
	public Object VisitForStatement(ASTForStatement forstmt) {
		forstmt.initialize().Accept(this);
		Type exp = (Type) forstmt.test().Accept(this);

		if (exp != BooleanType.instance())
			CompError.message(forstmt.line(),
					"VisitForStatement: for-loop condition must be boolean.");

		forstmt.increment().Accept(this);

		return null;
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
	public Object VisitFunction(ASTFunction function) {
		Type type = types.find(function.type());
		FunctionEntry checkPrototype = functions.find(function.name());
		Vector<Type> formalTs = (Vector<Type>) function.formals().Accept(this);
		int size = function.formals().size();

		if (checkPrototype == null) {
			functions
					.insert(function.name(), new FunctionEntry(type, formalTs));

		} else {
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
			enterVar = new VariableEntry(formalTs.elementAt(j));
			variables.insert(key, enterVar);
		}

		variables.insert("return", new VariableEntry(type));
		function.body().Accept(this);
		variables.endScope();

		return null;
	}

	@Override
	public Object VisitFunctionCallExpression(
			ASTFunctionCallExpression functioncall) {
		FunctionEntry func = functions.find(functioncall.name());

		if (func == null)
			CompError.message(
					functioncall.line(),
					"VisitFunctionCallExpression: function "
							+ functioncall.name() + " is not declared.");

		else {
			Type type;
			Type prototypeType;

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
						type = (Type) functioncall.elementAt(i).Accept(this);
						prototypeType = (Type) func.formals().elementAt(i);

						if (type != prototypeType)
							CompError
									.message(
											functioncall.line(),
											"VisitFunctionCallExpression: function "
													+ functioncall.name()
													+ "'s args do not match prototype.");
					}
				}
			}

			return func.result();
		}

		return IntegerType.instance();
	}

	@Override
	public Object VisitFunctionCallStatement(
			ASTFunctionCallStatement functioncall) {
		FunctionEntry func = functions.find(functioncall.name());

		if (func == null)
			CompError.message(
					functioncall.line(),
					"VisitFunctionCallStatement: function "
							+ functioncall.name() + " is not declared.");

		else {
			Type type;
			Type prototypeType;

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
						type = (Type) functioncall.elementAt(i).Accept(this);
						prototypeType = (Type) func.formals().elementAt(i);

						if (type != prototypeType)
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

		return null;
	}

	@Override
	public Object VisitIfStatement(ASTIfStatement ifsmt) {
		Type test = (Type) ifsmt.test().Accept(this);

		if (test != BooleanType.instance())
			CompError.message(ifsmt.line(),
					"VisitIfStatement: if test must be boolean.");

		ifsmt.thenstatement().Accept(this);

		if (ifsmt.elsestatement() != null)
			ifsmt.elsestatement().Accept(this);

		return null;
	}

	@Override
	public Object VisitIntegerLiteral(ASTIntegerLiteral literal) {
		return IntegerType.instance();
	}

	@Override
	public Type VisitInstanceVariableDef(ASTInstanceVariableDef variabledef) {
		Type type = types.find(variabledef.type());

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
		Type elements = (Type) newarray.elements().Accept(this);

		if (newarray.arraydimension() > 0)
			type = arrayTypeHelper(type, newarray.arraydimension(),
					newarray.type());

		if (elements != IntegerType.instance())
			CompError.message(newarray.line(),
					"VisitNewArrayExpression: elements must be int.");

		ArrayType array = (ArrayType) type;

		return array;
	}

	@Override
	public Object VisitNewClassExpression(ASTNewClassExpression newclass) {
		Type type = types.find(newclass.type());
		ClassType classs = (ClassType) type;

		return type;
	}

	@Override
	public Object VisitOperatorExpression(ASTOperatorExpression opexpr) {
		Type left = (Type) opexpr.left().Accept(this);
		Type right = (Type) opexpr.right().Accept(this);
		// int leftVal = ((Integer) left).intValue();
		// int rightVal = ((Integer) right).intValue();

		switch (opexpr.operator()) {
		case ASTOperatorExpression.PLUS:
			// return new Integer (leftVal + rightVal);
		case ASTOperatorExpression.MINUS:
			// return new Integer (leftVal - rightVal);
		case ASTOperatorExpression.MULTIPLY:
			// return new Integer (leftVal * rightVal);
		case ASTOperatorExpression.DIVIDE:
			// return new Integer (leftVal / rightVal);

			if (left != IntegerType.instance())
				CompError
						.message(opexpr.line(),
								"VisitOperatorExpression: left sub-expressions must be int.");

			if (right != IntegerType.instance())
				CompError
						.message(opexpr.line(),
								"VisitOperatorExpression: right sub-expressions must be int.");

			return IntegerType.instance();

		case ASTOperatorExpression.LESS_THAN:
		case ASTOperatorExpression.LESS_THAN_EQUAL:
		case ASTOperatorExpression.GREATER_THAN:
		case ASTOperatorExpression.GREATER_THAN_EQUAL:

			if (left != IntegerType.instance())
				CompError
						.message(opexpr.line(),
								"VisitOperatorExpression: left sub-expressions must be int.");

			if (right != IntegerType.instance())
				CompError
						.message(opexpr.line(),
								"VisitOperatorExpression: right sub-expressions must be int.");

			return BooleanType.instance();

		case ASTOperatorExpression.AND:
		case ASTOperatorExpression.OR:
		case ASTOperatorExpression.NOT_EQUAL:

			if (left != BooleanType.instance())
				CompError
						.message(opexpr.line(),
								"VisitOperatorExpression: left sub-expressions must be boolean.");

			if (right != BooleanType.instance())
				CompError
						.message(opexpr.line(),
								"VisitOperatorExpression: right sub-expressions must be boolean.");

			return BooleanType.instance();

		case ASTOperatorExpression.EQUAL:
			if (left != right)
				CompError
						.message(opexpr.line(),
								"VisitOperatorExpression: sub-expressions must be the same type.");
			return BooleanType.instance();

		default:
			System.out.println("Error -- Illegal Operator.");
			System.out.println("Operator: " + opexpr.operator());
			return new Integer(-1);
		}
	}

	@Override
	public Object VisitProgram(ASTProgram program) {
		program.classes().Accept(this);
		program.functiondefinitions().Accept(this);
		return null;
	}

	@Override
	public Object VisitFunctionDefinitions(
			ASTFunctionDefinitions functiondefinitions) {
		for (int i = 0; i < functiondefinitions.size(); i++)
			functiondefinitions.elementAt(i).Accept(this);

		return null;
	}

	@Override
	public Object VisitPrototype(ASTPrototype prototype) {
		Type result = types.find(prototype.type());
		Vector<Type> formals = (Vector<Type>) prototype.formals().Accept(this);

		if (result == null)
			CompError.message(prototype.line(), "VisitPrototype: type "
					+ prototype.type() + " is not defined.");
		else
			functions.insert(prototype.name(), new FunctionEntry(result,
					formals));

		return null;
	}

	@Override
	public Object VisitReturnStatement(ASTReturnStatement ret) {
		VariableEntry result = variables.find("return");

		if (ret.value() == null) {
			if (result.type() != VoidType.instance())
				CompError.message(ret.line(),
						"Return type doesn't match function definition.");

		} else {
			Type type = (Type) ret.value().Accept(this);

			if (result.type() != type)
				CompError.message(ret.line(),
						"Return type doesn't match function definition.");
		}

		return null;
	}

	@Override
	public Object VisitStatements(ASTStatements statements) {
		variables.beginScope();

		for (int i = 0; i < statements.size(); i++)
			statements.elementAt(i).Accept(this);

		variables.endScope();

		return null;
	}

	@Override
	public Object VisitUnaryOperatorExpression(
			ASTUnaryOperatorExpression operator) {
		Type type = (Type) operator.operand().Accept(this);

		if (type != BooleanType.instance())
			CompError.message(operator.operand().line(),
					"VisitUnaryOperatorExpression: operand must be boolean.");

		return type;
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

			variables.insert(vardef.name(), new VariableEntry(type));
		}
		
		if(initialize != null) {
			Type inittype = (Type) vardef.init().Accept(this);
			if (type != inittype)
				CompError.message(vardef.init().line(), "initialization-type does not match defclaration-type");
		}

		return null;
	}

	@Override
	public Object VisitVariableExpression(
			ASTVariableExpression variableexpression) {

		return (Type) variableexpression.variable().Accept(this);
	}

	@Override
	public Object VisitWhileStatement(ASTWhileStatement whilestatement) {
		Type condition = (Type) whilestatement.test().Accept(this);

		if (condition != BooleanType.instance())
			CompError
					.message(whilestatement.line(),
							"VisitWhileStatement: while-loop condition must be boolean.");

		whilestatement.body().Accept(this);

		return null;
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
}