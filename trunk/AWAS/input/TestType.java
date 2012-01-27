package input;

public class TestType {

	public static final String KEYWORD_PARTIALTEXT 	= "Partial Text";
	public static final String KEYWORD_TITLE 	= "Title";


	public enum TestTypeEnum {PARTIALTEXT, TITLE};

	public static TestTypeEnum getTestType(String keyword){
		if (keyword.contentEquals(KEYWORD_PARTIALTEXT))
			return TestTypeEnum.PARTIALTEXT;
		else
		if (keyword.contentEquals(KEYWORD_TITLE))
			return TestTypeEnum.TITLE;
		else 
		{
			System.out.println("Keyword of Input Type: "+keyword);			
		    return TestTypeEnum.PARTIALTEXT;	
		}
	}
}
