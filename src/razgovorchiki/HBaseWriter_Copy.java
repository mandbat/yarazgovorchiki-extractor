package razgovorchiki;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseWriter_Copy {

	@SuppressWarnings({ "deprecation", "unchecked" })
	public static void WriteToHBase(ArrayList<Object> records) throws IOException {

		Configuration config = HBaseConfiguration.create();
		HTable hTable = new HTable(config, "yablogs");

		for (Object rec0 : records) {

			Map<String, String> rec = (HashMap<String, String>) rec0;
			
			Put newHBaseRecord = new Put(Bytes.toBytes(rec.get("link")));

			if (rec.get("author") != null)
				newHBaseRecord.add(Bytes.toBytes("yablog"), Bytes.toBytes("author"), Bytes.toBytes(rec.get("author")));

			if (rec.get("pubDate") != null)
				newHBaseRecord.add(Bytes.toBytes("yablog"), Bytes.toBytes("pubDate"),
						Bytes.toBytes(rec.get("pubDate")));

			if (rec.get("title") != null)
				newHBaseRecord.add(Bytes.toBytes("yablog"), Bytes.toBytes("title"), Bytes.toBytes(rec.get("title")));

			if (rec.get("description") != null)
				newHBaseRecord.add(Bytes.toBytes("yablog"), Bytes.toBytes("description"),
						Bytes.toBytes(rec.get("description")));

			hTable.put(newHBaseRecord);
			System.out.println("Writed: " + rec.get("link"));

		}

		hTable.close();

	}

}
