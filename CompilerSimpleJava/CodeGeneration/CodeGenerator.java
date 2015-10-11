import java.io.*;

class CodeGenerator implements AATVisitor {

	public CodeGenerator(String output_filename) {
		try {
			output = new PrintWriter(new FileOutputStream(output_filename));
		} catch (IOException e) {
			System.out.println("Could not open file " + output_filename
					+ " for writing.");
		}
		/* Feel free to add code here, if you want to */
		EmitSetupCode();
	}

	public Object VisitCallExpression(AATCallExpression expression) {
		int n = 0;
		int calculate = 0;
		int offset = 0;

		for (int i = expression.actuals().size() - 1; i >= 0; i--) {
			((AATExpression) expression.actuals().elementAt(i)).Accept(this);
			n++;
			calculate = (4 * n) - 4;
			offset = calculate * -1;
			emit("sw " + Register.ACC() + ", " + offset + "(" + Register.SP()
					+ ")");
		}

		n *= -4;
		emit("addi " + Register.SP() + ", " + Register.SP() + ", " + n);
		emit("jal " + expression.label());
		n *= -1;
		emit("addi " + Register.SP() + ", " + Register.SP() + ", " + n);
		emit("addi " + Register.ACC() + ", " + Register.Result() + ", " + 0);

		return null;
	}

	public Object VisitMemory(AATMemory expression) {
		if (expression.mem() instanceof AATOperator) {
			AATOperator operator = (AATOperator) expression.mem();

			if (operator.operator() == AATOperator.PLUS
					|| operator.operator() == AATOperator.MINUS) {

				if (operator.right() instanceof AATConstant) {
					AATConstant constant = (AATConstant) operator.right();

					if (operator.left() instanceof AATRegister) {
						AATRegister register = (AATRegister) operator.left();
						emit("lw " + Register.ACC() + ", " + constant.value()
								+ "(" + register.register() + ")");
					} else {
						AATMemory memory = (AATMemory) operator.left();
						memory.mem().Accept(this);
						emit("lw " + Register.ACC() + ", " + constant.value()
								+ "(" + Register.ACC() + ")");
					}
				}
			}
		}

		expression.mem().Accept(this);
		emit("lw " + Register.ACC() + ", " + 0 + "(" + Register.ACC() + ")");
		return null;
	}

	public Object VisitOperator(AATOperator expression) {
		expression.left().Accept(this);
		emit("sw " + Register.ACC() + ", " + 0 + "(" + Register.ESP() + ")");
		emit("addi " + Register.ESP() + ", " + Register.ESP() + ", " + (-4));

		if (expression.operator() != AATOperator.NOT) {
			expression.right().Accept(this);
			emit("lw " + Register.Tmp1() + ", " + 4 + "(" + Register.ESP()
					+ ")");
			emit("addi " + Register.ESP() + ", " + Register.ESP() + ", " + 4);
			
		} else {
			emit("addi " + Register.Tmp1() + ", " + Register.Zero() + ", " + 1);
			emit("sub " + Register.ACC() + ", " + Register.Tmp1() + ", " + Register.ACC());
			
			return null;
		}

		switch (expression.operator()) {

		case AATOperator.PLUS:
			emit("add " + Register.ACC() + ", " + Register.Tmp1() + ", "
					+ Register.ACC());
			break;

		case AATOperator.MINUS:
			emit("sub " + Register.ACC() + ", " + Register.Tmp1() + ", "
					+ Register.ACC());
			break;

		case AATOperator.MULTIPLY:
			emit("mult " + Register.ACC() + ", " + Register.Tmp1());
			emit("mflo " + Register.ACC());
			break;

		case AATOperator.DIVIDE:
			emit("div " + Register.Tmp1() + ", " + Register.ACC());
			emit("mflo " + Register.ACC());
			break;

		case AATOperator.LESS_THAN:
			emit("slt " + Register.ACC() + ", " + Register.Tmp1() + ", "
					+ Register.ACC());
			break;

		case AATOperator.LESS_THAN_EQUAL:
			emit("addi " + Register.ACC() + ", " + Register.ACC() + ", " + 1);
			emit("slt " + Register.ACC() + ", " + Register.Tmp1() + ", "
					+ Register.ACC());
			break;

		case AATOperator.GREATER_THAN:
			emit("slt " + Register.ACC() + ", " + Register.ACC() + ", "
					+ Register.Tmp1());
			break;

		case AATOperator.GREATER_THAN_EQUAL:
			emit("addi " + Register.Tmp1() + ", " + Register.Tmp1() + ", " + 1);
			emit("slt " + Register.ACC() + ", " + Register.ACC() + ", "
					+ Register.Tmp1());
			break;

		case AATOperator.AND:
			emit("and " + Register.ACC() + ", " + Register.Tmp1() + ", "
					+ Register.ACC());
			break;

		case AATOperator.OR:
			emit("or " + Register.ACC() + ", " + Register.Tmp1() + ", "
					+ Register.ACC());
			break;

		case AATOperator.EQUAL:
			AATLabel fallThrough = new AATLabel(new Label("fallThrough"));
			AATLabel branch = new AATLabel(new Label("branch"));

			emit("beq " + Register.Tmp1() + ", " + Register.ACC() + ", "
					+ branch.label());
			emit("addi " + Register.ACC() + ", " + Register.Zero() + ", " + 0);
			emit("j " + fallThrough.label());
			emit(branch.label() + ": ");
			emit("addi " + Register.ACC() + ", " + Register.Zero() + ", " + 1);
			emit(fallThrough.label() + ": ");
			break;

		case AATOperator.NOT_EQUAL:
			AATLabel fallThrough2 = new AATLabel(new Label("fallThrough"));
			AATLabel branch2 = new AATLabel(new Label("branch"));

			emit("beq " + Register.Tmp1() + ", " + Register.ACC() + ", "
					+ branch2.label());
			emit("addi " + Register.ACC() + ", " + Register.Zero() + ", " + 1);
			emit("j " + fallThrough2.label());
			emit(branch2.label() + ": ");
			emit("addi " + Register.ACC() + ", " + Register.Zero() + ", " + 0);
			emit(fallThrough2.label() + ": ");
			break;

		}
		return null;
	}

