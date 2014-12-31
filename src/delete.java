import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 */

/**
 * @lyk
 * 
 */
public class delete {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<String> userName = new ArrayList();
	static Map<String, Boolean> judge = new HashMap<String, Boolean>();
	public static void main(String args[]){
	}
	@SuppressWarnings("resource")
	public static void clear() throws IOException {
		File fin = new File("F:\\map.txt");
		BufferedReader br = new BufferedReader(new FileReader(fin));
		String fs;
		while ((fs = br.readLine()) != null) {
			if (fs.length() > 2) {
				if (userName.contains(fs)) {
					judge.put(fs, true);
				} else {
					judge.put(fs, false);
					userName.add(fs);
				}
			}
		}
		br.close();
		FileOutputStream fos = new FileOutputStream("F:\\data\\mapnew.txt");
		br = new BufferedReader(new FileReader(fin));
		while((fs= br.readLine())!=null){
			if(fs.length()>2){
				if(judge.get(fs)){
					fos.write((fs+"\n").getBytes());
				}
			}
			else
				fos.write((fs+"\n").getBytes());
		}

	}
}
