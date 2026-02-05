package toy_interpreter.lab11_project.Model.Expression;

import toy_interpreter.lab11_project.Model.ADT.IDictionary;
import toy_interpreter.lab11_project.Model.ADT.IHeapTable;
import toy_interpreter.lab11_project.Model.Exceptions.MyException;
import toy_interpreter.lab11_project.Model.Type.BoolType;
import toy_interpreter.lab11_project.Model.Type.IType;
import toy_interpreter.lab11_project.Model.Value.BoolValue;
import toy_interpreter.lab11_project.Model.Value.IValue;

/**
 * Equality expression that works for any two operands of the same type:
 * (exp1 == exp2)
 */
public class EqualsExpression implements IExpression {
    private final IExpression exp1;
    private final IExpression exp2;

    public EqualsExpression(IExpression exp1, IExpression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public IValue eval(IDictionary<String, IValue> symbolTable, IHeapTable<IValue> heapTable) throws MyException {
        IValue v1 = exp1.eval(symbolTable, heapTable);
        IValue v2 = exp2.eval(symbolTable, heapTable);

        if (!v1.getType().equals(v2.getType())) {
            throw new MyException("Equality check between different types: " + v1.getType() + " and " + v2.getType());
        }

        return new BoolValue(v1.equals(v2));
    }

    @Override
    public IExpression deepCopy() {
        return new EqualsExpression(exp1.deepCopy(), exp2.deepCopy());
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        IType t1 = exp1.typeCheck(typeEnv);
        IType t2 = exp2.typeCheck(typeEnv);

        if (!t1.equals(t2)) {
            throw new MyException("TYPE CHECK ERROR: == operands must have the same type. Got " + t1 + " and " + t2);
        }
        return new BoolType();
    }

    @Override
    public String toString() {
        return exp1 + " == " + exp2;
    }
}
