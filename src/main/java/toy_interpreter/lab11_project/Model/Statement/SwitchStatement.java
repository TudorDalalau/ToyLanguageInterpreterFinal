package toy_interpreter.lab11_project.Model.Statement;

import toy_interpreter.lab11_project.Model.ADT.IDictionary;
import toy_interpreter.lab11_project.Model.Exceptions.MyException;
import toy_interpreter.lab11_project.Model.Expression.EqualsExpression;
import toy_interpreter.lab11_project.Model.Expression.IExpression;
import toy_interpreter.lab11_project.Model.ProgramState.ProgramState;
import toy_interpreter.lab11_project.Model.Type.IType;

/**
 * switch(exp) (case exp1: stmt1) (case exp2: stmt2) (default: stmt3)
 *
 * Execution: replaces itself on the execution stack with:
 * if(exp==exp1) then stmt1 else (if (exp==exp2) then stmt2 else stmt3)
 */
public class SwitchStatement implements IStatement {
    private final IExpression mainExp;
    private final IExpression caseExp1;
    private final IStatement caseStmt1;
    private final IExpression caseExp2;
    private final IStatement caseStmt2;
    private final IStatement defaultStmt;

    public SwitchStatement(IExpression mainExp,
                           IExpression caseExp1, IStatement caseStmt1,
                           IExpression caseExp2, IStatement caseStmt2,
                           IStatement defaultStmt) {
        this.mainExp = mainExp;
        this.caseExp1 = caseExp1;
        this.caseStmt1 = caseStmt1;
        this.caseExp2 = caseExp2;
        this.caseStmt2 = caseStmt2;
        this.defaultStmt = defaultStmt;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        IStatement desugared =
                new IfStatement(
                        new EqualsExpression(mainExp, caseExp1),
                        caseStmt1,
                        new IfStatement(
                                new EqualsExpression(mainExp, caseExp2),
                                caseStmt2,
                                defaultStmt
                        )
                );

        currentState.getExecutionStack().push(desugared);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new SwitchStatement(
                mainExp.deepCopy(),
                caseExp1.deepCopy(), caseStmt1.deepCopy(),
                caseExp2.deepCopy(), caseStmt2.deepCopy(),
                defaultStmt.deepCopy()
        );
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        IType tMain = mainExp.typeCheck(typeEnv);
        IType t1 = caseExp1.typeCheck(typeEnv);
        IType t2 = caseExp2.typeCheck(typeEnv);

        if (!tMain.equals(t1) || !tMain.equals(t2)) {
            throw new MyException("TYPE CHECK ERROR: switch expressions must have the same type. " +
                    "Got main=" + tMain + ", case1=" + t1 + ", case2=" + t2);
        }

        caseStmt1.typeCheck(typeEnv.shallowCopy());
        caseStmt2.typeCheck(typeEnv.shallowCopy());
        defaultStmt.typeCheck(typeEnv.shallowCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "switch(" + mainExp + ") " +
                "(case " + caseExp1 + ": " + caseStmt1 + ") " +
                "(case " + caseExp2 + ": " + caseStmt2 + ") " +
                "(default: " + defaultStmt + ")";
    }
}
