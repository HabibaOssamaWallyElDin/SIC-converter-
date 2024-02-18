package pipa;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class ReadingData {
	public static ArrayList<String> $$lc = new ArrayList<>();
	public static String start;
	public static String end;

	public static void main(String[] args) {
		ArrayList<String[]> table = null;
		try {
			table = getData("inSICtt.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ArrayList<String[]> symboleTable = null;
		symboleTable = pass1(table);

		ArrayList<String> objectCode = null;
		objectCode = pass2(table, symboleTable);

		ArrayList<String> $$hte = $$hte(table, objectCode);

		for (int i = 0; i < table.size(); i++) {
			System.out.println($$lc.get(i) + "\t" + table.get(i)[0] + "\t" + table.get(i)[1] + "\t" + table.get(i)[2]
					+ "\t" + objectCode.get(i));
		}
		for (int i = 0; i < symboleTable.size(); i++) {
			System.out.println(symboleTable.get(i)[0] + "\t" + symboleTable.get(i)[1]);
		}
		for (int i = 0; i < $$hte.size(); i++) {
			System.out.println($$hte.get(i));
		}
	}

	private static ArrayList<String> $$hte(ArrayList<String[]> table, ArrayList<String> objectCode) {
		ArrayList<String> $$hte = new ArrayList<>();

		String $$pn6 = String.format("%6s", table.get(0)[0]);
		String $$pn60 = $$pn6.replace(" ", "0");

		String $$s6 = String.format("%6s", table.get(0)[2]);
		String $$s60 = $$s6.replace(" ", "0");

		String $$leth = String
				.format("%6s", Integer.toHexString(Integer.parseInt(end, 16) - Integer.parseInt(start, 16)))
				.replace(" ", "0");

		$$hte.add("H" + $$pn60 + $$s60 + $$leth);

		String $$tot = "T" + String.format("%6s", $$s60).replace(" ", "0");
		String $$bef = $$s60;
		String $$to = "";
		for (int i = 1, j = 0; i < objectCode.size(); i++) {
			if (objectCode.get(i).equals("#")) {
				if ($$to.length() != 0) {
					String $$lg = String
							.format("%2s",
									Integer.toHexString(
											Integer.parseInt($$lc.get(i), 16) - Integer.parseInt($$bef, 16)))
							.replace(" ", "0");
					$$tot = $$tot + $$lg + $$to;
					$$hte.add($$tot);
					$$bef = $$lc.get(i + 1);
					$$tot = "T" + String.format("%6s", $$bef).replace(" ", "0");
					$$to = "";
					j = 0;
				} else {
					$$bef = $$lc.get(i + 1);
					$$tot = "T" + String.format("%6s", $$bef).replace(" ", "0");
				}
			} else if (j < 10 && ($$to.length()/2 + objectCode.get(i).length())/2 <= 10*3) {
				$$to = $$to + objectCode.get(i);
				j++;
			} else {
				String $$lg = String
						.format("%2s",
								Integer.toHexString(Integer.parseInt($$lc.get(i), 16) - Integer.parseInt($$bef, 16)))
						.replace(" ", "0");
				$$tot = $$tot + $$lg + $$to;
				$$hte.add($$tot);
				$$bef = $$lc.get(i);
				$$tot = "T" + String.format("%6s", $$bef).replace(" ", "0");
				$$to = "";
				j = 0;
				i--;
			}
		}
		if ($$to.length() == 0) {
			$$hte.add("E" + $$s60);
		} else {
			String $$lg = String
					.format("%2s",
							Integer.toHexString((Integer.parseInt(end, 16) - Integer.parseInt($$bef, 16))/* /2 */))
					.replace(" ", "0");
			$$tot = $$tot + $$lg + $$to;
			$$hte.add($$tot);
			$$hte.add("E" + $$s60);
		}

		return $$hte;
	}

	private static ArrayList<String> pass2(ArrayList<String[]> table, ArrayList<String[]> symboleTable) {
		ArrayList<String> objectCode = new ArrayList<>();
		objectCode.add("");
		for (int i = 1; i < table.size() - 1; i++) {
			if (table.get(i)[1].equalsIgnoreCase("RESW") || table.get(i)[1].equalsIgnoreCase("RESB")) {
				objectCode.add("#");
			} else if (table.get(i)[1].equalsIgnoreCase("WORD")) {
				String ref = table.get(i)[2];
				if (ref.contains(",")) {
					String[] group = ref.split(",");
					String obcode = "";
					for (int j = 0; j < group.length; j++) {
						int dec = Integer.parseInt(group[j]);
						String refHex = Integer.toHexString(dec);
						String ref6Hex = String.format("%6s", refHex);
						String ref60Hex = ref6Hex.replace(" ", "0");

						obcode += ref60Hex;
					}
					objectCode.add(obcode);
				} else {
					int dec = Integer.parseInt(ref);
					String refHex = Integer.toHexString(dec);
					String ref6Hex = String.format("%6s", refHex);
					String ref60Hex = ref6Hex.replace(" ", "0");
					objectCode.add(ref60Hex);
				}
			} else if (table.get(i)[1].equalsIgnoreCase("BYTE")) {
				String ref = table.get(i)[2];
				if (ref.toLowerCase().startsWith("c")) {
					String wRef = ref.substring(2, ref.length() - 1);
					String obcode = "";
					for (int j = 0; j < wRef.length(); j++) {
						char character = wRef.charAt(j);
						int ascii = character;
						String asciiHex = Integer.toHexString(ascii);
						String asciiHex6 = String.format("%2s", asciiHex);
						String asciiHex60 = asciiHex6.replace(" ", "0");
						obcode += asciiHex60;
					}
					objectCode.add(obcode);
				} else {
					String wRef = ref.substring(2, ref.length() - 1);
					if (wRef.length() % 2 == 0) {
						objectCode.add(wRef);
					} else {
						objectCode.add("0" + wRef);
					}

				}
			} else {
				String opCode = "";
				String[][] opCodeTable = converter.initialize();
				for (int j = 0; j < opCodeTable.length; j++) {
					if (table.get(i)[1].equalsIgnoreCase(opCodeTable[j][0])) {
						opCode = opCodeTable[j][2];
						break;
					}
				}
				String ref = "";
				boolean isIndex = false;
				String tableRef = table.get(i)[2];
				if (tableRef.contains(",")) {
					tableRef = tableRef.substring(0, tableRef.length() - 2);
					isIndex = true;
				}
				for (int j = 0; j < symboleTable.size(); j++) {
					if (tableRef.equalsIgnoreCase(symboleTable.get(j)[0])) {
						ref = symboleTable.get(j)[1];
						break;
					}
				}

				String obcode = "";
				if (isIndex) {
					int dec = Integer.parseInt(ref, 16);
					int eightThousand = Integer.parseInt("8000", 16);
					int sum = dec + eightThousand;
					ref = Integer.toHexString(sum);
					obcode = opCode + ref;
				} else {
					obcode = opCode + ref;
				}

				objectCode.add(obcode);

			}
		}
		objectCode.add("");

		return objectCode;
	}

	private static ArrayList<String[]> pass1(ArrayList<String[]> table) {
		ArrayList<String[]> symboleTable = new ArrayList<>();
		String startRefrance = table.get(0)[2];
		ReadingData.start = startRefrance;
		int counter = Integer.parseInt(startRefrance, 16);

		String[] start = new String[2];
		start[0] = table.get(0)[0];
		start[1] = startRefrance;
		symboleTable.add(start);

		$$lc.add(String.format("%4s",Integer.toHexString(counter)).replace(" ", "0"));
		for (int i = 1; i < table.size(); i++) {
			$$lc.add(String.format("%4s",Integer.toHexString(counter)).replace(" ", "0"));
			if (!table.get(i)[0].contains("#")) {
				String[] row = new String[2];
				row[0] = table.get(i)[0];
				String counterHex = Integer.toHexString(counter);
				String formated = String.format("%4s", counterHex);
				String rep = formated.replace(" ", "0");
				row[1] = rep;
				symboleTable.add(row);
			}

			if (table.get(i)[1].equalsIgnoreCase("RESW")) {
				int dec = Integer.parseInt(table.get(i)[2]);
				dec *= 3;
				counter += dec;
			} else if (table.get(i)[1].equalsIgnoreCase("RESB")) {
				int dec = Integer.parseInt(table.get(i)[2]);
				counter += dec;
			} else if (table.get(i)[1].equalsIgnoreCase("BYTE")) {
				String ref = table.get(i)[2].toLowerCase();
				if (ref.startsWith("c")) {
					int add = ref.length() - 3;
					counter += add;
				} else {
					int add = ref.length() - 3;
					if (add % 2 == 0) {
						add /= 2;
					} else {
						add++;
						add /= 2;
					}
					counter += add;
				}
			} else if (table.get(i)[1].equalsIgnoreCase("WORD")) {
				String ref = table.get(i)[2];
				if (ref.contains(",")) {
					String[] group = ref.split(",");
					int num = group.length;
					num *= 3;
					counter += num;
				} else {
					counter += 3;
				}
			} else {
				counter += 3;
			}
		}
		String counterHex = Integer.toHexString(counter - 3);
		String formated = String.format("%4s", counterHex);
		String rep = formated.replace(" ", "0");
		ReadingData.end = rep;
		return symboleTable;
	}

	public static ArrayList<String[]> getData(String path) throws FileNotFoundException {
		ArrayList<String[]> table = new ArrayList<>();

		Scanner scan = new Scanner(new File(path));

		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] row = line.split("`");

			String[] insRow = new String[3];
			if (row.length == 3) {
				insRow[0] = row[0];
				insRow[1] = row[1];
				insRow[2] = row[2];
			} else if (row.length == 2) {
				insRow[0] = "#";
				insRow[1] = row[0];
				insRow[2] = row[1];
			}
			table.add(insRow);
		}
		return table;
	}
}