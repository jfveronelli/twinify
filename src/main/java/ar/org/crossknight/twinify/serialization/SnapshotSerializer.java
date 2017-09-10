package ar.org.crossknight.twinify.serialization;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.Date;

import ar.org.crossknight.twinify.domain.snapshot.Archive;
import ar.org.crossknight.twinify.domain.snapshot.Folder;
import ar.org.crossknight.twinify.domain.snapshot.Snapshot;
import ar.org.crossknight.twinify.util.Formatter;
import ar.org.crossknight.twinify.util.Path;
import ar.org.crossknight.twinify.util.Utils;

public final class SnapshotSerializer {

	private static final String EOL = "\n";

	private SnapshotSerializer() {}

	public static final void write(OutputStream stream, Snapshot snapshot) throws IOException {
		Writer writer = new OutputStreamWriter(new BufferedOutputStream(stream), "UTF-8");
		writer.write(snapshot + EOL + EOL);
  		write(writer, snapshot.getRoot());
  		writer.close();
	}

	private static void write(Writer writer, Folder folder) throws IOException {
		writer.write(folder + EOL);
    	for (Archive archive: Utils.sort(folder.getArchives())) {
    		writer.write(archive + EOL);
    	}
    	writer.write(EOL);
    	for (Folder subfolder: Utils.sort(folder.getSubfolders())) {
    		write(writer, subfolder);
    	}
	}

	public static final Snapshot read(InputStream stream) throws IOException, ParseException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(stream), "UTF-8"));

		Snapshot snapshot = parseSnapshot(reader);
	    Folder folder = parseFolder(reader, snapshot);
		while (folder != null) {
			folder = parseFolder(reader, snapshot);
		}

		reader.close();
		return snapshot;
	}

	private static Snapshot parseSnapshot(BufferedReader reader) throws IOException {
		String path = reader.readLine();
		path = path.substring(1, path.length() - 1);
		reader.readLine();
		return new Snapshot(path);
	}

	private static Folder parseFolder(BufferedReader reader, Snapshot snapshot) throws IOException, ParseException {
		String path = reader.readLine();
		if (path == null) {
			return null;
		}

		String parent = Path.parent(path);
		Folder folder = parent == null? snapshot.getRoot(): new Folder(snapshot.findFolder(parent), Path.name(path));

		String line = reader.readLine();
		while (line.length() > 0) {
			parseArchive(line, folder);
			line = reader.readLine();
		}

		return folder;
	}

	private static void parseArchive(String line, Folder parent) throws ParseException {
		int pos1 = line.indexOf(Archive.FIELD_SEPARATOR);
		int pos2 = line.indexOf(Archive.FIELD_SEPARATOR, pos1 + Archive.FIELD_SEPARATOR.length());

		String name = line.substring(0, pos1);
		Date lastModified = Formatter.parseDate(line.substring(pos1 + Archive.FIELD_SEPARATOR.length(), pos2));
		long size = Long.parseLong(line.substring(pos2 + Archive.FIELD_SEPARATOR.length()));

		new Archive(parent, name, lastModified, size);
	}

}