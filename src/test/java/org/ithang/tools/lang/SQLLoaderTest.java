package org.ithang.tools.lang;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.ithang.tools.SQLLoader;
import org.junit.Test;

public class SQLLoaderTest {

	@Test
	public void test() {
		List<File> files=new ArrayList<File>();
		String dirStr=SQLLoaderTest.class.getResource("/").getPath();
		System.out.println(dirStr);
		SQLLoader.loadSQLFile(new File(dirStr), files);
		files.forEach(d->{
			System.out.println(d.getName());
		});
	}

}
