import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

//@lyk
public class parser {
	public static FileOutputStream fos;
	public static FileOutputStream result;
	public static FileOutputStream Num;
	public static FileOutputStream log_List;
	public static FileOutputStream log_Name;
	public static boolean begin = true;
	public static boolean flag = false;
	public static boolean flag4reboot = false;
	public static boolean finish = false;
	public static int count;
	public static int countALL;
	public static int fileNum;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<String> list = new ArrayList();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<String> userName = new ArrayList();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<String> follow = new ArrayList();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<String> followED = new ArrayList();
	public final static String a = "��ע��";

	public static String FileNum = "F:\\data\\num.txt";
	public static String FileList = "F:\\data\\list";
	public static String FileName = "F:\\data\\name";
	public static String FileOut = "F:\\data\\out\\out";

	static Map<String, String> cookies = new HashMap<String, String>();

	public static String doubanURL = "http://www.douban.com/people";

	public static void parse(String url) throws IOException,
			InterruptedException {
		/* �����ӵ�������ҳ */
		Document docMAIN = Jsoup.connect(url)
				.userAgent("(Windows NT 6.1; WOW64)").cookies(cookies)
				.timeout(20000).get();

		/* �ҵ���עҳ�� */

		if (docMAIN.select("div[id=content]").select("div[class=aside]")
				.select("div[id=friend]").select("h2").select("span[class=pl]")
				.select("a").hasAttr("href")) {

			String allfollowURL = url + "contacts";
			/* ���ӵ���עҳ�� */
			Document docFOLLOW = Jsoup.connect(allfollowURL)
					.userAgent("(Windows NT 6.1; WOW64)").cookies(cookies)
					.timeout(20000).get();
			/* ѡ��һ����ע */
			Iterator<Element> iteratorFollow = docFOLLOW
					.select("div[id=content]").select("div[class=article]")
					.select("dl[class=obu]").select("dd").select("a")
					.iterator();
			while (iteratorFollow.hasNext()) {
				Element e = iteratorFollow.next();
				if (!e.text().toString().contains(a)) {
					follow.add(e.attr("href").substring(28));
				}
			}
		} else {
			System.out.println(url + "�Ҳ�����עҳ��");
			list.remove(0);
			follow.clear();
			followED.clear();
			if (list.size() == 0)
				return;
			parse(list.get(0));
			return;
		}
		/* �ҵ���˿ҳ������ */
		if (docMAIN.select("div[id=content]").select("div[class=aside]")
				.select("p[class=rev-link]").select("a").hasAttr("href")) {
			String allfriendURL = docMAIN.select("div[id=content]")
					.select("div[class=aside]").select("p[class=rev-link]")
					.select("a").attr("href");
			/* ���ӵ���˿ҳ�� */
			Document docFOLLOWED = Jsoup.connect(allfriendURL)
					.userAgent("(Windows NT 6.1; WOW64)").cookies(cookies)
					.timeout(20000).get();

			/* ��ȡ��һҳ */
			Document docFRI = docFOLLOWED.clone();
			Iterator<Element> iteratorFANS = docFRI.select("div[id=content]")
					.select("div[class=article]").select("dl[class=obu]")
					.select("dd").select("a").iterator();
			while (iteratorFANS.hasNext()) {
				followED.add(iteratorFANS.next().attr("href").substring(28));
			}
			/* ����ж���һҳ */
			if (docFOLLOWED.select("div[id=content]")
					.select("div[class=article]")
					.select("div[class=paginator]").select("a").hasAttr("href")) {

				Iterator<Element> iteratorPAGE = docFOLLOWED
						.select("div[id=content]").select("div[class=article]")
						.select("div[class=paginator]").select("a").iterator();

				/* �������һҳ */
				while (iteratorPAGE.hasNext()) {
					Element pageNUM = iteratorPAGE.next();
					if (!pageNUM.text().toString().contains("��ҳ")) {
						String nextPAGE = "http://www.douban.com"
								+ pageNUM.attr("href");
						docFRI = Jsoup.connect(nextPAGE)
								.userAgent("(Windows NT 6.1; WOW64)")
								.cookies(cookies).timeout(20000).get();
						iteratorFANS = docFRI.select("div[id=content]")
								.select("div[class=article]")
								.select("dl[class=obu]").select("dd")
								.select("a").iterator();

						while (iteratorFANS.hasNext()) {
							followED.add(iteratorFANS.next().attr("href")
									.substring(28));
						}
					}
				}
			}
		} else {
			System.out.println(url + "�Ҳ�����˿ҳ��");
			list.remove(0);
			follow.clear();
			followED.clear();
			if (list.size() == 0)
				return;
			parse(list.get(0));
			return;
		}
		flag4reboot = true;
		follow.retainAll(followED);
		if (flag) {
			int i = 0;
			while (i < follow.size()) {
				if (!userName.contains(follow.get(i))) {
					follow.remove(i);
					continue;
				}
				i++;
			}
		}
		if (follow.size() >= 2) {
			fos.write((url.substring(28) + '\n').getBytes());// write the main
															// username into
															// file
			if (!userName.contains(list.get(0).substring(28)))
				userName.add(list.get(0).substring(28));
			list.remove(0);
			count++;

			while (follow.size() > 0) {
				String s = follow.get(0);
				if (!flag && !userName.contains(s) && !list.contains(doubanURL+s)) {
					list.add(doubanURL + s);
					userName.add(s);
					countALL++;
				}
				fos.write((s + '\n').getBytes());
				follow.remove(0);
			}
			followED.clear();
			fos.write("*\n".getBytes());
		} else {
			list.remove(0);
			followED.clear();
			follow.clear();
		}
		if (flag == false && countALL > 5000) {
			flag = true;
			System.out.println("flag is true from this moment.");
		}
		System.out.println(count);
		if (count % 2500 == 0)
			throw new IOException();
		// Thread.sleep(500);
		if (list.size() == 0) {
			finish = true;
			return;
		}
		parse(list.get(0));
		return;
	}

