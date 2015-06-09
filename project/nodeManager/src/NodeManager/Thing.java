package NodeManager;

public abstract class Thing {
	protected int mId = 0;
	protected int mValue = 0;
	protected Type mType = Type.Unknown;
	
    public abstract int GetID();
    public abstract void SetID(int id);
    // To Remove, if these do not need
    public abstract Type GetType();
    public abstract void SetType(Type type);
    //
    public abstract void GetValue();
    public abstract void SetValue();
    
    public abstract void doCommand();
}
