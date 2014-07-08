package de.longor1996.jasic;

/**
 * A string value.
 */
public class StringValue implements Value {
    public StringValue(String value) {
        this.value = value;
    }
    
    @Override public String toString() { return this.value; }
    @Override
	public double toNumber() { return Double.parseDouble(this.value); }
    @Override
	public Value evaluate() { return this; }
    
    @Override public boolean equals(Object o){
    	if(o instanceof StringValue) {
			if(((StringValue) o).value.equals(this.value)) {
				return true;
			}
		}
    	
    	if(o instanceof String) {
			if(((String) o).equals(this.value)) {
				return true;
			}
		}
    	
    	return false;
    }

    private final String value;
}