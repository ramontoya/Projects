import java.util.Vector;

public class AATBuildTree {

	public AATStatement functionDefinition(AATStatement body, int framesize,
			Label start, Label end) {
		AATLabel startLabel = new AATLabel(start);
		AATLabel endLabel = new AATLabel(end);

		AATRegister sp = new AATRegister(Register.SP());
		AATRegister fp = new AATRegister(Register.FP());
		AATRegister ra = new AATRegister(Register.ReturnAddr());

		AATExpression framePos = constantExpression(framesize);
		AATExpression stackPos = constantExpression(framesize + 4);
		AATExpression returnPos = constantExpression(framesize + 8);
		AATExpression decrementStack = constantExpression(framesize + 12);

		/** Set up Activation Record */
		// save FP to 0($sp)
		AATMove savefp = new AATMove(new AATMemory(operatorExpression(sp,
				framePos, AATOperator.MINUS)), fp);

		// save SP to -4($sp)
		AATMove savesp = new AATMove(new AATMemory(operatorExpression(sp,
				stackPos, AATOperator.MINUS)), sp);

		// save RA to -8($sp)
		AATMove savera = new AATMove(new AATMemory(operatorExpression(sp,
				returnPos, AATOperator.MINUS)), ra);

		// set fp to beginning of current activation record
		AATMove setfp = new AATMove(fp, sp);

		// sp = sp - 12
		AATMove setsp = new AATMove(sp, operatorExpression(sp, decrementStack,
				AATOperator.MINUS));

		/** Clean up Activation Record */
		// restore ra
		AATMove restorera = new AATMove(ra, new AATMemory(operatorExpression(fp, returnPos,
				AATOperator.MINUS)));

		// restore sp
		AATMove restoresp = new AATMove(sp, new AATMemory(operatorExpression(fp, stackPos,
				AATOperator.MINUS)));

		// restore fp
		AATMove restorefp = new AATMove(fp, new AATMemory(operatorExpression(sp, framePos,
				AATOperator.MINUS)));

		// SEQUENCE
		AATSequential eleventh = new AATSequential(restorefp, new AATReturn());
		AATSequential tenth = new AATSequential(restoresp, eleventh);
		AATSequential ninth = new AATSequential(restorera, tenth);
		AATSequential eigth = new AATSequential(endLabel, ninth);
		AATSequential seventh = new AATSequential(body, eigth);
		AATSequential sixth = new AATSequential(setsp, seventh);
		AATSequential fifth = new AATSequential(setfp, sixth);
		AATSequential fourth = new AATSequential(savera, fifth);
		AATSequential third = new AATSequential(savesp, fourth);
		AATSequential second = new AATSequential(savefp, third);

		//		AATPrintTree pt = new AATPrintTree();
		//		System.out.println("--------");
		///		second.Accept(pt);
		//		System.out.println("=========");
		return new AATSequential(startLabel, second);
	}

	public AATStatement ifStatement(AATExpression test, AATStatement ifbody,
			AATStatement elsebody) {
		Label then = new Label("testPassed");
		Label end = new Label("end");

		AATLabel thenLabel = new AATLabel(then);
		AATLabel endLabel = new AATLabel(end);

		AATConditionalJump jumpToThen = new AATConditionalJump(test, thenLabel.label());
		AATJump jumpToEnd = new AATJump(endLabel.label());

		AATSequential fifth = new AATSequential(ifbody, endLabel);
		AATSequential fourth = new AATSequential(thenLabel, fifth);
		AATSequential third = new AATSequential(jumpToEnd, fourth);
		AATSequential second = new AATSequential(elsebody, third);

		return new AATSequential(jumpToThen, second);
	}

	public AATExpression allocate(AATExpression size) {
		Label allocatelabel = Label.AbsLabel("allocate");
		Vector<AATExpression> sizeVector = new Vector<AATExpression>();
		sizeVector.add(size);

		return callExpression(sizeVector, allocatelabel);
	}

	public AATStatement whileStatement(AATExpression test,
			AATStatement whilebody) {
		Label start = new Label("start");
		Label whileTest = new Label("test");

		AATLabel startLabel = new AATLabel(start);
		AATLabel whileTestLabel = new AATLabel(whileTest);

		AATJump jumpToTest = new AATJump(whileTestLabel.label());
		AATConditionalJump jumpToStart = new AATConditionalJump(test, startLabel.label());

		AATSequential fourth = new AATSequential(whileTestLabel, jumpToStart);
		AATSequential third = new AATSequential(whilebody, fourth);
		AATSequential second = new AATSequential(startLabel, third);

		return new AATSequential(jumpToTest, second);
	}

	public AATStatement dowhileStatement(AATExpression test,
			AATStatement dowhilebody) {
		Label start = new Label("start");

		AATLabel startLabel = new AATLabel(start);

		AATConditionalJump jumpToStart = new AATConditionalJump(test, startLabel.label());

		AATSequential second = new AATSequential(dowhilebody, jumpToStart);

		return new AATSequential(startLabel, second);
	}

	public AATStatement forStatement(AATStatement init, AATExpression test,
			AATStatement increment, AATStatement body) {
		Label start = new Label("start");
		Label forTest = new Label("test");

		AATLabel startLabel = new AATLabel(start);
		AATLabel forTestLabel = new AATLabel(forTest);

		AATJump jumpToTest = new AATJump(forTestLabel.label());
		AATConditionalJump jumpToStart = new AATConditionalJump(test, startLabel.label());

		AATSequential sixth = new AATSequential(forTestLabel, jumpToStart);
		AATSequential fifth = new AATSequential(increment, sixth);
		AATSequential fourth = new AATSequential(body, fifth);
		AATSequential third = new AATSequential(startLabel, fourth);
		AATSequential second = new AATSequential(jumpToTest, third);

		return new AATSequential(init, second);
	}

	public AATStatement emptyStatement() {
		return new AATEmpty();
	}

	public AATStatement callStatement(Vector<AATExpression> actuals, Label name) {
		return new AATCallStatement(name, actuals);
	}

	public AATStatement assignmentStatement(AATExpression lhs, AATExpression rhs) {
		return new AATMove(lhs, rhs);
	}

	public AATStatement sequentialStatement(AATStatement first,
			AATStatement second) {
		return new AATSequential(first, second);
	}

	public AATExpression baseVariable(int offset) {
		return new AATMemory(new AATOperator(new AATRegister(Register.FP()),
				new AATConstant(offset), AATOperator.MINUS));
	}

	public AATExpression arrayVariable(AATExpression base, AATExpression index,
			int elementSize) {
		AATOperator offset = new AATOperator(index,
				new AATConstant(elementSize), AATOperator.MULTIPLY);

		return new AATMemory(new AATOperator(base, offset, AATOperator.MINUS));
	}

	public AATExpression classVariable(AATExpression base, int offset) {
		return new AATMemory(new AATOperator(base, new AATConstant(offset*4),
				AATOperator.MINUS));
	}

	public AATExpression constantExpression(int value) {
		return new AATConstant(value);
	}

	public AATExpression operatorExpression(AATExpression left,
			AATExpression right, int operator) {
		return new AATOperator(left, right, operator);
	}

	public AATExpression callExpression(Vector<AATExpression> actuals, Label name) {
		return new AATCallExpression(name, actuals);
	}

	public AATStatement returnStatement(AATExpression value, Label functionend) {
		AATMove move = new AATMove(new AATRegister(Register.Result()), value);
		AATJump jump = new AATJump(functionend);
		AATSequential returnSequence = new AATSequential(move, jump);

		return returnSequence;
	}
}
