package toy_interpreter.lab11_project.Model.Statement;

import toy_interpreter.lab11_project.Model.ADT.IDictionary;
import toy_interpreter.lab11_project.Model.Exceptions.MyException;
import toy_interpreter.lab11_project.Model.Expression.IExpression;
import toy_interpreter.lab11_project.Model.Expression.NotExpression;
import toy_interpreter.lab11_project.Model.ProgramState.ProgramState;
import toy_interpreter.lab11_project.Model.Type.BoolType;
import toy_interpreter.lab11_project.Model.Type.IType;

/**
 * repeat stmt1 until exp2
 *
 * Desugaring on the execution stack (exactly like the statement requires):
 * - pop repeat
 * - create: stmt1; (while(!exp2) stmt1)
 * - push the new statement
 */
public class RepeatUntilStatement implements IStatement {
    private final IStatement stmt1;
    private final IExpression exp2;

    public RepeatUntilStatement(IStatement stmt1, IExpression exp2) {
        this.stmt1 = stmt1;
        this.exp2 = exp2;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        // stmt1 ; while(!exp2) stmt1
        IStatement desugared = new CompoundStatement(
                stmt1,
                new WhileStatement(new NotExpression(exp2), stmt1.deepCopy())
        );

        currentState.getExecutionStack().push(desugared);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new RepeatUntilStatement(stmt1.deepCopy(), exp2.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        IType t = exp2.typeCheck(typeEnv);
        if (!t.equals(new BoolType())) {
            throw new MyException("TYPE CHECK ERROR: repeat-until condition is not bool: " + exp2);
        }
        stmt1.typeCheck(typeEnv.shallowCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "repeat(" + stmt1 + ") until(" + exp2 + ")";
    }
}
