public class ExampleClass {
    
    // This method has too many parameters and is too long
    public String processData(String input1, String input2, String input3, String input4, String input5, String input6) {
        String result = "";
        
        // String concatenation in loop - performance issue
        for (int i = 0; i < 100; i++) {
            result += "iteration " + i + " processed " + input1 + " with " + input2; // This line is too long and has trailing whitespace    
        }
        
        // Complex boolean condition
        if (input1 != null && input2 != null && input3 != null && input4 != null && input5 != null && input6 != null) {
            // Deep nesting
            if (input1.length() > 0) {
                if (input2.length() > 0) {
                    if (input3.length() > 0) {
                        if (input4.length() > 0) {
                            if (input5.length() > 0) {
                                if (input6.length() > 0) {
                                    result = "All inputs are valid";
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Potential null pointer exception
        String dangerous = input1!!.toUpperCase();
        
        // Empty catch block
        try {
            Integer.parseInt(input1);
        } catch (Exception e) {
            // TODO: Handle this exception properly
        }
        
        // Unnecessary object creation
        String unnecessary = new String("This is unnecessary");
        Integer unnecessaryInt = new Integer(42);
        
        return result;
    }
    
    // This variable name is way too long and descriptive for what it does
    private String thisIsAVeryLongVariableNameThatExceedsTheRecommendedLength = "test";
    
    // Method with complex condition
    public boolean isValid(String data, int count, boolean flag) {
        return data != null && data.length() > 0 && count > 0 && count < 100 && flag == true && data.contains("valid") && data.startsWith("prefix") && data.endsWith("suffix");
    }
}