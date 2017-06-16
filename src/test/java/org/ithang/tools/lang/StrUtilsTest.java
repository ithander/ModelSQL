package org.ithang.tools.lang;


import org.junit.Test;

public class StrUtilsTest {

	@Test
	public void addUnderlineTest() {
		System.out.println("abcDefGod ==>"+StrUtils.addUnderline("abcDefGod"));
	}
	
	@Test
	public void dropUnderlineTest(){
		System.out.println("abc_def_god ==> "+StrUtils.dropUnderline("abc_def_god"));
	}
	
	@Test
	public void trimPrefixTest(){
		System.out.println("abcGod==>"+StrUtils.trimPrefix("abcGod", "ab"));
		System.out.println("abcGod==>"+StrUtils.trimPrefix("abcGod", "bc"));
	}
	
	@Test
	public void trimSuffixTest(){
		System.out.println("abcGodxixi==>"+StrUtils.trimSuffix("abcGodxixi", "xi"));
		System.out.println("abcGod==>"+StrUtils.trimSuffix("abcGodxixi", "x"));
	}
	
	@Test
	public void headUpperTest(){
		System.out.println("iibb==>"+StrUtils.headUpper("iibb"));
	}
	
	@Test
	public void pkg(){
		String[] abs={"abc","efd","godj","eri"};
		System.out.println(StrUtils.pkg(abs, "_", "$,"));
	}

}