	public Object VisitRegister(AATRegister expression) {
		emit("addi " + Register.ACC() + ", " + expression.register() + ", " + 0);
		return null;
	}

	public Object VisitCallStatement(AATCallStatement statement) {
		int n = 0;
		int calculate = 0;
		int offset = 0;

		for (int i = statement.actuals().size() - 1; i >= 0; i--) {
			((AATExpression) statement.actuals().elementAt(i)).Accept(this);
			n++;
			calculate = (4 * n) - 4;
			offset = calculate * -1;
			emit("sw " + Register.ACC() + ", " + offset + "(" + Register.SP()
					+ ")");
		}

		n *= -4;
		emit("addi " + Register.SP() + ", " + Register.SP() + ", " + n);
		emit("jal " + statement.label());
		n *= -1;

		return null;
	}

	public Object VisitConditionalJump(AATConditionalJump statement) {
		statement.test().Accept(this);
		emit("bgtz " + Register.ACC() + ", " + statement.label());
		return null;
	}

	public Object VisitEmpty(AATEmpty statement) {
		return null;
	}

	public Object VisitJump(AATJump statement) {
		emit("j " + statement.label());
		return null;
	}

	public Object VisitLabel(AATLabel statement) {
		emit(statement.label() + ":");
		return null;
	}

	public Object VisitMove(AATMove statement) {

		// LHS is a register
		if (statement.lhs() instanceof AATRegister) {
			AATRegister left = (AATRegister) statement.lhs();
			statement.rhs().Accept(this);
			emit("addi " + left.register() + ", " + Register.ACC() + ", " + 0);
			
			// LHS is memory
		} else {
			AATMemory left = (AATMemory) statement.lhs();
	
			// LHS-memory is an operator
			if (left.mem() instanceof AATOperator) {
				AATOperator op = (AATOperator) left.mem();
				
				// RHS is a register
				if (statement.rhs() instanceof AATRegister) { 
					AATRegister rhsReg = (AATRegister) statement.rhs();
					
					// left-operand is a register and right-operand is constant
					if (op.left() instanceof AATRegister && op.right() instanceof AATConstant) {
						AATRegister operandReg = (AATRegister) op.left();
						AATConstant con = (AATConstant) op.right();
						int offset = con.value() * (-1);
						
						emit("sw " + rhsReg.register() + ", " + offset + "(" + operandReg.register() + ")");
						
						return null;
					}
					
					// RHS is a subtree
				} else if (statement.rhs() instanceof AATExpression) {
					AATExpression subtree = (AATExpression) statement.rhs();
					
					// left-operand is a register and right-operand is a constant
					if (op.left() instanceof AATRegister && op.right() instanceof AATConstant) {
						AATRegister operandReg = (AATRegister) op.left();
						AATConstant con = (AATConstant) op.right();
						int offset = con.value() * (-1);
						
						subtree.Accept(this);
						emit("sw " + Register.ACC() + ", " + offset + "(" + operandReg.register() + ")");
						
						return null;
						
						// left-operand is a subtree and right-operand is a constant
					} else if (op.left() instanceof AATExpression && op.right() instanceof AATConstant) {						
						AATExpression operandSubtree = (AATExpression) op.left();
						AATConstant con = (AATConstant) op.right();
						int offset = con.value() * (-1);
						
						operandSubtree.Accept(this);
						emit("sw " + Register.ACC() + ", " + 0 + "(" + Register.ESP() + ")");
						emit("addi " + Register.ESP() + ", " + Register.ESP() + ", " + (-4));
						
						subtree.Accept(this);
						emit("lw " + Register.Tmp1() + ", " + 4 + "(" + Register.ESP() + ")");
						emit("addi " + Register.ESP() + ", " + Register.ESP() + ", " + 4);
						emit("sw " + Register.ACC() + ", " + offset + "(" + Register.Tmp1() + ")");
						
						return null;
					}		
				}
			
				// LHS-memory is a subtree
			} else if (left.mem() instanceof AATExpression) {
				AATExpression subtree = (AATExpression) left.mem();
				
				// RHS is a register
				if (statement.rhs() instanceof AATRegister) {
					AATRegister rhsReg = (AATRegister) statement.rhs();
					
					subtree.Accept(this);
					emit("sw " + rhsReg.register() + ", " + 0 + "(" + Register.ACC() + ")");
					
					return null;
					
					// RHS is a subtree
				} else if (statement.rhs() instanceof AATExpression) {
					AATExpression rhsSubtree = (AATExpression) statement.rhs();
					
					subtree.Accept(this);
					emit("sw " + Register.ACC() + 0 + "(" + Register.ESP() + ")");
					emit("addi " + Register.ESP() + ", " + Register.ESP() + ", " + (-4));
					
					rhsSubtree.Accept(this);
					emit("lw " + Register.Tmp1() + ", " + 4 + "(" + Register.ESP() + ")");
					emit("addi " + Register.ESP() + ", " + Register.ESP() + ", " +4);
					emit("sw " + Register.ACC() + 0 + "(" + Register.Tmp1() + ")");
					
					return null;
				}		
			}
			
			// code for small tiles
			left.mem().Accept(this);
			emit("sw " + Register.ACC() + ", " + 0 + "(" + Register.ESP() + ")");
			emit("addi " + Register.ESP() + ", " + Register.ESP() + ", " + (-4));

			statement.rhs().Accept(this);
			emit("lw " + Register.Tmp1() + ", " + 4 + "(" + Register.ESP()
					+ ")");
			emit("addi " + Register.ESP() + ", " + Register.ESP() + ", " + 4);
			emit("sw " + Register.ACC() + ", " + 0 + "(" + Register.Tmp1()
					+ ")");
			
		}
		return null;
	}