	public static void main(String[] args) throws Exception {

		cookies.put("bid", "uujSTyJOV+w");
		cookies.put("ll", "108288");
		cookies.put("ct", "y");
		cookies.put("__utma",
				"30149280.410293970.1401375594.1401635512.1401638745.7");
		cookies.put("__utmb", "30149280.11.10.1401638745");
		cookies.put("__utmc", "30149280");
		cookies.put(
				"__utmz",
				"30149280.1401529554.5.5.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided)");
		cookies.put("__utmv", "30149280.8536");
		cookies.put("push_noty_num", "0");
		cookies.put("push_doumail_num", "0");
		cookies.put("dbcl2", "85364066:Z7BcKdEMB84");
		cookies.put("ck", "0HRY");
		cookies.put("viewed", "25835362");

		do {
			if (begin) {
				begin = false;

				File file_Num = new File(FileNum);
				BufferedReader br3 = new BufferedReader(
						new FileReader(file_Num));
				String fileStream = br3.readLine();
				fileNum = Integer.parseInt(fileStream);
				br3.close();
				// obtain fileNum

				File file_List = new File(FileList + (fileNum - 1) + ".txt");
				BufferedReader br1 = new BufferedReader(new FileReader(
						file_List));
				fileStream = br1.readLine();
				countALL = Integer.parseInt(fileStream);
				if (countALL >= 5000)
					flag = true;
				fileStream = br1.readLine();
				count = Integer.parseInt(fileStream);

				while ((fileStream = br1.readLine()) != null) {
					list.add(fileStream);
				}
				br1.close();

				File file_Name = new File(FileName + (fileNum - 1) + ".txt");
				BufferedReader br2 = new BufferedReader(new FileReader(
						file_Name));// ����һ��BufferedReader������ȡ�ļ�
				while ((fileStream = br2.readLine()) != null) {// ʹ��readLine������һ�ζ�һ��
					userName.add(fileStream);
				}
				br2.close();
			}
			fos = new FileOutputStream(FileOut + fileNum + ".txt");// file
																	// output
			log_List = new FileOutputStream(FileList + fileNum + ".txt");
			log_Name = new FileOutputStream(FileName + fileNum + ".txt"); // stream

			try {

				String index_page = list.get(0);
				try {
					parse(index_page);
					System.out.println("finally");

					System.out.println("count: " + count);

					if (list.size() > 0) {
						log_List.write((countALL + "\n" + count + "\n"
								+ list.get(0) + "\n").getBytes());
						list.remove(0);
					}
					while (list.size() > 0) {

						log_List.write((list.get(0) + '\n').getBytes());
						list.remove(0);
					}
					while (userName.size() > 0) {
						log_Name.write((userName.get(0) + '\n').getBytes());
						userName.remove(0);
					}

					result = new FileOutputStream("F:\\map.txt");
					BufferedReader br;
					File file;
					for (int i = 1; i <= fileNum; i++) {
						file = new File("F:\\data\\out\\out" + i + ".txt");
						br = new BufferedReader(new FileReader(file));
						String ResultStream;
						while ((ResultStream = br.readLine()) != null) {
							result.write((ResultStream + '\n').getBytes());
						}
						// result.write('\n');
						br.close();
					}
					result.close();
					delete.clear();

				} catch (IOException e1) {
					if (flag4reboot) {
						begin = true;
						flag4reboot = false;
						Num = new FileOutputStream(FileNum);
						// overwrite the new filenum into num.txt
						Num.write((fileNum + 1 + "\n").getBytes());
						Num.close();

						System.out.println("fileNum changed! current count: "
								+ count);

						if (list.size() > 0) {

							log_List.write((countALL + "\n" + count + "\n"
									+ list.get(0) + "\n").getBytes());
							list.remove(0);
						}
						while (list.size() > 0) {

							log_List.write((list.get(0) + '\n').getBytes());
							list.remove(0);
						}
						while (userName.size() > 0) {
							log_Name.write((userName.get(0) + '\n').getBytes());
							userName.remove(0);
						}
					} else {
						begin = false;
						System.out
								.println("Nothing is written, fileNum unchanged!");
					}
				}

			} finally {
				try {
					fos.close();
					log_List.close();
					log_Name.close();
				} catch (IOException e) {
				}
			}
			// Thread.sleep(2000);
		} while (!finish);
	}
}
