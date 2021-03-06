
import java.io.FileWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList; //https://www.google.ca/maps/search/
import java.util.HashMap;
public class DataCollector {
	String url;
	int l;
	private ArrayList<String> nstore;
	private ArrayList<String> astore;
	private ArrayList<String> pstore;
	private ArrayList<String> tstore;
	private HashMap<String,ArrayList<DataStructure>> mmstore;
	private HashMap<String,ArrayList<String>> cache;
	private ArrayList<Menu> mstore;
	public DataCollector(String url){
		this.l = url.length();
		this.url = "https://www.google.com/search?q=" + url + "飲料店&num=50";
	}
	
	public void call()throws IOException{
		System.out.println("資訊蒐集中...");
		nstore = new ArrayList<String>();
		astore = new ArrayList<String>();
		pstore = new ArrayList<String>();
		tstore = new ArrayList<String>();
		//System.out.println(url);
		Document map = Jsoup
					   .connect(url)
					   .userAgent(
					    "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
					   .timeout(6000).get();
		Elements target = map.select("a._Tbj");
	
		System.out.println(target);
		if(target.isEmpty()) {
			StringBuffer sb = new StringBuffer();
			sb.append(url);
			sb.insert(32+l, "附近的");
			String s = sb.toString();
			System.out.println(s);
			map = Jsoup
					   .connect(s)
					   .userAgent(
					    "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
					   .timeout(6000).get();
			target = map.select("a._Tbj");
		}
		String googleUrl = "https://google.com.tw";
		Document doc = Jsoup
					   .connect(googleUrl+target.attr("href"))
					   .userAgent(
						"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
					   .timeout(5000).get();
	
		//System.out.println(doc);
		Elements names = doc.select("div.rllt__wrapped");
		//System.out.println(names);
		int condition = 0; //0=name 1=time
		for(Element name:names) {
			//System.out.println(name);
			String temp = name.text();
			if(name.is("div._uee.rllt__wrapped")) {
				if(condition == 1) {
					tstore.add("查無資料");
					//System.out.println("none");
				}
				nstore.add(temp);
				//System.out.println("---" + temp);
				condition = 1;
			}else {
				tstore.add(temp);
				condition = 0;
				//System.out.println("time = " + temp);
			}
		}
		int status = 0; //1:address 2:phone
		Elements addresses = doc.select("div._Fxi span:not(._Q0s)");
		//System.out.println("________" +addresses);
		//System.out.println(addresses.get(0));
		for(Element address:addresses.select("span:not(._PXi)")) {
			
			String temp = address.text();
			temp = temp.replaceAll(" ", "");
			char[] str = temp.toCharArray();
			boolean isNumber = true;
			for(int i=0; i<str.length; i++) {
				if(!Character.isDigit(str[i])) {
					if(status == 1) {
						pstore.add("查無資料");
					}
					astore.add(temp);
					status = 1;
					isNumber = false;
					//System.out.println("address" + temp);
					break;
				}
			}
			if(isNumber) {
				if(status==2|| status==0) {
					astore.add("查無資料");
				}
				pstore.add(temp);
				status = 2;
				//System.out.println("phone" + temp);
			}		
		}
		//Elements times = doc.select("div.rllt__wrapped:not(._uee.rllt__wrapped)");
		//System.out.println(times);
		//for(int i=0; i<nstore.size(); i++) {
		//	System.out.println("name = " + nstore.get(i) + "address = " + astore.get(i) + "phone = " + pstore.get(i));
		//}
		if(astore.size()!=nstore.size())astore.add("查無資料");
		if(pstore.size()!=nstore.size())pstore.add("查無資料");
		if(tstore.size()!=nstore.size())tstore.add("查無資料");
	}
	
	public void DrinkSuggest(String drinktype)throws IOException {
		ArrayList<String> nor1 = new ArrayList<String>();
		nor1.add("拿鐵"); nor1.add("牛奶"); nor1.add("鮮奶"); nor1.add("歐蕾");nor1.add("撞奶");nor1.add("奶類");
		ArrayList<String> nor2 = new ArrayList<String>();
		nor2.add("珍珠"); nor2.add("粉圓"); nor2.add("波霸"); nor2.add("青蛙");nor2.add("Q");
		ArrayList<String> nor3 = new ArrayList<String>();
		nor3.add("阿華田"); nor3.add("可可"); nor3.add("巧克力"); 
		ArrayList<String> nor4 = new ArrayList<String>();
		nor4.add("多多"); nor4.add("養樂多"); 
		ArrayList<String> nor5 = new ArrayList<String>();
		nor5.add("果汁"); nor5.add("柚");nor5.add("橙");nor5.add("蘋");nor5.add("葡");nor5.add("檸");nor5.add("蕉");nor5.add("桔");nor5.add("百香");nor5.add("桃");nor5.add("梅");
		cache = new HashMap<String,ArrayList<String>>();
		cache.put("1", nor1);cache.put("2", nor2);cache.put("3", nor3);cache.put("4", nor4);cache.put("5", nor5);
		System.out.println("搜尋中...");
		//for(int i=0; i<)
		//add the url of the menu
		mmstore = new HashMap<String,ArrayList<DataStructure>>();
		mstore = new ArrayList<Menu>();
		Menu a01 = new Menu("8+9","https://nccudrink.github.io/drinkmenu/menu/8%2B9%E8%8F%9C%E5%96%AE.html");
		Menu a02 = new Menu("50嵐","https://nccudrink.github.io/drinkmenu/menu/50%E5%B5%90%E8%8F%9C%E5%96%AE.html");
		Menu a03 = new Menu("Coco","https://nccudrink.github.io/drinkmenu/menu/CoCo%E8%8F%9C%E5%96%AE.html");
		Menu a04 = new Menu("comebuy","https://nccudrink.github.io/drinkmenu/menu/comebuy%E8%8F%9C%E5%96%AE.html");
		Menu a05 = new Menu("Cow Banana","https://nccudrink.github.io/drinkmenu/menu/CowBanana%E8%8F%9C%E5%96%AE.html");
		Menu a06 = new Menu("rabbit","https://nccudrink.github.io/drinkmenu/menu/rabbitrabbit.html");
		Menu a07 = new Menu("TeaTank","https://nccudrink.github.io/drinkmenu/menu/TeaTank%E8%8F%9C%E5%96%AE.html");
		Menu a08 = new Menu("一芳","https://nccudrink.github.io/drinkmenu/menu/%E4%B8%80%E8%8A%B3%E8%8F%9C%E5%96%AE.html");
		Menu a09 = new Menu("丸作食茶","https://nccudrink.github.io/drinkmenu/menu/%E4%B8%B8%E4%BD%9C%E9%A3%9F%E8%8C%B6%E8%8F%9C%E5%96%AE.html");
		Menu a10 = new Menu("大苑子","https://nccudrink.github.io/drinkmenu/menu/%E5%A4%A7%E8%8B%91%E5%AD%90%E8%8F%9C%E5%96%AE.html");
		Menu a11 = new Menu("天仁茗茶","https://nccudrink.github.io/drinkmenu/menu/%E5%A4%A9%E4%BB%81%E8%8C%97%E8%8C%B6%E8%8F%9C%E5%96%AE.html");
		Menu a12 = new Menu("水巷茶弄","https://nccudrink.github.io/drinkmenu/menu/%E6%B0%B4%E5%B7%B7%E8%8C%B6%E5%BC%84%E8%8F%9C%E5%96%AE.html");
		Menu a13 = new Menu("布萊恩紅茶","https://nccudrink.github.io/drinkmenu/menu/%E5%B8%83%E8%90%8A%E6%81%A9%E8%8F%9C%E5%96%AE.html");
		Menu a14 = new Menu("波哥","https://nccudrink.github.io/drinkmenu/menu/%E6%B3%A2%E5%93%A5%E8%8F%9C%E5%96%AE.html");
		Menu a15 = new Menu("花甜果室","https://nccudrink.github.io/drinkmenu/menu/%E8%8A%B1%E7%94%9C%E6%9E%9C%E5%AE%A4%E8%8F%9C%E5%96%AE.html");
		Menu a16 = new Menu("珍煮丹","https://nccudrink.github.io/drinkmenu/menu/%E7%8F%8D%E7%85%AE%E4%B8%B9%E8%8F%9C%E5%96%AE.html");
		Menu a17 = new Menu("茶亭","https://nccudrink.github.io/drinkmenu/menu/%E8%8C%B6%E4%BA%AD%E8%8F%9C%E5%96%AE.html");
		Menu a18 = new Menu("茶湯會","https://nccudrink.github.io/drinkmenu/menu/%E8%8C%B6%E6%B9%AF%E6%9C%83%E8%8F%9C%E5%96%AE.html");
		Menu a19 = new Menu("貢茶","https://nccudrink.github.io/drinkmenu/menu/%E8%B2%A2%E8%8C%B6%E8%8F%9C%E5%96%AE.html");
		Menu a20 = new Menu("迷客夏","https://nccudrink.github.io/drinkmenu/menu/%E8%BF%B7%E5%AE%A2%E5%A4%8F%E8%8F%9C%E5%96%AE.html");
		Menu a21 = new Menu("清心","https://nccudrink.github.io/drinkmenu/menu/%E6%B8%85%E5%BF%83%E8%8F%9C%E5%96%AE.html");
		Menu a22 = new Menu("圓石禪飲","https://nccudrink.github.io/drinkmenu/menu/%E5%9C%93%E7%9F%B3%E7%A6%AA%E9%A3%B2%E8%8F%9C%E5%96%AE.html");
		Menu a23 = new Menu("龍角","https://nccudrink.github.io/drinkmenu/menu/%E9%BE%8D%E8%A7%92%E8%8F%9C%E5%96%AE.html");
		Menu a24 = new Menu("露易莎","https://nccudrink.github.io/drinkmenu/menu/%E9%9C%B2%E6%98%93%E8%8E%8E%E8%8F%9C%E5%96%AE.html");
		
		Menu b01 = new Menu("Baronesse","https://nccudrink.github.io/drinkmenu/menu/Baronesse%E8%8F%9C%E5%96%AE.html");

		Menu b02 = new Menu("十杯","https://nccudrink.github.io/drinkmenu/menu/%E5%8D%81%E6%9D%AF%E8%8F%9C%E5%96%AE.html");
		Menu b03 = new Menu("天下佈武菜單","https://nccudrink.github.io/drinkmenu/menu/%E5%A4%A9%E4%B8%8B%E4%BD%88%E6%AD%A6%E8%8F%9C%E5%96%AE.html");
		Menu b04 = new Menu("兔子","https://nccudrink.github.io/drinkmenu/menu/rabbitrabbit.html");
		Menu b05 = new Menu("","");

		

//		Menu c01 = new Menu("","");
//		Menu c02 = new Menu("","");

		
		mstore.add(a01);mstore.add(a02);mstore.add(a03);mstore.add(a04);mstore.add(a05);
		mstore.add(a06);mstore.add(a07);mstore.add(a08);mstore.add(a09);mstore.add(a10);
		mstore.add(a11);mstore.add(a12);mstore.add(a13);mstore.add(a14);mstore.add(a15);
		mstore.add(a16);mstore.add(a17);mstore.add(a18);mstore.add(a19);mstore.add(a20);
		mstore.add(a21);mstore.add(a22);mstore.add(a23);mstore.add(a24);
		

		mstore.add(b01);mstore.add(b02);//mstore.add(b03);mstore.add(b04);mstore.add(b05);
		String key_to_rec = "0";
		for(int z=0; z<nor1.size();z++) {
			String cache_ = nor1.get(z);
			if(drinktype.contains(cache_))key_to_rec = "1";
		}
		for(int z=0; z<nor2.size();z++) {
			String cache_ = nor2.get(z);
			if(drinktype.contains(cache_))key_to_rec = "2";
		}
		for(int z=0; z<nor3.size();z++) {
			String cache_ = nor3.get(z);
			if(drinktype.contains(cache_))key_to_rec = "3";
		}
		for(int z=0; z<nor4.size();z++) {
			String cache_ = nor4.get(z);
			if(drinktype.contains(cache_))key_to_rec = "4";
		}
		for(int z=0; z<nor5.size();z++) {
			String cache_ = nor5.get(z);
			if(drinktype.contains(cache_))key_to_rec = "5";
		}

		mstore.add(b01);mstore.add(b02);mstore.add(b03);
		
//		mstore.add(c01);mstore.add(c02);
		
		for(int i=0; i<nstore.size(); i++) {
			for(int j=0; j<mstore.size(); j++) {
				if(nstore.get(i).toLowerCase().contains(mstore.get(j).Name.toLowerCase())) {
					Document drinksmenu = Jsoup
							   .connect(mstore.get(j).url)
							   .userAgent(
							    "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
							   .timeout(5000).get();
					Elements drinks = drinksmenu.select("th");
					ArrayList<DataStructure> tempstore = new ArrayList<DataStructure>();
					
					int k = 0;
					String temp1 = "";
					String temp2 = "";
					String temp3 = "";
					String temp4 = "";
					boolean key = false;
					for(Element drink:drinks) {
						String temp = drink.text();
						if(!key_to_rec.equals("0")) {
							for(int c=0; c<cache.get(key_to_rec).size(); c++) {
								if(temp.contains(cache.get(key_to_rec).get(c)))key = true;
							}
						}
						if(temp.contains(drinktype))key = true;
						if(key) {
							if(k%4==0) {
								temp1 = temp;
								k+=1;
							}else if(k%4==1) {
								temp2 = temp;
								k+=1;
							}else if(k%4==2) {
								temp3 = temp;
								k+=1;
							}else {
								temp4 = temp;
								DataStructure s = new DataStructure(temp1,temp4,temp3,temp2);
								tempstore.add(s);
								k=0;
								key = false;
							}
						}
					}
					mmstore.put(nstore.get(i), tempstore);//get the menu
				}
			}
		}
		//print result
		System.out.println("Total: " + nstore.size() + " result");
		for(int i=0; i<nstore.size(); i++) {
			System.out.println("------------------------------------------------------------------------------------------");
			System.out.println(i+ ". 店名: " + nstore.get(i));
			System.out.println("地址: " + astore.get(i) + "\t電話: " + pstore.get(i) + "\t營業時間: " + tstore.get(i));
			String status = "";
			if(mmstore.containsKey(nstore.get(i))) {
				for(DataStructure ds:mmstore.get(nstore.get(i))) {
					String zero1 = "";
					String zero2  ="";
					String zero3 = "";
					if(!ds.large.equals("0"))zero1 = "L:" + ds.large + " ";
					if(!ds.medium.equals("0"))zero2 = "M:" + ds.medium + " ";
					if(!ds.small.equals("0"))zero3 = "S:" + ds.small + " ";
					status = status + "[" + ds.drinkname + " " + zero1  + zero2 + zero3 + "] ";
				}
			}else status = "QQ沒有";
			System.out.println("符合您搜尋結果的飲料有: " + status);
		}
	}
	public void ShowTheMenu(String i) throws IOException{
		String map = nstore.get(Integer.valueOf(i));
		String RunUrl = "";
		for(int m=0; m<mstore.size(); m++) {
			if(map.contains(mstore.get(m).Name)) {
				RunUrl = mstore.get(m).url;
			}
		}

		if(!RunUrl.equals("")) {
			Document drinksmenu = Jsoup
					.connect(RunUrl)
					.userAgent(
							"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
					.timeout(5000).get();
			
			Elements drinks = drinksmenu.select("th");
			int k=0;
			String temp1 = "";
			String temp2 = "";
			String temp3 = "";
			String temp4 = "";
			String str = "";
			for(Element drink:drinks) {
				String temp = drink.text();
				if(k%4==0) {
					if(!temp.equals("0"))temp1 = temp;
					k+=1;
				}else if(k%4==1) {
					if(!temp.equals("0"))temp2 =" L:" + temp;
					k+=1;
				}else if(k%4==2) {
					if(!temp.equals("0"))temp3 =" M:" + temp;
					k+=1;
				}else {
					if(!temp.equals("0"))temp4 =" S:" + temp;
					str = str + "[" + temp1 + temp2 + temp3 + temp4 + "]\n";
					temp1 = "";
					temp2 = "";
					temp3 = "";
					temp4 = "";
					k=0;
				}	
			}
			System.out.println(str);
		}else {
			System.out.println("找不到菜單");
		}
		
	}
}