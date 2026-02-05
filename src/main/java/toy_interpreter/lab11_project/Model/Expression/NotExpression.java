package toy_interpreter.lab11_project.Model.Expression;

import toy_interpreter.lab11_project.Model.ADT.IDictionary;
import toy_interpreter.lab11_project.Model.ADT.IHeapTable;
import toy_interpreter.lab11_project.Model.Exceptions.MyException;
import toy_interpreter.lab11_project.Model.Type.BoolType;
import toy_interpreter.lab11_project.Model.Type.IType;
import toy_interpreter.lab11_project.Model.Value.BoolValue;
import toy_interpreter.lab11_project.Model.Value.IValue;

public class NotExpression implements IExpression {
    private final IExpression exp;

    public NotExpression(IExpression exp) {
        this.exp = exp;
    }

    @Override
    public IValue eval(IDictionary<String, IValue> symbolTable, IHeapTable<IValue> heapTable) throws MyException {
        IValue v = exp.eval(symbolTable, heapTable);
        if (!v.getType().equals(new BoolType())) {
            throw new MyException("ERROR: operand of ! is not bool: " + exp);
        }
        return new BoolValue(!((BoolValue) v).getValue());
    }

    @Override
    public IExpression deepCopy() {
        return new NotExpression(exp.deepCopy());
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        IType t = exp.typeCheck(typeEnv);
        if (!t.equals(new BoolType())) {
            throw new MyException("TYPE CHECK ERROR: operand of ! is not bool: " + exp);
        }
        return new BoolType();
    }

    @Override
    public String toString() {
        return "!(" + exp + ")";
    }
}
