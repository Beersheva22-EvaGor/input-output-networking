package telran.employees.apllication.controller;

public enum Departments {
	MANAGEMENT("Management",1), QA_DEPARTMENT("QA department",2), CELLERS("Cellers",3), DEVELOPERS("Developers",4);
	
	private final String value;
    private final Integer key;

    Departments(String value, Integer key) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }
    public String getValue() {
        return value;
    }
}
