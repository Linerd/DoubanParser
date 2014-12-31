import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class combine {
	public static void main() throws IOException {

		FileOutputStream result = new FileOutputStream("F:\\map.txt");
		BufferedReader br;
		File file;
		for (int i = 1; i <= 158; i++) {
			file = new File("F:\\data\\out\\out" + i + ".txt");
			br = new BufferedReader(new FileReader(file));
			String ResultStream;
			while ((ResultStream = br.readLine()) != null) {
				result.write((ResultStream + '\n').getBytes());
			}
			br.close();
		}
		result.close();
	}
}