	public Object VisitReturn(AATReturn statement) {
		emit("jr " + Register.ReturnAddr());
		return null;
	}

	public Object VisitHalt(AATHalt halt) {
		/*
		 * Don't need to implement halt -- you can leave this as it is, if you
		 * like
		 */
		return null;
	}

	public Object VisitSequential(AATSequential statement) {
		statement.left().Accept(this);
		statement.right().Accept(this);
		return null;
	}

	public Object VisitConstant(AATConstant expression) {
		emit("addi " + Register.ACC() + ", " + Register.Zero() + ", "
				+ expression.value());
		return null;
	}

	private void emit(String assem) {
		assem = assem.trim();
		if (assem.charAt(assem.length() - 1) == ':')
			output.println(assem);
		else
			output.println("\t" + assem);
	}

	public void GenerateLibrary() {
		emit("Print:");
		emit("lw $a0, 4(" + Register.SP() + ")");
		emit("li $v0, 1");
		emit("syscall");
		emit("li $v0,4");
		emit("la $a0, sp");
		emit("syscall");
		emit("jr $ra");
		emit("Println:");
		emit("li $v0,4");
		emit("la $a0, cr");
		emit("syscall");
		emit("jr $ra");
		emit("Read:");
		emit("li $v0,5");
		emit("syscall");
		emit("jr $ra");
		emit("allocate:");
		emit("la " + Register.Tmp1() + ", HEAPPTR");
		emit("lw " + Register.Result() + ",0(" + Register.Tmp1() + ")");
		emit("lw " + Register.Tmp2() + ", 4(" + Register.SP() + ")");
		emit("sub " + Register.Tmp2() + "," + Register.Result() + ","
				+ Register.Tmp2());
		emit("sw " + Register.Tmp2() + ",0(" + Register.Tmp1() + ")");
		emit("jr $ra");
		emit(".data");
		emit("cr:");
		emit(".asciiz \"\\n\"");
		emit("sp:");
		emit(".asciiz \" \"");
		emit("HEAPPTR:");
		emit(".word 0");
		output.flush();
	}

	private void EmitSetupCode() {
		emit(".globl main");
		emit("main:");
		emit("addi " + Register.ESP() + "," + Register.SP() + ",0");
		emit("addi " + Register.SP() + "," + Register.SP() + ","
				+ -MachineDependent.WORDSIZE * STACKSIZE);
		emit("addi " + Register.Tmp1() + "," + Register.SP() + ",0");
		emit("addi " + Register.Tmp1() + "," + Register.Tmp1() + ","
				+ -MachineDependent.WORDSIZE * STACKSIZE);
		emit("la " + Register.Tmp2() + ", HEAPPTR");
		emit("sw " + Register.Tmp1() + ",0(" + Register.Tmp2() + ")");
		emit("sw " + Register.ReturnAddr() + "," + MachineDependent.WORDSIZE
				+ "(" + Register.SP() + ")");
		emit("jal main1");
		emit("li $v0, 10");
		emit("syscall");
	}

	private final int STACKSIZE = 1000;
	private PrintWriter output;
	/* Feel Free to add more instance variables, if you like */
}
